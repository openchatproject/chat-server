package com.openchat.secureim.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class ThemeUtil {
  public static Drawable resolveIcon(Context c, int iconAttr)
  {
    TypedValue out = new TypedValue();
    c.getTheme().resolveAttribute(iconAttr, out, true);
    return c.getResources().getDrawable(out.resourceId);
  }
}
