package com.openchat.secureim.transport;

import android.content.Context;
import android.util.Log;

import com.google.protobuf.ByteString;

import com.openchat.secureim.crypto.KeyExchangeProcessor;
import com.openchat.secureim.crypto.OpenchatServiceCipher;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsSmsColumns;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.mms.PartParser;
import com.openchat.secureim.push.PushServiceSocketFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.secureim.util.Util;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.protocol.CiphertextMessage;
import com.openchat.protocal.state.PreKeyBundle;
import com.openchat.protocal.state.SessionStore;
import com.openchat.imservice.crypto.AttachmentCipher;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.crypto.TransportDetails;
import com.openchat.imservice.push.MismatchedDevices;
import com.openchat.imservice.push.MismatchedDevicesException;
import com.openchat.imservice.push.OutgoingPushMessage;
import com.openchat.imservice.push.OutgoingPushMessageList;
import com.openchat.imservice.push.PushAddress;
import com.openchat.imservice.push.PushAttachmentData;
import com.openchat.imservice.push.PushAttachmentPointer;
import com.openchat.imservice.push.PushBody;
import com.openchat.imservice.push.PushMessageProtos.PushMessageContent;
import com.openchat.imservice.push.PushServiceSocket;
import com.openchat.imservice.push.PushTransportDetails;
import com.openchat.imservice.push.StaleDevices;
import com.openchat.imservice.push.StaleDevicesException;
import com.openchat.imservice.push.UnregisteredUserException;
import com.openchat.imservice.storage.SessionUtil;
import com.openchat.imservice.storage.OpenchatServiceSessionStore;
import com.openchat.imservice.util.Base64;
import com.openchat.imservice.util.InvalidNumberException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.PduBody;
import ws.com.google.android.mms.pdu.SendReq;

import static com.openchat.imservice.push.PushMessageProtos.IncomingPushMessageOpenchat;
import static com.openchat.imservice.push.PushMessageProtos.PushMessageContent.AttachmentPointer;
import static com.openchat.imservice.push.PushMessageProtos.PushMessageContent.GroupContext;

public class PushTransport extends BaseTransport {

  private final Context      context;
  private final MasterSecret masterSecret;

  public PushTransport(Context context, MasterSecret masterSecret) {
    this.context      = context.getApplicationContext();
    this.masterSecret = masterSecret;
  }

  public void deliver(SmsMessageRecord message)
      throws IOException, UntrustedIdentityException
  {
    try {
      Recipient         recipient = message.getIndividualRecipient();
      long              threadId  = message.getThreadId();
      PushServiceSocket socket    = PushServiceSocketFactory.create(context);
      byte[]            plaintext = getPlaintextMessage(message);

      deliver(socket, recipient, threadId, plaintext);

      if (message.isEndSession()) {
        SessionStore sessionStore = new OpenchatServiceSessionStore(context, masterSecret);
        sessionStore.deleteAllSessions(recipient.getRecipientId());
        KeyExchangeProcessor.broadcastSecurityUpdateEvent(context, threadId);
      }

      context.sendBroadcast(constructSentIntent(context, message.getId(), message.getType(), true, true));

    } catch (InvalidNumberException e) {
      Log.w("PushTransport", e);
      throw new IOException("Badly formatted number.");
    }
  }

  public void deliver(SendReq message, long threadId)
      throws IOException, RecipientFormattingException, InvalidNumberException, EncapsulatedExceptions
  {
    PushServiceSocket socket      = PushServiceSocketFactory.create(context);
    byte[]            plaintext   = getPlaintextMessage(socket, message);
    String            destination = message.getTo()[0].getString();

    Recipients recipients;

    if (GroupUtil.isEncodedGroup(destination)) {
      recipients = DatabaseFactory.getGroupDatabase(context)
                                  .getGroupMembers(GroupUtil.getDecodedId(destination), false);
    } else {
      recipients = RecipientFactory.getRecipientsFromString(context, destination, false);
    }

    List<UntrustedIdentityException> untrustedIdentities = new LinkedList<>();
    List<UnregisteredUserException>  unregisteredUsers   = new LinkedList<>();

    for (Recipient recipient : recipients.getRecipientsList()) {
      try {
        deliver(socket, recipient, threadId, plaintext);
      } catch (UntrustedIdentityException e) {
        Log.w("PushTransport", e);
        untrustedIdentities.add(e);
      } catch (UnregisteredUserException e) {
        Log.w("PushTransport", e);
        unregisteredUsers.add(e);
      }
    }

    if (!untrustedIdentities.isEmpty() || !unregisteredUsers.isEmpty()) {
      throw new EncapsulatedExceptions(untrustedIdentities, unregisteredUsers);
    }
  }

