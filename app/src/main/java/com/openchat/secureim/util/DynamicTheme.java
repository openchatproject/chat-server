package com.openchat.secureim.util;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.openchat.secureim.ApplicationPreferencesActivity;
import com.openchat.secureim.ConversationActivity;
import com.openchat.secureim.ConversationListActivity;
import com.openchat.secureim.R;

public class DynamicTheme {

  private int currentTheme;

  public void onCreate(Activity activity) {
    currentTheme = getSelectedTheme(activity);
    activity.setTheme(currentTheme);
  }

  public void onResume(Activity activity) {
    if (currentTheme != getSelectedTheme(activity)) {
      Intent intent = activity.getIntent();
      activity.finish();
      OverridePendingTransition.invoke(activity);
      activity.startActivity(intent);
      OverridePendingTransition.invoke(activity);
    }
  }

  private static int getSelectedTheme(Activity activity) {
    String theme = OpenchatServicePreferences.getTheme(activity);

    if (theme.equals("dark")) return R.style.OpenchatService_DarkTheme;

    return R.style.OpenchatService_LightTheme;
  }

  private static final class OverridePendingTransition {
    static void invoke(Activity activity) {
      activity.overridePendingTransition(0, 0);
    }
  }
}
