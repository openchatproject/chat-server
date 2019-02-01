package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.libim.InvalidVersionException;
import com.openchat.imservice.api.messages.openchatServiceEnvelope;

import java.io.IOException;

public class PushContentReceiveJob extends PushReceivedJob {

  private static final String TAG = PushContentReceiveJob.class.getSimpleName();

  private final String data;

  public PushContentReceiveJob(Context context) {
    super(context, JobParameters.newBuilder().create());
    this.data = null;
  }

  public PushContentReceiveJob(Context context, String data) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withWakeLock(true)
                                .create());

    this.data = data;
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() {
    try {
      String                sessionKey = TextSecurePreferences.getopenchatingKey(context);
      openchatServiceEnvelope envelope   = new openchatServiceEnvelope(data, sessionKey);

      handle(envelope);
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
}
