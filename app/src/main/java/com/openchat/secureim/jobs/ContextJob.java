package com.openchat.secureim.jobs;

import android.content.Context;

import com.openchat.jobqueue.Job;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.dependencies.ContextDependent;

public abstract class ContextJob extends Job implements ContextDependent {

  protected transient Context context;

  protected ContextJob(Context context, JobParameters parameters) {
    super(parameters);
    this.context = context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  protected Context getContext() {
    return context;
  }
}
