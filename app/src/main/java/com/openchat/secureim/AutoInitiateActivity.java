package com.openchat.secureim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.openchat.secureim.crypto.KeyExchangeInitiator;
import com.openchat.secureim.protocol.Tag;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.util.MemoryCleaner;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.storage.Session;
import com.openchat.imservice.storage.SessionRecordV2;

public class AutoInitiateActivity extends Activity {

  private long threadId;
  private Recipient recipient;
  private MasterSecret masterSecret;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.auto_initiate_activity);

    initializeResources();
  }

  @Override
  public void onDestroy() {
    MemoryCleaner.clean(masterSecret);
    super.onDestroy();
  }

  private void initializeResources() {
    this.threadId     = this.getIntent().getLongExtra("threadId", -1);
    this.recipient    = (Recipient)this.getIntent().getParcelableExtra("recipient");
    this.masterSecret = (MasterSecret)this.getIntent().getParcelableExtra("masterSecret");

    ((Button)findViewById(R.id.initiate_button)).setOnClickListener(new OkListener());
    ((Button)findViewById(R.id.cancel_button)).setOnClickListener(new CancelListener());
  }

  private void initiateKeyExchange() {
    KeyExchangeInitiator.initiate(this, masterSecret, recipient, true);
    finish();
  }

  private class OkListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      initiateKeyExchange();
    }
  }

  private class CancelListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      Log.w("AutoInitiateActivity", "Exempting threadID: " + threadId);
      exemptThread(AutoInitiateActivity.this, threadId);
      AutoInitiateActivity.this.finish();
    }
  }

  public static void exemptThread(Context context, long threadId) {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    sp.edit().putBoolean("pref_thread_auto_init_exempt_" + threadId, true).apply();
  }

  public static boolean isValidAutoInitiateSituation(Context context, MasterSecret masterSecret,
                 Recipient recipient, String message, long threadId)
  {
    return
        Tag.isTagged(message)                           &&
        OpenchatServicePreferences.isPushRegistered(context) &&
        isThreadQualified(context, threadId)            &&
        isExchangeQualified(context, masterSecret, recipient);
  }

  private static boolean isThreadQualified(Context context, long threadId) {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    return !sp.getBoolean("pref_thread_auto_init_exempt_" + threadId, false);
  }

  private static boolean isExchangeQualified(Context context,
                                             MasterSecret masterSecret,
                                             Recipient recipient)
  {
    return !Session.hasSession(context, masterSecret, recipient);
  }
}
