package com.openchat.secureim.util;

import android.app.Activity;

import com.openchat.secureim.R;

public class DynamicIntroTheme extends DynamicTheme {
  @Override
  protected int getSelectedTheme(Activity activity) {
    String theme = OpenchatServicePreferences.getTheme(activity);

    if (theme.equals("dark")) return R.style.OpenchatService_DarkIntroTheme;

    return R.style.OpenchatService_LightIntroTheme;
  }
}
