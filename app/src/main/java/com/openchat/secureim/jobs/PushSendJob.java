package com.openchat.secureim.jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.TextSecureExpiredException;
import com.openchat.secureim.attachments.Attachment;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.ProfileKeyUtil;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.events.PartProgressEvent;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.mms.PartAuthority;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.util.TextSecurePreferences;

import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.messages.openchatServiceAttachment;
import com.openchat.imservice.api.messages.openchatServiceAttachment.ProgressListener;
import com.openchat.imservice.api.push.openchatServiceAddress;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public abstract class PushSendJob extends SendJob {

  private static final String TAG = PushSendJob.class.getSimpleName();

  protected PushSendJob(Context context, JobParameters parameters) {
    super(context, parameters);
  }

  protected static JobParameters constructParameters(Context context, Address destination) {
    JobParameters.Builder builder = JobParameters.newBuilder();
    builder.withPersistence();
    builder.withGroupId(destination.serialize());
    builder.withRequirement(new MasterSecretRequirement(context));
    builder.withRequirement(new NetworkRequirement(context));
    builder.withRetryCount(5);

    return builder.create();
  }

  @Override
  protected final void onSend(MasterSecret masterSecret) throws Exception {
    if (TextSecurePreferences.getSignedPreKeyFailureCount(context) > 5) {
      ApplicationContext.getInstance(context)
                        .getJobManager()
                        .add(new RotateSignedPreKeyJob(context));

      throw new TextSecureExpiredException("Too many signed prekey rotation failures");
    }

    onPushSend(masterSecret);
  }

  protected Optional<byte[]> getProfileKey(@NonNull Recipient recipient) {
    if (!recipient.resolve().isSystemContact() && !recipient.resolve().isProfileSharing()) {
      return Optional.absent();
    }

    return Optional.of(ProfileKeyUtil.getProfileKey(context));
  }

  protected openchatServiceAddress getPushAddress(Address address) {
//    String relay = TextSecureDirectory.getInstance(context).getRelay(address.toPhoneString());
    String relay = null;
    return new openchatServiceAddress(address.toPhoneString(), Optional.fromNullable(relay));
  }

  protected List<openchatServiceAttachment> getAttachmentsFor(MasterSecret masterSecret, List<Attachment> parts) {
    List<openchatServiceAttachment> attachments = new LinkedList<>();

    for (final Attachment attachment : parts) {
      try {
        if (attachment.getDataUri() == null || attachment.getSize() == 0) throw new IOException("Assertion failed, outgoing attachment has no data!");
        InputStream is = PartAuthority.getAttachmentStream(context, masterSecret, attachment.getDataUri());
        attachments.add(openchatServiceAttachment.newStreamBuilder()
                                               .withStream(is)
                                               .withContentType(attachment.getContentType())
                                               .withLength(attachment.getSize())
                                               .withFileName(attachment.getFileName())
                                               .withVoiceNote(attachment.isVoiceNote())
                                               .withListener(new ProgressListener() {
                                                 @Override
                                                 public void onAttachmentProgress(long total, long progress) {
                                                   EventBus.getDefault().postSticky(new PartProgressEvent(attachment, total, progress));
                                                 }
                                               })
                                               .build());
      } catch (IOException ioe) {
        Log.w(TAG, "Couldn't open attachment", ioe);
      }
    }

    return attachments;
  }

  protected void notifyMediaMessageDeliveryFailed(Context context, long messageId) {
    long      threadId  = DatabaseFactory.getMmsDatabase(context).getThreadIdForMessage(messageId);
    Recipient recipient = DatabaseFactory.getThreadDatabase(context).getRecipientForThreadId(threadId);

    if (threadId != -1 && recipient != null) {
      MessageNotifier.notifyMessageDeliveryFailed(context, recipient, threadId);
    }
  }

  protected abstract void onPushSend(MasterSecret masterSecret) throws Exception;
}
