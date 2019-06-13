package com.openchat.secureim.notifications;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.openchat.secureim.database.RecipientPreferenceDatabase;
import com.openchat.secureim.preferences.NotificationPrivacyPreference;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.secureim.util.Util;

public abstract class AbstractNotificationBuilder extends NotificationCompat.Builder {

  protected Context                       context;
  protected NotificationPrivacyPreference privacy;

  public AbstractNotificationBuilder(Context context, NotificationPrivacyPreference privacy) {
    super(context);

    this.context = context;
    this.privacy = privacy;
  }

  protected CharSequence getStyledMessage(@NonNull Recipient recipient, @Nullable CharSequence message) {
    SpannableStringBuilder builder = new SpannableStringBuilder();
    builder.append(Util.getBoldedString(recipient.toShortString()));
    builder.append(": ");
    builder.append(message == null ? "" : message);

    return builder;
  }

  public void setAlarms(@Nullable Uri ringtone, RecipientPreferenceDatabase.VibrateState vibrate) {
    String defaultRingtoneName   = OpenchatServicePreferences.getNotificationRingtone(context);
    boolean defaultVibrate       = OpenchatServicePreferences.isNotificationVibrateEnabled(context);
    String ledColor              = OpenchatServicePreferences.getNotificationLedColor(context);
    String ledBlinkPattern       = OpenchatServicePreferences.getNotificationLedPattern(context);
    String ledBlinkPatternCustom = OpenchatServicePreferences.getNotificationLedPatternCustom(context);
    String[] blinkPatternArray   = parseBlinkPattern(ledBlinkPattern, ledBlinkPatternCustom);

    if      (ringtone != null)                        setSound(ringtone);
    else if (!TextUtils.isEmpty(defaultRingtoneName)) setSound(Uri.parse(defaultRingtoneName));

    if (vibrate == RecipientPreferenceDatabase.VibrateState.ENABLED ||
        (vibrate == RecipientPreferenceDatabase.VibrateState.DEFAULT && defaultVibrate))
    {
      setDefaults(Notification.DEFAULT_VIBRATE);
    }

    if (!ledColor.equals("none")) {
      setLights(Color.parseColor(ledColor),
                Integer.parseInt(blinkPatternArray[0]),
                Integer.parseInt(blinkPatternArray[1]));
    }
  }

  private String[] parseBlinkPattern(String blinkPattern, String blinkPatternCustom) {
    if (blinkPattern.equals("custom"))
      blinkPattern = blinkPatternCustom;

    return blinkPattern.split(",");
  }
}
