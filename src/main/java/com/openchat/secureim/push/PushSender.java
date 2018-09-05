package com.openchat.secureim.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.CryptoEncodingException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;
import com.openchat.secureim.entities.MessageProtos;
import com.openchat.secureim.entities.PendingMessage;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;

import static com.openchat.secureim.entities.MessageProtos.OutgoingMessageSignal;

public class PushSender {

  private final Logger logger = LoggerFactory.getLogger(PushSender.class);

  private final GCMSender       gcmSender;
  private final APNSender       apnSender;
  private final WebsocketSender webSocketSender;

  public PushSender(GCMSender gcmClient,
                    APNSender apnSender,
                    WebsocketSender websocketSender)
  {
    this.gcmSender       = gcmClient;
    this.apnSender       = apnSender;
    this.webSocketSender = websocketSender;
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

  private void sendGcmMessage(Account account, Device device, PendingMessage pendingMessage) {
    String number         = account.getNumber();
    long   deviceId       = device.getId();
    String registrationId = device.getGcmId();

    gcmSender.sendMessage(number, deviceId, registrationId, pendingMessage);
  }

  private void sendApnMessage(Account account, Device device, PendingMessage outgoingMessage)
      throws TransientPushFailureException
  {
    apnSender.sendMessage(account, device, device.getApnId(), outgoingMessage);
  }

  private void sendWebSocketMessage(Account account, Device device, PendingMessage outgoingMessage)
  {
    webSocketSender.sendMessage(account, device, outgoingMessage);
  }
}
