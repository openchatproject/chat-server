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
import com.openchat.imservice.api.crypto.AttachmentCipherOutputStream;
import com.openchat.imservice.api.crypto.OpenchatServiceCipher;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.imservice.api.messages.OpenchatServiceDataMessage;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceReceiptMessage;
import com.openchat.imservice.api.messages.calls.AnswerMessage;
import com.openchat.imservice.api.messages.calls.IceUpdateMessage;
import com.openchat.imservice.api.messages.calls.OfferMessage;
import com.openchat.imservice.api.messages.calls.OpenchatServiceCallMessage;
import com.openchat.imservice.api.messages.multidevice.BlockedListMessage;
import com.openchat.imservice.api.messages.multidevice.ConfigurationMessage;
import com.openchat.imservice.api.messages.multidevice.ReadMessage;
import com.openchat.imservice.api.messages.multidevice.OpenchatServiceSyncMessage;
import com.openchat.imservice.api.messages.multidevice.VerifiedMessage;
import com.openchat.imservice.api.messages.shared.SharedContact;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.api.push.exceptions.EncapsulatedExceptions;
import com.openchat.imservice.api.push.exceptions.NetworkFailureException;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;
import com.openchat.imservice.api.push.exceptions.UnregisteredUserException;
import com.openchat.imservice.api.util.CredentialsProvider;
import com.openchat.imservice.internal.configuration.OpenchatServiceConfiguration;
import com.openchat.imservice.internal.crypto.PaddingInputStream;
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
import com.openchat.imservice.internal.push.OpenchatServiceProtos.NullMessage;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified;
import com.openchat.imservice.internal.push.StaleDevices;
import com.openchat.imservice.internal.push.exceptions.MismatchedDevicesException;
import com.openchat.imservice.internal.push.exceptions.StaleDevicesException;
import com.openchat.imservice.internal.push.http.AttachmentCipherOutputStreamFactory;
import com.openchat.imservice.internal.util.Base64;
import com.openchat.imservice.internal.util.StaticCredentialsProvider;
import com.openchat.imservice.internal.util.Util;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class OpenchatServiceMessageSender {

  private static final String TAG = OpenchatServiceMessageSender.class.getSimpleName();

  private final PushServiceSocket                                   socket;
  private final OpenchatProtocolStore                                 store;
  private final OpenchatServiceAddress                                localAddress;
  private final AtomicReference<Optional<OpenchatServiceMessagePipe>> pipe;
  private final Optional<EventListener>                             eventListener;

  
  public OpenchatServiceMessageSender(OpenchatServiceConfiguration urls,
                                    String user, String password,
                                    OpenchatProtocolStore store,
                                    String userAgent,
                                    Optional<OpenchatServiceMessagePipe> pipe,
                                    Optional<EventListener> eventListener)
  {
    this(urls, new StaticCredentialsProvider(user, password, null), store, userAgent, pipe, eventListener);
  }

  public OpenchatServiceMessageSender(OpenchatServiceConfiguration urls,
                                    CredentialsProvider credentialsProvider,
                                    OpenchatProtocolStore store,
                                    String userAgent,
                                    Optional<OpenchatServiceMessagePipe> pipe,
                                    Optional<EventListener> eventListener)
  {
    this.socket        = new PushServiceSocket(urls, credentialsProvider, userAgent);
    this.store         = store;
    this.localAddress  = new OpenchatServiceAddress(credentialsProvider.getUser());
    this.pipe          = new AtomicReference<>(pipe);
    this.eventListener = eventListener;
  }

  
  public void sendReceipt(OpenchatServiceAddress recipient, OpenchatServiceReceiptMessage message)
      throws IOException, UntrustedIdentityException
  {
    byte[] content = createReceiptContent(message);
    sendMessage(recipient, message.getWhen(), content, true);
  }

  
  public void sendCallMessage(OpenchatServiceAddress recipient, OpenchatServiceCallMessage message)
      throws IOException, UntrustedIdentityException
  {
    byte[] content = createCallContent(message);
    sendMessage(recipient, System.currentTimeMillis(), content, true);
  }

  
  public void sendMessage(OpenchatServiceAddress recipient, OpenchatServiceDataMessage message)
      throws UntrustedIdentityException, IOException
  {
    byte[]              content   = createMessageContent(message);
    long                timestamp = message.getTimestamp();
    boolean             silent    = message.getGroupInfo().isPresent() && message.getGroupInfo().get().getType() == OpenchatServiceGroup.Type.REQUEST_INFO;
    SendMessageResponse response  = sendMessage(recipient, timestamp, content, silent);

    if (response != null && response.getNeedsSync()) {
      byte[] syncMessage = createMultiDeviceSentTranscriptContent(content, Optional.of(recipient), timestamp);
      sendMessage(localAddress, timestamp, syncMessage, false);
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
    SendMessageResponseList response  = sendMessage(recipients, timestamp, content);

    try {
      if (response.getNeedsSync()) {
        byte[] syncMessage = createMultiDeviceSentTranscriptContent(content, Optional.<OpenchatServiceAddress>absent(), timestamp);
        sendMessage(localAddress, timestamp, syncMessage, false);
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
      content = createMultiDeviceContactsContent(message.getContacts().get().getContactsStream().asStream(),
                                                 message.getContacts().get().isComplete());
    } else if (message.getGroups().isPresent()) {
      content = createMultiDeviceGroupsContent(message.getGroups().get().asStream());
    } else if (message.getRead().isPresent()) {
      content = createMultiDeviceReadContent(message.getRead().get());
    } else if (message.getBlockedList().isPresent()) {
      content = createMultiDeviceBlockedContent(message.getBlockedList().get());
    } else if (message.getConfiguration().isPresent()) {
      content = createMultiDeviceConfigurationContent(message.getConfiguration().get());
    } else if (message.getVerified().isPresent()) {
      sendMessage(message.getVerified().get());
      return;
    } else {
      throw new IOException("Unsupported sync message!");
    }

    sendMessage(localAddress, System.currentTimeMillis(), content, false);
  }

  public void setSoTimeoutMillis(long soTimeoutMillis) {
    socket.setSoTimeoutMillis(soTimeoutMillis);
  }

  public void cancelInFlightRequests() {
    socket.cancelInFlightRequests();
  }

  public void setMessagePipe(OpenchatServiceMessagePipe pipe) {
    this.pipe.set(Optional.fromNullable(pipe));
  }

  private void sendMessage(VerifiedMessage message) throws IOException, UntrustedIdentityException {
    byte[] nullMessageBody = DataMessage.newBuilder()
                                        .setBody(Base64.encodeBytes(Util.getRandomLengthBytes(140)))
                                        .build()
                                        .toByteArray();

    NullMessage nullMessage = NullMessage.newBuilder()
                                         .setPadding(ByteString.copyFrom(nullMessageBody))
                                         .build();

    byte[] content          = Content.newBuilder()
                                     .setNullMessage(nullMessage)
                                     .build()
                                     .toByteArray();

    SendMessageResponse response = sendMessage(new OpenchatServiceAddress(message.getDestination()), message.getTimestamp(), content, false);

    if (response != null && response.getNeedsSync()) {
      byte[] syncMessage = createMultiDeviceVerifiedContent(message, nullMessage.toByteArray());
      sendMessage(localAddress, message.getTimestamp(), syncMessage, false);
    }
  }

  private byte[] createReceiptContent(OpenchatServiceReceiptMessage message) throws IOException {
    Content.Builder        container = Content.newBuilder();
    ReceiptMessage.Builder builder   = ReceiptMessage.newBuilder();

    for (long timestamp : message.getTimestamps()) {
      builder.addTimestamp(timestamp);
    }

    if      (message.isDeliveryReceipt()) builder.setType(ReceiptMessage.Type.DELIVERY);
    else if (message.isReadReceipt())     builder.setType(ReceiptMessage.Type.READ);

    return container.setReceiptMessage(builder).build().toByteArray();
  }

  private byte[] createMessageContent(OpenchatServiceDataMessage message) throws IOException {
    Content.Builder         container = Content.newBuilder();
    DataMessage.Builder     builder   = DataMessage.newBuilder();
    List<AttachmentPointer> pointers  = createAttachmentPointers(message.getAttachments());

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

    if (message.isProfileKeyUpdate()) {
      builder.setFlags(DataMessage.Flags.PROFILE_KEY_UPDATE_VALUE);
    }

    if (message.getExpiresInSeconds() > 0) {
      builder.setExpireTimer(message.getExpiresInSeconds());
    }

    if (message.getProfileKey().isPresent()) {
      builder.setProfileKey(ByteString.copyFrom(message.getProfileKey().get()));
    }

    if (message.getQuote().isPresent()) {
      DataMessage.Quote.Builder quoteBuilder = DataMessage.Quote.newBuilder()
                                                                .setId(message.getQuote().get().getId())
                                                                .setAuthor(message.getQuote().get().getAuthor().getNumber())
                                                                .setText(message.getQuote().get().getText());

      for (OpenchatServiceDataMessage.Quote.QuotedAttachment attachment : message.getQuote().get().getAttachments()) {
        DataMessage.Quote.QuotedAttachment.Builder quotedAttachment = DataMessage.Quote.QuotedAttachment.newBuilder();

        quotedAttachment.setContentType(attachment.getContentType());

        if (attachment.getFileName() != null) {
          quotedAttachment.setFileName(attachment.getFileName());
        }

        if (attachment.getThumbnail() != null) {
          quotedAttachment.setThumbnail(createAttachmentPointer(attachment.getThumbnail().asStream()));
        }

        quoteBuilder.addAttachments(quotedAttachment);
      }

      builder.setQuote(quoteBuilder);
    }

    if (message.getSharedContacts().isPresent()) {
      builder.addAllContact(createSharedContactContent(message.getSharedContacts().get()));
    }

    builder.setTimestamp(message.getTimestamp());

    return container.setDataMessage(builder).build().toByteArray();
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

  private byte[] createMultiDeviceContactsContent(OpenchatServiceAttachmentStream contacts, boolean complete) throws IOException {
    Content.Builder     container = Content.newBuilder();
    SyncMessage.Builder builder   = createSyncMessageBuilder();
    builder.setContacts(SyncMessage.Contacts.newBuilder()
                                            .setBlob(createAttachmentPointer(contacts))
                                            .setComplete(complete));

    return container.setSyncMessage(builder).build().toByteArray();
  }

  private byte[] createMultiDeviceGroupsContent(OpenchatServiceAttachmentStream groups) throws IOException {
    Content.Builder     container = Content.newBuilder();
    SyncMessage.Builder builder   = createSyncMessageBuilder();
    builder.setGroups(SyncMessage.Groups.newBuilder()
                                        .setBlob(createAttachmentPointer(groups)));

    return container.setSyncMessage(builder).build().toByteArray();
  }

  private byte[] createMultiDeviceSentTranscriptContent(byte[] content, Optional<OpenchatServiceAddress> recipient, long timestamp)
  {
    try {
      Content.Builder          container   = Content.newBuilder();
      SyncMessage.Builder      syncMessage = createSyncMessageBuilder();
      SyncMessage.Sent.Builder sentMessage = SyncMessage.Sent.newBuilder();
      DataMessage              dataMessage = Content.parseFrom(content).getDataMessage();

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
    SyncMessage.Builder builder   = createSyncMessageBuilder();

    for (ReadMessage readMessage : readMessages) {
      builder.addRead(SyncMessage.Read.newBuilder()
                                      .setTimestamp(readMessage.getTimestamp())
                                      .setSender(readMessage.getSender()));
    }

    return container.setSyncMessage(builder).build().toByteArray();
  }

  private byte[] createMultiDeviceBlockedContent(BlockedListMessage blocked) {
    Content.Builder             container      = Content.newBuilder();
    SyncMessage.Builder         syncMessage    = createSyncMessageBuilder();
    SyncMessage.Blocked.Builder blockedMessage = SyncMessage.Blocked.newBuilder();

    blockedMessage.addAllNumbers(blocked.getNumbers());

    return container.setSyncMessage(syncMessage.setBlocked(blockedMessage)).build().toByteArray();
  }

  private byte[] createMultiDeviceConfigurationContent(ConfigurationMessage configuration) {
    Content.Builder                   container            = Content.newBuilder();
    SyncMessage.Builder               syncMessage          = createSyncMessageBuilder();
    SyncMessage.Configuration.Builder configurationMessage = SyncMessage.Configuration.newBuilder();

    if (configuration.getReadReceipts().isPresent()) {
      configurationMessage.setReadReceipts(configuration.getReadReceipts().get());
    }

    return container.setSyncMessage(syncMessage.setConfiguration(configurationMessage)).build().toByteArray();
  }

  private byte[] createMultiDeviceVerifiedContent(VerifiedMessage verifiedMessage, byte[] nullMessage) {
    Content.Builder     container              = Content.newBuilder();
    SyncMessage.Builder syncMessage            = createSyncMessageBuilder();
    Verified.Builder    verifiedMessageBuilder = Verified.newBuilder();

    verifiedMessageBuilder.setNullMessage(ByteString.copyFrom(nullMessage));
    verifiedMessageBuilder.setDestination(verifiedMessage.getDestination());
    verifiedMessageBuilder.setIdentityKey(ByteString.copyFrom(verifiedMessage.getIdentityKey().serialize()));

      switch(verifiedMessage.getVerified()) {
        case DEFAULT:    verifiedMessageBuilder.setState(Verified.State.DEFAULT);    break;
        case VERIFIED:   verifiedMessageBuilder.setState(Verified.State.VERIFIED);   break;
        case UNVERIFIED: verifiedMessageBuilder.setState(Verified.State.UNVERIFIED); break;
        default:         throw new AssertionError("Unknown: " + verifiedMessage.getVerified());
      }

    syncMessage.setVerified(verifiedMessageBuilder);
    return container.setSyncMessage(syncMessage).build().toByteArray();
  }

  private SyncMessage.Builder createSyncMessageBuilder() {
    SecureRandom random  = new SecureRandom();
    byte[]       padding = Util.getRandomLengthBytes(512);
    random.nextBytes(padding);

    SyncMessage.Builder builder = SyncMessage.newBuilder();
    builder.setPadding(ByteString.copyFrom(padding));

    return builder;
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

  private List<DataMessage.Contact> createSharedContactContent(List<SharedContact> contacts) throws IOException {
    List<DataMessage.Contact> results = new LinkedList<>();

    for (SharedContact contact : contacts) {
      DataMessage.Contact.Name.Builder nameBuilder    = DataMessage.Contact.Name.newBuilder();

      if (contact.getName().getFamily().isPresent())  nameBuilder.setFamilyName(contact.getName().getFamily().get());
      if (contact.getName().getGiven().isPresent())   nameBuilder.setGivenName(contact.getName().getGiven().get());
      if (contact.getName().getMiddle().isPresent())  nameBuilder.setMiddleName(contact.getName().getMiddle().get());
      if (contact.getName().getPrefix().isPresent())  nameBuilder.setPrefix(contact.getName().getPrefix().get());
      if (contact.getName().getSuffix().isPresent())  nameBuilder.setSuffix(contact.getName().getSuffix().get());
      if (contact.getName().getDisplay().isPresent()) nameBuilder.setDisplayName(contact.getName().getDisplay().get());

      DataMessage.Contact.Builder contactBuilder = DataMessage.Contact.newBuilder()
                                                                      .setName(nameBuilder);

      if (contact.getAddress().isPresent()) {
        for (SharedContact.PostalAddress address : contact.getAddress().get()) {
          DataMessage.Contact.PostalAddress.Builder addressBuilder = DataMessage.Contact.PostalAddress.newBuilder();

          switch (address.getType()) {
            case HOME:   addressBuilder.setType(DataMessage.Contact.PostalAddress.Type.HOME); break;
            case WORK:   addressBuilder.setType(DataMessage.Contact.PostalAddress.Type.WORK); break;
            case CUSTOM: addressBuilder.setType(DataMessage.Contact.PostalAddress.Type.CUSTOM); break;
            default:     throw new AssertionError("Unknown type: " + address.getType());
          }

          if (address.getCity().isPresent())         addressBuilder.setCity(address.getCity().get());
          if (address.getCountry().isPresent())      addressBuilder.setCountry(address.getCountry().get());
          if (address.getLabel().isPresent())        addressBuilder.setLabel(address.getLabel().get());
          if (address.getNeighborhood().isPresent()) addressBuilder.setNeighborhood(address.getNeighborhood().get());
          if (address.getPobox().isPresent())        addressBuilder.setPobox(address.getPobox().get());
          if (address.getPostcode().isPresent())     addressBuilder.setPostcode(address.getPostcode().get());
          if (address.getRegion().isPresent())       addressBuilder.setRegion(address.getRegion().get());
          if (address.getStreet().isPresent())       addressBuilder.setStreet(address.getStreet().get());

          contactBuilder.addAddress(addressBuilder);
        }
      }

      if (contact.getEmail().isPresent()) {
        for (SharedContact.Email email : contact.getEmail().get()) {
          DataMessage.Contact.Email.Builder emailBuilder = DataMessage.Contact.Email.newBuilder()
                                                                                    .setValue(email.getValue());

          switch (email.getType()) {
            case HOME:   emailBuilder.setType(DataMessage.Contact.Email.Type.HOME);   break;
            case WORK:   emailBuilder.setType(DataMessage.Contact.Email.Type.WORK);   break;
            case MOBILE: emailBuilder.setType(DataMessage.Contact.Email.Type.MOBILE); break;
            case CUSTOM: emailBuilder.setType(DataMessage.Contact.Email.Type.CUSTOM); break;
            default:     throw new AssertionError("Unknown type: " + email.getType());
          }

          if (email.getLabel().isPresent()) emailBuilder.setLabel(email.getLabel().get());

          contactBuilder.addEmail(emailBuilder);
        }
      }

      if (contact.getPhone().isPresent()) {
        for (SharedContact.Phone phone : contact.getPhone().get()) {
          DataMessage.Contact.Phone.Builder phoneBuilder = DataMessage.Contact.Phone.newBuilder()
                                                                                    .setValue(phone.getValue());

          switch (phone.getType()) {
            case HOME:   phoneBuilder.setType(DataMessage.Contact.Phone.Type.HOME);   break;
            case WORK:   phoneBuilder.setType(DataMessage.Contact.Phone.Type.WORK);   break;
            case MOBILE: phoneBuilder.setType(DataMessage.Contact.Phone.Type.MOBILE); break;
            case CUSTOM: phoneBuilder.setType(DataMessage.Contact.Phone.Type.CUSTOM); break;
            default:     throw new AssertionError("Unknown type: " + phone.getType());
          }

          if (phone.getLabel().isPresent()) phoneBuilder.setLabel(phone.getLabel().get());

          contactBuilder.addNumber(phoneBuilder);
        }
      }

      if (contact.getAvatar().isPresent()) {
        contactBuilder.setAvatar(DataMessage.Contact.Avatar.newBuilder()
                                                           .setAvatar(createAttachmentPointer(contact.getAvatar().get().getAttachment().asStream()))
                                                           .setIsProfile(contact.getAvatar().get().isProfile()));
      }

      if (contact.getOrganization().isPresent()) {
        contactBuilder.setOrganization(contact.getOrganization().get());
      }

      results.add(contactBuilder.build());
    }

    return results;
  }

  private SendMessageResponseList sendMessage(List<OpenchatServiceAddress> recipients, long timestamp, byte[] content)
      throws IOException
  {
    SendMessageResponseList responseList = new SendMessageResponseList();

    for (OpenchatServiceAddress recipient : recipients) {
      try {
        SendMessageResponse response = sendMessage(recipient, timestamp, content, false);
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

  private SendMessageResponse sendMessage(OpenchatServiceAddress recipient, long timestamp, byte[] content, boolean silent)
      throws UntrustedIdentityException, IOException
  {
    for (int i=0;i<3;i++) {
      try {
        OutgoingPushMessageList            messages = getEncryptedMessages(socket, recipient, timestamp, content, silent);
        Optional<OpenchatServiceMessagePipe> pipe     = this.pipe.get();

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
    byte[]             attachmentKey    = Util.getSecretBytes(64);
    long               paddedLength     = PaddingInputStream.getPaddedSize(attachment.getLength());
    long               ciphertextLength = AttachmentCipherOutputStream.getCiphertextLength(paddedLength);
    PushAttachmentData attachmentData   = new PushAttachmentData(attachment.getContentType(),
                                                                 new PaddingInputStream(attachment.getInputStream(), attachment.getLength()),
                                                                 ciphertextLength,
                                                                 new AttachmentCipherOutputStreamFactory(attachmentKey),
                                                                 attachment.getListener());

    Pair<Long, byte[]> attachmentIdAndDigest = socket.sendAttachment(attachmentData);

    AttachmentPointer.Builder builder = AttachmentPointer.newBuilder()
                                                         .setContentType(attachment.getContentType())
                                                         .setId(attachmentIdAndDigest.first())
                                                         .setKey(ByteString.copyFrom(attachmentKey))
                                                         .setDigest(ByteString.copyFrom(attachmentIdAndDigest.second()))
                                                         .setSize((int)attachment.getLength());

    if (attachment.getFileName().isPresent()) {
      builder.setFileName(attachment.getFileName().get());
    }

    if (attachment.getPreview().isPresent()) {
      builder.setThumbnail(ByteString.copyFrom(attachment.getPreview().get()));
    }

    if (attachment.getWidth() > 0) {
      builder.setWidth(attachment.getWidth());
    }

    if (attachment.getHeight() > 0) {
      builder.setHeight(attachment.getHeight());
    }

    if (attachment.getVoiceNote()) {
      builder.setFlags(AttachmentPointer.Flags.VOICE_MESSAGE_VALUE);
    }

    return builder.build();
  }

  private OutgoingPushMessageList getEncryptedMessages(PushServiceSocket socket,
                                                       OpenchatServiceAddress recipient,
                                                       long timestamp,
                                                       byte[] plaintext,
                                                       boolean silent)
      throws IOException, UntrustedIdentityException
  {
    List<OutgoingPushMessage> messages = new LinkedList<>();

    if (!recipient.equals(localAddress)) {
      messages.add(getEncryptedMessage(socket, recipient, OpenchatServiceAddress.DEFAULT_DEVICE_ID, plaintext, silent));
    }

    for (int deviceId : store.getSubDeviceSessions(recipient.getNumber())) {
      if (store.containsSession(new OpenchatProtocolAddress(recipient.getNumber(), deviceId))) {
        messages.add(getEncryptedMessage(socket, recipient, deviceId, plaintext, silent));
      }
    }

    return new OutgoingPushMessageList(recipient.getNumber(), timestamp, recipient.getRelay().orNull(), messages);
  }

  private OutgoingPushMessage getEncryptedMessage(PushServiceSocket socket, OpenchatServiceAddress recipient, int deviceId, byte[] plaintext, boolean silent)
      throws IOException, UntrustedIdentityException
  {
    OpenchatProtocolAddress openchatProtocolAddress = new OpenchatProtocolAddress(recipient.getNumber(), deviceId);
    OpenchatServiceCipher   cipher                = new OpenchatServiceCipher(localAddress, store);

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

    try {
      return cipher.encrypt(openchatProtocolAddress, plaintext, silent);
    } catch (com.openchat.protocal.UntrustedIdentityException e) {
      throw new UntrustedIdentityException("Untrusted on send", recipient.getNumber(), e.getUntrustedIdentity());
    }
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
