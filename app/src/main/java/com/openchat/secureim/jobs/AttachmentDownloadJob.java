package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.AsymmetricMasterSecret;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.crypto.MediaKey;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.PartDatabase;
import com.openchat.secureim.database.PartDatabase.PartId;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.jobs.requirements.MediaNetworkRequirement;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.util.Util;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.imservice.api.OpenchatServiceMessageReceiver;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment.ProgressListener;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentPointer;
import com.openchat.imservice.api.push.exceptions.NonSuccessfulResponseCodeException;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import ws.com.google.android.mms.MmsException;
import ws.com.google.android.mms.pdu.PduPart;

public class AttachmentDownloadJob extends MasterSecretJob implements InjectableType {
  private static final long   serialVersionUID = 1L;
  private static final String TAG              = AttachmentDownloadJob.class.getSimpleName();

  @Inject transient OpenchatServiceMessageReceiver messageReceiver;

  private final long messageId;
  private final long partRowId;
  private final long partUniqueId;

  public AttachmentDownloadJob(Context context, long messageId, PartId partId) {
    super(context, JobParameters.newBuilder()
                                .withGroupId(AttachmentDownloadJob.class.getCanonicalName())
                                .withRequirement(new MasterSecretRequirement(context))
                                .withRequirement(new NetworkRequirement(context))
                                .withRequirement(new MediaNetworkRequirement(context, messageId, partId))
                                .withPersistence()
                                .create());

    this.messageId    = messageId;
    this.partRowId    = partId.getRowId();
    this.partUniqueId = partId.getUniqueId();
  }

  @Override
  public void onAdded() {
  }

  @Override
  public void onRun(MasterSecret masterSecret) throws IOException {
    final PartId  partId = new PartId(partRowId, partUniqueId);
    final PduPart part   = DatabaseFactory.getPartDatabase(context).getPart(partId);

    if (part == null) {
      Log.w(TAG, "part no longer exists.");
      return;
    }
    if (part.getDataUri() != null) {
      Log.w(TAG, "part was already downloaded.");
      return;
    }

    Log.w(TAG, "Downloading push part " + partId);

    retrievePart(masterSecret, part, messageId);
    MessageNotifier.updateNotification(context, masterSecret);
  }

  @Override
  public void onCanceled() {
    final PartId  partId = new PartId(partRowId, partUniqueId);
    final PduPart part   = DatabaseFactory.getPartDatabase(context).getPart(partId);
    markFailed(messageId, part, part.getPartId());
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    return (exception instanceof PushNetworkException);
  }

  private void retrievePart(MasterSecret masterSecret, PduPart part, long messageId)
      throws IOException
  {

    PartDatabase database       = DatabaseFactory.getPartDatabase(context);
    File         attachmentFile = null;

    final PartId partId = part.getPartId();
    try {
      attachmentFile = createTempFile();

      OpenchatServiceAttachmentPointer pointer    = createAttachmentPointer(masterSecret, part);
      InputStream                 attachment = messageReceiver.retrieveAttachment(pointer, attachmentFile, new ProgressListener() {
        @Override public void onAttachmentProgress(long total, long progress) {
          EventBus.getDefault().postSticky(new PartProgressEvent(partId, total, progress));
        }
      });

      database.updateDownloadedPart(masterSecret, messageId, partId, part, attachment);
    } catch (InvalidPartException | NonSuccessfulResponseCodeException | InvalidMessageException | MmsException e) {
      Log.w(TAG, e);
      markFailed(messageId, part, partId);
    } finally {
      if (attachmentFile != null)
        attachmentFile.delete();
    }
  }

  private OpenchatServiceAttachmentPointer createAttachmentPointer(MasterSecret masterSecret, PduPart part)
      throws InvalidPartException
  {
    if (part.getContentLocation() == null) throw new InvalidPartException("null content location");

    try {
      AsymmetricMasterSecret asymmetricMasterSecret = MasterSecretUtil.getAsymmetricMasterSecret(context, masterSecret);
      long                   id                     = Long.parseLong(Util.toIsoString(part.getContentLocation()));
      byte[]                 key                    = MediaKey.getDecrypted(masterSecret, asymmetricMasterSecret, Util.toIsoString(part.getContentDisposition()));
      String                 relay                  = null;

      if (part.getName() != null) {
        relay = Util.toIsoString(part.getName());
      }

      return new OpenchatServiceAttachmentPointer(id, null, key, relay);
    } catch (InvalidMessageException | IOException e) {
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

  private void markFailed(long messageId, PduPart part, PartDatabase.PartId partId) {
    try {
      PartDatabase database = DatabaseFactory.getPartDatabase(context);
      database.updateFailedDownloadedPart(messageId, partId, part);
    } catch (MmsException e) {
      Log.w(TAG, e);
    }
  }

  private static class InvalidPartException extends Exception {
    public InvalidPartException(String s) {super(s);}
    public InvalidPartException(Exception e) {super(e);}
  }

}
