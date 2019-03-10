package com.openchat.secureim.dependencies;

import android.content.Context;

import com.openchat.secureim.Release;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.jobs.AttachmentDownloadJob;
import com.openchat.secureim.jobs.AvatarDownloadJob;
import com.openchat.secureim.jobs.CleanPreKeysJob;
import com.openchat.secureim.jobs.CreateSignedPreKeyJob;
import com.openchat.secureim.jobs.DeliveryReceiptJob;
import com.openchat.secureim.jobs.PushGroupSendJob;
import com.openchat.secureim.jobs.PushMediaSendJob;
import com.openchat.secureim.jobs.PushTextSendJob;
import com.openchat.secureim.jobs.RefreshPreKeysJob;
import com.openchat.secureim.push.SecurityEventListener;
import com.openchat.secureim.push.OpenchatServicePushTrustStore;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.OpenchatServiceAccountManager;
import com.openchat.imservice.api.OpenchatServiceMessageReceiver;
import com.openchat.imservice.api.OpenchatServiceMessageSender;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, injects = {CleanPreKeysJob.class,
                                     CreateSignedPreKeyJob.class,
                                     DeliveryReceiptJob.class,
                                     PushGroupSendJob.class,
                                     PushTextSendJob.class,
                                     PushMediaSendJob.class,
                                     AttachmentDownloadJob.class,
                                     RefreshPreKeysJob.class})
public class OpenchatServiceCommunicationModule {

  private final Context context;

  public OpenchatServiceCommunicationModule(Context context) {
    this.context = context;
  }

  @Provides OpenchatServiceAccountManager provideOpenchatServiceAccountManager() {
    return new OpenchatServiceAccountManager(Release.PUSH_URL,
                                        new OpenchatServicePushTrustStore(context),
                                        OpenchatServicePreferences.getLocalNumber(context),
                                        OpenchatServicePreferences.getPushServerPassword(context));
  }

  @Provides OpenchatServiceMessageSenderFactory provideOpenchatServiceMessageSenderFactory() {
    return new OpenchatServiceMessageSenderFactory() {
      @Override
      public OpenchatServiceMessageSender create(MasterSecret masterSecret) {
        return new OpenchatServiceMessageSender(Release.PUSH_URL,
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
    return new OpenchatServiceMessageReceiver(Release.PUSH_URL,
                                           new OpenchatServicePushTrustStore(context),
                                           OpenchatServicePreferences.getLocalNumber(context),
                                           OpenchatServicePreferences.getPushServerPassword(context));
  }

  public static interface OpenchatServiceMessageSenderFactory {
    public OpenchatServiceMessageSender create(MasterSecret masterSecret);
  }

}
