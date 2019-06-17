package com.openchat.secureim;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;

import com.openchat.secureim.crypto.PRNGFixes;
import com.openchat.secureim.dependencies.OpenchatStorageModule;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.dependencies.OpenchatServiceCommunicationModule;
import com.openchat.secureim.jobs.GcmRefreshJob;
import com.openchat.secureim.jobs.persistence.EncryptingJobSerializer;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirementProvider;
import com.openchat.secureim.jobs.requirements.MediaNetworkRequirementProvider;
import com.openchat.secureim.jobs.requirements.ServiceRequirementProvider;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobManager;
import com.openchat.jobqueue.dependencies.DependencyInjector;
import com.openchat.jobqueue.requirements.NetworkRequirementProvider;
import com.openchat.protocal.logging.OpenchatLoggerProvider;
import com.openchat.protocal.util.AndroidOpenchatLogger;

import dagger.ObjectGraph;

public class ApplicationContext extends Application implements DependencyInjector {

  private JobManager  jobManager;
  private ObjectGraph objectGraph;

  private MediaNetworkRequirementProvider mediaNetworkRequirementProvider = new MediaNetworkRequirementProvider();

  public static ApplicationContext getInstance(Context context) {
    return (ApplicationContext)context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    initializeDeveloperBuild();
    initializeRandomNumberFix();
    initializeLogging();
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

  private void initializeDeveloperBuild() {
    if (BuildConfig.DEV_BUILD) {
      StrictMode.setThreadPolicy(new ThreadPolicy.Builder().detectAll()
                                                           .penaltyLog()
                                                           .build());
      StrictMode.setVmPolicy(new VmPolicy.Builder().detectAll().penaltyLog().build());
    }
  }

  private void initializeRandomNumberFix() {
    PRNGFixes.apply();
  }

  private void initializeLogging() {
    OpenchatLoggerProvider.setProvider(new AndroidOpenchatLogger());
  }

  private void initializeJobManager() {
    this.jobManager = JobManager.newBuilder(this)
                                .withName("OpenchatServiceJobs")
                                .withDependencyInjector(this)
                                .withJobSerializer(new EncryptingJobSerializer())
                                .withRequirementProviders(new MasterSecretRequirementProvider(this),
                                                          new ServiceRequirementProvider(this),
                                                          new NetworkRequirementProvider(this),
                                                          mediaNetworkRequirementProvider)
                                .withConsumerThreads(5)
                                .build();
  }

  public void notifyMediaControlEvent() {
    mediaNetworkRequirementProvider.notifyMediaControlEvent();
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
