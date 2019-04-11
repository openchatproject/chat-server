package com.openchat.secureim.util;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.openchat.secureim.R;

import java.util.concurrent.TimeUnit;

public class DateUtils extends android.text.format.DateUtils {

  private static boolean isWithin(final long millis, final long span, final TimeUnit unit) {
    return System.currentTimeMillis() - millis <= unit.toMillis(span);
  }

  private static int convertDelta(final long millis, TimeUnit to) {
    return (int) to.convert(System.currentTimeMillis() - millis, TimeUnit.MILLISECONDS);
  }

  public static String getBriefRelativeTimeSpanString(final Context c, final long timestamp) {
    if (isWithin(timestamp, 1, TimeUnit.MINUTES)) {
      return c.getString(R.string.DateUtils_now);
    } else if (isWithin(timestamp, 1, TimeUnit.HOURS)) {
      int mins = convertDelta(timestamp, TimeUnit.MINUTES);
      return c.getResources().getQuantityString(R.plurals.minutes_ago, mins, mins);
    } else if (isWithin(timestamp, 1, TimeUnit.DAYS)) {
      int hours = convertDelta(timestamp, TimeUnit.HOURS);
      return c.getResources().getQuantityString(R.plurals.hours_ago, hours, hours);
    } else if (isWithin(timestamp, 6, TimeUnit.DAYS)) {
      return formatDateTime(c, timestamp, DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY);
    } else if (isWithin(timestamp, 365, TimeUnit.DAYS)) {
      return formatDateTime(c, timestamp, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_ALL);
    } else {
      return formatDateTime(c, timestamp, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
    }
  }

  public static String getExtendedRelativeTimeSpanString(final Context c, final long timestamp) {
    if (isWithin(timestamp, 1, TimeUnit.MINUTES)) {
      return c.getString(R.string.DateUtils_now);
    } else if (isWithin(timestamp, 1, TimeUnit.HOURS)) {
      int mins = (int)TimeUnit.MINUTES.convert(System.currentTimeMillis() - timestamp, TimeUnit.MILLISECONDS);
      return c.getResources().getQuantityString(R.plurals.minutes_ago, mins, mins);
    } else {
      int formatFlags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_TIME;
      if (isWithin(timestamp, 6, TimeUnit.DAYS)) {
        formatFlags |= DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY;
      } else if (isWithin(timestamp, 365, TimeUnit.DAYS)) {
        formatFlags |= DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_ALL;
      } else {
        formatFlags |= DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL;
      }
      return DateUtils.formatDateTime(c, timestamp, formatFlags);
    }
  }

  public static SimpleDateFormat getDetailedDateFormatter(Context context) {
    String dateFormatPattern;

    if (DateFormat.is24HourFormat(context)) {
      dateFormatPattern = "MMM d, yyyy HH:mm:ss zzz";
    } else {
      dateFormatPattern = "MMM d, yyyy hh:mm:ss a zzz";
    }

    return new SimpleDateFormat(dateFormatPattern, Locale.getDefault());
  }

}
