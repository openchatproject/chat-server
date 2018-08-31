package com.openchat.secureim.storage;

import com.openchat.secureim.entities.CryptoEncodingException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;
import com.openchat.secureim.websocket.WebsocketAddress;

import java.util.List;

public class StoredMessageManager {

  private final StoredMessages storedMessages;
  private final PubSubManager  pubSubManager;

  public StoredMessageManager(StoredMessages storedMessages, PubSubManager pubSubManager) {
    this.storedMessages = storedMessages;
    this.pubSubManager  = pubSubManager;
  }

  public void storeMessage(Account account, Device device, EncryptedOutgoingMessage outgoingMessage)
      throws CryptoEncodingException
  {
    storeMessage(account, device, outgoingMessage.serialize());
  }

  public void storeMessages(Account account, Device device, List<String> serializedMessages) {
    for (String serializedMessage : serializedMessages) {
      storeMessage(account, device, serializedMessage);
    }
  }

  private void storeMessage(Account account, Device device, String serializedMessage) {
    if (device.getFetchesMessages()) {
      WebsocketAddress address       = new WebsocketAddress(account.getId(), device.getId());
      PubSubMessage    pubSubMessage = new PubSubMessage(PubSubMessage.TYPE_DELIVER, serializedMessage);

      if (!pubSubManager.publish(address, pubSubMessage)) {
        storedMessages.insert(account.getId(), device.getId(), serializedMessage);
        pubSubManager.publish(address, new PubSubMessage(PubSubMessage.TYPE_QUERY_DB, null));
      }

      return;
    }

    storedMessages.insert(account.getId(), device.getId(), serializedMessage);
  }

  public List<String> getOutgoingMessages(Account account, Device device) {
    return storedMessages.getMessagesForDevice(account.getId(), device.getId());
  }
}
