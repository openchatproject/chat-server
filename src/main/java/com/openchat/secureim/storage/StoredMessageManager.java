package com.openchat.secureim.storage;

import com.openchat.secureim.entities.EncryptedOutgoingMessage;

public class StoredMessageManager {
  StoredMessages storedMessages;
  public StoredMessageManager(StoredMessages storedMessages) {
    this.storedMessages = storedMessages;
  }

  public void storeMessage(Account account, EncryptedOutgoingMessage outgoingMessage) {
    storedMessages.insert(account.getId(), outgoingMessage);
  }
}
