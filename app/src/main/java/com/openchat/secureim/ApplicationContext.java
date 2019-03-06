package com.openchat.secureim;

import android.app.Application;
import android.content.Context;

import com.openchat.secureim.crypto.PRNGFixes;
import com.openchat.secureim.jobs.EncryptingJobSerializer;
import com.openchat.secureim.jobs.GcmRefreshJob;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobManager;
import com.openchat.jobqueue.requirements.NetworkRequirementProvider;

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
    this.jobManager = new JobManager(this, "OpenchatServiceJobs",
                                     new NetworkRequirementProvider(this),
                                     new EncryptingJobSerializer(this), 5);
  }

  private void initializeGcmCheck() {
    if (OpenchatServicePreferences.isPushRegistered(this) &&
        OpenchatServicePreferences.getGcmRegistrationId(this) == null)
    {
      this.jobManager.add(new GcmRefreshJob(this));
    }
  }

}
