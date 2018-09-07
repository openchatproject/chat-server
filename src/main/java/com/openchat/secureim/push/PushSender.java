package com.openchat.secureim.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.ApnMessage;
import com.openchat.secureim.entities.CryptoEncodingException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;
import com.openchat.secureim.entities.GcmMessage;
import com.openchat.secureim.entities.PendingMessage;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;

import static com.openchat.secureim.entities.MessageProtos.OutgoingMessageSignal;

public class PushSender {

  private final Logger logger = LoggerFactory.getLogger(PushSender.class);

  private static final String APN_PAYLOAD = "{\"aps\":{\"sound\":\"default\",\"alert\":{\"loc-key\":\"APN_Message\"},\"content-available\":1,\"category\":\"Signal_Message\"}}";

  private final PushServiceClient pushServiceClient;
  private final WebsocketSender   webSocketSender;

  public PushSender(PushServiceClient pushServiceClient, WebsocketSender websocketSender) {
    this.pushServiceClient = pushServiceClient;
    this.webSocketSender   = websocketSender;
  }

  public void sendMessage(Account account, Device device, OutgoingMessageSignal message)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    try {
      boolean                  isReceipt        = message.getType() == OutgoingMessageSignal.Type.RECEIPT_VALUE;
      String                   signalingKey     = device.getSignalingKey();
      EncryptedOutgoingMessage encryptedMessage = new EncryptedOutgoingMessage(message, signalingKey);
      PendingMessage           pendingMessage   = new PendingMessage(message.getSource(),
                                                                     message.getTimestamp(),
                                                                     isReceipt,
                                                                     encryptedMessage.serialize());

      sendMessage(account, device, pendingMessage);
    } catch (CryptoEncodingException e) {
      throw new NotPushRegisteredException(e);
    }
  }

  public void sendMessage(Account account, Device device, PendingMessage pendingMessage)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    if      (device.getGcmId() != null)   sendGcmMessage(account, device, pendingMessage);
    else if (device.getApnId() != null)   sendApnMessage(account, device, pendingMessage);
    else if (device.getFetchesMessages()) sendWebSocketMessage(account, device, pendingMessage);
    else                                  throw new NotPushRegisteredException("No delivery possible!");
  }

  private void sendGcmMessage(Account account, Device device, PendingMessage pendingMessage)
      throws TransientPushFailureException
  {
    String     number         = account.getNumber();
    long       deviceId       = device.getId();
    String     registrationId = device.getGcmId();
    GcmMessage gcmMessage     = new GcmMessage(registrationId, number, (int)deviceId,
                                               pendingMessage.getEncryptedOutgoingMessage(),
                                               pendingMessage.isReceipt()                              );

    pushServiceClient.send(gcmMessage);
  }

  private void sendApnMessage(Account account, Device device, PendingMessage outgoingMessage)
      throws TransientPushFailureException
  {
    boolean online = webSocketSender.sendMessage(account, device, outgoingMessage, true);

    if (!online && !outgoingMessage.isReceipt()) {
      ApnMessage apnMessage = new ApnMessage(device.getApnId(), account.getNumber(),
                                             (int)device.getId(), APN_PAYLOAD);
      pushServiceClient.send(apnMessage);
    }
  }

  private void sendWebSocketMessage(Account account, Device device, PendingMessage outgoingMessage)
  {
    webSocketSender.sendMessage(account, device, outgoingMessage, false);
  }
}
