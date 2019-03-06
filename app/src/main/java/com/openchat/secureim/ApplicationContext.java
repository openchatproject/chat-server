package com.openchat.secureim;

import android.app.Application;
import android.content.Context;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import com.openchat.secureim.crypto.PRNGFixes;
import com.openchat.secureim.jobs.ContextInjector;
import com.openchat.secureim.jobs.GcmRefreshJob;
import com.openchat.secureim.jobs.JobLogger;
import com.openchat.secureim.util.OpenchatServicePreferences;

public class ApplicationContext extends Application {

  private JobManager jobManager;

  public static ApplicationContext getInstance(Context context) {
    return (ApplicationContext)context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    initializeRandomNumberFix();
    initializeJobManager();
    initializeGcmCheck();
  }

  public JobManager getJobManager() {
    return jobManager;
  }

  private void initializeRandomNumberFix() {
    PRNGFixes.apply();
  }

  private void initializeJobManager() {
    Configuration configuration = new Configuration.Builder(this)
        .minConsumerCount(1)
        .injector(new ContextInjector(this))
        .customLogger(new JobLogger())
        .build();

    this.jobManager = new JobManager(this, configuration);
  }

  private void initializeGcmCheck() {
    if (OpenchatServicePreferences.isPushRegistered(this) &&
        OpenchatServicePreferences.getGcmRegistrationId(this) == null)
    {
      this.jobManager.addJob(new GcmRefreshJob());
    }
  }

}
