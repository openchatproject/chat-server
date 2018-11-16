package com.openchat.imservice.api;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.SessionBuilder;
import com.openchat.protocal.OpenchatProtocolAddress;
import com.openchat.protocal.logging.Log;
import com.openchat.protocal.state.PreKeyBundle;
import com.openchat.protocal.state.OpenchatProtocolStore;
import com.openchat.protocal.util.Pair;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.crypto.OpenchatServiceCipher;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.imservice.api.messages.OpenchatServiceDataMessage;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.calls.AnswerMessage;
import com.openchat.imservice.api.messages.calls.IceUpdateMessage;
import com.openchat.imservice.api.messages.calls.OfferMessage;
import com.openchat.imservice.api.messages.calls.OpenchatServiceCallMessage;
import com.openchat.imservice.api.messages.multidevice.BlockedListMessage;
import com.openchat.imservice.api.messages.multidevice.ReadMessage;
import com.openchat.imservice.api.messages.multidevice.OpenchatServiceSyncMessage;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.api.push.exceptions.EncapsulatedExceptions;
import com.openchat.imservice.api.push.exceptions.NetworkFailureException;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;
import com.openchat.imservice.api.push.exceptions.UnregisteredUserException;
import com.openchat.imservice.internal.push.MismatchedDevices;
import com.openchat.imservice.internal.push.OutgoingPushMessage;
import com.openchat.imservice.internal.push.OutgoingPushMessageList;
import com.openchat.imservice.internal.push.PushAttachmentData;
import com.openchat.imservice.internal.push.PushServiceSocket;
import com.openchat.imservice.internal.push.SendMessageResponse;
import com.openchat.imservice.internal.push.SendMessageResponseList;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.Content;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage;
import com.openchat.imservice.internal.push.OpenchatServiceUrl;
import com.openchat.imservice.internal.push.StaleDevices;
import com.openchat.imservice.internal.push.exceptions.MismatchedDevicesException;
import com.openchat.imservice.internal.push.exceptions.StaleDevicesException;
import com.openchat.imservice.internal.util.StaticCredentialsProvider;
import com.openchat.imservice.internal.util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class OpenchatServiceMessageSender {

  private static final String TAG = OpenchatServiceMessageSender.class.getSimpleName();

  private final PushServiceSocket                  socket;
  private final OpenchatProtocolStore                store;
  private final OpenchatServiceAddress               localAddress;
  private final Optional<OpenchatServiceMessagePipe> pipe;
  private final Optional<EventListener>            eventListener;

  
  public OpenchatServiceMessageSender(OpenchatServiceUrl[] urls,
                                    String user, String password,
                                    OpenchatProtocolStore store,
                                    String userAgent,
                                    Optional<OpenchatServiceMessagePipe> pipe,
                                    Optional<EventListener> eventListener)
  {
    this.socket        = new PushServiceSocket(urls, new StaticCredentialsProvider(user, password, null), userAgent);
    this.store         = store;
    this.localAddress  = new OpenchatServiceAddress(user);
    this.pipe          = pipe;
    this.eventListener = eventListener;
  }

  
  public void sendDeliveryReceipt(OpenchatServiceAddress recipient, long messageId) throws IOException {
    this.socket.sendReceipt(recipient.getNumber(), messageId, recipient.getRelay());
  }

  public void sendCallMessage(OpenchatServiceAddress recipient, OpenchatServiceCallMessage message)
      throws IOException, UntrustedIdentityException
  {
    byte[] content = createCallContent(message);
    sendMessage(recipient, System.currentTimeMillis(), content, false, true);
  }

  
  public void sendMessage(OpenchatServiceAddress recipient, OpenchatServiceDataMessage message)
      throws UntrustedIdentityException, IOException
  {
    byte[]              content   = createMessageContent(message);
    long                timestamp = message.getTimestamp();
    boolean             silent    = message.getGroupInfo().isPresent() && message.getGroupInfo().get().getType() == OpenchatServiceGroup.Type.REQUEST_INFO;
    SendMessageResponse response  = sendMessage(recipient, timestamp, content, true, silent);

    if (response != null && response.getNeedsSync()) {
      byte[] syncMessage = createMultiDeviceSentTranscriptContent(content, Optional.of(recipient), timestamp);
      sendMessage(localAddress, timestamp, syncMessage, false, false);
    }

    if (message.isEndSession()) {
      store.deleteAllSessions(recipient.getNumber());

      if (eventListener.isPresent()) {
        eventListener.get().onSecurityEvent(recipient);
      }
    }
  }

  
  public void sendMessage(List<OpenchatServiceAddress> recipients, OpenchatServiceDataMessage message)
      throws IOException, EncapsulatedExceptions
  {
    byte[]                  content   = createMessageContent(message);
    long                    timestamp = message.getTimestamp();
    SendMessageResponseList response  = sendMessage(recipients, timestamp, content, true);

    try {
      if (response.getNeedsSync()) {
        byte[] syncMessage = createMultiDeviceSentTranscriptContent(content, Optional.<OpenchatServiceAddress>absent(), timestamp);
        sendMessage(localAddress, timestamp, syncMessage, false, false);
      }
    } catch (UntrustedIdentityException e) {
      response.addException(e);
    }

    if (response.hasExceptions()) {
      throw new EncapsulatedExceptions(response.getUntrustedIdentities(), response.getUnregisteredUsers(), response.getNetworkExceptions());
    }
  }

  public void sendMessage(OpenchatServiceSyncMessage message)
      throws IOException, UntrustedIdentityException
  {
    byte[] content;

    if (message.getContacts().isPresent()) {
      content = createMultiDeviceContactsContent(message.getContacts().get().asStream());
    } else if (message.getGroups().isPresent()) {
      content = createMultiDeviceGroupsContent(message.getGroups().get().asStream());
    } else if (message.getRead().isPresent()) {
      content = createMultiDeviceReadContent(message.getRead().get());
    } else if (message.getBlockedList().isPresent()) {
      content = createMultiDeviceBlockedContent(message.getBlockedList().get());
    } else {
      throw new IOException("Unsupported sync message!");
    }

    sendMessage(localAddress, System.currentTimeMillis(), content, false, false);
  }

  public void setSoTimeoutMillis(long soTimeoutMillis) {
    socket.setSoTimeoutMillis(soTimeoutMillis);
  }

  public void cancelInFlightRequests() {
    socket.cancelInFlightRequests();
  }

  private byte[] createMessageContent(OpenchatServiceDataMessage message) throws IOException {
    DataMessage.Builder     builder  = DataMessage.newBuilder();
    List<AttachmentPointer> pointers = createAttachmentPointers(message.getAttachments());

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
      builder.setFlags(DataMessage.Flags.END_SESSION_VALUE);
    }

    if (message.isExpirationUpdate()) {
      builder.setFlags(DataMessage.Flags.EXPIRATION_TIMER_UPDATE_VALUE);
    }

    if (message.getExpiresInSeconds() > 0) {
      builder.setExpireTimer(message.getExpiresInSeconds());
    }

    return builder.build().toByteArray();
  }

  private byte[] createCallContent(OpenchatServiceCallMessage callMessage) {
    Content.Builder     container = Content.newBuilder();
    CallMessage.Builder builder   = CallMessage.newBuilder();

    if (callMessage.getOfferMessage().isPresent()) {
      OfferMessage offer = callMessage.getOfferMessage().get();
      builder.setOffer(CallMessage.Offer.newBuilder()
                                        .setId(offer.getId())
                                        .setDescription(offer.getDescription()));
    } else if (callMessage.getAnswerMessage().isPresent()) {
      AnswerMessage answer = callMessage.getAnswerMessage().get();
      builder.setAnswer(CallMessage.Answer.newBuilder()
                                          .setId(answer.getId())
                                          .setDescription(answer.getDescription()));
    } else if (callMessage.getIceUpdateMessages().isPresent()) {
      List<IceUpdateMessage> updates = callMessage.getIceUpdateMessages().get();

      for (IceUpdateMessage update : updates) {
        builder.addIceUpdate(CallMessage.IceUpdate.newBuilder()
                                                  .setId(update.getId())
                                                  .setSdp(update.getSdp())
                                                  .setSdpMid(update.getSdpMid())
                                                  .setSdpMLineIndex(update.getSdpMLineIndex()));
      }
    } else if (callMessage.getHangupMessage().isPresent()) {
      builder.setHangup(CallMessage.Hangup.newBuilder().setId(callMessage.getHangupMessage().get().getId()));
    } else if (callMessage.getBusyMessage().isPresent()) {
      builder.setBusy(CallMessage.Busy.newBuilder().setId(callMessage.getBusyMessage().get().getId()));
    }

    container.setCallMessage(builder);
    return container.build().toByteArray();
  }

  private byte[] createMultiDeviceContactsContent(OpenchatServiceAttachmentStream contacts) throws IOException {
    Content.Builder     container = Content.newBuilder();
    SyncMessage.Builder builder   = SyncMessage.newBuilder();
    builder.setContacts(SyncMessage.Contacts.newBuilder()
                                            .setBlob(createAttachmentPointer(contacts)));

    return container.setSyncMessage(builder).build().toByteArray();
  }

  private byte[] createMultiDeviceGroupsContent(OpenchatServiceAttachmentStream groups) throws IOException {
    Content.Builder     container = Content.newBuilder();
    SyncMessage.Builder builder   = SyncMessage.newBuilder();
    builder.setGroups(SyncMessage.Groups.newBuilder()
                                        .setBlob(createAttachmentPointer(groups)));

    return container.setSyncMessage(builder).build().toByteArray();
  }

  private byte[] createMultiDeviceSentTranscriptContent(byte[] content, Optional<OpenchatServiceAddress> recipient, long timestamp)
  {
    try {
      Content.Builder          container   = Content.newBuilder();
      SyncMessage.Builder      syncMessage = SyncMessage.newBuilder();
      SyncMessage.Sent.Builder sentMessage = SyncMessage.Sent.newBuilder();
      DataMessage              dataMessage = DataMessage.parseFrom(content);

      sentMessage.setTimestamp(timestamp);
      sentMessage.setMessage(dataMessage);

      if (recipient.isPresent()) {
        sentMessage.setDestination(recipient.get().getNumber());
      }

      if (dataMessage.getExpireTimer() > 0) {
        sentMessage.setExpirationStartTimestamp(System.currentTimeMillis());
      }

      return container.setSyncMessage(syncMessage.setSent(sentMessage)).build().toByteArray();
    } catch (InvalidProtocolBufferException e) {
      throw new AssertionError(e);
    }
  }

  private byte[] createMultiDeviceReadContent(List<ReadMessage> readMessages) {
    Content.Builder     container = Content.newBuilder();
    SyncMessage.Builder builder   = SyncMessage.newBuilder();

    for (ReadMessage readMessage : readMessages) {
      builder.addRead(SyncMessage.Read.newBuilder()
                                      .setTimestamp(readMessage.getTimestamp())
                                      .setSender(readMessage.getSender()));
    }

    return container.setSyncMessage(builder).build().toByteArray();
  }

  private byte[] createMultiDeviceBlockedContent(BlockedListMessage blocked) {
    Content.Builder             container      = Content.newBuilder();
    SyncMessage.Builder         syncMessage    = SyncMessage.newBuilder();
    SyncMessage.Blocked.Builder blockedMessage = SyncMessage.Blocked.newBuilder();

    blockedMessage.addAllNumbers(blocked.getNumbers());

    return container.setSyncMessage(syncMessage.setBlocked(blockedMessage)).build().toByteArray();
  }

  private GroupContext createGroupContent(OpenchatServiceGroup group) throws IOException {
    GroupContext.Builder builder = GroupContext.newBuilder();
    builder.setId(ByteString.copyFrom(group.getGroupId()));

    if (group.getType() != OpenchatServiceGroup.Type.DELIVER) {
      if      (group.getType() == OpenchatServiceGroup.Type.UPDATE)       builder.setType(GroupContext.Type.UPDATE);
      else if (group.getType() == OpenchatServiceGroup.Type.QUIT)         builder.setType(GroupContext.Type.QUIT);
      else if (group.getType() == OpenchatServiceGroup.Type.REQUEST_INFO) builder.setType(GroupContext.Type.REQUEST_INFO);
      else                                                              throw new AssertionError("Unknown type: " + group.getType());

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

  private SendMessageResponseList sendMessage(List<OpenchatServiceAddress> recipients, long timestamp, byte[] content, boolean legacy)
      throws IOException
  {
    SendMessageResponseList responseList = new SendMessageResponseList();

    for (OpenchatServiceAddress recipient : recipients) {
      try {
        SendMessageResponse response = sendMessage(recipient, timestamp, content, legacy, false);
        responseList.addResponse(response);
      } catch (UntrustedIdentityException e) {
        Log.w(TAG, e);
        responseList.addException(e);
      } catch (UnregisteredUserException e) {
        Log.w(TAG, e);
        responseList.addException(e);
      } catch (PushNetworkException e) {
        Log.w(TAG, e);
        responseList.addException(new NetworkFailureException(recipient.getNumber(), e));
      }
    }

    return responseList;
  }

  private SendMessageResponse sendMessage(OpenchatServiceAddress recipient, long timestamp, byte[] content, boolean legacy, boolean silent)
      throws UntrustedIdentityException, IOException
  {
    for (int i=0;i<3;i++) {
      try {
        OutgoingPushMessageList messages = getEncryptedMessages(socket, recipient, timestamp, content, legacy, silent);

        if (pipe.isPresent()) {
          try {
            Log.w(TAG, "Transmitting over pipe...");
            return pipe.get().send(messages);
          } catch (IOException e) {
            Log.w(TAG, e);
            Log.w(TAG, "Falling back to new connection...");
          }
        }

        Log.w(TAG, "Not transmitting over pipe...");
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
                                                               attachment.getListener(),
                                                               attachmentKey);

    Pair<Long, byte[]> attachmentIdAndDigest = socket.sendAttachment(attachmentData);

    AttachmentPointer.Builder builder = AttachmentPointer.newBuilder()
                                                         .setContentType(attachment.getContentType())
                                                         .setId(attachmentIdAndDigest.first())
                                                         .setKey(ByteString.copyFrom(attachmentKey))
                                                         .setDigest(ByteString.copyFrom(attachmentIdAndDigest.second()))
                                                         .setSize((int)attachment.getLength());

    if (attachment.getPreview().isPresent()) {
      builder.setThumbnail(ByteString.copyFrom(attachment.getPreview().get()));
    }

    return builder.build();
  }

  private OutgoingPushMessageList getEncryptedMessages(PushServiceSocket socket,
                                                       OpenchatServiceAddress recipient,
                                                       long timestamp,
                                                       byte[] plaintext,
                                                       boolean legacy,
                                                       boolean silent)
      throws IOException, UntrustedIdentityException
  {
    List<OutgoingPushMessage> messages = new LinkedList<>();

    if (!recipient.equals(localAddress)) {
      messages.add(getEncryptedMessage(socket, recipient, OpenchatServiceAddress.DEFAULT_DEVICE_ID, plaintext, legacy, silent));
    }

    for (int deviceId : store.getSubDeviceSessions(recipient.getNumber())) {
      messages.add(getEncryptedMessage(socket, recipient, deviceId, plaintext, legacy, silent));
    }

    return new OutgoingPushMessageList(recipient.getNumber(), timestamp, recipient.getRelay().orNull(), messages);
  }

  private OutgoingPushMessage getEncryptedMessage(PushServiceSocket socket, OpenchatServiceAddress recipient, int deviceId, byte[] plaintext, boolean legacy, boolean silent)
      throws IOException, UntrustedIdentityException
  {
    OpenchatProtocolAddress openchatProtocolAddress = new OpenchatProtocolAddress(recipient.getNumber(), deviceId);
    OpenchatServiceCipher cipher                = new OpenchatServiceCipher(localAddress, store);

    if (!store.containsSession(openchatProtocolAddress)) {
      try {
        List<PreKeyBundle> preKeys = socket.getPreKeys(recipient, deviceId);

        for (PreKeyBundle preKey : preKeys) {
          try {
            OpenchatProtocolAddress preKeyAddress  = new OpenchatProtocolAddress(recipient.getNumber(), preKey.getDeviceId());
            SessionBuilder        sessionBuilder = new SessionBuilder(store, preKeyAddress);
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

    return cipher.encrypt(openchatProtocolAddress, plaintext, legacy, silent);
  }

  private void handleMismatchedDevices(PushServiceSocket socket, OpenchatServiceAddress recipient,
                                       MismatchedDevices mismatchedDevices)
      throws IOException, UntrustedIdentityException
  {
    try {
      for (int extraDeviceId : mismatchedDevices.getExtraDevices()) {
        store.deleteSession(new OpenchatProtocolAddress(recipient.getNumber(), extraDeviceId));
      }

      for (int missingDeviceId : mismatchedDevices.getMissingDevices()) {
        PreKeyBundle preKey = socket.getPreKey(recipient, missingDeviceId);

        try {
          SessionBuilder sessionBuilder = new SessionBuilder(store, new OpenchatProtocolAddress(recipient.getNumber(), missingDeviceId));
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
      store.deleteSession(new OpenchatProtocolAddress(recipient.getNumber(), staleDeviceId));
    }
  }

  public static interface EventListener {
    public void onSecurityEvent(OpenchatServiceAddress address);
  }

}
