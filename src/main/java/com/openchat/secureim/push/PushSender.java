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
import com.openchat.secureim.storage.PubSubManager;
import com.openchat.secureim.storage.StoredMessages;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class PushSender {

  private final Logger logger = LoggerFactory.getLogger(PushSender.class);

  private final AccountsManager accounts;
  private final GCMSender       gcmSender;
  private final APNSender       apnSender;
  private final WebsocketSender webSocketSender;

  public PushSender(GcmConfiguration gcmConfiguration,
                    ApnConfiguration apnConfiguration,
                    StoredMessages   storedMessages,
                    PubSubManager    pubSubManager,
                    AccountsManager  accounts)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException
  {
    this.accounts        = accounts;
    this.webSocketSender = new WebsocketSender(storedMessages, pubSubManager);
    this.gcmSender       = new GCMSender(gcmConfiguration.getApiKey());
    this.apnSender       = new APNSender(pubSubManager, storedMessages,
                                         apnConfiguration.getCertificate(),
                                         apnConfiguration.getKey());
  }

  public void sendMessage(Account account, Device device, MessageProtos.OutgoingMessageSignal message)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    try {
      String                   signalingKey     = device.getSignalingKey();
      EncryptedOutgoingMessage encryptedMessage = new EncryptedOutgoingMessage(message, signalingKey);

      sendMessage(account, device, encryptedMessage);
    } catch (CryptoEncodingException e) {
      throw new NotPushRegisteredException(e);
    }
  }

  public void sendMessage(Account account, Device device, EncryptedOutgoingMessage message)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    if      (device.getGcmId() != null)   sendGcmMessage(account, device, message);
    else if (device.getApnId() != null)   sendApnMessage(account, device, message);
    else if (device.getFetchesMessages()) sendWebSocketMessage(account, device, message);
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
      apnSender.sendMessage(account, device, device.getApnId(), outgoingMessage);
    } catch (NotPushRegisteredException e) {
      device.setApnId(null);
      accounts.update(account);
      throw new NotPushRegisteredException(e);
    }
  }

  private void sendWebSocketMessage(Account account, Device device, EncryptedOutgoingMessage outgoingMessage)
      throws NotPushRegisteredException
  {
    try {
      webSocketSender.sendMessage(account, device, outgoingMessage);
    } catch (CryptoEncodingException e) {
      throw new NotPushRegisteredException(e);
    }
  }
}
