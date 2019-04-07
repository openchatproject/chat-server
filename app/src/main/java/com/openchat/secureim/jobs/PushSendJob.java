package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.OpenchatServiceDirectory;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.mms.PartAuthority;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.secureim.util.Util;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.api.util.InvalidNumberException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.PduPart;
import ws.com.google.android.mms.pdu.SendReq;

public abstract class PushSendJob extends SendJob {

  private static final String TAG = PushSendJob.class.getSimpleName();

  protected PushSendJob(Context context, JobParameters parameters) {
    super(context, parameters);
  }

  protected static JobParameters constructParameters(Context context, String destination, boolean media) {
    JobParameters.Builder builder = JobParameters.newBuilder();
    builder.withPersistence();
    builder.withGroupId(destination);
    builder.withRequirement(new MasterSecretRequirement(context));

    if (!isSmsFallbackSupported(context, destination, media)) {
      builder.withRequirement(new NetworkRequirement(context));
      builder.withRetryCount(5);
    }

    return builder.create();
  }

  protected static boolean isSmsFallbackSupported(Context context, String destination, boolean media) {
    try {
      String e164number = Util.canonicalizeNumber(context, destination);

      if (GroupUtil.isEncodedGroup(e164number)) {
        return false;
      }

      if (!OpenchatServicePreferences.isFallbackSmsAllowed(context)) {
        return false;
      }

      if (media && !OpenchatServicePreferences.isFallbackMmsEnabled(context)) {
        return false;
      }

      OpenchatServiceDirectory directory = OpenchatServiceDirectory.getInstance(context);
      return directory.isSmsFallbackSupported(e164number);
    } catch (InvalidNumberException e) {
      Log.w(TAG, e);
      return false;
    }
  }

  protected OpenchatServiceAddress getPushAddress(String number) throws InvalidNumberException {
    String e164number = Util.canonicalizeNumber(context, number);
    String relay      = OpenchatServiceDirectory.getInstance(context).getRelay(e164number);
    return new OpenchatServiceAddress(e164number, Optional.fromNullable(relay));
  }

  protected boolean isSmsFallbackApprovalRequired(String destination, boolean media) {
    return (isSmsFallbackSupported(context, destination, media) && OpenchatServicePreferences.isFallbackSmsAskRequired(context));
  }

  protected List<OpenchatServiceAttachment> getAttachments(final MasterSecret masterSecret, final SendReq message) {
    List<OpenchatServiceAttachment> attachments = new LinkedList<>();

    for (int i=0;i<message.getBody().getPartsNum();i++) {
      PduPart part = message.getBody().getPart(i);
      String contentType = Util.toIsoString(part.getContentType());
      if (ContentType.isImageType(contentType) ||
          ContentType.isAudioType(contentType) ||
          ContentType.isVideoType(contentType))
      {

        try {
          InputStream is = PartAuthority.getPartStream(context, masterSecret, part.getDataUri());
          attachments.add(new OpenchatServiceAttachmentStream(is, contentType, part.getDataSize()));
        } catch (IOException ioe) {
          Log.w(TAG, "Couldn't open attachment", ioe);
        }
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
