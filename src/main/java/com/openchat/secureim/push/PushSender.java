package com.openchat.secureim.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.ApnMessage;
import com.openchat.secureim.entities.CryptoEncodingException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;
import com.openchat.secureim.entities.GcmMessage;
import com.openchat.secureim.push.WebsocketSender.DeliveryStatus;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;

import static com.openchat.secureim.entities.MessageProtos.OutgoingMessageSignal;

public class PushSender {

  private final Logger logger = LoggerFactory.getLogger(PushSender.class);

  private static final String APN_PAYLOAD = "{\"aps\":{\"sound\":\"default\",\"badge\":%d,\"alert\":{\"loc-key\":\"APN_Message\"}}}";

  private final PushServiceClient pushServiceClient;
  private final WebsocketSender   webSocketSender;

  public PushSender(PushServiceClient pushServiceClient, WebsocketSender websocketSender) {
    this.pushServiceClient = pushServiceClient;
    this.webSocketSender   = websocketSender;
  }

  public void sendMessage(Account account, Device device, OutgoingMessageSignal message)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    if      (device.getGcmId() != null)   sendGcmMessage(account, device, message);
    else if (device.getApnId() != null)   sendApnMessage(account, device, message);
    else if (device.getFetchesMessages()) sendWebSocketMessage(account, device, message);
    else                                  throw new NotPushRegisteredException("No delivery possible!");
  }

  public WebsocketSender getWebSocketSender() {
    return webSocketSender;
  }

  private void sendGcmMessage(Account account, Device device, OutgoingMessageSignal message)
      throws TransientPushFailureException, NotPushRegisteredException
  {
    if (device.getFetchesMessages()) sendNotificationGcmMessage(account, device, message);
    else                             sendPayloadGcmMessage(account, device, message);
  }

  private void sendPayloadGcmMessage(Account account, Device device, OutgoingMessageSignal message)
      throws TransientPushFailureException, NotPushRegisteredException
  {
    try {
      String                   number           = account.getNumber();
      long                     deviceId         = device.getId();
      String                   registrationId   = device.getGcmId();
      boolean                  isReceipt        = message.getType() == OutgoingMessageSignal.Type.RECEIPT_VALUE;
      EncryptedOutgoingMessage encryptedMessage = new EncryptedOutgoingMessage(message, device.getSignalingKey());
      GcmMessage               gcmMessage       = new GcmMessage(registrationId, number, (int) deviceId,
                                                                 encryptedMessage.toEncodedString(), isReceipt, false);

      pushServiceClient.send(gcmMessage);
    } catch (CryptoEncodingException e) {
      throw new NotPushRegisteredException(e);
    }
  }

  private void sendNotificationGcmMessage(Account account, Device device, OutgoingMessageSignal message)
      throws TransientPushFailureException
  {
    DeliveryStatus deliveryStatus = webSocketSender.sendMessage(account, device, message, WebsocketSender.Type.GCM);

    if (!deliveryStatus.isDelivered()) {
      GcmMessage gcmMessage = new GcmMessage(device.getGcmId(), account.getNumber(),
                                             (int)device.getId(), "", false, true);

      pushServiceClient.send(gcmMessage);
    }
  }

  private void sendApnMessage(Account account, Device device, OutgoingMessageSignal outgoingMessage)
      throws TransientPushFailureException
  {
    DeliveryStatus deliveryStatus = webSocketSender.sendMessage(account, device, outgoingMessage, WebsocketSender.Type.APN);

    if (!deliveryStatus.isDelivered() && outgoingMessage.getType() != OutgoingMessageSignal.Type.RECEIPT_VALUE) {
      ApnMessage apnMessage = new ApnMessage(device.getApnId(), account.getNumber(), (int)device.getId(),
                                             String.format(APN_PAYLOAD, deliveryStatus.getMessageQueueDepth()));
      pushServiceClient.send(apnMessage);
    }
  }

  private void sendWebSocketMessage(Account account, Device device, OutgoingMessageSignal outgoingMessage)
  {
    webSocketSender.sendMessage(account, device, outgoingMessage, WebsocketSender.Type.WEB);
  }
}
