package com.openchat.secureim.jobs;

import android.content.Context;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.jobqueue.JobParameters;

public abstract class MasterSecretJob extends ContextJob {

  public MasterSecretJob(Context context, JobParameters parameters) {
    super(context, parameters);
  }

  protected MasterSecret getMasterSecret() throws RequirementNotMetException {
    MasterSecret masterSecret = KeyCachingService.getMasterSecret(context);

    if (masterSecret == null) throw new RequirementNotMetException();
    else                      return masterSecret;
  }

  protected static class RequirementNotMetException extends Exception {}

}
