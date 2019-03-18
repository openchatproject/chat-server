package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.Job;
import com.openchat.jobqueue.JobParameters;

public class TrimThreadJob extends Job {

  private static final String TAG = TrimThreadJob.class.getSimpleName();

  private final Context context;
  private final long    threadId;

  public TrimThreadJob(Context context, long threadId) {
    super(JobParameters.newBuilder().withGroupId(TrimThreadJob.class.getSimpleName()).create());
    this.context  = context;
    this.threadId = threadId;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun() {
    boolean trimmingEnabled   = OpenchatServicePreferences.isThreadLengthTrimmingEnabled(context);
    int     threadLengthLimit = OpenchatServicePreferences.getThreadTrimLength(context);

    if (!trimmingEnabled)
      return;

    DatabaseFactory.getThreadDatabase(context).trimThread(threadId, threadLengthLimit);
  }

  @Override
  public boolean onShouldRetry(Exception exception) {
    return false;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Canceling trim attempt: " + threadId);
  }
}
