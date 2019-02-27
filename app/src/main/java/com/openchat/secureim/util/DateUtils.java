package com.openchat.secureim.util;

import android.content.Context;

public class DateUtils extends android.text.format.DateUtils {

  private final static long DAY_IN_MILLIS  = 86400000L;
  private final static long WEEK_IN_MILLIS = 7 * DAY_IN_MILLIS;
  private final static long YEAR_IN_MILLIS = (long)(52.1775 * WEEK_IN_MILLIS);

  private static boolean isWithinWeek(final long millis) {
    return System.currentTimeMillis() - millis <= (WEEK_IN_MILLIS - DAY_IN_MILLIS);
  }

  private static boolean isWithinYear(final long millis) {
    return System.currentTimeMillis() - millis <= YEAR_IN_MILLIS;
  }

  public static String getBetterRelativeTimeSpanString(final Context c, final long millis) {
    int formatFlags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_TIME;
    if (!isToday(millis)) {
      if (isWithinWeek(millis)) {
        formatFlags |= DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY;
      } else if (isWithinYear(millis)) {
        formatFlags |= DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_ALL;
      } else {
        formatFlags |= DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL;
      }
    }
    return DateUtils.formatDateTime(c, millis, formatFlags);
  }
}
