package com.openchat.imservice.api.crypto;

import com.google.protobuf.InvalidProtocolBufferException;

import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.SessionCipher;
import com.openchat.protocal.OpenchatProtocolAddress;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.protocol.CiphertextMessage;
import com.openchat.protocal.protocol.PreKeyOpenchatMessage;
import com.openchat.protocal.protocol.OpenchatMessage;
import com.openchat.protocal.state.OpenchatProtocolStore;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentPointer;
import com.openchat.imservice.api.messages.OpenchatServiceContent;
import com.openchat.imservice.api.messages.OpenchatServiceDataMessage;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceReceiptMessage;
import com.openchat.imservice.api.messages.calls.AnswerMessage;
import com.openchat.imservice.api.messages.calls.BusyMessage;
import com.openchat.imservice.api.messages.calls.HangupMessage;
import com.openchat.imservice.api.messages.calls.IceUpdateMessage;
import com.openchat.imservice.api.messages.calls.OfferMessage;
import com.openchat.imservice.api.messages.calls.OpenchatServiceCallMessage;
import com.openchat.imservice.api.messages.multidevice.ReadMessage;
import com.openchat.imservice.api.messages.multidevice.RequestMessage;
import com.openchat.imservice.api.messages.multidevice.SentTranscriptMessage;
import com.openchat.imservice.api.messages.multidevice.OpenchatServiceSyncMessage;
import com.openchat.imservice.api.messages.multidevice.VerifiedMessage;
import com.openchat.imservice.api.messages.multidevice.VerifiedMessage.VerifiedState;
import com.openchat.imservice.api.messages.shared.SharedContact;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.internal.push.OutgoingPushMessage;
import com.openchat.imservice.internal.push.PushTransportDetails;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.Content;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.ReceiptMessage;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified;
import com.openchat.imservice.internal.util.Base64;

import java.util.LinkedList;
import java.util.List;

import static com.openchat.imservice.internal.push.OpenchatServiceProtos.CallMessage;
import static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type.DELIVER;

public class OpenchatServiceCipher {

  @SuppressWarnings("unused")
  private static final String TAG = OpenchatServiceCipher.class.getSimpleName();

  private final OpenchatProtocolStore  openchatProtocolStore;
  private final OpenchatServiceAddress localAddress;

  public OpenchatServiceCipher(OpenchatServiceAddress localAddress, OpenchatProtocolStore openchatProtocolStore) {
    this.openchatProtocolStore = openchatProtocolStore;
    this.localAddress = localAddress;
  }

  public OutgoingPushMessage encrypt(OpenchatProtocolAddress destination, byte[] unpaddedMessage, boolean silent)
      throws UntrustedIdentityException
  {
    SessionCipher        sessionCipher        = new SessionCipher(openchatProtocolStore, destination);
    PushTransportDetails transportDetails     = new PushTransportDetails(sessionCipher.getSessionVersion());
    CiphertextMessage    message              = sessionCipher.encrypt(transportDetails.getPaddedMessageBody(unpaddedMessage));
    int                  remoteRegistrationId = sessionCipher.getRemoteRegistrationId();
    String               body                 = Base64.encodeBytes(message.serialize());

    int type;

    switch (message.getType()) {
      case CiphertextMessage.PREKEY_TYPE:  type = Type.PREKEY_BUNDLE_VALUE; break;
      case CiphertextMessage.OPENCHAT_TYPE: type = Type.CIPHERTEXT_VALUE;    break;
      default: throw new AssertionError("Bad type: " + message.getType());
    }

    return new OutgoingPushMessage(type, destination.getDeviceId(), remoteRegistrationId, body, silent);
  }

  
  public OpenchatServiceContent decrypt(OpenchatServiceEnvelope envelope)
      throws InvalidVersionException, InvalidMessageException, InvalidKeyException,
             DuplicateMessageException, InvalidKeyIdException, UntrustedIdentityException,
             LegacyMessageException, NoSessionException
  {
    try {
      OpenchatServiceContent content = new OpenchatServiceContent();

      if (envelope.hasLegacyMessage()) {
        DataMessage message = DataMessage.parseFrom(decrypt(envelope, envelope.getLegacyMessage()));
        content = new OpenchatServiceContent(createOpenchatServiceMessage(envelope, message));
      } else if (envelope.hasContent()) {
        Content message = Content.parseFrom(decrypt(envelope, envelope.getContent()));

        if (message.hasDataMessage()) {
          content = new OpenchatServiceContent(createOpenchatServiceMessage(envelope, message.getDataMessage()));
        } else if (message.hasSyncMessage() && localAddress.getNumber().equals(envelope.getSource())) {
          content = new OpenchatServiceContent(createSynchronizeMessage(envelope, message.getSyncMessage()));
        } else if (message.hasCallMessage()) {
          content = new OpenchatServiceContent(createCallMessage(message.getCallMessage()));
        } else if (message.hasReceiptMessage()) {
          content = new OpenchatServiceContent(createReceiptMessage(envelope, message.getReceiptMessage()));
        }
      }

      return content;
    } catch (InvalidProtocolBufferException e) {
      throw new InvalidMessageException(e);
    }
  }

