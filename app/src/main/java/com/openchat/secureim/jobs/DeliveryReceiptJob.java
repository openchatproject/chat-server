package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.push.PushServiceSocketFactory;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.imservice.push.PushServiceSocket;
import com.openchat.imservice.push.exceptions.NonSuccessfulResponseCodeException;
import com.openchat.imservice.push.exceptions.PushNetworkException;

public class DeliveryReceiptJob extends ContextJob {

  private static final String TAG = DeliveryReceiptJob.class.getSimpleName();

  private final String destination;
  private final long   timestamp;
  private final String relay;

  public DeliveryReceiptJob(Context context, String destination, long timestamp, String relay) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withPersistence()
                                .withRetryCount(50)
                                .create());

    this.destination = destination;
    this.timestamp   = timestamp;
    this.relay       = relay;
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws Throwable {
    Log.w("DeliveryReceiptJob", "Sending delivery receipt...");
    PushServiceSocket socket = PushServiceSocketFactory.create(context);
    socket.sendReceipt(destination, timestamp, relay);
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Failed to send receipt after retry exhausted!");
  }

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    Log.w(TAG, throwable);
    if (throwable instanceof NonSuccessfulResponseCodeException) return false;
    if (throwable instanceof PushNetworkException)               return true;

    return false;
  }
}
