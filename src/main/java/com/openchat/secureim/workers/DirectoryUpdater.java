package com.openchat.secureim.workers;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.ClientContact;
import com.openchat.secureim.federation.FederatedClient;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.DirectoryManager;
import com.openchat.secureim.storage.DirectoryManager.BatchOperationHandle;
import com.openchat.secureim.util.Base64;
import com.openchat.secureim.util.Util;

import java.util.Iterator;
import java.util.List;

public class DirectoryUpdater {

  private final Logger logger = LoggerFactory.getLogger(DirectoryUpdater.class);

  private final AccountsManager        accountsManager;
  private final FederatedClientManager federatedClientManager;
  private final DirectoryManager       directory;

  public DirectoryUpdater(AccountsManager accountsManager,
                          FederatedClientManager federatedClientManager,
                          DirectoryManager directory)
  {
    this.accountsManager        = accountsManager;
    this.federatedClientManager = federatedClientManager;
    this.directory              = directory;
  }

  public void updateFromLocalDatabase() {
    BatchOperationHandle batchOperation = directory.startBatchOperation();

    try {
      Iterator<Account> accounts = accountsManager.getAll();

      if (accounts == null)
        return;

      while (accounts.hasNext()) {
        Account account = accounts.next();

        if (account.isActive()) {
          byte[]        token         = Util.getContactToken(account.getNumber());
          ClientContact clientContact = new ClientContact(token, null, account.getSupportsSms());

          directory.add(batchOperation, clientContact);

          logger.debug("Adding local token: " + Base64.encodeBytesWithoutPadding(token));
        } else {
          directory.remove(batchOperation, account.getNumber());
        }
      }
    } finally {
      directory.stopBatchOperation(batchOperation);
    }

    logger.info("Local directory is updated.");
  }

  public void updateFromPeers() {
    logger.info("Updating peer directories.");
    List<FederatedClient> clients = federatedClientManager.getClients();

    for (FederatedClient client : clients) {
      logger.info("Updating directory from peer: " + client.getPeerName());
      BatchOperationHandle handle = directory.startBatchOperation();

      try {
        int userCount = client.getUserCount();
        int retrieved = 0;

        logger.info("Remote peer user count: " + userCount);

        while (retrieved < userCount) {
          List<ClientContact> clientContacts = client.getUserTokens(retrieved);

          if (clientContacts == null)
            break;

          for (ClientContact clientContact : clientContacts) {
            clientContact.setRelay(client.getPeerName());

            Optional<ClientContact> existing = directory.get(clientContact.getToken());

            if (!clientContact.isInactive() && (!existing.isPresent() || existing.get().getRelay().equals(client.getPeerName()))) {
              directory.add(handle, clientContact);
            } else {
              if (existing != null && client.getPeerName().equals(existing.get().getRelay())) {
                directory.remove(clientContact.getToken());
              }
            }
          }

          retrieved += clientContacts.size();
        }

        logger.info("Update from peer complete.");
      } finally {
        directory.stopBatchOperation(handle);
      }
    }

    logger.info("Update from peer directories complete.");
  }
}
