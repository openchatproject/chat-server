package com.openchat.secureim.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.push.PushServiceSocketFactory;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.crypto.IdentityKey;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.crypto.PreKeyUtil;
import com.openchat.imservice.crypto.ecc.Curve;
import com.openchat.imservice.push.PushServiceSocket;
import com.openchat.imservice.storage.PreKeyRecord;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PreKeyService extends Service {

  private static final String TAG            = PreKeyService.class.getSimpleName();
  public static final  String REFRESH_ACTION = "com.openchat.secureim.PreKeyService.REFRESH";

  private static final int PREKEY_MINIMUM = 10;

  private final Executor executor = Executors.newSingleThreadExecutor();

  public static void initiateRefresh(Context context, MasterSecret masterSecret) {
    Intent intent = new Intent(context, PreKeyService.class);
    intent.setAction(PreKeyService.REFRESH_ACTION);
    intent.putExtra("master_secret", masterSecret);
    context.startService(intent);
  }

  @Override
  public int onStartCommand(Intent intent, int flats, int startId) {
    if (REFRESH_ACTION.equals(intent.getAction())) {
      MasterSecret masterSecret = intent.getParcelableExtra("master_secret");
      executor.execute(new RefreshTask(this, masterSecret));
    }

    return START_NOT_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private static class RefreshTask implements Runnable {

    private final Context      context;
    private final MasterSecret masterSecret;

    public RefreshTask(Context context, MasterSecret masterSecret) {
      this.context      = context.getApplicationContext();
      this.masterSecret = masterSecret;
    }

    public void run() {
      try {
        if (!OpenchatServicePreferences.isPushRegistered(context)) return;

        PushServiceSocket socket        = PushServiceSocketFactory.create(context);
        int               availableKeys = socket.getAvailablePreKeys();

        if (availableKeys >= PREKEY_MINIMUM) {
          Log.w(TAG, "Available keys sufficient: " + availableKeys);
          return;
        }

        List<PreKeyRecord> preKeyRecords       = PreKeyUtil.generatePreKeys(context, masterSecret);
        PreKeyRecord       lastResortKeyRecord = PreKeyUtil.generateLastResortKey(context, masterSecret);
        IdentityKey        identityKey         = IdentityKeyUtil.getIdentityKey(context);

        Log.w(TAG, "Registering new prekeys...");

        socket.registerPreKeys(identityKey, lastResortKeyRecord, preKeyRecords);
      } catch (IOException e) {
        Log.w(TAG, e);
      }
    }
  }

}
