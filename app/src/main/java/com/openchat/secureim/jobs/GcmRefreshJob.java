package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.openchat.secureim.push.OpenchatServiceCommunicationFactory;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.OpenchatServiceAccountManager;
import com.openchat.imservice.push.exceptions.NonSuccessfulResponseCodeException;

public class GcmRefreshJob extends ContextJob {

  private static final String TAG = GcmRefreshJob.class.getSimpleName();

  public static final String REGISTRATION_ID = "312334754206";

  public GcmRefreshJob(Context context) {
    super(context, JobParameters.newBuilder().withRequirement(new NetworkRequirement(context)).create());
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws Exception {
    String registrationId = OpenchatServicePreferences.getGcmRegistrationId(context);

    if (registrationId == null) {
      Log.w(TAG, "GCM registrationId expired, reregistering...");
      int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

      if (result != ConnectionResult.SUCCESS) {
        Toast.makeText(context, "Unable to register with GCM!", Toast.LENGTH_LONG).show();
      }

      String                   gcmId          = GoogleCloudMessaging.getInstance(context).register(REGISTRATION_ID);
      OpenchatServiceAccountManager accountManager = OpenchatServiceCommunicationFactory.createManager(context);

      accountManager.setGcmId(Optional.of(gcmId));

      OpenchatServicePreferences.setGcmRegistrationId(context, gcmId);
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

}
