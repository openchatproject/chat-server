package com.openchat.secureim.storage;

import com.openchat.secureim.entities.CryptoEncodingException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;

import java.io.IOException;
import java.util.List;

public class StoredMessageManager {
  StoredMessages storedMessages;
  public StoredMessageManager(StoredMessages storedMessages) {
    this.storedMessages = storedMessages;
  }

  public void storeMessage(Device device, EncryptedOutgoingMessage outgoingMessage)
      throws CryptoEncodingException
  {
    storedMessages.insert(device.getId(), outgoingMessage.serialize());
  }

  public List<String> getStoredMessage(Device device) {
    return storedMessages.getMessagesForAccountId(device.getId());
  }
}
