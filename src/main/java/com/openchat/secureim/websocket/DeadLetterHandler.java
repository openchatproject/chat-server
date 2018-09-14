package com.openchat.secureim.websocket;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.MessageProtos.OutgoingMessageSignal;
import com.openchat.secureim.storage.MessagesManager;
import com.openchat.secureim.storage.PubSubProtos;

public class DeadLetterHandler {

  private final Logger logger = LoggerFactory.getLogger(DeadLetterHandler.class);

  private final MessagesManager messagesManager;

  public DeadLetterHandler(MessagesManager messagesManager) {
    this.messagesManager = messagesManager;
  }

  public void handle(byte[] channel, PubSubProtos.PubSubMessage pubSubMessage) {
    try {
      WebsocketAddress address = new WebsocketAddress(new String(channel));

      logger.warn("Handling dead letter to: " + address);

      switch (pubSubMessage.getType().getNumber()) {
        case PubSubProtos.PubSubMessage.Type.DELIVER_VALUE:
          OutgoingMessageSignal message = OutgoingMessageSignal.parseFrom(pubSubMessage.getContent());
          messagesManager.insert(address.getNumber(), address.getDeviceId(), message);
          break;
      }
    } catch (InvalidProtocolBufferException e) {
      logger.warn("Bad pubsub message", e);
    } catch (InvalidWebsocketAddressException e) {
      logger.warn("Invalid websocket address", e);
    }
  }

}
