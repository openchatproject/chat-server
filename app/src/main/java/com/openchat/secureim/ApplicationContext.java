package com.openchat.secureim;

import android.app.Application;
import android.content.Context;

import com.openchat.secureim.crypto.PRNGFixes;
import com.openchat.secureim.dependencies.OpenchatStorageModule;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.dependencies.OpenchatServiceCommunicationModule;
import com.openchat.secureim.jobs.GcmRefreshJob;
import com.openchat.secureim.jobs.persistence.EncryptingJobSerializer;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirementProvider;
import com.openchat.secureim.jobs.requirements.ServiceRequirementProvider;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobManager;
import com.openchat.jobqueue.dependencies.DependencyInjector;
import com.openchat.jobqueue.requirements.NetworkRequirementProvider;

import java.security.Security;

import dagger.ObjectGraph;

public class ApplicationContext extends Application implements DependencyInjector {

  private JobManager jobManager;
  private ObjectGraph objectGraph;

  public static ApplicationContext getInstance(Context context) {
    return (ApplicationContext)context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    initializeRandomNumberFix();
    initializeDependencyInjection();
    initializeJobManager();
    initializeGcmCheck();
  }

  @Override
  public void injectDependencies(Object object) {
    if (object instanceof InjectableType) {
      objectGraph.inject(object);
    }
  }

  public JobManager getJobManager() {
    return jobManager;
  }

  private void initializeRandomNumberFix() {
    PRNGFixes.apply();
  }

  private void initializeJobManager() {
    this.jobManager = JobManager.newBuilder(this)
                                .withName("OpenchatServiceJobs")
                                .withDependencyInjector(this)
                                .withJobSerializer(new EncryptingJobSerializer())
                                .withRequirementProviders(new MasterSecretRequirementProvider(this),
                                                          new ServiceRequirementProvider(this),
                                                          new NetworkRequirementProvider(this))
                                .withConsumerThreads(5)
                                .build();
  }

  private void initializeDependencyInjection() {
    this.objectGraph = ObjectGraph.create(new OpenchatServiceCommunicationModule(this),
                                          new OpenchatStorageModule(this));
  }

  private void initializeGcmCheck() {
    if (OpenchatServicePreferences.isPushRegistered(this) &&
        OpenchatServicePreferences.getGcmRegistrationId(this) == null)
    {
      this.jobManager.add(new GcmRefreshJob(this));
    }
  }

}
