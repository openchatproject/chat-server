package com.openchat.secureim.push;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.SharedMetricRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.ApnMessage;
import com.openchat.secureim.entities.GcmMessage;
import com.openchat.secureim.push.ApnFallbackManager.ApnFallbackTask;
import com.openchat.secureim.push.WebsocketSender.DeliveryStatus;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.util.BlockingThreadPoolExecutor;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.util.Util;
import com.openchat.secureim.websocket.WebsocketAddress;

import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;
import io.dropwizard.lifecycle.Managed;
import static com.openchat.secureim.entities.MessageProtos.Envelope;

public class PushSender implements Managed {

  private final Logger logger = LoggerFactory.getLogger(PushSender.class);

  private static final String APN_PAYLOAD = "{\"aps\":{\"sound\":\"default\",\"badge\":%d,\"alert\":{\"loc-key\":\"APN_Message\"}}}";

  private final ApnFallbackManager         apnFallbackManager;
  private final PushServiceClient          pushServiceClient;
  private final WebsocketSender            webSocketSender;
  private final BlockingThreadPoolExecutor executor;
  private final int                        queueSize;

  public PushSender(ApnFallbackManager apnFallbackManager, PushServiceClient pushServiceClient,
                    WebsocketSender websocketSender, int queueSize)
  {
    this.apnFallbackManager = apnFallbackManager;
    this.pushServiceClient  = pushServiceClient;
    this.webSocketSender    = websocketSender;
    this.queueSize          = queueSize;
    this.executor           = new BlockingThreadPoolExecutor(50, queueSize);

    SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME)
                          .register(name(PushSender.class, "send_queue_depth"),
                                    new Gauge<Integer>() {
                                      @Override
                                      public Integer getValue() {
                                        return executor.getSize();
                                      }
                                    });
  }

  public void sendMessage(final Account account, final Device device, final Envelope message)
      throws NotPushRegisteredException
  {
    if (device.getGcmId() == null && device.getApnId() == null && !device.getFetchesMessages()) {
      throw new NotPushRegisteredException("No delivery possible!");
    }

    if (queueSize > 0) {
      executor.execute(new Runnable() {
        @Override
        public void run() {
          sendSynchronousMessage(account, device, message);
        }
      });
    } else {
      sendSynchronousMessage(account, device, message);
    }
  }

  public void sendQueuedNotification(Account account, Device device, int messageQueueDepth, boolean fallback)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    if      (device.getGcmId() != null)    sendGcmNotification(account, device);
    else if (device.getApnId() != null)    sendApnNotification(account, device, messageQueueDepth, fallback);
    else if (!device.getFetchesMessages()) throw new NotPushRegisteredException("No notification possible!");
  }

  public WebsocketSender getWebSocketSender() {
    return webSocketSender;
  }

  private void sendSynchronousMessage(Account account, Device device, Envelope message) {
    if      (device.getGcmId() != null)   sendGcmMessage(account, device, message);
    else if (device.getApnId() != null)   sendApnMessage(account, device, message);
    else if (device.getFetchesMessages()) sendWebSocketMessage(account, device, message);
    else                                  throw new AssertionError();
  }

  private void sendGcmMessage(Account account, Device device, Envelope message) {
    DeliveryStatus deliveryStatus = webSocketSender.sendMessage(account, device, message, WebsocketSender.Type.GCM);

    if (!deliveryStatus.isDelivered()) {
      sendGcmNotification(account, device);
    }
  }

  private void sendGcmNotification(Account account, Device device) {
    try {
      GcmMessage gcmMessage = new GcmMessage(device.getGcmId(), account.getNumber(),
                                             (int)device.getId(), "", false, true);

      pushServiceClient.send(gcmMessage);
    } catch (TransientPushFailureException e) {
      logger.warn("SILENT PUSH LOSS", e);
    }
  }

  private void sendApnMessage(Account account, Device device, Envelope outgoingMessage) {
    DeliveryStatus deliveryStatus = webSocketSender.sendMessage(account, device, outgoingMessage, WebsocketSender.Type.APN);

    if (!deliveryStatus.isDelivered() && outgoingMessage.getType() != Envelope.Type.RECEIPT) {
      boolean fallback = !outgoingMessage.getSource().equals(account.getNumber());
      sendApnNotification(account, device, deliveryStatus.getMessageQueueDepth(), fallback);
    }
  }

  private void sendApnNotification(Account account, Device device, int messageQueueDepth, boolean fallback) {
    ApnMessage apnMessage;

    if (!Util.isEmpty(device.getVoipApnId())) {
      apnMessage = new ApnMessage(device.getVoipApnId(), account.getNumber(), (int)device.getId(),
                                  String.format(APN_PAYLOAD, messageQueueDepth),
                                  true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));

      if (fallback) {
        apnFallbackManager.schedule(new WebsocketAddress(account.getNumber(), device.getId()),
                                    new ApnFallbackTask(device.getApnId(), apnMessage));
      }
    } else {
      apnMessage = new ApnMessage(device.getApnId(), account.getNumber(), (int)device.getId(),
                                  String.format(APN_PAYLOAD, messageQueueDepth),
                                  false, ApnMessage.MAX_EXPIRATION);
    }

    try {
      pushServiceClient.send(apnMessage);
    } catch (TransientPushFailureException e) {
      logger.warn("SILENT PUSH LOSS", e);
    }
  }

  private void sendWebSocketMessage(Account account, Device device, Envelope outgoingMessage)
  {
    webSocketSender.sendMessage(account, device, outgoingMessage, WebsocketSender.Type.WEB);
  }

  @Override
  public void start() throws Exception {

  }

  @Override
  public void stop() throws Exception {
    executor.shutdown();
    executor.awaitTermination(5, TimeUnit.MINUTES);
  }
}
