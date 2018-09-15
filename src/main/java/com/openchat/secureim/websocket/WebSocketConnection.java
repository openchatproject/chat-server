package com.openchat.secureim.websocket;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.dispatch.DispatchChannel;
import com.openchat.secureim.entities.CryptoEncodingException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;
import com.openchat.secureim.push.NotPushRegisteredException;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.push.TransientPushFailureException;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.MessagesManager;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.util.Pair;
import com.openchat.websocket.WebSocketClient;
import com.openchat.websocket.messages.WebSocketResponseMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.codahale.metrics.MetricRegistry.name;
import static com.openchat.secureim.entities.MessageProtos.OutgoingMessageSignal;
import static com.openchat.secureim.storage.PubSubProtos.PubSubMessage;

public class WebSocketConnection implements DispatchChannel {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketConnection.class);

  private final AccountsManager  accountsManager;
  private final PushSender       pushSender;
  private final MessagesManager  messagesManager;

  private final Account          account;
  private final Device           device;
  private final WebSocketClient  client;

  private long connectionStartTime;

  public WebSocketConnection(AccountsManager accountsManager,
                             PushSender pushSender,
                             MessagesManager messagesManager,
                             Account account,
                             Device device,
                             WebSocketClient client)
  {
    this.accountsManager = accountsManager;
    this.pushSender      = pushSender;
    this.messagesManager = messagesManager;
    this.account         = account;
    this.device          = device;
    this.client          = client;
  }

  @Override
  public void onDispatchMessage(String channel, byte[] message) {
    try {
      PubSubMessage pubSubMessage = PubSubMessage.parseFrom(message);

      switch (pubSubMessage.getType().getNumber()) {
        case PubSubMessage.Type.QUERY_DB_VALUE:
          processStoredMessages();
          break;
        case PubSubMessage.Type.DELIVER_VALUE:
          sendMessage(OutgoingMessageSignal.parseFrom(pubSubMessage.getContent()), Optional.<Long>absent());
          break;
        default:
          logger.warn("Unknown pubsub message: " + pubSubMessage.getType().getNumber());
      }
    } catch (InvalidProtocolBufferException e) {
      logger.warn("Protobuf parse error", e);
    }
  }

  @Override
  public void onDispatchUnsubscribed(String channel) {
    client.close(1000, "OK");
  }

  public void onDispatchSubscribed(String channel) {
    processStoredMessages();
  }

  private void sendMessage(final OutgoingMessageSignal message,
                           final Optional<Long> storedMessageId)
  {
    try {
      EncryptedOutgoingMessage                   encryptedMessage = new EncryptedOutgoingMessage(message, device.getSignalingKey());
      Optional<byte[]>                           body             = Optional.fromNullable(encryptedMessage.toByteArray());
      ListenableFuture<WebSocketResponseMessage> response         = client.sendRequest("PUT", "/api/v1/message", body);

      Futures.addCallback(response, new FutureCallback<WebSocketResponseMessage>() {
        @Override
        public void onSuccess(@Nullable WebSocketResponseMessage response) {
          boolean isReceipt = message.getType() == OutgoingMessageSignal.Type.RECEIPT_VALUE;

          if (isSuccessResponse(response)) {
            if (storedMessageId.isPresent()) messagesManager.delete(storedMessageId.get());
            if (!isReceipt)                  sendDeliveryReceiptFor(message);
          } else if (!isSuccessResponse(response) & !storedMessageId.isPresent()) {
            requeueMessage(message);
          }
        }

        @Override
        public void onFailure(@Nonnull Throwable throwable) {
          if (!storedMessageId.isPresent()) requeueMessage(message);
        }

        private boolean isSuccessResponse(WebSocketResponseMessage response) {
          return response != null && response.getStatus() >= 200 && response.getStatus() < 300;
        }
      });
    } catch (CryptoEncodingException e) {
      logger.warn("Bad signaling key", e);
    }
  }

  private void requeueMessage(OutgoingMessageSignal message) {
    try {
      pushSender.sendMessage(account, device, message);
    } catch (NotPushRegisteredException | TransientPushFailureException e) {
      logger.warn("requeueMessage", e);
      messagesManager.insert(account.getNumber(), device.getId(), message);
    }
  }

  private void sendDeliveryReceiptFor(OutgoingMessageSignal message) {
    try {
      Optional<Account> source = accountsManager.get(message.getSource());

      if (!source.isPresent()) {
        logger.warn(String.format("Source account disappeared? (%s)", message.getSource()));
        return;
      }

      OutgoingMessageSignal.Builder receipt =
          OutgoingMessageSignal.newBuilder()
                               .setSource(account.getNumber())
                               .setSourceDevice((int) device.getId())
                               .setTimestamp(message.getTimestamp())
                               .setType(OutgoingMessageSignal.Type.RECEIPT_VALUE);

      for (Device device : source.get().getDevices()) {
        pushSender.sendMessage(source.get(), device, receipt.build());
      }
    } catch (NotPushRegisteredException | TransientPushFailureException e) {
      logger.warn("sendDeliveryReceiptFor", "Delivery receipet", e);
    }
  }

  private void processStoredMessages() {
    List<Pair<Long, OutgoingMessageSignal>> messages = messagesManager.getMessagesForDevice(account.getNumber(),
                                                                                            device.getId());

    for (Pair<Long, OutgoingMessageSignal> message : messages) {
      sendMessage(message.second(), Optional.of(message.first()));
    }
  }


}
