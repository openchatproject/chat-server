package com.openchat.secureim.jobs;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUnion;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.mms.ApnUnavailableException;
import com.openchat.secureim.mms.CompatMmsConnection;
import com.openchat.secureim.mms.IncomingMediaMessage;
import com.openchat.secureim.mms.MmsRadioException;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.util.guava.Optional;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ws.com.google.android.mms.MmsException;
import ws.com.google.android.mms.pdu.NotificationInd;
import ws.com.google.android.mms.pdu.RetrieveConf;

public class MmsDownloadJob extends MasterSecretJob {

  private static final String TAG = MmsDownloadJob.class.getSimpleName();

  private final long    messageId;
  private final long    threadId;
  private final boolean automatic;

  public MmsDownloadJob(Context context, long messageId, long threadId, boolean automatic) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withRequirement(new MasterSecretRequirement(context))
                                .withRequirement(new NetworkRequirement(context))
                                .withGroupId("mms-operation")
                                .withWakeLock(true, 30, TimeUnit.SECONDS)
                                .create());

    this.messageId = messageId;
    this.threadId  = threadId;
    this.automatic = automatic;
  }

  @Override
  public void onAdded() {
    if (automatic && KeyCachingService.getMasterSecret(context) == null) {
      DatabaseFactory.getMmsDatabase(context).markIncomingNotificationReceived(threadId);
      MessageNotifier.updateNotification(context, null);
    }
  }

  @Override
  public void onRun(MasterSecret masterSecret) {
    Log.w(TAG, "onRun()");

    MmsDatabase               database     = DatabaseFactory.getMmsDatabase(context);
    Optional<NotificationInd> notification = database.getNotification(messageId);

    if (!notification.isPresent()) {
      Log.w(TAG, "No notification for ID: " + messageId);
      return;
    }

    try {
      if (notification.get().getContentLocation() == null) {
        throw new MmsException("Notification content location was null.");
      }

      database.markDownloadState(messageId, MmsDatabase.Status.DOWNLOAD_CONNECTING);

      String contentLocation = new String(notification.get().getContentLocation());
      byte[] transactionId   = notification.get().getTransactionId();

      Log.w(TAG, "Downloading mms at " + Uri.parse(contentLocation).getHost());

      RetrieveConf retrieveConf = new CompatMmsConnection(context).retrieve(contentLocation, transactionId);
      if (retrieveConf == null) {
        throw new MmsException("RetrieveConf was null");
      }
      storeRetrievedMms(masterSecret, contentLocation, messageId, threadId, retrieveConf);
    } catch (ApnUnavailableException e) {
      Log.w(TAG, e);
      handleDownloadError(masterSecret, messageId, threadId, MmsDatabase.Status.DOWNLOAD_APN_UNAVAILABLE,
                          automatic);
    } catch (MmsException e) {
      Log.w(TAG, e);
      handleDownloadError(masterSecret, messageId, threadId,
                          MmsDatabase.Status.DOWNLOAD_HARD_FAILURE,
                          automatic);
    } catch (MmsRadioException | IOException e) {
      Log.w(TAG, e);
      handleDownloadError(masterSecret, messageId, threadId,
                          MmsDatabase.Status.DOWNLOAD_SOFT_FAILURE,
                          automatic);
    } catch (DuplicateMessageException e) {
      Log.w(TAG, e);
      database.markAsDecryptDuplicate(messageId, threadId);
    } catch (LegacyMessageException e) {
      Log.w(TAG, e);
      database.markAsLegacyVersion(messageId, threadId);
    } catch (NoSessionException e) {
      Log.w(TAG, e);
      database.markAsNoSession(messageId, threadId);
    } catch (InvalidMessageException e) {
      Log.w(TAG, e);
      database.markAsDecryptFailed(messageId, threadId);
    }
  }

  @Override
  public void onCanceled() {
    MmsDatabase database = DatabaseFactory.getMmsDatabase(context);
    database.markDownloadState(messageId, MmsDatabase.Status.DOWNLOAD_SOFT_FAILURE);

    if (automatic) {
      database.markIncomingNotificationReceived(threadId);
      MessageNotifier.updateNotification(context, null, threadId);
    }
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    return false;
  }

  private void storeRetrievedMms(MasterSecret masterSecret, String contentLocation,
                                 long messageId, long threadId, RetrieveConf retrieved)
      throws MmsException, NoSessionException, DuplicateMessageException, InvalidMessageException,
             LegacyMessageException
  {
    MmsDatabase          database = DatabaseFactory.getMmsDatabase(context);
    IncomingMediaMessage message  = new IncomingMediaMessage(retrieved);

    Pair<Long, Long> messageAndThreadId  = database.insertMessageInbox(new MasterSecretUnion(masterSecret),
                                                                       message, contentLocation, threadId);
    database.delete(messageId);

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleDownloadError(MasterSecret masterSecret, long messageId, long threadId,
                                   int downloadStatus, boolean automatic)
  {
    MmsDatabase db = DatabaseFactory.getMmsDatabase(context);

    db.markDownloadState(messageId, downloadStatus);

    if (automatic) {
      db.markIncomingNotificationReceived(threadId);
      MessageNotifier.updateNotification(context, masterSecret, threadId);
    }
  }
}
