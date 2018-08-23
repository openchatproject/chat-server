package com.openchat.secureim.storage;

import com.openchat.secureim.entities.EncryptedOutgoingMessage;

import java.io.IOException;
import java.util.List;

public class StoredMessageManager {
  StoredMessages storedMessages;
  public StoredMessageManager(StoredMessages storedMessages) {
    this.storedMessages = storedMessages;
  }

  public void storeMessage(Account account, EncryptedOutgoingMessage outgoingMessage) throws IOException {
    storedMessages.insert(account.getId(), outgoingMessage.serialize());
  }

  public List<String> getStoredMessage(Account account) {
    return storedMessages.getMessagesForAccountId(account.getId());
  }
}
