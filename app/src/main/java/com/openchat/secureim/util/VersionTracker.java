package com.openchat.secureim.util;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.IOException;

public class VersionTracker {

  public static int getLastSeenVersion(Context context) {
    return OpenchatServicePreferences.getLastVersionCode(context);
  }

  public static void updateLastSeenVersion(Context context) {
    try {
      int currentVersionCode = Util.getCurrentApkReleaseVersion(context);
      OpenchatServicePreferences.setLastVersionCode(context, currentVersionCode);
    } catch (IOException ioe) {
      throw new AssertionError(ioe);
    }
  }
}
