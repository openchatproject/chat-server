package com.openchat.secureim.jobs;

import android.content.Context;

import com.openchat.secureim.BuildConfig;
import com.openchat.secureim.OpenchatServiceExpiredException;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.Util;
import com.openchat.jobqueue.JobParameters;

public abstract class SendJob extends MasterSecretJob {

  public SendJob(Context context, JobParameters parameters) {
    super(context, parameters);
  }

  @Override
  public final void onRun(MasterSecret masterSecret) throws Exception {
    if (!Util.isBuildFresh()) {
      throw new OpenchatServiceExpiredException(String.format("OpenchatService expired (build %d, now %d)",
                                                         BuildConfig.BUILD_TIMESTAMP,
                                                         System.currentTimeMillis()));
    }

    onSend(masterSecret);
  }

  protected abstract void onSend(MasterSecret masterSecret) throws Exception;
}
