package com.openchat.secureim.jobs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.openchat.secureim.PlayServicesProblemActivity;
import com.openchat.secureim.R;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.openchatServiceAccountManager;
import com.openchat.imservice.api.push.exceptions.NonSuccessfulResponseCodeException;

import javax.inject.Inject;

public class GcmRefreshJob extends ContextJob implements InjectableType {

  private static final String TAG = GcmRefreshJob.class.getSimpleName();

  public static final String REGISTRATION_ID = "312334754206";

  @Inject transient openchatServiceAccountManager textSecureAccountManager;

  public GcmRefreshJob(Context context) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withRetryCount(1)
                                .create());
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws Exception {
    if (TextSecurePreferences.isGcmDisabled(context)) return;

    Log.w(TAG, "Reregistering GCM...");
    int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

    if (result != ConnectionResult.SUCCESS) {
      notifyGcmFailure();
    } else {
      String gcmId = GoogleCloudMessaging.getInstance(context).register(REGISTRATION_ID);
      textSecureAccountManager.setGcmId(Optional.of(gcmId));

      TextSecurePreferences.setGcmRegistrationId(context, gcmId);
      TextSecurePreferences.setGcmRegistrationIdLastSetTime(context, System.currentTimeMillis());
      TextSecurePreferences.setWebsocketRegistered(context, true);
    }
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "GCM reregistration failed after retry attempt exhaustion!");
  }

  @Override
  public boolean onShouldRetry(Exception throwable) {
    if (throwable instanceof NonSuccessfulResponseCodeException) return false;
    return true;
  }

  private void notifyGcmFailure() {
    Intent                     intent        = new Intent(context, PlayServicesProblemActivity.class);
    PendingIntent              pendingIntent = PendingIntent.getActivity(context, 1122, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    NotificationCompat.Builder builder       = new NotificationCompat.Builder(context);

    builder.setSmallIcon(R.drawable.icon_notification);
    builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                                      R.drawable.ic_action_warning_red));
    builder.setContentTitle(context.getString(R.string.GcmRefreshJob_Permanent_openchat_communication_failure));
    builder.setContentText(context.getString(R.string.GcmRefreshJob_openchat_was_unable_to_register_with_Google_Play_Services));
    builder.setTicker(context.getString(R.string.GcmRefreshJob_Permanent_openchat_communication_failure));
    builder.setVibrate(new long[] {0, 1000});
    builder.setContentIntent(pendingIntent);

    ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE))
        .notify(12, builder.build());
  }

}
