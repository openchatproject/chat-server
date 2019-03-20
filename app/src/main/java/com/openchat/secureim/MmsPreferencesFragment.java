package com.openchat.secureim;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;
import android.widget.Toast;

import com.openchat.secureim.mms.OutgoingMmsConnection;
import com.openchat.secureim.util.OpenchatServicePreferences;

public class MmsPreferencesFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    initializePreferences();
    initializeEditTextSummaries();
  }

  private void initializePreferences() {
    if (!OutgoingMmsConnection.isConnectionPossible(getActivity())) {
      OpenchatServicePreferences.setUseLocalApnsEnabled(getActivity(), true);
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
    initializeEditTextSummary((EditTextPreference)this.findPreference(OpenchatServicePreferences.MMSC_USERNAME_PREF));
    initializeEditTextSummary((EditTextPreference)this.findPreference(OpenchatServicePreferences.MMSC_PASSWORD_PREF));
  }

  private class OverrideMmsChangeListener implements Preference.OnPreferenceChangeListener {
    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
      OpenchatServicePreferences.setUseLocalApnsEnabled(getActivity(), true);
      Toast.makeText(getActivity(), R.string.mms_preferences_activity__manual_mms_settings_are_required, Toast.LENGTH_SHORT).show();
      return false;
    }
  }
}
