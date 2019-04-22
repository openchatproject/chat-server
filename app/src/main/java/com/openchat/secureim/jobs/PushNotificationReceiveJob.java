package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.imservice.api.OpenchatServiceMessageReceiver;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class PushNotificationReceiveJob extends PushReceivedJob implements InjectableType {

  private static final String TAG = PushNotificationReceiveJob.class.getSimpleName();

  @Inject transient OpenchatServiceMessageReceiver receiver;

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
    receiver.retrieveMessages(new OpenchatServiceMessageReceiver.MessageReceivedCallback() {
      @Override
      public void onMessage(OpenchatServiceEnvelope envelope) {
        handle(envelope, false);
      }
    });
  }

  @Override
  public boolean onShouldRetry(Exception e) {
    return e instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "***** Failed to download pending message!");
  }
}
