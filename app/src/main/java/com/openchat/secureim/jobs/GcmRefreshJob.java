package com.openchat.secureim.jobs;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.path.android.jobqueue.Params;

import com.openchat.secureim.push.PushServiceSocketFactory;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.push.PushServiceSocket;
import com.openchat.imservice.push.exceptions.MismatchedDevicesException;
import com.openchat.imservice.push.exceptions.NonSuccessfulResponseCodeException;
import com.openchat.imservice.push.exceptions.PushNetworkException;

public class GcmRefreshJob extends ContextJob {

  private static final String TAG = GcmRefreshJob.class.getSimpleName();

  public static final String REGISTRATION_ID = "312334754206";

  public GcmRefreshJob() {
    super(new Params(Priorities.NORMAL).requireNetwork());
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

      String            gcmId  = GoogleCloudMessaging.getInstance(context).register(REGISTRATION_ID);
      PushServiceSocket socket = PushServiceSocketFactory.create(context);

      socket.registerGcmId(gcmId);
      OpenchatServicePreferences.setGcmRegistrationId(context, gcmId);
    }
  }

  @Override
  protected void onCancel() {
    Log.w(TAG, "GCM reregistration failed after retry attempt exhaustion!");
  }

  @Override
  protected boolean shouldReRunOnThrowable(Throwable throwable) {
    if (throwable instanceof NonSuccessfulResponseCodeException) return false;
    return true;
  }
}
