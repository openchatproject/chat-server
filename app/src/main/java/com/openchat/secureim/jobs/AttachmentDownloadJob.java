package com.openchat.secureim.jobs;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import com.openchat.secureim.attachments.Attachment;
import com.openchat.secureim.attachments.AttachmentId;
import com.openchat.secureim.crypto.AsymmetricMasterSecret;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.crypto.MediaKey;
import com.openchat.secureim.database.AttachmentDatabase;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.events.PartProgressEvent;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.mms.MmsException;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.util.AttachmentUtil;
import com.openchat.secureim.util.Hex;
import com.openchat.secureim.util.Util;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.libim.InvalidMessageException;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.openchatServiceMessageReceiver;
import com.openchat.imservice.api.messages.openchatServiceAttachment.ProgressListener;
import com.openchat.imservice.api.messages.openchatServiceAttachmentPointer;
import com.openchat.imservice.api.push.exceptions.NonSuccessfulResponseCodeException;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

public class AttachmentDownloadJob extends MasterSecretJob implements InjectableType {
  private static final long   serialVersionUID    = 2L;
  private static final int    MAX_ATTACHMENT_SIZE = 150 * 1024  * 1024;
  private static final String TAG                  = AttachmentDownloadJob.class.getSimpleName();

  @Inject transient openchatServiceMessageReceiver messageReceiver;

  private final long    messageId;
  private final long    partRowId;
  private final long    partUniqueId;
  private final boolean manual;

  public AttachmentDownloadJob(Context context, long messageId, AttachmentId attachmentId, boolean manual) {
    super(context, JobParameters.newBuilder()
                                .withGroupId(AttachmentDownloadJob.class.getCanonicalName())
                                .withRequirement(new MasterSecretRequirement(context))
                                .withRequirement(new NetworkRequirement(context))
                                .withPersistence()
                                .create());

    this.messageId    = messageId;
    this.partRowId    = attachmentId.getRowId();
    this.partUniqueId = attachmentId.getUniqueId();
    this.manual       = manual;
  }

  @Override
  public void onAdded() {
  }

  @Override
  public void onRun(MasterSecret masterSecret) throws IOException {
    final AttachmentDatabase database     = DatabaseFactory.getAttachmentDatabase(context);
    final AttachmentId       attachmentId = new AttachmentId(partRowId, partUniqueId);
    final Attachment         attachment   = database.getAttachment(masterSecret, attachmentId);

    if (attachment == null) {
      Log.w(TAG, "attachment no longer exists.");
      return;
    }

    if (!attachment.isInProgress()) {
      Log.w(TAG, "Attachment was already downloaded.");
      return;
    }

    if (!manual && !AttachmentUtil.isAutoDownloadPermitted(context, attachment)) {
      Log.w(TAG, "Attachment can't be auto downloaded...");
      return;
    }

    Log.w(TAG, "Downloading push part " + attachmentId);
    database.setTransferState(messageId, attachmentId, AttachmentDatabase.TRANSFER_PROGRESS_STARTED);

    retrieveAttachment(masterSecret, messageId, attachmentId, attachment);
    MessageNotifier.updateNotification(context, masterSecret);
  }

  @Override
  public void onCanceled() {
    final AttachmentId attachmentId = new AttachmentId(partRowId, partUniqueId);
    markFailed(messageId, attachmentId);
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    return (exception instanceof PushNetworkException);
  }

  private void retrieveAttachment(MasterSecret masterSecret,
                                  long messageId,
                                  final AttachmentId attachmentId,
                                  final Attachment attachment)
      throws IOException
  {

    AttachmentDatabase database       = DatabaseFactory.getAttachmentDatabase(context);
    File               attachmentFile = null;

    try {
      attachmentFile = createTempFile();

      openchatServiceAttachmentPointer pointer = createAttachmentPointer(masterSecret, attachment);
      InputStream                    stream  = messageReceiver.retrieveAttachment(pointer, attachmentFile, MAX_ATTACHMENT_SIZE, new ProgressListener() {
        @Override
        public void onAttachmentProgress(long total, long progress) {
          EventBus.getDefault().postSticky(new PartProgressEvent(attachment, total, progress));
        }
      });

      database.insertAttachmentsForPlaceholder(masterSecret, messageId, attachmentId, stream);
    } catch (InvalidPartException | NonSuccessfulResponseCodeException | InvalidMessageException | MmsException e) {
      Log.w(TAG, e);
      markFailed(messageId, attachmentId);
    } finally {
      if (attachmentFile != null)
        attachmentFile.delete();
    }
  }

  @VisibleForTesting
  openchatServiceAttachmentPointer createAttachmentPointer(MasterSecret masterSecret, Attachment attachment)
      throws InvalidPartException
  {
    if (TextUtils.isEmpty(attachment.getLocation())) {
      throw new InvalidPartException("empty content id");
    }

    if (TextUtils.isEmpty(attachment.getKey())) {
      throw new InvalidPartException("empty encrypted key");
    }

    try {
      AsymmetricMasterSecret asymmetricMasterSecret = MasterSecretUtil.getAsymmetricMasterSecret(context, masterSecret);
      long                   id                     = Long.parseLong(attachment.getLocation());
      byte[]                 key                    = MediaKey.getDecrypted(masterSecret, asymmetricMasterSecret, attachment.getKey());
      String                 relay                  = null;

      if (TextUtils.isEmpty(attachment.getRelay())) {
        relay = attachment.getRelay();
      }

      if (attachment.getDigest() != null) {
        Log.w(TAG, "Downloading attachment with digest: " + Hex.toString(attachment.getDigest()));
      } else {
        Log.w(TAG, "Downloading attachment with no digest...");
      }

      return new openchatServiceAttachmentPointer(id, null, key, relay,
                                                Optional.of(Util.toIntExact(attachment.getSize())),
                                                Optional.absent(),
                                                Optional.fromNullable(attachment.getDigest()),
                                                Optional.fromNullable(attachment.getFileName()),
                                                attachment.isVoiceNote());
    } catch (InvalidMessageException | IOException | ArithmeticException e) {
      Log.w(TAG, e);
      throw new InvalidPartException(e);
    }
  }

  private File createTempFile() throws InvalidPartException {
    try {
      File file = File.createTempFile("push-attachment", "tmp", context.getCacheDir());
      file.deleteOnExit();

      return file;
    } catch (IOException e) {
      throw new InvalidPartException(e);
    }
  }

  private void markFailed(long messageId, AttachmentId attachmentId) {
    try {
      AttachmentDatabase database = DatabaseFactory.getAttachmentDatabase(context);
      database.setTransferProgressFailed(attachmentId, messageId);
    } catch (MmsException e) {
      Log.w(TAG, e);
    }
  }

  @VisibleForTesting static class InvalidPartException extends Exception {
    public InvalidPartException(String s) {super(s);}
    public InvalidPartException(Exception e) {super(e);}
  }

}
