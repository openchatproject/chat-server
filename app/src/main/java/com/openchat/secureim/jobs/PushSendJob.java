package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.secureim.util.Util;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.secureim.database.OpenchatServiceDirectory;
import com.openchat.imservice.push.PushAddress;
import com.openchat.imservice.util.InvalidNumberException;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.SendReq;

public abstract class PushSendJob extends MasterSecretJob {

  private static final String TAG = PushSendJob.class.getSimpleName();

  protected PushSendJob(Context context, JobParameters parameters) {
    super(context, parameters);
  }

  protected static JobParameters constructParameters(Context context, String destination) {
    JobParameters.Builder builder = JobParameters.newBuilder();
    builder.withPersistence();
    builder.withGroupId(destination);
    builder.withRequirement(new MasterSecretRequirement(context));

    if (!isSmsFallbackSupported(context, destination)) {
      builder.withRequirement(new NetworkRequirement(context));
      builder.withRetryCount(5);
    }

    return builder.create();
  }

  protected static boolean isSmsFallbackSupported(Context context, String destination) {
    if (GroupUtil.isEncodedGroup(destination)) {
      return false;
    }

    if (!OpenchatServicePreferences.isFallbackSmsAllowed(context)) {
      return false;
    }

    OpenchatServiceDirectory directory = OpenchatServiceDirectory.getInstance(context);
    return directory.isSmsFallbackSupported(destination);
  }

  protected PushAddress getPushAddress(Recipient recipient) throws InvalidNumberException {
    String e164number = Util.canonicalizeNumber(context, recipient.getNumber());
    String relay      = OpenchatServiceDirectory.getInstance(context).getRelay(e164number);
    return new PushAddress(recipient.getRecipientId(), e164number, 1, relay);
  }

  protected boolean isSmsFallbackApprovalRequired(String destination) {
    return (isSmsFallbackSupported(context, destination) && OpenchatServicePreferences.isFallbackSmsAskRequired(context));
  }

  protected List<OpenchatServiceAttachment> getAttachments(SendReq message) {
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

  protected void notifyMediaMessageDeliveryFailed(Context context, long messageId) {
    long       threadId   = DatabaseFactory.getMmsDatabase(context).getThreadIdForMessage(messageId);
    Recipients recipients = DatabaseFactory.getThreadDatabase(context).getRecipientsForThreadId(threadId);

    MessageNotifier.notifyMessageDeliveryFailed(context, recipients, threadId);
  }
}
