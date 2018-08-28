package com.openchat.secureim.push;

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

  public void fillLocalAccountsCache(Map<String, Pair<Boolean, Set<Long>>> destinations, Map<Pair<String, Long>, Account> accountCache, List<String> numbersMissingDevices) {
    for (Map.Entry<String, Pair<Boolean, Set<Long>>> destination : destinations.entrySet()) {
      if (destination.getValue().first()) {
        String number = destination.getKey();
        List<Account> accountList = accounts.getAllByNumber(number);
        Set<Long> deviceIdsIncluded = destination.getValue().second();
        if (accountList.size() != deviceIdsIncluded.size())
          numbersMissingDevices.add(number);
        else {
          for (Account account : accountList) {
            if (!deviceIdsIncluded.contains(account.getDeviceId())) {
              numbersMissingDevices.add(number);
              break;
            }
          }
          for (Account account : accountList)
            accountCache.put(new Pair<>(number, account.getDeviceId()), account);
        }
      }
    }
  }

  public void sendMessage(Account account, MessageProtos.OutgoingMessageSignal outgoingMessage)
      throws IOException, NoSuchUserException
  {
    String signalingKey              = account.getSignalingKey();
    EncryptedOutgoingMessage message = new EncryptedOutgoingMessage(outgoingMessage, signalingKey);

    if      (account.getGcmRegistrationId() != null) sendGcmMessage(account, message);
    else if (account.getApnRegistrationId() != null) sendApnMessage(account, message);
    else if (account.getFetchesMessages())           storeFetchedMessage(account, message);
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

  private void storeFetchedMessage(Account account, EncryptedOutgoingMessage outgoingMessage) throws IOException {
    storedMessageManager.storeMessage(account, outgoingMessage);
  }
}
