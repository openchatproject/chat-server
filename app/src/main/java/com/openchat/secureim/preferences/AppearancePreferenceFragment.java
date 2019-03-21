package com.openchat.secureim.preferences;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import com.openchat.secureim.ApplicationPreferencesActivity;
import com.openchat.secureim.R;
import com.openchat.secureim.util.OpenchatServicePreferences;

public class AppearancePreferenceFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    addPreferencesFromResource(R.xml.preferences_appearance);
  }

  @Override
  public void onStart() {
    super.onStart();
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener((ApplicationPreferencesActivity)getActivity());
  }

  @Override
  public void onResume() {
    super.onResume();
    ((ApplicationPreferencesActivity) getActivity()).getSupportActionBar().setTitle(R.string.preferences__appearance);
  }

  @Override
  public void onStop() {
    super.onStop();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener((ApplicationPreferencesActivity) getActivity());
  }

  public static CharSequence getSummary(Context context) {
    String[] languageEntries     = context.getResources().getStringArray(R.array.language_entries);
    String[] languageEntryValues = context.getResources().getStringArray(R.array.language_values);
    String[] themeEntries        = context.getResources().getStringArray(R.array.pref_theme_entries);
    String[] themeEntryValues    = context.getResources().getStringArray(R.array.pref_theme_values);

    Integer langIndex  = findIndexOfValue(OpenchatServicePreferences.getLanguage(context), languageEntryValues);
    Integer themeIndex = findIndexOfValue(OpenchatServicePreferences.getTheme(context), themeEntryValues);

    return context.getString(R.string.preferences__theme)    + ": " + themeEntries[themeIndex] + ", " +
      context.getString(R.string.preferences__language) + ": " + languageEntries[langIndex];
  }

  private static int findIndexOfValue(String value,  CharSequence[] mEntryValues) {
    if (value != null && mEntryValues != null) {
      for (int i = mEntryValues.length - 1; i >= 0; i--) {
        if (mEntryValues[i].equals(value)) {
          return i;
        }
      }
    }
    return -1;
  }
}
