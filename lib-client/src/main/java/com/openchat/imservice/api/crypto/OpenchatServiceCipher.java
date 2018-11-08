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
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentPointer;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;
import com.openchat.imservice.api.messages.OpenchatServiceSyncContext;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.internal.push.OutgoingPushMessage;
import com.openchat.imservice.internal.push.PushTransportDetails;
import com.openchat.imservice.internal.util.Base64;

import java.util.LinkedList;
import java.util.List;

import static com.openchat.imservice.internal.push.PushMessageProtos.IncomingPushMessageOpenchat.Type;
import static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent;
import static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type.DELIVER;

public class OpenchatServiceCipher {

  private final OpenchatStore      axolotlStore;
  private final OpenchatServiceAddress localAddress;

  public OpenchatServiceCipher(OpenchatServiceAddress localAddress, OpenchatStore axolotlStore) {
    this.axolotlStore = axolotlStore;
    this.localAddress = localAddress;
  }

  public OutgoingPushMessage encrypt(OpenchatAddress destination, byte[] unpaddedMessage) {
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

    return new OutgoingPushMessage(type, destination.getDeviceId(), remoteRegistrationId, body);
  }

  
  public OpenchatServiceMessage decrypt(OpenchatServiceEnvelope envelope)
      throws InvalidVersionException, InvalidMessageException, InvalidKeyException,
             DuplicateMessageException, InvalidKeyIdException, UntrustedIdentityException,
             LegacyMessageException, NoSessionException
  {
    try {
      OpenchatAddress sourceAddress = new OpenchatAddress(envelope.getSource(), envelope.getSourceDevice());
      SessionCipher  sessionCipher = new SessionCipher(axolotlStore, sourceAddress);

      byte[] paddedMessage;

      if (envelope.isPreKeyOpenchatMessage()) {
        paddedMessage = sessionCipher.decrypt(new PreKeyOpenchatMessage(envelope.getMessage()));
      } else if (envelope.isOpenchatMessage()) {
        paddedMessage = sessionCipher.decrypt(new OpenchatMessage(envelope.getMessage()));
      } else if (envelope.isPlaintext()) {
        paddedMessage = envelope.getMessage();
      } else {
        throw new InvalidMessageException("Unknown type: " + envelope.getType());
      }

      PushTransportDetails transportDetails = new PushTransportDetails(sessionCipher.getSessionVersion());
      PushMessageContent   content          = PushMessageContent.parseFrom(transportDetails.getStrippedPaddingMessageBody(paddedMessage));

      return createOpenchatServiceMessage(envelope, content);
    } catch (InvalidProtocolBufferException e) {
      throw new InvalidMessageException(e);
    }
  }

  private OpenchatServiceMessage createOpenchatServiceMessage(OpenchatServiceEnvelope envelope, PushMessageContent content) {
    OpenchatServiceGroup            groupInfo   = createGroupInfo(envelope, content);
    OpenchatServiceSyncContext      syncContext = createSyncContext(envelope, content);
    List<OpenchatServiceAttachment> attachments = new LinkedList<>();
    boolean                    endSession  = ((content.getFlags() & PushMessageContent.Flags.END_SESSION_VALUE) != 0);
    boolean                    secure      = envelope.isOpenchatMessage() || envelope.isPreKeyOpenchatMessage();

    for (PushMessageContent.AttachmentPointer pointer : content.getAttachmentsList()) {
      attachments.add(new OpenchatServiceAttachmentPointer(pointer.getId(),
                                                      pointer.getContentType(),
                                                      pointer.getKey().toByteArray(),
                                                      envelope.getRelay()));
    }

    return new OpenchatServiceMessage(envelope.getTimestamp(), groupInfo, attachments,
                                 content.getBody(), syncContext, secure, endSession);
  }

  private OpenchatServiceSyncContext createSyncContext(OpenchatServiceEnvelope envelope, PushMessageContent content) {
    if (!content.hasSync())                                     return null;
    if (!envelope.getSource().equals(localAddress.getNumber())) return null;

    return new OpenchatServiceSyncContext(content.getSync().getDestination(),
                                     content.getSync().getTimestamp());
  }

  private OpenchatServiceGroup createGroupInfo(OpenchatServiceEnvelope envelope, PushMessageContent content) {
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

