package com.openchat.imservice.api;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import com.openchat.protocal.OpenchatAddress;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.SessionBuilder;
import com.openchat.protocal.logging.Log;
import com.openchat.protocal.protocol.CiphertextMessage;
import com.openchat.protocal.state.OpenchatStore;
import com.openchat.protocal.state.PreKeyBundle;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.crypto.OpenchatServiceCipher;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.api.push.TrustStore;
import com.openchat.imservice.api.push.exceptions.NetworkFailureException;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;
import com.openchat.imservice.internal.push.MismatchedDevices;
import com.openchat.imservice.internal.push.OutgoingPushMessage;
import com.openchat.imservice.internal.push.OutgoingPushMessageList;
import com.openchat.imservice.internal.push.PushAttachmentData;
import com.openchat.imservice.internal.push.PushBody;
import com.openchat.imservice.internal.push.PushServiceSocket;
import com.openchat.imservice.internal.push.SendMessageResponse;
import com.openchat.imservice.internal.push.StaleDevices;
import com.openchat.imservice.api.push.exceptions.UnregisteredUserException;
import com.openchat.imservice.api.push.exceptions.EncapsulatedExceptions;
import com.openchat.imservice.internal.push.exceptions.MismatchedDevicesException;
import com.openchat.imservice.internal.push.exceptions.StaleDevicesException;
import com.openchat.imservice.internal.util.StaticCredentialsProvider;
import com.openchat.imservice.internal.util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type;
import static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent;
import static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.AttachmentPointer;
import static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext;

public class OpenchatServiceMessageSender {

  private static final String TAG = OpenchatServiceMessageSender.class.getSimpleName();

  private final PushServiceSocket       socket;
  private final OpenchatStore            store;
  private final OpenchatServiceAddress       syncAddress;
  private final Optional<EventListener> eventListener;

  
  public OpenchatServiceMessageSender(String url, TrustStore trustStore,
                                 String user, String password,
                                 OpenchatStore store,
                                 Optional<EventListener> eventListener)
  {
    this.socket        = new PushServiceSocket(url, trustStore, new StaticCredentialsProvider(user, password, null));
    this.store         = store;
    this.syncAddress   = new OpenchatServiceAddress(user);
    this.eventListener = eventListener;
  }

  
  public void sendDeliveryReceipt(OpenchatServiceAddress recipient, long messageId) throws IOException {
    this.socket.sendReceipt(recipient.getNumber(), messageId, recipient.getRelay());
  }

  
  public void sendMessage(OpenchatServiceAddress recipient, OpenchatServiceMessage message)
      throws UntrustedIdentityException, IOException
  {
    byte[]              content   = createMessageContent(message);
    long                timestamp = message.getTimestamp();
    SendMessageResponse response  = sendMessage(recipient, timestamp, content);

    if (response != null && response.getNeedsSync()) {
      byte[] syncMessage = createSyncMessageContent(content, recipient, timestamp);
      sendMessage(syncAddress, timestamp, syncMessage);
    }

    if (message.isEndSession()) {
      store.deleteAllSessions(recipient.getNumber());

      if (eventListener.isPresent()) {
        eventListener.get().onSecurityEvent(recipient);
      }
    }
  }

  
  public void sendMessage(List<OpenchatServiceAddress> recipients, OpenchatServiceMessage message)
      throws IOException, EncapsulatedExceptions
  {
    byte[] content = createMessageContent(message);
    sendMessage(recipients, message.getTimestamp(), content);
  }

  private byte[] createMessageContent(OpenchatServiceMessage message) throws IOException {
    PushMessageContent.Builder builder  = PushMessageContent.newBuilder();
    List<AttachmentPointer>    pointers = createAttachmentPointers(message.getAttachments());

    if (!pointers.isEmpty()) {
      builder.addAllAttachments(pointers);
    }

    if (message.getBody().isPresent()) {
      builder.setBody(message.getBody().get());
    }

    if (message.getGroupInfo().isPresent()) {
      builder.setGroup(createGroupContent(message.getGroupInfo().get()));
    }

    if (message.isEndSession()) {
      builder.setFlags(PushMessageContent.Flags.END_SESSION_VALUE);
    }

    return builder.build().toByteArray();
  }

  private byte[] createSyncMessageContent(byte[] content, OpenchatServiceAddress recipient, long timestamp) {
    try {
      PushMessageContent.Builder builder = PushMessageContent.parseFrom(content).toBuilder();
      builder.setSync(PushMessageContent.SyncMessageContext.newBuilder()
                                                           .setDestination(recipient.getNumber())
                                                           .setTimestamp(timestamp)
                                                           .build());

      return builder.build().toByteArray();
    } catch (InvalidProtocolBufferException e) {
      throw new AssertionError(e);
    }
  }

