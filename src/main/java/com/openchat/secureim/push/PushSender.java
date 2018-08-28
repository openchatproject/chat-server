package com.openchat.secureim.push;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.configuration.ApnConfiguration;
import com.openchat.secureim.configuration.GcmConfiguration;
import com.openchat.secureim.controllers.NoSuchUserException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;
import com.openchat.secureim.entities.MessageProtos;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.DirectoryManager;
import com.openchat.secureim.storage.StoredMessageManager;
import com.openchat.secureim.util.Pair;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PushSender {

  private final Logger logger = LoggerFactory.getLogger(PushSender.class);

  private final AccountsManager  accounts;

  private final GCMSender gcmSender;
  private final APNSender apnSender;
  private final StoredMessageManager storedMessageManager;

  public PushSender(GcmConfiguration gcmConfiguration,
                    ApnConfiguration apnConfiguration,
                    StoredMessageManager storedMessageManager,
                    AccountsManager accounts,
                    DirectoryManager directory)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException
  {
    this.accounts  = accounts;

    this.storedMessageManager = storedMessageManager;
    this.gcmSender            = new GCMSender(gcmConfiguration.getApiKey());
    this.apnSender            = new APNSender(apnConfiguration.getCertificate(), apnConfiguration.getKey());
  }

  public void sendMessage(Device device, MessageProtos.OutgoingMessageSignal outgoingMessage)
      throws IOException, NoSuchUserException
  {
    String signalingKey              = device.getSignalingKey();
    EncryptedOutgoingMessage message = new EncryptedOutgoingMessage(outgoingMessage, signalingKey);

    if      (device.getGcmRegistrationId() != null) sendGcmMessage(device, message);
    else if (device.getApnRegistrationId() != null) sendApnMessage(device, message);
    else if (device.getFetchesMessages())           storeFetchedMessage(device, message);
    else                                            throw new NoSuchUserException("No push identifier!");
  }

  private void sendGcmMessage(Device device, EncryptedOutgoingMessage outgoingMessage)
      throws IOException, NoSuchUserException
  {
    try {
      String canonicalId = gcmSender.sendMessage(device.getGcmRegistrationId(),
                                                 outgoingMessage);

      if (canonicalId != null) {
        device.setGcmRegistrationId(canonicalId);
        accounts.update(device);
      }

    } catch (NoSuchUserException e) {
      logger.debug("No Such User", e);
      device.setGcmRegistrationId(null);
      accounts.update(device);
      throw new NoSuchUserException("User no longer exists in GCM.");
    }
  }

  private void sendApnMessage(Device device, EncryptedOutgoingMessage outgoingMessage)
      throws IOException
  {
    apnSender.sendMessage(device.getApnRegistrationId(), outgoingMessage);
  }

  private void storeFetchedMessage(Device device, EncryptedOutgoingMessage outgoingMessage) throws IOException {
    storedMessageManager.storeMessage(device, outgoingMessage);
  }
}
