package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.NotInDirectoryException;
import com.openchat.secureim.database.OpenchatServiceDirectory;
import com.openchat.jobqueue.JobManager;
import com.openchat.jobqueue.JobParameters;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.push.ContactTokenDetails;

public abstract class PushReceivedJob extends ContextJob {

  private static final String TAG = PushReceivedJob.class.getSimpleName();

  protected PushReceivedJob(Context context, JobParameters parameters) {
    super(context, parameters);
  }

  public void handle(OpenchatServiceEnvelope envelope, boolean sendExplicitReceipt) {
    if (!isActiveNumber(context, envelope.getSource())) {
      OpenchatServiceDirectory directory           = OpenchatServiceDirectory.getInstance(context);
      ContactTokenDetails contactTokenDetails = new ContactTokenDetails();
      contactTokenDetails.setNumber(envelope.getSource());

      directory.setNumber(contactTokenDetails, true);
    }

    if (envelope.isReceipt()) handleReceipt(envelope);
    else                      handleMessage(envelope, sendExplicitReceipt);
  }

  private void handleMessage(OpenchatServiceEnvelope envelope, boolean sendExplicitReceipt) {
    JobManager jobManager = ApplicationContext.getInstance(context).getJobManager();
    long       messageId  = DatabaseFactory.getPushDatabase(context).insert(envelope);

    if (sendExplicitReceipt) {
      jobManager.add(new DeliveryReceiptJob(context, envelope.getSource(),
                                            envelope.getTimestamp(),
                                            envelope.getRelay()));
    }

    jobManager.add(new PushDecryptJob(context, messageId, envelope.getSource()));
  }

  private void handleReceipt(OpenchatServiceEnvelope envelope) {
    Log.w(TAG, String.format("Received receipt: (XXXXX, %d)", envelope.getTimestamp()));
    DatabaseFactory.getMmsSmsDatabase(context).incrementDeliveryReceiptCount(envelope.getSource(),
                                                                             envelope.getTimestamp());
  }

  private boolean isActiveNumber(Context context, String e164number) {
    boolean isActiveNumber;

    try {
      isActiveNumber = OpenchatServiceDirectory.getInstance(context).isActiveNumber(e164number);
    } catch (NotInDirectoryException e) {
      isActiveNumber = false;
    }

    return isActiveNumber;
  }

}
