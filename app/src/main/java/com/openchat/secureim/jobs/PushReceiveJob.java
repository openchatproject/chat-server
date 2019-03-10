package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobManager;
import com.openchat.jobqueue.JobParameters;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.imservice.directory.Directory;
import com.openchat.imservice.directory.NotInDirectoryException;
import com.openchat.imservice.push.ContactTokenDetails;
import com.openchat.imservice.push.IncomingEncryptedPushMessage;
import com.openchat.imservice.push.IncomingPushMessage;

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
      String                       sessionKey = OpenchatServicePreferences.getOpenchatingKey(context);
      IncomingEncryptedPushMessage encrypted  = new IncomingEncryptedPushMessage(data, sessionKey);
      IncomingPushMessage          message    = encrypted.getIncomingPushMessage();

      if (!isActiveNumber(context, message.getSource())) {
        Directory           directory           = Directory.getInstance(context);
        ContactTokenDetails contactTokenDetails = new ContactTokenDetails();
        contactTokenDetails.setNumber(message.getSource());

        directory.setNumber(contactTokenDetails, true);
      }

      if (message.isReceipt()) handleReceipt(message);
      else                     handleMessage(message);
    } catch (IOException | InvalidVersionException e) {
      Log.w(TAG, e);
    }
  }

  @Override
  public void onCanceled() {

  }

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    return false;
  }

  private void handleMessage(IncomingPushMessage message) {
    JobManager jobManager = ApplicationContext.getInstance(context).getJobManager();
    long       messageId  = DatabaseFactory.getPushDatabase(context).insert(message);

    jobManager.add(new DeliveryReceiptJob(context, message.getSource(),
                                          message.getTimestampMillis(),
                                          message.getRelay()));

    jobManager.add(new PushDecryptJob(context, messageId));
  }

  private void handleReceipt(IncomingPushMessage message) {
    Log.w(TAG, String.format("Received receipt: (XXXXX, %d)", message.getTimestampMillis()));
    DatabaseFactory.getMmsSmsDatabase(context).incrementDeliveryReceiptCount(message.getSource(),
                                                                             message.getTimestampMillis());
  }

  private boolean isActiveNumber(Context context, String e164number) {
    boolean isActiveNumber;

    try {
      isActiveNumber = Directory.getInstance(context).isActiveNumber(e164number);
    } catch (NotInDirectoryException e) {
      isActiveNumber = false;
    }

    return isActiveNumber;
  }

}
