package com.openchat.secureim.jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MessagingDatabase.SyncMessageId;
import com.openchat.secureim.database.RecipientDatabase;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.jobqueue.JobManager;
import com.openchat.jobqueue.JobParameters;
import com.openchat.imservice.api.messages.openchatServiceEnvelope;

public abstract class PushReceivedJob extends ContextJob {

  private static final String TAG = PushReceivedJob.class.getSimpleName();

  protected PushReceivedJob(Context context, JobParameters parameters) {
    super(context, parameters);
  }

  public void handle(openchatServiceEnvelope envelope) {
    Address   source    = Address.fromExternal(context, envelope.getSource());
    Recipient recipient = Recipient.from(context, source, false);

    if (!isActiveNumber(recipient)) {
      DatabaseFactory.getRecipientDatabase(context).setRegistered(recipient, RecipientDatabase.RegisteredState.REGISTERED);
      ApplicationContext.getInstance(context).getJobManager().add(new DirectoryRefreshJob(context, KeyCachingService.getMasterSecret(context), recipient, false));
    }

    if (envelope.isReceipt()) {
      handleReceipt(envelope);
    } else if (envelope.isPreKeyopenchatMessage() || envelope.isopenchatMessage()) {
      handleMessage(envelope, source);
    } else {
      Log.w(TAG, "Received envelope of unknown type: " + envelope.getType());
    }
  }

  private void handleMessage(openchatServiceEnvelope envelope, Address source) {
    Recipient  recipients = Recipient.from(context, source, false);
    JobManager jobManager = ApplicationContext.getInstance(context).getJobManager();

    if (!recipients.isBlocked()) {
      long messageId = DatabaseFactory.getPushDatabase(context).insert(envelope);
      jobManager.add(new PushDecryptJob(context, messageId));
    } else {
      Log.w(TAG, "*** Received blocked push message, ignoring...");
    }
  }

  private void handleReceipt(openchatServiceEnvelope envelope) {
    Log.w(TAG, String.format("Received receipt: (XXXXX, %d)", envelope.getTimestamp()));
    DatabaseFactory.getMmsSmsDatabase(context).incrementDeliveryReceiptCount(new SyncMessageId(Address.fromExternal(context, envelope.getSource()),
                                                                                               envelope.getTimestamp()), System.currentTimeMillis());
  }

  private boolean isActiveNumber(@NonNull Recipient recipient) {
    return recipient.resolve().getRegistered() == RecipientDatabase.RegisteredState.REGISTERED;
  }


}