  private GroupContext createGroupContent(OpenchatServiceGroup group) throws IOException {
    GroupContext.Builder builder = GroupContext.newBuilder();
    builder.setId(ByteString.copyFrom(group.getGroupId()));

    if (group.getType() != OpenchatServiceGroup.Type.DELIVER) {
      if      (group.getType() == OpenchatServiceGroup.Type.UPDATE) builder.setType(GroupContext.Type.UPDATE);
      else if (group.getType() == OpenchatServiceGroup.Type.QUIT)   builder.setType(GroupContext.Type.QUIT);
      else                                                     throw new AssertionError("Unknown type: " + group.getType());

      if (group.getName().isPresent()) builder.setName(group.getName().get());
      if (group.getMembers().isPresent()) builder.addAllMembers(group.getMembers().get());

      if (group.getAvatar().isPresent() && group.getAvatar().get().isStream()) {
        AttachmentPointer pointer = createAttachmentPointer(group.getAvatar().get().asStream());
        builder.setAvatar(pointer);
      }
    } else {
      builder.setType(GroupContext.Type.DELIVER);
    }

    return builder.build();
  }

  private void sendMessage(List<OpenchatServiceAddress> recipients, long timestamp, byte[] content)
      throws IOException, EncapsulatedExceptions
  {
    List<UntrustedIdentityException> untrustedIdentities = new LinkedList<>();
    List<UnregisteredUserException>  unregisteredUsers   = new LinkedList<>();
    List<NetworkFailureException>    networkExceptions   = new LinkedList<>();

    for (OpenchatServiceAddress recipient : recipients) {
      try {
        sendMessage(recipient, timestamp, content);
      } catch (UntrustedIdentityException e) {
        Log.w(TAG, e);
        untrustedIdentities.add(e);
      } catch (UnregisteredUserException e) {
        Log.w(TAG, e);
        unregisteredUsers.add(e);
      } catch (PushNetworkException e) {
        Log.w(TAG, e);
        networkExceptions.add(new NetworkFailureException(recipient.getNumber(), e));
      }
    }

    if (!untrustedIdentities.isEmpty() || !unregisteredUsers.isEmpty() || !networkExceptions.isEmpty()) {
      throw new EncapsulatedExceptions(untrustedIdentities, unregisteredUsers, networkExceptions);
    }
  }

  private SendMessageResponse sendMessage(OpenchatServiceAddress recipient, long timestamp, byte[] content)
      throws UntrustedIdentityException, IOException
  {
    for (int i=0;i<3;i++) {
      try {
        OutgoingPushMessageList messages = getEncryptedMessages(socket, recipient, timestamp, content);
        return socket.sendMessage(messages);
      } catch (MismatchedDevicesException mde) {
        Log.w(TAG, mde);
        handleMismatchedDevices(socket, recipient, mde.getMismatchedDevices());
      } catch (StaleDevicesException ste) {
        Log.w(TAG, ste);
        handleStaleDevices(recipient, ste.getStaleDevices());
      }
    }

    throw new IOException("Failed to resolve conflicts after 3 attempts!");
  }

  private List<AttachmentPointer> createAttachmentPointers(Optional<List<OpenchatServiceAttachment>> attachments) throws IOException {
    List<AttachmentPointer> pointers = new LinkedList<>();

    if (!attachments.isPresent() || attachments.get().isEmpty()) {
      Log.w(TAG, "No attachments present...");
      return pointers;
    }

    for (OpenchatServiceAttachment attachment : attachments.get()) {
      if (attachment.isStream()) {
        Log.w(TAG, "Found attachment, creating pointer...");
        pointers.add(createAttachmentPointer(attachment.asStream()));
      }
    }

    return pointers;
  }

  private AttachmentPointer createAttachmentPointer(OpenchatServiceAttachmentStream attachment)
      throws IOException
  {
    byte[]             attachmentKey  = Util.getSecretBytes(64);
    PushAttachmentData attachmentData = new PushAttachmentData(attachment.getContentType(),
                                                               attachment.getInputStream(),
                                                               attachment.getLength(),
                                                               attachmentKey);

    long attachmentId = socket.sendAttachment(attachmentData);

    return AttachmentPointer.newBuilder()
                            .setContentType(attachment.getContentType())
                            .setId(attachmentId)
                            .setKey(ByteString.copyFrom(attachmentKey))
                            .build();
  }