  private byte[] decrypt(OpenchatServiceEnvelope envelope, byte[] ciphertext)
      throws InvalidVersionException, InvalidMessageException, InvalidKeyException,
             DuplicateMessageException, InvalidKeyIdException, UntrustedIdentityException,
             LegacyMessageException, NoSessionException
  {
    OpenchatProtocolAddress sourceAddress = new OpenchatProtocolAddress(envelope.getSource(), envelope.getSourceDevice());
    SessionCipher         sessionCipher = new SessionCipher(openchatProtocolStore, sourceAddress);

    byte[] paddedMessage;

    if (envelope.isPreKeyOpenchatMessage()) {
      paddedMessage = sessionCipher.decrypt(new PreKeyOpenchatMessage(ciphertext));
    } else if (envelope.isOpenchatMessage()) {
      paddedMessage = sessionCipher.decrypt(new OpenchatMessage(ciphertext));
    } else {
      throw new InvalidMessageException("Unknown type: " + envelope.getType());
    }

    PushTransportDetails transportDetails = new PushTransportDetails(sessionCipher.getSessionVersion());
    return transportDetails.getStrippedPaddingMessageBody(paddedMessage);
  }

  private OpenchatServiceDataMessage createOpenchatServiceMessage(OpenchatServiceEnvelope envelope, DataMessage content) throws InvalidMessageException {
    OpenchatServiceGroup             groupInfo        = createGroupInfo(envelope, content);
    List<OpenchatServiceAttachment>  attachments      = new LinkedList<>();
    boolean                        endSession       = ((content.getFlags() & DataMessage.Flags.END_SESSION_VALUE            ) != 0);
    boolean                        expirationUpdate = ((content.getFlags() & DataMessage.Flags.EXPIRATION_TIMER_UPDATE_VALUE) != 0);
    boolean                        profileKeyUpdate = ((content.getFlags() & DataMessage.Flags.PROFILE_KEY_UPDATE_VALUE     ) != 0);
    OpenchatServiceDataMessage.Quote quote            = createQuote(envelope, content);
    List<SharedContact>            sharedContacts   = createSharedContacts(envelope, content);

    for (AttachmentPointer pointer : content.getAttachmentsList()) {
      attachments.add(createAttachmentPointer(envelope, pointer));
    }

    if (content.hasTimestamp() && content.getTimestamp() != envelope.getTimestamp()) {
      throw new InvalidMessageException("Timestamps don't match: " + content.getTimestamp() + " vs " + envelope.getTimestamp());
    }

    return new OpenchatServiceDataMessage(envelope.getTimestamp(), groupInfo, attachments,
                                        content.getBody(), endSession, content.getExpireTimer(),
                                        expirationUpdate, content.hasProfileKey() ? content.getProfileKey().toByteArray() : null,
                                        profileKeyUpdate, quote, sharedContacts);
  }

