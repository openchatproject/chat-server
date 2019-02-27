package com.openchat.secureim.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.openchat.secureim.R;
import com.openchat.secureim.push.PushServiceSocketFactory;
import com.openchat.secureim.util.Dialogs;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.push.PushServiceSocket;

import java.io.IOException;
import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GcmRegistrationService extends Service implements Runnable {

  private static final String TAG = GcmRegistrationService.class.getSimpleName();

  public static final String REGISTRATION_ID = "312334754206";

  private ExecutorService executor;

  @Override
  public void onCreate() {
    super.onCreate();
    this.executor = Executors.newSingleThreadExecutor();
  }

  @Override
  public int onStartCommand(Intent intent, int flats, int startId) {
    executor.execute(this);
    return START_NOT_STICKY;
  }

  @Override
  public void run() {
    Log.w(TAG, "Running GCM Registration Service...");
    try {
      String registrationId = OpenchatServicePreferences.getGcmRegistrationId(this);

      if (registrationId == null) {
        Log.w(TAG, "GCM registrationId expired, reregistering...");
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {
          Log.w(TAG, "Unable to register with GCM! " + result);
          return;
        }

        String            gcmId  = GoogleCloudMessaging.getInstance(this).register(REGISTRATION_ID);
        PushServiceSocket socket = PushServiceSocketFactory.create(this);

        socket.registerGcmId(gcmId);
        OpenchatServicePreferences.setGcmRegistrationId(this, gcmId);

        stopSelf();
      }
    } catch (IOException e) {
      Log.w(TAG, e);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    executor.shutdown();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

}
