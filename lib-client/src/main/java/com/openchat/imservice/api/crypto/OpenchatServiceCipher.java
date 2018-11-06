package com.openchat.imservice.api.crypto;

import com.google.protobuf.InvalidProtocolBufferException;

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
import com.openchat.imservice.internal.push.PushTransportDetails;

import java.util.LinkedList;
import java.util.List;

import static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent;
import static com.openchat.imservice.internal.push.PushMessageProtos.PushMessageContent.GroupContext.Type.DELIVER;

public class OpenchatServiceCipher {

  private final SessionCipher sessionCipher;

  public OpenchatServiceCipher(OpenchatStore axolotlStore, long recipientId, int deviceId) {
    this.sessionCipher = new SessionCipher(axolotlStore, recipientId, deviceId);
  }

  public CiphertextMessage encrypt(byte[] unpaddedMessage) {
    PushTransportDetails transportDetails = new PushTransportDetails(sessionCipher.getSessionVersion());
    return sessionCipher.encrypt(transportDetails.getPaddedMessageBody(unpaddedMessage));
  }

  
  public OpenchatServiceMessage decrypt(OpenchatServiceEnvelope envelope)
      throws InvalidVersionException, InvalidMessageException, InvalidKeyException,
             DuplicateMessageException, InvalidKeyIdException, UntrustedIdentityException,
             LegacyMessageException, NoSessionException
  {
    try {
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

  public int getRemoteRegistrationId() {
    return sessionCipher.getRemoteRegistrationId();
  }

  private OpenchatServiceMessage createOpenchatServiceMessage(OpenchatServiceEnvelope envelope, PushMessageContent content) {
    OpenchatServiceGroup            groupInfo   = createGroupInfo(envelope, content);
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
                                 content.getBody(), secure, endSession);
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

