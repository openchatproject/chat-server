package com.openchat.secureim.notifications;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.openchat.secureim.ConversationListActivity;
import com.openchat.secureim.R;
import com.openchat.secureim.database.RecipientDatabase;
import com.openchat.secureim.preferences.widgets.NotificationPrivacyPreference;
import com.openchat.secureim.util.TextSecurePreferences;

public class PendingMessageNotificationBuilder extends AbstractNotificationBuilder {

  public PendingMessageNotificationBuilder(Context context, NotificationPrivacyPreference privacy) {
    super(context, privacy);

    Intent intent = new Intent(context, ConversationListActivity.class);

    setSmallIcon(R.drawable.icon_notification);
    setColor(context.getResources().getColor(R.color.textsecure_primary));
    setPriority(TextSecurePreferences.getNotificationPriority(context));
    setCategory(NotificationCompat.CATEGORY_MESSAGE);

    setContentTitle(context.getString(R.string.MessageNotifier_pending_openchat_messages));
    setContentText(context.getString(R.string.MessageNotifier_you_have_pending_openchat_messages));
    setTicker(context.getString(R.string.MessageNotifier_you_have_pending_openchat_messages));

    setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
    setAutoCancel(true);
    setAlarms(null, RecipientDatabase.VibrateState.DEFAULT);
  }
}
