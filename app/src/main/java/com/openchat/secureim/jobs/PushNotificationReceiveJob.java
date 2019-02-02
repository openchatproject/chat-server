package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.imservice.api.openchatServiceMessageReceiver;
import com.openchat.imservice.api.messages.openchatServiceEnvelope;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class PushNotificationReceiveJob extends PushReceivedJob implements InjectableType {

  private static final String TAG = PushNotificationReceiveJob.class.getSimpleName();

  @Inject transient openchatServiceMessageReceiver receiver;

  public PushNotificationReceiveJob(Context context) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withGroupId("__notification_received")
                                .withWakeLock(true, 30, TimeUnit.SECONDS).create());
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws IOException {
    receiver.retrieveMessages(new openchatServiceMessageReceiver.MessageReceivedCallback() {
      @Override
      public void onMessage(openchatServiceEnvelope envelope) {
        handle(envelope);
      }
    });
  }

  @Override
  public boolean onShouldRetry(Exception e) {
    Log.w(TAG, e);
    return e instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "***** Failed to download pending message!");
//    MessageNotifier.notifyMessagesPending(getContext());
  }
}
