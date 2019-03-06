package com.openchat.secureim.jobs;

import android.util.Log;

import com.path.android.jobqueue.Params;

import com.openchat.secureim.push.PushServiceSocketFactory;
import com.openchat.imservice.push.PushServiceSocket;
import com.openchat.imservice.push.exceptions.NonSuccessfulResponseCodeException;
import com.openchat.imservice.push.exceptions.PushNetworkException;

public class DeliveryReceiptJob extends ContextJob {

  private static final String TAG = DeliveryReceiptJob.class.getSimpleName();

  private final String destination;
  private final long   timestamp;
  private final String relay;

  public DeliveryReceiptJob(String destination, long timestamp, String relay) {
    super(new Params(Priorities.HIGH).requireNetwork().persist());

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
  protected void onCancel() {
    Log.w(TAG, "Failed to send receipt after retry exhausted!");
  }

  @Override
  protected boolean shouldReRunOnThrowable(Throwable throwable) {
    Log.w(TAG, throwable);
    if (throwable instanceof NonSuccessfulResponseCodeException) return false;
    if (throwable instanceof PushNetworkException) return true;

    return false;
  }

  @Override
  protected int getRetryLimit() {
    return 50;
  }
}
