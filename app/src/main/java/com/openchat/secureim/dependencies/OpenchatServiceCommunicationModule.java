package com.openchat.secureim.dependencies;

import android.content.Context;

import com.openchat.secureim.BuildConfig;
import com.openchat.secureim.DeviceListActivity;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.jobs.AttachmentDownloadJob;
import com.openchat.secureim.jobs.CleanPreKeysJob;
import com.openchat.secureim.jobs.CreateSignedPreKeyJob;
import com.openchat.secureim.jobs.DeliveryReceiptJob;
import com.openchat.secureim.jobs.MultiDeviceContactUpdateJob;
import com.openchat.secureim.jobs.PushGroupSendJob;
import com.openchat.secureim.jobs.PushMediaSendJob;
import com.openchat.secureim.jobs.PushNotificationReceiveJob;
import com.openchat.secureim.jobs.PushTextSendJob;
import com.openchat.secureim.jobs.RefreshPreKeysJob;
import com.openchat.secureim.push.SecurityEventListener;
import com.openchat.secureim.push.OpenchatServicePushTrustStore;
import com.openchat.secureim.service.MessageRetrievalService;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.OpenchatServiceAccountManager;
import com.openchat.imservice.api.OpenchatServiceMessageReceiver;
import com.openchat.imservice.api.OpenchatServiceMessageSender;
import com.openchat.imservice.api.util.CredentialsProvider;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, injects = {CleanPreKeysJob.class,
                                     CreateSignedPreKeyJob.class,
                                     DeliveryReceiptJob.class,
                                     PushGroupSendJob.class,
                                     PushTextSendJob.class,
                                     PushMediaSendJob.class,
                                     AttachmentDownloadJob.class,
                                     RefreshPreKeysJob.class,
                                     MessageRetrievalService.class,
                                     PushNotificationReceiveJob.class,
                                     MultiDeviceContactUpdateJob.class,
                                     DeviceListActivity.DeviceListFragment.class})
public class OpenchatServiceCommunicationModule {

  private final Context context;

  public OpenchatServiceCommunicationModule(Context context) {
    this.context = context;
  }

  @Provides OpenchatServiceAccountManager provideOpenchatServiceAccountManager() {
    return new OpenchatServiceAccountManager(BuildConfig.PUSH_URL,
                                        new OpenchatServicePushTrustStore(context),
                                        OpenchatServicePreferences.getLocalNumber(context),
                                        OpenchatServicePreferences.getPushServerPassword(context));
  }

  @Provides OpenchatServiceMessageSenderFactory provideOpenchatServiceMessageSenderFactory() {
    return new OpenchatServiceMessageSenderFactory() {
      @Override
      public OpenchatServiceMessageSender create(MasterSecret masterSecret) {
        return new OpenchatServiceMessageSender(BuildConfig.PUSH_URL,
                                           new OpenchatServicePushTrustStore(context),
                                           OpenchatServicePreferences.getLocalNumber(context),
                                           OpenchatServicePreferences.getPushServerPassword(context),
                                           new OpenchatServiceOpenchatStore(context, masterSecret),
                                           Optional.of((OpenchatServiceMessageSender.EventListener)
                                                           new SecurityEventListener(context)));
      }
    };
  }

  @Provides OpenchatServiceMessageReceiver provideOpenchatServiceMessageReceiver() {
    return new OpenchatServiceMessageReceiver(BuildConfig.PUSH_URL,
                                         new OpenchatServicePushTrustStore(context),
                                         new DynamicCredentialsProvider(context));
  }

  public static interface OpenchatServiceMessageSenderFactory {
    public OpenchatServiceMessageSender create(MasterSecret masterSecret);
  }

  private static class DynamicCredentialsProvider implements CredentialsProvider {

    private final Context context;

    private DynamicCredentialsProvider(Context context) {
      this.context = context.getApplicationContext();
    }

    @Override
    public String getUser() {
      return OpenchatServicePreferences.getLocalNumber(context);
    }

    @Override
    public String getPassword() {
      return OpenchatServicePreferences.getPushServerPassword(context);
    }

    @Override
    public String getOpenchatingKey() {
      return OpenchatServicePreferences.getOpenchatingKey(context);
    }
  }

}
