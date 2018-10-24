package com.openchat.secureim.websocket;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Timer;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.push.ApnFallbackManager;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.push.ReceiptSender;
import com.openchat.secureim.redis.RedisOperation;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.MessagesManager;
import com.openchat.secureim.storage.PubSubManager;
import com.openchat.secureim.storage.PubSubProtos.PubSubMessage;
import com.openchat.secureim.util.Constants;
import com.openchat.websocket.session.WebSocketSessionContext;
import com.openchat.websocket.setup.WebSocketConnectListener;

import java.security.SecureRandom;

import static com.codahale.metrics.MetricRegistry.name;

public class AuthenticatedConnectListener implements WebSocketConnectListener {

  private static final Logger         logger         = LoggerFactory.getLogger(WebSocketConnection.class);
  private static final MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);
  private static final Timer          durationTimer  = metricRegistry.timer(name(WebSocketConnection.class, "connected_duration"));

  private final PushSender         pushSender;
  private final ReceiptSender      receiptSender;
  private final MessagesManager    messagesManager;
  private final PubSubManager      pubSubManager;
  private final ApnFallbackManager apnFallbackManager;

  public AuthenticatedConnectListener(PushSender pushSender,
                                      ReceiptSender receiptSender,
                                      MessagesManager messagesManager,
                                      PubSubManager pubSubManager,
                                      ApnFallbackManager apnFallbackManager)
  {
    this.pushSender         = pushSender;
    this.receiptSender      = receiptSender;
    this.messagesManager    = messagesManager;
    this.pubSubManager      = pubSubManager;
    this.apnFallbackManager = apnFallbackManager;
  }

  @Override
  public void onWebSocketConnect(WebSocketSessionContext context) {
    final Account                 account        = context.getAuthenticated(Account.class);
    final Device                  device         = account.getAuthenticatedDevice().get();
    final String                  connectionId   = String.valueOf(new SecureRandom().nextLong());
    final Timer.Context           timer          = durationTimer.time();
    final WebsocketAddress        address        = new WebsocketAddress(account.getNumber(), device.getId());
    final WebSocketConnection     connection     = new WebSocketConnection(pushSender, receiptSender,
                                                                           messagesManager, account, device,
                                                                           context.getClient(), connectionId);
    final PubSubMessage           connectMessage = PubSubMessage.newBuilder().setType(PubSubMessage.Type.CONNECTED)
                                                                .setContent(ByteString.copyFrom(connectionId.getBytes()))
                                                                .build();

    RedisOperation.unchecked(() -> apnFallbackManager.cancel(account, device));
    pubSubManager.publish(address, connectMessage);
    pubSubManager.subscribe(address, connection);

    context.addListener(new WebSocketSessionContext.WebSocketEventListener() {
      @Override
      public void onWebSocketClose(WebSocketSessionContext context, int statusCode, String reason) {
        pubSubManager.unsubscribe(address, connection);
        timer.stop();
      }
    });
  }
}

