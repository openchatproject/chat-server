package com.openchat.secureim.transport;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsSmsColumns;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.mms.PartParser;
import com.openchat.secureim.push.OpenchatServiceMessageSenderFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.secureim.util.Util;
import com.openchat.imservice.api.OpenchatServiceMessageSender;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;
import com.openchat.imservice.crypto.UntrustedIdentityException;
import com.openchat.imservice.directory.Directory;
import com.openchat.imservice.push.PushAddress;
import com.openchat.imservice.push.UnregisteredUserException;
import com.openchat.imservice.push.exceptions.EncapsulatedExceptions;
import com.openchat.imservice.util.Base64;
import com.openchat.imservice.util.InvalidNumberException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.SendReq;

import static com.openchat.imservice.push.PushMessageProtos.PushMessageContent.GroupContext;

public class PushTransport extends BaseTransport {

  private static final String TAG = PushTransport.class.getSimpleName();

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
      PushAddress             address       = getPushAddress(message.getIndividualRecipient());
      OpenchatServiceMessageSender messageSender = OpenchatServiceMessageSenderFactory.create(context, masterSecret);

      if (message.isEndSession()) {
        messageSender.sendMessage(address, new OpenchatServiceMessage(message.getDateSent(), null,
                                                                 null, null, true, true));
      } else {
        messageSender.sendMessage(address, new OpenchatServiceMessage(message.getDateSent(), null,
                                                                 message.getBody().getBody()));
      }

      context.sendBroadcast(constructSentIntent(context, message.getId(), message.getType(), true, true));
    } catch (InvalidNumberException e) {
      Log.w(TAG, e);
      throw new IOException("Badly formatted number.");
    }
  }

  public void deliverGroupMessage(SendReq message)
      throws IOException, RecipientFormattingException, InvalidNumberException, EncapsulatedExceptions
  {
    OpenchatServiceMessageSender    messageSender = OpenchatServiceMessageSenderFactory.create(context, masterSecret);
    byte[]                     groupId       = GroupUtil.getDecodedId(message.getTo()[0].getString());
    Recipients                 recipients    = DatabaseFactory.getGroupDatabase(context).getGroupMembers(groupId, false);
    List<PushAddress>          addresses     = getPushAddresses(recipients);
    List<OpenchatServiceAttachment> attachments   = getAttachments(message);

    if (MmsSmsColumns.Types.isGroupUpdate(message.getDatabaseMessageBox()) ||
        MmsSmsColumns.Types.isGroupQuit(message.getDatabaseMessageBox()))
    {
      String content = PartParser.getMessageText(message.getBody());

      if (content != null && !content.trim().isEmpty()) {
        GroupContext         groupContext = GroupContext.parseFrom(Base64.decode(content));
        OpenchatServiceAttachment avatar       = attachments.isEmpty() ? null : attachments.get(0);
        OpenchatServiceGroup.Type type         = MmsSmsColumns.Types.isGroupQuit(message.getDatabaseMessageBox()) ? OpenchatServiceGroup.Type.QUIT : OpenchatServiceGroup.Type.UPDATE;
        OpenchatServiceGroup      group        = new OpenchatServiceGroup(type, groupId, groupContext.getName(), groupContext.getMembersList(), avatar);
        OpenchatServiceMessage    groupMessage = new OpenchatServiceMessage(message.getSentTimestamp(), group, null, null);

        messageSender.sendMessage(addresses, groupMessage);
      }
    } else {
      String            body         = PartParser.getMessageText(message.getBody());
      OpenchatServiceGroup   group        = new OpenchatServiceGroup(groupId);
      OpenchatServiceMessage groupMessage = new OpenchatServiceMessage(message.getSentTimestamp(), group, attachments, body);

      messageSender.sendMessage(addresses, groupMessage);
    }
  }

  public void deliver(SendReq message)
      throws IOException, RecipientFormattingException, InvalidNumberException, EncapsulatedExceptions
  {
    OpenchatServiceMessageSender messageSender = OpenchatServiceMessageSenderFactory.create(context, masterSecret);
    String                  destination   = message.getTo()[0].getString();

    List<UntrustedIdentityException> untrustedIdentities = new LinkedList<>();
    List<UnregisteredUserException>  unregisteredUsers   = new LinkedList<>();

    if (GroupUtil.isEncodedGroup(destination)) {
      deliverGroupMessage(message);
      return;
    }

    try {
      Recipients                 recipients   = RecipientFactory.getRecipientsFromString(context, destination, false);
      PushAddress                address      = getPushAddress(recipients.getPrimaryRecipient());
      List<OpenchatServiceAttachment> attachments  = getAttachments(message);
      String                     body         = PartParser.getMessageText(message.getBody());
      OpenchatServiceMessage          mediaMessage = new OpenchatServiceMessage(message.getSentTimestamp(), attachments, body);

      messageSender.sendMessage(address, mediaMessage);
    } catch (UntrustedIdentityException e) {
      Log.w(TAG, e);
      untrustedIdentities.add(e);
    } catch (UnregisteredUserException e) {
      Log.w(TAG, e);
      unregisteredUsers.add(e);
    }

    if (!untrustedIdentities.isEmpty() || !unregisteredUsers.isEmpty()) {
      throw new EncapsulatedExceptions(untrustedIdentities, unregisteredUsers);
    }
  }

  private PushAddress getPushAddress(Recipient recipient) throws InvalidNumberException {
    String e164number = Util.canonicalizeNumber(context, recipient.getNumber());
    String relay      = Directory.getInstance(context).getRelay(e164number);
    return new PushAddress(recipient.getRecipientId(), e164number, 1, relay);
  }

  private List<PushAddress> getPushAddresses(Recipients recipients) throws InvalidNumberException {
    List<PushAddress> addresses = new LinkedList<>();

    for (Recipient recipient : recipients.getRecipientsList()) {
      addresses.add(getPushAddress(recipient));
    }

    return addresses;
  }

  private List<OpenchatServiceAttachment> getAttachments(SendReq message) {
    List<OpenchatServiceAttachment> attachments = new LinkedList<>();

    for (int i=0;i<message.getBody().getPartsNum();i++) {
      String contentType = Util.toIsoString(message.getBody().getPart(i).getContentType());
      if (ContentType.isImageType(contentType) ||
          ContentType.isAudioType(contentType) ||
          ContentType.isVideoType(contentType))
      {
        byte[] data = message.getBody().getPart(i).getData();
        Log.w(TAG, "Adding attachment...");
        attachments.add(new OpenchatServiceAttachmentStream(new ByteArrayInputStream(data), contentType, data.length));
      }
    }

    return attachments;
  }
}
