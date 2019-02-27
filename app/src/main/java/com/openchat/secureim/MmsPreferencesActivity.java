package com.openchat.secureim;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;

import com.openchat.secureim.mms.IncomingMmsConnection;
import com.openchat.secureim.service.SendReceiveService;
import com.openchat.secureim.util.DynamicLanguage;
import com.openchat.secureim.util.DynamicTheme;
import com.openchat.secureim.util.MemoryCleaner;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.crypto.MasterSecret;

public class MmsPreferencesActivity extends PassphraseRequiredSherlockPreferenceActivity {

  private MasterSecret masterSecret;

  private final DynamicTheme dynamicTheme       = new DynamicTheme();
  private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

  @Override
  protected void onCreate(Bundle icicle) {
    dynamicTheme.onCreate(this);
    dynamicLanguage.onCreate(this);
    super.onCreate(icicle);

    this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    initializePreferences();

    masterSecret = getIntent().getParcelableExtra("master_secret");

    initializeEditTextSummaries();
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
    dynamicLanguage.onResume(this);
  }

  @Override
  public void onDestroy() {
    MemoryCleaner.clean(masterSecret);
    MemoryCleaner.clean((MasterSecret) getIntent().getParcelableExtra("master_secret"));
    super.onDestroy();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        handleDownloadMmsPendingApn();
        finish();
        return true;
    }

    return false;
  }

  @Override
  public void onBackPressed() {
    handleDownloadMmsPendingApn();
    super.onBackPressed();
  }

  private void initializePreferences() {
    if (!IncomingMmsConnection.isConnectionPossible(this, null)) {
      OpenchatServicePreferences.setUseLocalApnsEnabled(this, true);
      addPreferencesFromResource(R.xml.mms_preferences);
      this.findPreference(OpenchatServicePreferences.ENABLE_MANUAL_MMS_PREF).setOnPreferenceChangeListener(new OverrideMmsChangeListener());
    } else {
      addPreferencesFromResource(R.xml.mms_preferences);
    }
  }

  private void initializeEditTextSummary(final EditTextPreference preference) {
    if (preference.getText() == null) {
      preference.setSummary("Not set");
    } else {
      preference.setSummary(preference.getText());
    }

    preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference pref, Object newValue) {
        preference.setSummary(newValue == null ? "Not set" : ((String) newValue));
        return true;
      }
    });
  }

  private void initializeEditTextSummaries() {
    initializeEditTextSummary((EditTextPreference)this.findPreference(OpenchatServicePreferences.MMSC_HOST_PREF));
    initializeEditTextSummary((EditTextPreference)this.findPreference(OpenchatServicePreferences.MMSC_PROXY_HOST_PREF));
    initializeEditTextSummary((EditTextPreference)this.findPreference(OpenchatServicePreferences.MMSC_PROXY_PORT_PREF));
  }

  private void handleDownloadMmsPendingApn() {
    Intent intent = new Intent(this, SendReceiveService.class);
    intent.setAction(SendReceiveService.DOWNLOAD_MMS_PENDING_APN_ACTION);
    startService(intent);
  }

  private class OverrideMmsChangeListener implements Preference.OnPreferenceChangeListener {
    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
      OpenchatServicePreferences.setUseLocalApnsEnabled(MmsPreferencesActivity.this, true);
      Toast.makeText(MmsPreferencesActivity.this, R.string.mms_preferences_activity__manual_mms_settings_are_required, Toast.LENGTH_SHORT).show();
      return false;
    }
  }

}
