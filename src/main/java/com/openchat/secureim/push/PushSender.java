package com.openchat.secureim.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.ApnMessage;
import com.openchat.secureim.entities.GcmMessage;
import com.openchat.secureim.push.ApnFallbackManager.ApnFallbackTask;
import com.openchat.secureim.push.WebsocketSender.DeliveryStatus;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.util.Util;
import com.openchat.secureim.websocket.WebsocketAddress;

import java.util.concurrent.TimeUnit;

import static com.openchat.secureim.entities.MessageProtos.Envelope;

public class PushSender {

  private final Logger logger = LoggerFactory.getLogger(PushSender.class);

  private static final String APN_PAYLOAD = "{\"aps\":{\"sound\":\"default\",\"badge\":%d,\"alert\":{\"loc-key\":\"APN_Message\"}}}";

  private final ApnFallbackManager apnFallbackManager;
  private final PushServiceClient  pushServiceClient;
  private final WebsocketSender    webSocketSender;

  public PushSender(ApnFallbackManager apnFallbackManager, PushServiceClient pushServiceClient, WebsocketSender websocketSender) {
    this.apnFallbackManager = apnFallbackManager;
    this.pushServiceClient  = pushServiceClient;
    this.webSocketSender    = websocketSender;
  }

  public void sendMessage(Account account, Device device, Envelope message)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    if      (device.getGcmId() != null)   sendGcmMessage(account, device, message);
    else if (device.getApnId() != null)   sendApnMessage(account, device, message);
    else if (device.getFetchesMessages()) sendWebSocketMessage(account, device, message);
    else                                  throw new NotPushRegisteredException("No delivery possible!");
  }

  public void sendQueuedNotification(Account account, Device device, int messageQueueDepth)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    if      (device.getGcmId() != null)    sendGcmNotification(account, device);
    else if (device.getApnId() != null)    sendApnNotification(account, device, messageQueueDepth);
    else if (!device.getFetchesMessages()) throw new NotPushRegisteredException("No notification possible!");
  }

  public WebsocketSender getWebSocketSender() {
    return webSocketSender;
  }

  private void sendGcmMessage(Account account, Device device, Envelope message)
      throws TransientPushFailureException
  {
    DeliveryStatus deliveryStatus = webSocketSender.sendMessage(account, device, message, WebsocketSender.Type.GCM);

    if (!deliveryStatus.isDelivered()) {
      sendGcmNotification(account, device);
    }
  }

  private void sendGcmNotification(Account account, Device device)
      throws TransientPushFailureException
  {
    GcmMessage gcmMessage = new GcmMessage(device.getGcmId(), account.getNumber(),
                                           (int)device.getId(), "", false, true);

    pushServiceClient.send(gcmMessage);
  }

  private void sendApnMessage(Account account, Device device, Envelope outgoingMessage)
      throws TransientPushFailureException
  {
    DeliveryStatus deliveryStatus = webSocketSender.sendMessage(account, device, outgoingMessage, WebsocketSender.Type.APN);

    if (!deliveryStatus.isDelivered() && outgoingMessage.getType() != Envelope.Type.RECEIPT) {
      sendApnNotification(account, device, deliveryStatus.getMessageQueueDepth());
    }
  }

  private void sendApnNotification(Account account, Device device, int messageQueueDepth)
      throws TransientPushFailureException
  {
    ApnMessage apnMessage;

    if (!Util.isEmpty(device.getVoipApnId())) {
      apnMessage = new ApnMessage(device.getVoipApnId(), account.getNumber(), (int)device.getId(),
                                  String.format(APN_PAYLOAD, messageQueueDepth),
                                  true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(30));

      apnFallbackManager.schedule(new WebsocketAddress(account.getNumber(), device.getId()),
                                  new ApnFallbackTask(device.getApnId(), apnMessage));
    } else {
      apnMessage = new ApnMessage(device.getApnId(), account.getNumber(), (int)device.getId(),
                                  String.format(APN_PAYLOAD, messageQueueDepth),
                                  false, ApnMessage.MAX_EXPIRATION);
    }

    pushServiceClient.send(apnMessage);
  }

  private void sendWebSocketMessage(Account account, Device device, Envelope outgoingMessage)
  {
    webSocketSender.sendMessage(account, device, outgoingMessage, WebsocketSender.Type.WEB);
  }
}
