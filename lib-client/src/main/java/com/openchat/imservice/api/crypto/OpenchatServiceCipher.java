package com.openchat.imservice.api.crypto;

import com.google.protobuf.InvalidProtocolBufferException;

import com.openchat.protocal.OpenchatAddress;
import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.SessionCipher;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.protocol.CiphertextMessage;
import com.openchat.protocal.protocol.PreKeyOpenchatMessage;
import com.openchat.protocal.protocol.OpenchatMessage;
import com.openchat.protocal.state.OpenchatStore;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentPointer;
import com.openchat.imservice.api.messages.OpenchatServiceContent;
import com.openchat.imservice.api.messages.OpenchatServiceDataMessage;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.multidevice.RequestMessage;
import com.openchat.imservice.api.messages.multidevice.SentTranscriptMessage;
import com.openchat.imservice.api.messages.multidevice.OpenchatServiceSyncMessage;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.internal.push.OutgoingPushMessage;
import com.openchat.imservice.internal.push.PushTransportDetails;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.AttachmentPointer;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.Content;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.DataMessage;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope.Type;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage;
import com.openchat.imservice.internal.util.Base64;

import java.util.LinkedList;
import java.util.List;

import static com.openchat.imservice.internal.push.OpenchatServiceProtos.GroupContext.Type.DELIVER;

public class OpenchatServiceCipher {

  private static final String TAG = OpenchatServiceCipher.class.getSimpleName();

  private final OpenchatStore      axolotlStore;
  private final OpenchatServiceAddress localAddress;

  public OpenchatServiceCipher(OpenchatServiceAddress localAddress, OpenchatStore axolotlStore) {
    this.axolotlStore = axolotlStore;
    this.localAddress = localAddress;
  }

  public OutgoingPushMessage encrypt(OpenchatAddress destination, byte[] unpaddedMessage, boolean legacy) {
    SessionCipher        sessionCipher        = new SessionCipher(axolotlStore, destination);
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

    return new OutgoingPushMessage(type, destination.getDeviceId(), remoteRegistrationId,
                                   legacy ? body : null, legacy ? null : body);
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
    OpenchatAddress sourceAddress = new OpenchatAddress(envelope.getSource(), envelope.getSourceDevice());
    SessionCipher  sessionCipher = new SessionCipher(axolotlStore, sourceAddress);

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

  private OpenchatServiceDataMessage createOpenchatServiceMessage(OpenchatServiceEnvelope envelope, DataMessage content) {
    OpenchatServiceGroup            groupInfo   = createGroupInfo(envelope, content);
    List<OpenchatServiceAttachment> attachments = new LinkedList<>();
    boolean                    endSession  = ((content.getFlags() & DataMessage.Flags.END_SESSION_VALUE) != 0);

    for (AttachmentPointer pointer : content.getAttachmentsList()) {
      attachments.add(new OpenchatServiceAttachmentPointer(pointer.getId(),
                                                      pointer.getContentType(),
                                                      pointer.getKey().toByteArray(),
                                                      envelope.getRelay(),
                                                      pointer.hasSize() ? Optional.of(pointer.getSize()) : Optional.<Integer>absent(),
                                                      pointer.hasThumbnail() ? Optional.of(pointer.getThumbnail().toByteArray()): Optional.<byte[]>absent()));
    }

    return new OpenchatServiceDataMessage(envelope.getTimestamp(), groupInfo, attachments,
                                     content.getBody(), endSession);
  }

  private OpenchatServiceSyncMessage createSynchronizeMessage(OpenchatServiceEnvelope envelope, SyncMessage content) {
    if (content.hasSent()) {
      SyncMessage.Sent sentContent = content.getSent();
      return OpenchatServiceSyncMessage.forSentTranscript(new SentTranscriptMessage(sentContent.getDestination(),
                                                                               sentContent.getTimestamp(),
                                                                               createOpenchatServiceMessage(envelope, sentContent.getMessage())));
    }

    if (content.hasRequest()) {
      return OpenchatServiceSyncMessage.forRequest(new RequestMessage(content.getRequest()));
    }

    return OpenchatServiceSyncMessage.empty();
  }

  private OpenchatServiceGroup createGroupInfo(OpenchatServiceEnvelope envelope, DataMessage content) {
    if (!content.hasGroup()) return null;

    OpenchatServiceGroup.Type type;

    switch (content.getGroup().getType()) {
      case DELIVER: type = OpenchatServiceGroup.Type.DELIVER; break;
      case UPDATE:  type = OpenchatServiceGroup.Type.UPDATE;  break;
      case QUIT:    type = OpenchatServiceGroup.Type.QUIT;    break;
      default:      type = OpenchatServiceGroup.Type.UNKNOWN; break;
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
        avatar = new OpenchatServiceAttachmentPointer(content.getGroup().getAvatar().getId(),
                                                 content.getGroup().getAvatar().getContentType(),
                                                 content.getGroup().getAvatar().getKey().toByteArray(),
                                                 envelope.getRelay());
      }

      return new OpenchatServiceGroup(type, content.getGroup().getId().toByteArray(), name, members, avatar);
    }

    return new OpenchatServiceGroup(content.getGroup().getId().toByteArray());
  }

}

