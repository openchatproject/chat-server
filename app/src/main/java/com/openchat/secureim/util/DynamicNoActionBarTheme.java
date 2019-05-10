package com.openchat.secureim.util;

import android.app.Activity;

import com.openchat.secureim.R;

public class DynamicNoActionBarTheme extends DynamicTheme {
  @Override
  protected int getSelectedTheme(Activity activity) {
    String theme = OpenchatServicePreferences.getTheme(activity);

    if (theme.equals("dark")) return R.style.OpenchatService_DarkNoActionBar;

    return R.style.OpenchatService_LightNoActionBar;
  }
}
