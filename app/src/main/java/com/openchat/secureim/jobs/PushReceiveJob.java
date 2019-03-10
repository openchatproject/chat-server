package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobManager;
import com.openchat.jobqueue.JobParameters;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.secureim.database.OpenchatServiceDirectory;
import com.openchat.secureim.database.NotInDirectoryException;
import com.openchat.imservice.push.ContactTokenDetails;

import java.io.IOException;

public class PushReceiveJob extends ContextJob {

  private static final String TAG = PushReceiveJob.class.getSimpleName();

  private final String data;

  public PushReceiveJob(Context context, String data) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .create());

    this.data = data;
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() {
    try {
      String             sessionKey = OpenchatServicePreferences.getOpenchatingKey(context);
      OpenchatServiceEnvelope envelope   = new OpenchatServiceEnvelope(data, sessionKey);

      if (!isActiveNumber(context, envelope.getSource())) {
        OpenchatServiceDirectory directory           = OpenchatServiceDirectory.getInstance(context);
        ContactTokenDetails contactTokenDetails = new ContactTokenDetails();
        contactTokenDetails.setNumber(envelope.getSource());

        directory.setNumber(contactTokenDetails, true);
      }

      if (envelope.isReceipt()) handleReceipt(envelope);
      else                     handleMessage(envelope);
    } catch (IOException | InvalidVersionException e) {
      Log.w(TAG, e);
    }
  }

  @Override
  public void onCanceled() {

  }

  @Override
  public boolean onShouldRetry(Exception exception) {
    return false;
  }

  private void handleMessage(OpenchatServiceEnvelope envelope) {
    JobManager jobManager = ApplicationContext.getInstance(context).getJobManager();
    long       messageId  = DatabaseFactory.getPushDatabase(context).insert(envelope);

    jobManager.add(new DeliveryReceiptJob(context, envelope.getSource(),
                                          envelope.getTimestamp(),
                                          envelope.getRelay()));

    jobManager.add(new PushDecryptJob(context, messageId));
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
