package com.openchat.secureim.contacts.avatars;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import com.openchat.secureim.R;
import com.openchat.secureim.color.MaterialColor;
import com.openchat.secureim.color.MaterialColors;
import com.openchat.secureim.color.ThemeType;
import com.openchat.protocal.util.guava.Optional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ContactColors {

  public static final MaterialColor UNKNOWN_COLOR = MaterialColors.GREY;
  
  public static MaterialColor generateFor(@NonNull String name) {
    return MaterialColors.CONVERSATION_PALETTE.get(Math.abs(name.hashCode()) % MaterialColors.CONVERSATION_PALETTE.size());
  }

  public static MaterialColor getGroupColor(Context context) {
    final int actionBarColor = context.getResources().getColor(R.color.openchatservice_primary);
    final int statusBarColor = context.getResources().getColor(R.color.openchatservice_primary_dark);

    return new MaterialColor(new HashMap<String, Integer>()) {
      @Override
      public int toConversationColor(ThemeType themeType) {
        return UNKNOWN_COLOR.toConversationColor(themeType);
      }

      @Override
      public int toActionBarColor(ThemeType themeType) {
        return actionBarColor;
      }

      @Override
      public int toStatusBarColor(ThemeType themeType) {
        return statusBarColor;
      }

      @Override
      public String serialize() {
        return "group_color";
      }
    };

  }

}
