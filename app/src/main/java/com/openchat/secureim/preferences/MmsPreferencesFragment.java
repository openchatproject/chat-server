package com.openchat.secureim.preferences;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;
import android.util.Log;

import com.openchat.secureim.PassphraseRequiredActionBarActivity;
import com.openchat.secureim.R;
import com.openchat.secureim.components.CustomDefaultPreference;
import com.openchat.secureim.database.ApnDatabase;
import com.openchat.secureim.mms.MmsConnection;
import com.openchat.secureim.util.TelephonyUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;

import java.io.IOException;

public class MmsPreferencesFragment extends PreferenceFragment {

  private static final String TAG = MmsPreferencesFragment.class.getSimpleName();

  @Override
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    addPreferencesFromResource(R.xml.preferences_manual_mms);

    ((PassphraseRequiredActionBarActivity) getActivity()).getSupportActionBar()
        .setTitle(R.string.preferences__advanced_mms_access_point_names);
  }

  @Override
  public void onResume() {
    super.onResume();
    new LoadApnDefaultsTask().execute();
  }

  private class LoadApnDefaultsTask extends AsyncTask<Void, Void, MmsConnection.Apn> {

    @Override
    protected MmsConnection.Apn doInBackground(Void... params) {
      try {
        Context context = getActivity();

        if (context != null) {
          return ApnDatabase.getInstance(context)
                            .getDefaultApnParameters(TelephonyUtil.getMccMnc(context),
                                                     TelephonyUtil.getApn(context));
        }
      } catch (IOException e) {
        Log.w(TAG, e);
      }

      return null;
    }

    @Override
    protected void onPostExecute(MmsConnection.Apn apnDefaults) {
      ((CustomDefaultPreference)findPreference(OpenchatServicePreferences.MMSC_HOST_PREF))
          .setValidator(new CustomDefaultPreference.UriValidator())
          .setDefaultValue(apnDefaults.getMmsc());

      ((CustomDefaultPreference)findPreference(OpenchatServicePreferences.MMSC_PROXY_HOST_PREF))
          .setValidator(new CustomDefaultPreference.HostnameValidator())
          .setDefaultValue(apnDefaults.getProxy());

      ((CustomDefaultPreference)findPreference(OpenchatServicePreferences.MMSC_PROXY_PORT_PREF))
          .setValidator(new CustomDefaultPreference.PortValidator())
          .setDefaultValue(apnDefaults.getPort());

      ((CustomDefaultPreference)findPreference(OpenchatServicePreferences.MMSC_USERNAME_PREF))
          .setDefaultValue(apnDefaults.getPort());

      ((CustomDefaultPreference)findPreference(OpenchatServicePreferences.MMSC_PASSWORD_PREF))
          .setDefaultValue(apnDefaults.getPassword());
    }
  }

}
