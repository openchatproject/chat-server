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
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.DirectoryManager;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class PushSender {

  private final Logger logger = LoggerFactory.getLogger(PushSender.class);

  private final AccountsManager  accounts;
  private final DirectoryManager directory;

  private final GCMSender gcmSender;
  private final APNSender apnSender;

  public PushSender(GcmConfiguration gcmConfiguration,
                    ApnConfiguration apnConfiguration,
                    AccountsManager accounts,
                    DirectoryManager directory)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException
  {
    this.accounts  = accounts;
    this.directory = directory;

    this.gcmSender = new GCMSender(gcmConfiguration.getApiKey());
    this.apnSender = new APNSender(apnConfiguration.getCertificate(), apnConfiguration.getKey());
  }

  public void sendMessage(String destination, MessageProtos.OutgoingMessageSignal outgoingMessage)
      throws IOException, NoSuchUserException
  {
    Optional<Account> account = accounts.get(destination);

    if (!account.isPresent()) {
      directory.remove(destination);
      throw new NoSuchUserException("No such local destination: " + destination);
    }

    String signalingKey              = account.get().getSignalingKey();
    EncryptedOutgoingMessage message = new EncryptedOutgoingMessage(outgoingMessage, signalingKey);

    if      (account.get().getGcmRegistrationId() != null) sendGcmMessage(account.get(), message);
    else if (account.get().getApnRegistrationId() != null) sendApnMessage(account.get(), message);
    else                                             throw new NoSuchUserException("No push identifier!");
  }

  private void sendGcmMessage(Account account, EncryptedOutgoingMessage outgoingMessage)
      throws IOException, NoSuchUserException
  {
    try {
      String canonicalId = gcmSender.sendMessage(account.getGcmRegistrationId(),
                                                 outgoingMessage);

      if (canonicalId != null) {
        account.setGcmRegistrationId(canonicalId);
        accounts.update(account);
      }

    } catch (NoSuchUserException e) {
      logger.debug("No Such User", e);
      account.setGcmRegistrationId(null);
      accounts.update(account);
      throw new NoSuchUserException("User no longer exists in GCM.");
    }
  }

  private void sendApnMessage(Account account, EncryptedOutgoingMessage outgoingMessage)
      throws IOException
  {
    apnSender.sendMessage(account.getApnRegistrationId(), outgoingMessage);
  }

}