  private OutgoingPushMessageList getEncryptedMessages(PushServiceSocket socket,
                                                       OpenchatServiceAddress recipient,
                                                       long timestamp,
                                                       byte[] plaintext)
      throws IOException, UntrustedIdentityException
  {
    List<OutgoingPushMessage> messages = new LinkedList<>();

    if (!recipient.equals(syncAddress)) {
      PushBody masterBody = getEncryptedMessage(socket, recipient, OpenchatServiceAddress.DEFAULT_DEVICE_ID, plaintext);
      messages.add(new OutgoingPushMessage(recipient, OpenchatServiceAddress.DEFAULT_DEVICE_ID, masterBody));
    }

    for (int deviceId : store.getSubDeviceSessions(recipient.getNumber())) {
      PushBody body = getEncryptedMessage(socket, recipient, deviceId, plaintext);
      messages.add(new OutgoingPushMessage(recipient, deviceId, body));
    }

    return new OutgoingPushMessageList(recipient.getNumber(), timestamp, recipient.getRelay().orNull(), messages);
  }

  private PushBody getEncryptedMessage(PushServiceSocket socket, OpenchatServiceAddress recipient, int deviceId, byte[] plaintext)
      throws IOException, UntrustedIdentityException
  {
    OpenchatAddress axolotlAddress = new OpenchatAddress(recipient.getNumber(), deviceId);

    if (!store.containsSession(axolotlAddress)) {
      try {
        List<PreKeyBundle> preKeys = socket.getPreKeys(recipient, deviceId);

        for (PreKeyBundle preKey : preKeys) {
          try {
            OpenchatAddress preKeyAddress  = new OpenchatAddress(recipient.getNumber(), preKey.getDeviceId());
            SessionBuilder sessionBuilder = new SessionBuilder(store, preKeyAddress);
            sessionBuilder.process(preKey);
          } catch (com.openchat.protocal.UntrustedIdentityException e) {
            throw new UntrustedIdentityException("Untrusted identity key!", recipient.getNumber(), preKey.getIdentityKey());
          }
        }

        if (eventListener.isPresent()) {
          eventListener.get().onSecurityEvent(recipient);
        }
      } catch (InvalidKeyException e) {
        throw new IOException(e);
      }
    }

    OpenchatServiceCipher  cipher               = new OpenchatServiceCipher(store, axolotlAddress);
    CiphertextMessage message              = cipher.encrypt(plaintext);
    int               remoteRegistrationId = cipher.getRemoteRegistrationId();

    if (message.getType() == CiphertextMessage.PREKEY_TYPE) {
      return new PushBody(Type.PREKEY_BUNDLE_VALUE, remoteRegistrationId, message.serialize());
    } else if (message.getType() == CiphertextMessage.OPENCHAT_TYPE) {
      return new PushBody(Type.CIPHERTEXT_VALUE, remoteRegistrationId, message.serialize());
    } else {
      throw new AssertionError("Unknown ciphertext type: " + message.getType());
    }
  }

  private void handleMismatchedDevices(PushServiceSocket socket, OpenchatServiceAddress recipient,
                                       MismatchedDevices mismatchedDevices)
      throws IOException, UntrustedIdentityException
  {
    try {
      for (int extraDeviceId : mismatchedDevices.getExtraDevices()) {
        store.deleteSession(new OpenchatAddress(recipient.getNumber(), extraDeviceId));
      }

      for (int missingDeviceId : mismatchedDevices.getMissingDevices()) {
        PreKeyBundle preKey = socket.getPreKey(recipient, missingDeviceId);

        try {
          SessionBuilder sessionBuilder = new SessionBuilder(store, new OpenchatAddress(recipient.getNumber(), missingDeviceId));
          sessionBuilder.process(preKey);
        } catch (com.openchat.protocal.UntrustedIdentityException e) {
          throw new UntrustedIdentityException("Untrusted identity key!", recipient.getNumber(), preKey.getIdentityKey());
        }
      }
    } catch (InvalidKeyException e) {
      throw new IOException(e);
    }
  }

  private void handleStaleDevices(OpenchatServiceAddress recipient, StaleDevices staleDevices) {
    for (int staleDeviceId : staleDevices.getStaleDevices()) {
      store.deleteSession(new OpenchatAddress(recipient.getNumber(), staleDeviceId));
    }
  }

  public static interface EventListener {
    public void onSecurityEvent(OpenchatServiceAddress address);
  }

}
