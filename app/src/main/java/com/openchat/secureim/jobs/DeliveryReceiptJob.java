package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.imservice.api.OpenchatServiceMessageSender;
import com.openchat.imservice.api.push.PushAddress;
import com.openchat.imservice.api.push.exceptions.NonSuccessfulResponseCodeException;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;

import javax.inject.Inject;

import static com.openchat.secureim.dependencies.OpenchatServiceCommunicationModule.OpenchatServiceMessageSenderFactory;

public class DeliveryReceiptJob extends ContextJob implements InjectableType {

  private static final String TAG = DeliveryReceiptJob.class.getSimpleName();

  @Inject transient OpenchatServiceMessageSenderFactory messageSenderFactory;

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
  public void onRun() throws IOException {
    Log.w("DeliveryReceiptJob", "Sending delivery receipt...");
    OpenchatServiceMessageSender messageSender = messageSenderFactory.create(null);
    PushAddress             pushAddress   = new PushAddress(-1, destination, 1, relay);

    messageSender.sendDeliveryReceipt(pushAddress, timestamp);
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Failed to send receipt after retry exhausted!");
  }

  @Override
  public boolean onShouldRetry(Exception exception) {
    Log.w(TAG, exception);
    if (exception instanceof NonSuccessfulResponseCodeException) return false;
    if (exception instanceof PushNetworkException)               return true;

    return false;
  }
}
