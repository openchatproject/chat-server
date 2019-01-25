package com.openchat.secureim.dependencies;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import com.openchat.secureim.BuildConfig;
import com.openchat.secureim.CreateProfileActivity;
import com.openchat.secureim.DeviceListFragment;
import com.openchat.secureim.crypto.storage.openchatProtocolStoreImpl;
import com.openchat.secureim.events.ReminderUpdateEvent;
import com.openchat.secureim.jobs.AttachmentDownloadJob;
import com.openchat.secureim.jobs.AvatarDownloadJob;
import com.openchat.secureim.jobs.CleanPreKeysJob;
import com.openchat.secureim.jobs.CreateSignedPreKeyJob;
import com.openchat.secureim.jobs.GcmRefreshJob;
import com.openchat.secureim.jobs.MultiDeviceBlockedUpdateJob;
import com.openchat.secureim.jobs.MultiDeviceContactUpdateJob;
import com.openchat.secureim.jobs.MultiDeviceGroupUpdateJob;
import com.openchat.secureim.jobs.MultiDeviceProfileKeyUpdateJob;
import com.openchat.secureim.jobs.MultiDeviceReadReceiptUpdateJob;
import com.openchat.secureim.jobs.MultiDeviceReadUpdateJob;
import com.openchat.secureim.jobs.MultiDeviceVerifiedUpdateJob;
import com.openchat.secureim.jobs.PushGroupSendJob;
import com.openchat.secureim.jobs.PushGroupUpdateJob;
import com.openchat.secureim.jobs.PushMediaSendJob;
import com.openchat.secureim.jobs.PushNotificationReceiveJob;
import com.openchat.secureim.jobs.PushTextSendJob;
import com.openchat.secureim.jobs.RefreshAttributesJob;
import com.openchat.secureim.jobs.RefreshPreKeysJob;
import com.openchat.secureim.jobs.RequestGroupInfoJob;
import com.openchat.secureim.jobs.RetrieveProfileAvatarJob;
import com.openchat.secureim.jobs.RetrieveProfileJob;
import com.openchat.secureim.jobs.RotateSignedPreKeyJob;
import com.openchat.secureim.jobs.SendReadReceiptJob;
import com.openchat.secureim.push.SecurityEventListener;
import com.openchat.secureim.push.openchatServiceNetworkAccess;
import com.openchat.secureim.service.MessageRetrievalService;
import com.openchat.secureim.service.WebRtcCallService;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.openchatServiceAccountManager;
import com.openchat.imservice.api.openchatServiceMessageReceiver;
import com.openchat.imservice.api.openchatServiceMessageSender;
import com.openchat.imservice.api.util.CredentialsProvider;
import com.openchat.imservice.api.websocket.ConnectivityListener;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, injects = {CleanPreKeysJob.class,
                                     CreateSignedPreKeyJob.class,
                                     PushGroupSendJob.class,
                                     PushTextSendJob.class,
                                     PushMediaSendJob.class,
                                     AttachmentDownloadJob.class,
                                     RefreshPreKeysJob.class,
                                     MessageRetrievalService.class,
                                     PushNotificationReceiveJob.class,
                                     MultiDeviceContactUpdateJob.class,
                                     MultiDeviceGroupUpdateJob.class,
                                     MultiDeviceReadUpdateJob.class,
                                     MultiDeviceBlockedUpdateJob.class,
                                     DeviceListFragment.class,
                                     RefreshAttributesJob.class,
                                     GcmRefreshJob.class,
                                     RequestGroupInfoJob.class,
                                     PushGroupUpdateJob.class,
                                     AvatarDownloadJob.class,
                                     RotateSignedPreKeyJob.class,
                                     WebRtcCallService.class,
                                     RetrieveProfileJob.class,
                                     MultiDeviceVerifiedUpdateJob.class,
                                     CreateProfileActivity.class,
                                     RetrieveProfileAvatarJob.class,
                                     MultiDeviceProfileKeyUpdateJob.class,
                                     SendReadReceiptJob.class,
                                     MultiDeviceReadReceiptUpdateJob.class})
public class openchatCommunicationModule {

  private static final String TAG = openchatCommunicationModule.class.getSimpleName();

  private final Context                      context;
  private final openchatServiceNetworkAccess   networkAccess;

  private openchatServiceAccountManager  accountManager;
  private openchatServiceMessageSender   messageSender;
  private openchatServiceMessageReceiver messageReceiver;

  public openchatCommunicationModule(Context context, openchatServiceNetworkAccess networkAccess) {
    this.context       = context;
    this.networkAccess = networkAccess;
  }

  @Provides
  synchronized openchatServiceAccountManager provideopenchatAccountManager() {
    if (this.accountManager == null) {
      this.accountManager = new openchatServiceAccountManager(networkAccess.getConfiguration(context),
                                                            new DynamicCredentialsProvider(context),
                                                            BuildConfig.USER_AGENT);
    }

    return this.accountManager;
  }

  @Provides
  synchronized openchatServiceMessageSender provideopenchatMessageSender() {
    if (this.messageSender == null) {
      this.messageSender = new openchatServiceMessageSender(networkAccess.getConfiguration(context),
                                                          new DynamicCredentialsProvider(context),
                                                          new openchatProtocolStoreImpl(context),
                                                          BuildConfig.USER_AGENT,
                                                          Optional.fromNullable(MessageRetrievalService.getPipe()),
                                                          Optional.of(new SecurityEventListener(context)));
    } else {
      this.messageSender.setMessagePipe(MessageRetrievalService.getPipe());
    }

    return this.messageSender;
  }

  @Provides
  synchronized openchatServiceMessageReceiver provideopenchatMessageReceiver() {
    if (this.messageReceiver == null) {
      this.messageReceiver = new openchatServiceMessageReceiver(networkAccess.getConfiguration(context),
                                                              new DynamicCredentialsProvider(context),
                                                              BuildConfig.USER_AGENT,
                                                              new PipeConnectivityListener());
    }

    return this.messageReceiver;
  }

  private static class DynamicCredentialsProvider implements CredentialsProvider {

    private final Context context;

    private DynamicCredentialsProvider(Context context) {
      this.context = context.getApplicationContext();
    }

    @Override
    public String getUser() {
      return TextSecurePreferences.getLocalNumber(context);
    }

    @Override
    public String getPassword() {
      return TextSecurePreferences.getPushServerPassword(context);
    }

    @Override
    public String getopenchatingKey() {
      return TextSecurePreferences.getopenchatingKey(context);
    }
  }

  private class PipeConnectivityListener implements ConnectivityListener {

    @Override
    public void onConnected() {
      Log.w(TAG, "onConnected()");
    }

    @Override
    public void onConnecting() {
      Log.w(TAG, "onConnecting()");
    }

    @Override
    public void onDisconnected() {
      Log.w(TAG, "onDisconnected()");
    }

    @Override
    public void onAuthenticationFailure() {
      Log.w(TAG, "onAuthenticationFailure()");
      TextSecurePreferences.setUnauthorizedReceived(context, true);
      EventBus.getDefault().post(new ReminderUpdateEvent());
    }

  }

}