  private OpenchatServiceSyncMessage createSynchronizeMessage(OpenchatServiceEnvelope envelope, SyncMessage content) throws InvalidMessageException {
    if (content.hasSent()) {
      SyncMessage.Sent sentContent = content.getSent();
      return OpenchatServiceSyncMessage.forSentTranscript(new SentTranscriptMessage(sentContent.getDestination(),
                                                                                  sentContent.getTimestamp(),
                                                                                  createOpenchatServiceMessage(envelope, sentContent.getMessage()),
                                                                                  sentContent.getExpirationStartTimestamp()));
    }

    if (content.hasRequest()) {
      return OpenchatServiceSyncMessage.forRequest(new RequestMessage(content.getRequest()));
    }

    if (content.getReadList().size() > 0) {
      List<ReadMessage> readMessages = new LinkedList<>();

      for (SyncMessage.Read read : content.getReadList()) {
        readMessages.add(new ReadMessage(read.getSender(), read.getTimestamp()));
      }

      return OpenchatServiceSyncMessage.forRead(readMessages);
    }

    if (content.hasVerified()) {
      try {
        Verified    verified    = content.getVerified();
        String      destination = verified.getDestination();
        IdentityKey identityKey = new IdentityKey(verified.getIdentityKey().toByteArray(), 0);

        VerifiedState verifiedState;

        if (verified.getState() == Verified.State.DEFAULT) {
          verifiedState = VerifiedState.DEFAULT;
        } else if (verified.getState() == Verified.State.VERIFIED) {
          verifiedState = VerifiedState.VERIFIED;
        } else if (verified.getState() == Verified.State.UNVERIFIED) {
          verifiedState = VerifiedState.UNVERIFIED;
        } else {
          throw new InvalidMessageException("Unknown state: " + verified.getState().getNumber());
        }

        return OpenchatServiceSyncMessage.forVerified(new VerifiedMessage(destination, identityKey, verifiedState, System.currentTimeMillis()));
      } catch (InvalidKeyException e) {
        throw new InvalidMessageException(e);
      }
    }

    return OpenchatServiceSyncMessage.empty();
  }

  private OpenchatServiceCallMessage createCallMessage(CallMessage content) {
    if (content.hasOffer()) {
      CallMessage.Offer offerContent = content.getOffer();
      return OpenchatServiceCallMessage.forOffer(new OfferMessage(offerContent.getId(), offerContent.getDescription()));
    } else if (content.hasAnswer()) {
      CallMessage.Answer answerContent = content.getAnswer();
      return OpenchatServiceCallMessage.forAnswer(new AnswerMessage(answerContent.getId(), answerContent.getDescription()));
    } else if (content.getIceUpdateCount() > 0) {
      List<IceUpdateMessage> iceUpdates = new LinkedList<>();

      for (CallMessage.IceUpdate iceUpdate : content.getIceUpdateList()) {
        iceUpdates.add(new IceUpdateMessage(iceUpdate.getId(), iceUpdate.getSdpMid(), iceUpdate.getSdpMLineIndex(), iceUpdate.getSdp()));
      }

      return OpenchatServiceCallMessage.forIceUpdates(iceUpdates);
    } else if (content.hasHangup()) {
      CallMessage.Hangup hangup = content.getHangup();
      return OpenchatServiceCallMessage.forHangup(new HangupMessage(hangup.getId()));
    } else if (content.hasBusy()) {
      CallMessage.Busy busy = content.getBusy();
      return OpenchatServiceCallMessage.forBusy(new BusyMessage(busy.getId()));
    }

    return OpenchatServiceCallMessage.empty();
  }

  private OpenchatServiceReceiptMessage createReceiptMessage(OpenchatServiceEnvelope envelope, ReceiptMessage content) {
    OpenchatServiceReceiptMessage.Type type;

    if      (content.getType() == ReceiptMessage.Type.DELIVERY) type = OpenchatServiceReceiptMessage.Type.DELIVERY;
    else if (content.getType() == ReceiptMessage.Type.READ)     type = OpenchatServiceReceiptMessage.Type.READ;
    else                                                        type = OpenchatServiceReceiptMessage.Type.UNKNOWN;

    return new OpenchatServiceReceiptMessage(type, content.getTimestampList(), envelope.getTimestamp());
  }

