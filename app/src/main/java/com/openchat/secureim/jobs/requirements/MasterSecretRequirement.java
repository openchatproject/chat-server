package com.openchat.secureim.jobs.requirements;

import android.content.Context;

import com.openchat.secureim.service.KeyCachingService;
import com.openchat.jobqueue.dependencies.ContextDependent;
import com.openchat.jobqueue.requirements.Requirement;

public class MasterSecretRequirement implements Requirement, ContextDependent {

  private transient Context context;

  public MasterSecretRequirement(Context context) {
    this.context = context;
  }

  @Override
  public boolean isPresent() {
    return KeyCachingService.getMasterSecret(context) != null;
  }

  @Override
  public void setContext(Context context) {
    this.context = context;
  }
}
