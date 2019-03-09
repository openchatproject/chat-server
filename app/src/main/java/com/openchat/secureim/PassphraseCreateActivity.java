package com.openchat.secureim;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.util.MemoryCleaner;
import com.openchat.secureim.util.VersionTracker;
import com.openchat.imservice.util.Util;

public class PassphraseCreateActivity extends PassphraseActivity {

  public PassphraseCreateActivity() { }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.create_passphrase_activity);

    initializeResources();
  }

  private void initializeResources() {
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    getSupportActionBar().setCustomView(R.layout.light_centered_app_title);
    mitigateAndroidTilingBug();

    OpenchatServicePreferences.setPasswordDisabled(this, true);
    new SecretGenerator().execute(MasterSecretUtil.UNENCRYPTED_PASSPHRASE);
  }

  private class SecretGenerator extends AsyncTask<String, Void, Void> {
    private MasterSecret   masterSecret;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(String... params) {
      String passphrase = params[0];
      masterSecret      = MasterSecretUtil.generateMasterSecret(PassphraseCreateActivity.this,
                                                                passphrase);

      MemoryCleaner.clean(passphrase);

      MasterSecretUtil.generateAsymmetricMasterSecret(PassphraseCreateActivity.this, masterSecret);
      IdentityKeyUtil.generateIdentityKeys(PassphraseCreateActivity.this, masterSecret);
      VersionTracker.updateLastSeenVersion(PassphraseCreateActivity.this);

      return null;
    }

    @Override
    protected void onPostExecute(Void param) {
      setMasterSecret(masterSecret);
    }
  }

  private void mitigateAndroidTilingBug() {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      Drawable actionBarBackground = getResources().getDrawable(R.drawable.background_pattern_repeat);
      Util.fixBackgroundRepeat(actionBarBackground);
      getSupportActionBar().setBackgroundDrawable(actionBarBackground);
      Util.fixBackgroundRepeat(findViewById(R.id.scroll_parent).getBackground());
    }
  }

  @Override
  protected void cleanup() {
    System.gc();
  }
}