  private OpenchatServiceDataMessage.Quote createQuote(OpenchatServiceEnvelope envelope, DataMessage content) {
    if (!content.hasQuote()) return null;

    List<OpenchatServiceDataMessage.Quote.QuotedAttachment> attachments = new LinkedList<>();

    for (DataMessage.Quote.QuotedAttachment attachment : content.getQuote().getAttachmentsList()) {
      attachments.add(new OpenchatServiceDataMessage.Quote.QuotedAttachment(attachment.getContentType(),
                                                                          attachment.getFileName(),
                                                                          attachment.hasThumbnail() ? createAttachmentPointer(envelope, attachment.getThumbnail()) : null));
    }

    return new OpenchatServiceDataMessage.Quote(content.getQuote().getId(),
                                              new OpenchatServiceAddress(content.getQuote().getAuthor()),
                                              content.getQuote().getText(),
                                              attachments);
  }

  private List<SharedContact> createSharedContacts(OpenchatServiceEnvelope envelope, DataMessage content) {
    if (content.getContactCount() <= 0) return null;

    List<SharedContact> results = new LinkedList<>();

    for (DataMessage.Contact contact : content.getContactList()) {
      SharedContact.Builder builder = SharedContact.newBuilder()
                                                   .setName(SharedContact.Name.newBuilder()
                                                                              .setDisplay(contact.getName().getDisplayName())
                                                                              .setFamily(contact.getName().getFamilyName())
                                                                              .setGiven(contact.getName().getGivenName())
                                                                              .setMiddle(contact.getName().getMiddleName())
                                                                              .setPrefix(contact.getName().getPrefix())
                                                                              .setSuffix(contact.getName().getSuffix())
                                                                              .build());

      if (contact.getAddressCount() > 0) {
        for (DataMessage.Contact.PostalAddress address : contact.getAddressList()) {
          SharedContact.PostalAddress.Type type = SharedContact.PostalAddress.Type.HOME;

          switch (address.getType()) {
            case WORK:   type = SharedContact.PostalAddress.Type.WORK;   break;
            case HOME:   type = SharedContact.PostalAddress.Type.HOME;   break;
            case CUSTOM: type = SharedContact.PostalAddress.Type.CUSTOM; break;
          }

          builder.withAddress(SharedContact.PostalAddress.newBuilder()
                                                         .setCity(address.getCity())
                                                         .setCountry(address.getCountry())
                                                         .setLabel(address.getLabel())
                                                         .setNeighborhood(address.getNeighborhood())
                                                         .setPobox(address.getPobox())
                                                         .setPostcode(address.getPostcode())
                                                         .setRegion(address.getRegion())
                                                         .setStreet(address.getStreet())
                                                         .setType(type)
                                                         .build());
        }
      }

      if (contact.getNumberCount() > 0) {
        for (DataMessage.Contact.Phone phone : contact.getNumberList()) {
          SharedContact.Phone.Type type = SharedContact.Phone.Type.HOME;

          switch (phone.getType()) {
            case HOME:   type = SharedContact.Phone.Type.HOME;   break;
            case WORK:   type = SharedContact.Phone.Type.WORK;   break;
            case MOBILE: type = SharedContact.Phone.Type.MOBILE; break;
            case CUSTOM: type = SharedContact.Phone.Type.CUSTOM; break;
          }

          builder.withPhone(SharedContact.Phone.newBuilder()
                                               .setLabel(phone.getLabel())
                                               .setType(type)
                                               .setValue(phone.getValue())
                                               .build());
        }
      }

      if (contact.getEmailCount() > 0) {
        for (DataMessage.Contact.Email email : contact.getEmailList()) {
          SharedContact.Email.Type type = SharedContact.Email.Type.HOME;

          switch (email.getType()) {
            case HOME:   type = SharedContact.Email.Type.HOME;   break;
            case WORK:   type = SharedContact.Email.Type.WORK;   break;
            case MOBILE: type = SharedContact.Email.Type.MOBILE; break;
            case CUSTOM: type = SharedContact.Email.Type.CUSTOM; break;
          }

          builder.withEmail(SharedContact.Email.newBuilder()
                                               .setLabel(email.getLabel())
                                               .setType(type)
                                               .setValue(email.getValue())
                                               .build());
        }
      }

      if (contact.hasAvatar()) {
        builder.setAvatar(SharedContact.Avatar.newBuilder()
                                              .withAttachment(createAttachmentPointer(envelope, contact.getAvatar().getAvatar()))
                                              .withProfileFlag(contact.getAvatar().getIsProfile())
                                              .build());
      }

      if (contact.hasOrganization()) {
        builder.withOrganization(contact.getOrganization());
      }

      results.add(builder.build());
    }

    return results;
  }