  private void deliver(PushServiceSocket socket, Recipient recipient, long threadId, byte[] plaintext)
      throws IOException, InvalidNumberException, UntrustedIdentityException
  {
    for (int i=0;i<3;i++) {
      try {
        OutgoingPushMessageList messages = getEncryptedMessages(socket, threadId,
                                                                recipient, plaintext);
        socket.sendMessage(messages);

        return;
      } catch (MismatchedDevicesException mde) {
        Log.w("PushTransport", mde);
        handleMismatchedDevices(socket, threadId, recipient, mde.getMismatchedDevices());
      } catch (StaleDevicesException ste) {
        Log.w("PushTransport", ste);
        handleStaleDevices(recipient, ste.getStaleDevices());
      }
    }
  }

  private List<PushAttachmentPointer> getPushAttachmentPointers(PushServiceSocket socket, PduBody body)
      throws IOException
  {
    List<PushAttachmentPointer> attachments = new LinkedList<>();

    for (int i=0;i<body.getPartsNum();i++) {
      String contentType = Util.toIsoString(body.getPart(i).getContentType());
      if (ContentType.isImageType(contentType) ||
          ContentType.isAudioType(contentType) ||
          ContentType.isVideoType(contentType))
      {
        attachments.add(getPushAttachmentPointer(socket, contentType, body.getPart(i).getData()));
      }
    }

    return attachments;
  }

  private PushAttachmentPointer getPushAttachmentPointer(PushServiceSocket socket,
                                                         String contentType, byte[] data)
      throws IOException
  {
    AttachmentCipher   cipher               = new AttachmentCipher();
    byte[]             key                  = cipher.getCombinedKeyMaterial();
    byte[]             ciphertextAttachment = cipher.encrypt(data);
    PushAttachmentData attachmentData       = new PushAttachmentData(contentType, ciphertextAttachment);
    long               attachmentId         = socket.sendAttachment(attachmentData);

    return new PushAttachmentPointer(contentType, attachmentId, key);
  }

  private void handleMismatchedDevices(PushServiceSocket socket, long threadId,
                                       Recipient recipient,
                                       MismatchedDevices mismatchedDevices)
      throws InvalidNumberException, IOException, UntrustedIdentityException
  {
    try {
      SessionStore sessionStore = new OpenchatServiceSessionStore(context, masterSecret);
      String       e164number   = Util.canonicalizeNumber(context, recipient.getNumber());
      long         recipientId  = recipient.getRecipientId();

      for (int extraDeviceId : mismatchedDevices.getExtraDevices()) {
        sessionStore.deleteSession(recipientId, extraDeviceId);
      }

      for (int missingDeviceId : mismatchedDevices.getMissingDevices()) {
        PushAddress          address   = PushAddress.create(context, recipientId, e164number, missingDeviceId);
        PreKeyBundle         preKey    = socket.getPreKey(address);
        KeyExchangeProcessor processor = new KeyExchangeProcessor(context, masterSecret, address);

        try {
          processor.processKeyExchangeMessage(preKey, threadId);
        } catch (com.openchat.protocal.UntrustedIdentityException e) {
          throw new UntrustedIdentityException("Untrusted identity key!", e164number, preKey.getIdentityKey());
        }

      }
    } catch (InvalidKeyException e) {
      throw new IOException(e);
    }
  }

  private void handleStaleDevices(Recipient recipient, StaleDevices staleDevices) {
    SessionStore sessionStore = new OpenchatServiceSessionStore(context, masterSecret);
    long         recipientId  = recipient.getRecipientId();

    for (int staleDeviceId : staleDevices.getStaleDevices()) {
      sessionStore.deleteSession(recipientId, staleDeviceId);
    }
  }

