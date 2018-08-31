package com.openchat.secureim.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.configuration.ApnConfiguration;
import com.openchat.secureim.configuration.GcmConfiguration;
import com.openchat.secureim.entities.CryptoEncodingException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;
import com.openchat.secureim.entities.MessageProtos;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.StoredMessageManager;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class PushSender {

  private final Logger logger = LoggerFactory.getLogger(PushSender.class);

  private final AccountsManager      accounts;
  private final GCMSender            gcmSender;
  private final APNSender            apnSender;
  private final StoredMessageManager storedMessageManager;

  public PushSender(GcmConfiguration gcmConfiguration,
                    ApnConfiguration apnConfiguration,
                    StoredMessageManager storedMessageManager,
                    AccountsManager accounts)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException
  {
    this.accounts             = accounts;
    this.storedMessageManager = storedMessageManager;
    this.gcmSender            = new GCMSender(gcmConfiguration.getApiKey());
    this.apnSender            = new APNSender(apnConfiguration.getCertificate(), apnConfiguration.getKey());
  }

  public void sendMessage(Account account, Device device, MessageProtos.OutgoingMessageSignal outgoingMessage)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    String                   signalingKey = device.getSignalingKey();
    EncryptedOutgoingMessage message      = new EncryptedOutgoingMessage(outgoingMessage, signalingKey);

    if      (device.getGcmId() != null)   sendGcmMessage(account, device, message);
    else if (device.getApnId() != null)   sendApnMessage(account, device, message);
    else if (device.getFetchesMessages()) storeFetchedMessage(account, device, message);
    else                                  throw new NotPushRegisteredException("No delivery possible!");
  }

  private void sendGcmMessage(Account account, Device device, EncryptedOutgoingMessage outgoingMessage)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    try {
      String canonicalId = gcmSender.sendMessage(device.getGcmId(), outgoingMessage);

      if (canonicalId != null) {
        device.setGcmId(canonicalId);
        accounts.update(account);
      }

    } catch (NotPushRegisteredException e) {
      logger.debug("No Such User", e);
      device.setGcmId(null);
      accounts.update(account);
      throw new NotPushRegisteredException(e);
    }
  }

  private void sendApnMessage(Account account, Device device, EncryptedOutgoingMessage outgoingMessage)
      throws TransientPushFailureException, NotPushRegisteredException
  {
    try {
      apnSender.sendMessage(device.getApnId(), outgoingMessage);
    } catch (NotPushRegisteredException e) {
      device.setApnId(null);
      accounts.update(account);
      throw new NotPushRegisteredException(e);
    }
  }

  private void storeFetchedMessage(Account account, Device device, EncryptedOutgoingMessage outgoingMessage)
      throws NotPushRegisteredException
  {
    try {
      storedMessageManager.storeMessage(account, device, outgoingMessage);
    } catch (CryptoEncodingException e) {
      throw new NotPushRegisteredException(e);
    }
  }
}
