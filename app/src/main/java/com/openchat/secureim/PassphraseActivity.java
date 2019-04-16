package com.openchat.secureim;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.service.KeyCachingService;

public abstract class PassphraseActivity extends BaseActionBarActivity {

  private KeyCachingService keyCachingService;
  private MasterSecret masterSecret;

  protected void setMasterSecret(MasterSecret masterSecret) {
    this.masterSecret = masterSecret;
    Intent bindIntent = new Intent(this, KeyCachingService.class);
    startService(bindIntent);
    bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
  }

  protected abstract void cleanup();

  private ServiceConnection serviceConnection = new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName className, IBinder service) {
        keyCachingService = ((KeyCachingService.KeySetBinder)service).getService();
        keyCachingService.setMasterSecret(masterSecret);

        PassphraseActivity.this.unbindService(PassphraseActivity.this.serviceConnection);

        masterSecret = null;
        cleanup();

        Intent nextIntent = getIntent().getParcelableExtra("next_intent");
        if (nextIntent != null) startActivity(nextIntent);
        finish();
      }

      @Override
      public void onServiceDisconnected(ComponentName name) {
        keyCachingService = null;
      }
  };
}