  private byte[] getPlaintextMessage(PushServiceSocket socket, SendReq message) throws IOException {
    String                      messageBody = PartParser.getMessageText(message.getBody());
    List<PushAttachmentPointer> attachments = getPushAttachmentPointers(socket, message.getBody());

    PushMessageContent.Builder builder = PushMessageContent.newBuilder();

    if (GroupUtil.isEncodedGroup(message.getTo()[0].getString())) {
      GroupContext.Builder groupBuilder = GroupContext.newBuilder();
      byte[]               groupId      = GroupUtil.getDecodedId(message.getTo()[0].getString());

      groupBuilder.setId(ByteString.copyFrom(groupId));
      groupBuilder.setType(GroupContext.Type.DELIVER);

      if (MmsSmsColumns.Types.isGroupUpdate(message.getDatabaseMessageBox()) ||
          MmsSmsColumns.Types.isGroupQuit(message.getDatabaseMessageBox()))
      {
        if (messageBody != null && messageBody.trim().length() > 0) {
          groupBuilder = GroupContext.parseFrom(Base64.decode(messageBody)).toBuilder();
          messageBody  = null;

          if (attachments != null && !attachments.isEmpty()) {
            groupBuilder.setAvatar(AttachmentPointer.newBuilder()
                                                    .setId(attachments.get(0).getId())
                                                    .setContentType(attachments.get(0).getContentType())
                                                    .setKey(ByteString.copyFrom(attachments.get(0).getKey()))
                                                    .build());

            attachments.remove(0);
          }
        }
      }

      builder.setGroup(groupBuilder.build());
    }

    if (messageBody != null) {
      builder.setBody(messageBody);
    }

    for (PushAttachmentPointer attachment : attachments) {
      AttachmentPointer.Builder attachmentBuilder =
          AttachmentPointer.newBuilder();

      attachmentBuilder.setId(attachment.getId());
      attachmentBuilder.setContentType(attachment.getContentType());
      attachmentBuilder.setKey(ByteString.copyFrom(attachment.getKey()));

      builder.addAttachments(attachmentBuilder.build());
    }

    return builder.build().toByteArray();
  }

  private byte[] getPlaintextMessage(SmsMessageRecord record) {
    PushMessageContent.Builder builder = PushMessageContent.newBuilder()
                                                           .setBody(record.getBody().getBody());

    if (record.isEndSession()) {
      builder.setFlags(PushMessageContent.Flags.END_SESSION_VALUE);
    }

    return builder.build().toByteArray();
  }

  private OutgoingPushMessageList getEncryptedMessages(PushServiceSocket socket, long threadId,
                                                       Recipient recipient, byte[] plaintext)
      throws IOException, InvalidNumberException, UntrustedIdentityException
  {
    SessionStore sessionStore = new OpenchatServiceSessionStore(context, masterSecret);
    String       e164number   = Util.canonicalizeNumber(context, recipient.getNumber());
    long         recipientId  = recipient.getRecipientId();
    PushAddress  masterDevice = PushAddress.create(context, recipientId, e164number, 1);
    PushBody     masterBody   = getEncryptedMessage(socket, threadId, masterDevice, plaintext);

    List<OutgoingPushMessage> messages = new LinkedList<>();
    messages.add(new OutgoingPushMessage(masterDevice, masterBody));

    for (int deviceId : sessionStore.getSubDeviceSessions(recipientId)) {
      PushAddress device = PushAddress.create(context, recipientId, e164number, deviceId);
      PushBody    body   = getEncryptedMessage(socket, threadId, device, plaintext);

      messages.add(new OutgoingPushMessage(device, body));
    }

    return new OutgoingPushMessageList(e164number, masterDevice.getRelay(), messages);
  }

  private PushBody getEncryptedMessage(PushServiceSocket socket, long threadId,
                                       PushAddress pushAddress, byte[] plaintext)
      throws IOException, UntrustedIdentityException
  {
    if (!SessionUtil.hasEncryptCapableSession(context, masterSecret, pushAddress)) {
      try {
        List<PreKeyBundle> preKeys = socket.getPreKeys(pushAddress);

        for (PreKeyBundle preKey : preKeys) {
          PushAddress          device    = PushAddress.create(context, pushAddress.getRecipientId(), pushAddress.getNumber(), preKey.getDeviceId());
          KeyExchangeProcessor processor = new KeyExchangeProcessor(context, masterSecret, device);

          try {
            processor.processKeyExchangeMessage(preKey, threadId);
          } catch (com.openchat.protocal.UntrustedIdentityException e) {
            throw new UntrustedIdentityException("Untrusted identity key!", pushAddress.getNumber(), preKey.getIdentityKey());
          }
        }
      } catch (InvalidKeyException e) {
        throw new IOException(e);
      }
    }

    TransportDetails  transportDetails     = new PushTransportDetails(SessionUtil.getSessionVersion(context, masterSecret, pushAddress));
    OpenchatServiceCipher  cipher               = new OpenchatServiceCipher(context, masterSecret, pushAddress, transportDetails);
    CiphertextMessage message              = cipher.encrypt(plaintext);
    int               remoteRegistrationId = cipher.getRemoteRegistrationId();

    if (message.getType() == CiphertextMessage.PREKEY_TYPE) {
      return new PushBody(IncomingPushMessageOpenchat.Type.PREKEY_BUNDLE_VALUE, remoteRegistrationId, message.serialize());
    } else if (message.getType() == CiphertextMessage.OPENCHAT_TYPE) {
      return new PushBody(IncomingPushMessageOpenchat.Type.CIPHERTEXT_VALUE, remoteRegistrationId, message.serialize());
    } else {
      throw new AssertionError("Unknown ciphertext type: " + message.getType());
    }
  }
}
