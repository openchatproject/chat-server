package com.openchat.secureim.preferences;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;

import com.openchat.secureim.ApplicationPreferencesActivity;
import com.openchat.secureim.R;
import com.openchat.secureim.util.OpenchatServicePreferences;

import java.util.Arrays;

public class AppearancePreferenceFragment extends ListSummaryPreferenceFragment {

  @Override
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    addPreferencesFromResource(R.xml.preferences_appearance);

    this.findPreference(OpenchatServicePreferences.THEME_PREF).setOnPreferenceChangeListener(new ListSummaryListener());
    this.findPreference(OpenchatServicePreferences.LANGUAGE_PREF).setOnPreferenceChangeListener(new ListSummaryListener());
    initializeListSummary((ListPreference)findPreference(OpenchatServicePreferences.THEME_PREF));
    initializeListSummary((ListPreference)findPreference(OpenchatServicePreferences.LANGUAGE_PREF));
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

    int langIndex  = Arrays.asList(languageEntryValues).indexOf(OpenchatServicePreferences.getLanguage(context));
    int themeIndex = Arrays.asList(themeEntryValues).indexOf(OpenchatServicePreferences.getTheme(context));

    return context.getString(R.string.preferences__theme_summary,    themeEntries[themeIndex]) + ", " +
           context.getString(R.string.preferences__language_summary, languageEntries[langIndex]);
  }
}
