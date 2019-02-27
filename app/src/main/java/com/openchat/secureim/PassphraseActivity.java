package com.openchat.secureim;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.util.MemoryCleaner;

import com.actionbarsherlock.app.SherlockActivity;

public abstract class PassphraseActivity extends SherlockActivity {

  private KeyCachingService keyCachingService;
  private MasterSecret masterSecret;

  protected void setMasterSecret(MasterSecret masterSecret) {
    this.masterSecret = masterSecret;
    Intent bindIntent = new Intent(this, KeyCachingService.class);
    bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
  }

  protected MasterSecret getMasterSecret() {
    return masterSecret;
  }

  protected abstract void cleanup();

  private ServiceConnection serviceConnection = new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName className, IBinder service) {
        keyCachingService = ((KeyCachingService.KeyCachingBinder)service).getService();
        keyCachingService.setMasterSecret(masterSecret);

        PassphraseActivity.this.unbindService(PassphraseActivity.this.serviceConnection);

        MemoryCleaner.clean(masterSecret);
        cleanup();

        PassphraseActivity.this.setResult(RESULT_OK);
        PassphraseActivity.this.finish();
      }

      @Override
      public void onServiceDisconnected(ComponentName name) {
        keyCachingService = null;
      }
  };
}
