package com.openchat.secureim;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.preference.PreferenceFragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.openchat.secureim.mms.OutgoingMmsConnection;
import com.openchat.secureim.util.OpenchatServicePreferences;

import java.net.URI;
import java.net.URISyntaxException;

public class MmsPreferencesFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    initializePreferences();
    initializeEditTextSummaries();
    ((PassphraseRequiredActionBarActivity) getActivity()).getSupportActionBar()
        .setTitle(R.string.preferences__advanced_mms_access_point_names);
  }

  private void initializePreferences() {
    if (!OutgoingMmsConnection.isConnectionPossible(getActivity())) {
      OpenchatServicePreferences.setUseLocalApnsEnabled(getActivity(), true);
      addPreferencesFromResource(R.xml.preferences_manual_mms);
      this.findPreference(OpenchatServicePreferences.ENABLE_MANUAL_MMS_PREF)
          .setOnPreferenceChangeListener(new OverrideMmsChangeListener());
    } else {
      addPreferencesFromResource(R.xml.preferences_manual_mms);
    }
    this.findPreference(OpenchatServicePreferences.MMSC_HOST_PREF).setOnPreferenceChangeListener(new ValidUriVerificationListener());
    this.findPreference(OpenchatServicePreferences.MMSC_PROXY_HOST_PREF).setOnPreferenceChangeListener(new ValidHostnameVerificationListener());
    this.findPreference(OpenchatServicePreferences.MMSC_PROXY_PORT_PREF).setOnPreferenceChangeListener(new EditTextVerificationListener());
    this.findPreference(OpenchatServicePreferences.MMSC_USERNAME_PREF).setOnPreferenceChangeListener(new EditTextVerificationListener());
    this.findPreference(OpenchatServicePreferences.MMSC_PASSWORD_PREF).setOnPreferenceChangeListener(new EditTextVerificationListener());
  }

  private void initializeEditTextSummary(final EditTextPreference preference) {
    preference.setSummary(TextUtils.isEmpty(preference.getText()) ? getString(R.string.MmsPreferencesFragment__not_set) : preference.getText());
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
      Toast.makeText(getActivity(), R.string.MmsPreferencesFragment__manual_mms_settings_are_required,
                     Toast.LENGTH_SHORT).show();
      return false;
    }
  }

  public static CharSequence getSummary(Context context) {
    final int enabledResId  = R.string.MmsPreferencesFragment__enabled;
    final int disabledResId = R.string.MmsPreferencesFragment__disabled;

    return context.getString(OpenchatServicePreferences.isUseLocalApnsEnabled(context) ? enabledResId : disabledResId);
  }

  private class EditTextVerificationListener implements OnPreferenceChangeListener {
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
      String newString = (String)newValue;
      if (isValid(newString)) {
        preference.setSummary(TextUtils.isEmpty(newString) ? getString(R.string.MmsPreferencesFragment__not_set) : newString);
        return true;
      } else {
        Toast.makeText(getActivity(), getErrorMessage(), Toast.LENGTH_LONG).show();
        return false;
      }
    }

    protected boolean isValid(String newString) { return true; }
    protected int getErrorMessage() { return 0; }
  }

  private class ValidUriVerificationListener extends EditTextVerificationListener {
    @Override
    protected boolean isValid(String newString) {
      if (TextUtils.isEmpty(newString)) return true;
      try {
        new URI(newString);
        return true;
      } catch (URISyntaxException mue) {
        return false;
      }
    }

    @Override
    protected int getErrorMessage() {
      return R.string.MmsPreferencesFragment__invalid_uri;
    }
  }

  private class ValidHostnameVerificationListener extends EditTextVerificationListener {
    @Override
    protected boolean isValid(String newString) {
      if (TextUtils.isEmpty(newString)) return true;
      try {
        URI uri = new URI(null, newString, null, null);
        return true;
      } catch (URISyntaxException mue) {
        return false;
      }
    }

    @Override
    protected int getErrorMessage() {
      return R.string.MmsPreferencesFragment__invalid_host;
    }
  }
}
