package com.openchat.secureim.crypto;


import android.content.Context;
import android.support.annotation.NonNull;

import com.openchat.secureim.util.Base64;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.secureim.util.Util;

import java.io.IOException;

public class ProfileKeyUtil {

  public static synchronized boolean hasProfileKey(@NonNull Context context) {
    return TextSecurePreferences.getProfileKey(context) != null;
  }

  public static synchronized @NonNull byte[] getProfileKey(@NonNull Context context) {
    try {
      String encodedProfileKey = TextSecurePreferences.getProfileKey(context);

      if (encodedProfileKey == null) {
        encodedProfileKey = Util.getSecret(32);
        TextSecurePreferences.setProfileKey(context, encodedProfileKey);
      }

      return Base64.decode(encodedProfileKey);
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

}