  private OpenchatServiceAttachmentPointer createAttachmentPointer(OpenchatServiceEnvelope envelope, AttachmentPointer pointer) {
    return new OpenchatServiceAttachmentPointer(pointer.getId(),
                                              pointer.getContentType(),
                                              pointer.getKey().toByteArray(),
                                              envelope.getRelay(),
                                              pointer.hasSize() ? Optional.of(pointer.getSize()) : Optional.<Integer>absent(),
                                              pointer.hasThumbnail() ? Optional.of(pointer.getThumbnail().toByteArray()): Optional.<byte[]>absent(),
                                              pointer.getWidth(), pointer.getHeight(),
                                              pointer.hasDigest() ? Optional.of(pointer.getDigest().toByteArray()) : Optional.<byte[]>absent(),
                                              pointer.hasFileName() ? Optional.of(pointer.getFileName()) : Optional.<String>absent(),
                                              (pointer.getFlags() & AttachmentPointer.Flags.VOICE_MESSAGE_VALUE) != 0);

  }

  private OpenchatServiceGroup createGroupInfo(OpenchatServiceEnvelope envelope, DataMessage content) {
    if (!content.hasGroup()) return null;

    OpenchatServiceGroup.Type type;

    switch (content.getGroup().getType()) {
      case DELIVER:      type = OpenchatServiceGroup.Type.DELIVER;      break;
      case UPDATE:       type = OpenchatServiceGroup.Type.UPDATE;       break;
      case QUIT:         type = OpenchatServiceGroup.Type.QUIT;         break;
      case REQUEST_INFO: type = OpenchatServiceGroup.Type.REQUEST_INFO; break;
      default:           type = OpenchatServiceGroup.Type.UNKNOWN;      break;
    }

    if (content.getGroup().getType() != DELIVER) {
      String                      name    = null;
      List<String>                members = null;
      OpenchatServiceAttachmentPointer avatar  = null;

      if (content.getGroup().hasName()) {
        name = content.getGroup().getName();
      }

      if (content.getGroup().getMembersCount() > 0) {
        members = content.getGroup().getMembersList();
      }

      if (content.getGroup().hasAvatar()) {
        AttachmentPointer pointer = content.getGroup().getAvatar();

        avatar = new OpenchatServiceAttachmentPointer(pointer.getId(),
                                                    pointer.getContentType(),
                                                    pointer.getKey().toByteArray(),
                                                    envelope.getRelay(),
                                                    Optional.of(pointer.getSize()),
                                                    Optional.<byte[]>absent(), 0, 0,
                                                    Optional.fromNullable(pointer.hasDigest() ? pointer.getDigest().toByteArray() : null),
                                                    Optional.<String>absent(),
                                                    false);
      }

      return new OpenchatServiceGroup(type, content.getGroup().getId().toByteArray(), name, members, avatar);
    }

    return new OpenchatServiceGroup(content.getGroup().getId().toByteArray());
  }

}

