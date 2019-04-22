package com.openchat.secureim.util;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.util.TypedValue;

public class ResUtil {

  public static int getColor(Context context, @AttrRes int attr) {
    final TypedArray styledAttributes = context.obtainStyledAttributes(new int[]{attr});
    final int        result           = styledAttributes.getColor(0, -1);
    styledAttributes.recycle();
    return result;
  }

  public static int getDrawableRes(Context c, @AttrRes int attr) {
    return getDrawableRes(c.getTheme(), attr);
  }

  public static int getDrawableRes(Theme theme, @AttrRes int attr) {
    final TypedValue out = new TypedValue();
    theme.resolveAttribute(attr, out, true);
    return out.resourceId;
  }

  public static Drawable getDrawable(Context c, @AttrRes int attr) {
    return c.getResources().getDrawable(getDrawableRes(c, attr));
  }
}
