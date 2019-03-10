package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.MmsSmsColumns;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.mms.PartParser;
import com.openchat.secureim.push.OpenchatServiceMessageSenderFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.sms.IncomingIdentityUpdateMessage;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.imservice.api.OpenchatServiceMessageSender;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;
import com.openchat.imservice.crypto.UntrustedIdentityException;
import com.openchat.imservice.push.PushAddress;
import com.openchat.imservice.push.PushMessageProtos;
import com.openchat.imservice.push.exceptions.EncapsulatedExceptions;
import com.openchat.imservice.util.Base64;
import com.openchat.imservice.util.InvalidNumberException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ws.com.google.android.mms.MmsException;
import ws.com.google.android.mms.pdu.SendReq;

public class PushGroupSendJob extends PushSendJob {

  private static final String TAG = PushGroupSendJob.class.getSimpleName();

  private final long messageId;

  public PushGroupSendJob(Context context, long messageId, String destination) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withGroupId(destination)
                                .withRequirement(new MasterSecretRequirement(context))
                                .withRequirement(new NetworkRequirement(context))
                                .withRetryCount(5)
                                .create());

    this.messageId = messageId;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun() throws RequirementNotMetException, MmsException, IOException, NoSuchMessageException {
    MasterSecret masterSecret = getMasterSecret();
    MmsDatabase  database     = DatabaseFactory.getMmsDatabase(context);
    SendReq      message      = database.getOutgoingMessage(masterSecret, messageId);

    try {
      deliver(masterSecret, message);

      database.markAsPush(messageId);
      database.markAsSecure(messageId);
      database.markAsSent(messageId, "push".getBytes(), 0);
    } catch (InvalidNumberException | RecipientFormattingException e) {
      Log.w(TAG, e);
      database.markAsSentFailed(messageId);
      notifyMediaMessageDeliveryFailed(context, messageId);
    } catch (EncapsulatedExceptions e) {
      Log.w(TAG, e);
      if (!e.getUnregisteredUserExceptions().isEmpty()) {
        database.markAsSentFailed(messageId);
      }

      for (UntrustedIdentityException uie : e.getUntrustedIdentityExceptions()) {
        IncomingIdentityUpdateMessage identityUpdateMessage = IncomingIdentityUpdateMessage.createFor(message.getTo()[0].getString(), uie.getIdentityKey());
        DatabaseFactory.getEncryptingSmsDatabase(context).insertMessageInbox(masterSecret, identityUpdateMessage);
        database.markAsSentFailed(messageId);
      }

      notifyMediaMessageDeliveryFailed(context, messageId);
    }
  }

  @Override
  public void onCanceled() {
    DatabaseFactory.getMmsDatabase(context).markAsSentFailed(messageId);
  }

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    if (throwable instanceof RequirementNotMetException) return true;
    if (throwable instanceof IOException)                return true;
    return false;
  }

  private void deliver(MasterSecret masterSecret, SendReq message)
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
        PushMessageProtos.PushMessageContent.GroupContext groupContext = PushMessageProtos.PushMessageContent.GroupContext.parseFrom(Base64.decode(content));
        OpenchatServiceAttachment avatar       = attachments.isEmpty() ? null : attachments.get(0);
        OpenchatServiceGroup.Type type         = MmsSmsColumns.Types.isGroupQuit(message.getDatabaseMessageBox()) ? OpenchatServiceGroup.Type.QUIT : OpenchatServiceGroup.Type.UPDATE;
        OpenchatServiceGroup      group        = new OpenchatServiceGroup(type, groupId, groupContext.getName(), groupContext.getMembersList(), avatar);
        OpenchatServiceMessage groupMessage = new OpenchatServiceMessage(message.getSentTimestamp(), group, null, null);

        messageSender.sendMessage(addresses, groupMessage);
      }
    } else {
      String            body         = PartParser.getMessageText(message.getBody());
      OpenchatServiceGroup   group        = new OpenchatServiceGroup(groupId);
      OpenchatServiceMessage groupMessage = new OpenchatServiceMessage(message.getSentTimestamp(), group, attachments, body);

      messageSender.sendMessage(addresses, groupMessage);
    }
  }

  private List<PushAddress> getPushAddresses(Recipients recipients) throws InvalidNumberException {
    List<PushAddress> addresses = new LinkedList<>();

    for (Recipient recipient : recipients.getRecipientsList()) {
      addresses.add(getPushAddress(recipient));
    }

    return addresses;
  }

}
