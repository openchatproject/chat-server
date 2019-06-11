package com.openchat.secureim.notifications;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;

import com.openchat.secureim.ConversationActivity;
import com.openchat.secureim.R;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsSmsDatabase;
import com.openchat.secureim.database.PushDatabase;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.database.ThreadDatabase;
import com.openchat.secureim.database.model.MessageRecord;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.util.SpanUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MessageNotifier {

  private static final String TAG = MessageNotifier.class.getSimpleName();

  public static final int NOTIFICATION_ID = 1338;

  private volatile static long visibleThread = -1;

  public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";

  public static void setVisibleThread(long threadId) {
    visibleThread = threadId;
  }

  public static void notifyMessageDeliveryFailed(Context context, Recipients recipients, long threadId) {
    if (visibleThread == threadId) {
      sendInThreadNotification(context, recipients);
    } else {
      Intent intent = new Intent(context, ConversationActivity.class);
      intent.putExtra(ConversationActivity.RECIPIENTS_EXTRA, recipients.getIds());
      intent.putExtra(ConversationActivity.THREAD_ID_EXTRA, threadId);
      intent.setData((Uri.parse("custom://" + System.currentTimeMillis())));

      FailedNotificationBuilder builder = new FailedNotificationBuilder(context, OpenchatServicePreferences.getNotificationPrivacy(context), intent);
      ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE))
        .notify((int)threadId, builder.build());
    }
  }

  public static void updateNotification(@NonNull Context context, @Nullable MasterSecret masterSecret) {
    if (!OpenchatServicePreferences.isNotificationsEnabled(context)) {
      return;
    }

    updateNotification(context, masterSecret, false, false, 0);
  }

  public static void updateNotification(@NonNull  Context context,
                                        @Nullable MasterSecret masterSecret,
                                        long threadId)
  {
    updateNotification(context, masterSecret, false, threadId);
  }

  public static void updateNotification(@NonNull  Context context,
                                        @Nullable MasterSecret masterSecret,
                                        boolean   includePushDatabase,
                                        long      threadId)
  {
    Recipients recipients = DatabaseFactory.getThreadDatabase(context)
                                           .getRecipientsForThreadId(threadId);

    if (!OpenchatServicePreferences.isNotificationsEnabled(context) ||
        (recipients != null && recipients.isMuted()))
    {
      return;
    }

    if (visibleThread == threadId) {
      ThreadDatabase threads = DatabaseFactory.getThreadDatabase(context);
      threads.setRead(threadId);
      sendInThreadNotification(context, threads.getRecipientsForThreadId(threadId));
    } else {
      updateNotification(context, masterSecret, true, includePushDatabase, 0);
    }
  }

  private static void updateNotification(@NonNull  Context context,
                                         @Nullable MasterSecret masterSecret,
                                         boolean openchat,
                                         boolean includePushDatabase,
                                         int     reminderCount)
  {
    Cursor telcoCursor = null;
    Cursor pushCursor  = null;

    try {
      telcoCursor = DatabaseFactory.getMmsSmsDatabase(context).getUnread();
      pushCursor  = DatabaseFactory.getPushDatabase(context).getPending();

      if ((telcoCursor == null || telcoCursor.isAfterLast()) &&
          (pushCursor == null || pushCursor.isAfterLast()))
      {
        ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE))
          .cancel(NOTIFICATION_ID);
        updateBadge(context, 0);
        clearReminder(context);
        return;
      }

      NotificationState notificationState = constructNotificationState(context, masterSecret, telcoCursor);

      if (includePushDatabase) {
        appendPushNotificationState(context, notificationState, pushCursor);
      }

      if (notificationState.hasMultipleThreads()) {
        sendMultipleThreadNotification(context, notificationState, openchat);
      } else {
        sendSingleThreadNotification(context, masterSecret, notificationState, openchat);
      }

      updateBadge(context, notificationState.getMessageCount());
      scheduleReminder(context, reminderCount);
    } finally {
      if (telcoCursor != null) telcoCursor.close();
      if (pushCursor != null)  pushCursor.close();
    }
  }

  private static void sendSingleThreadNotification(@NonNull  Context context,
                                                   @Nullable MasterSecret masterSecret,
                                                   @NonNull  NotificationState notificationState,
                                                   boolean openchat)
  {
    if (notificationState.getNotifications().isEmpty()) {
      ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE))
          .cancel(NOTIFICATION_ID);
      return;
    }

    SingleRecipientNotificationBuilder builder       = new SingleRecipientNotificationBuilder(context, OpenchatServicePreferences.getNotificationPrivacy(context));
    List<NotificationItem>             notifications = notificationState.getNotifications();

    builder.setSender(notifications.get(0).getIndividualRecipient());
    builder.setMessageCount(notificationState.getMessageCount());
    builder.setPrimaryMessageBody(notifications.get(0).getText());
    builder.setContentIntent(notifications.get(0).getPendingIntent(context));

    long timestamp = notifications.get(0).getTimestamp();
    if (timestamp != 0) builder.setWhen(timestamp);

    builder.addActions(masterSecret,
                       notificationState.getMarkAsReadIntent(context),
                       notificationState.getQuickReplyIntent(context, notifications.get(0).getRecipients()),
                       notificationState.getWearableReplyIntent(context, notifications.get(0).getRecipients()));

    ListIterator<NotificationItem> iterator = notifications.listIterator(notifications.size());

    while(iterator.hasPrevious()) {
      builder.addMessageBody(iterator.previous().getText());

    }

    if (openchat) {
      builder.setAlarms(notificationState.getRingtone(), notificationState.getVibrate());
      builder.setTicker(notifications.get(0).getIndividualRecipient(),
                        notifications.get(0).getText());
    }

    ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE))
      .notify(NOTIFICATION_ID, builder.build());
  }

  private static void sendMultipleThreadNotification(@NonNull  Context context,
                                                     @NonNull  NotificationState notificationState,
                                                     boolean openchat)
  {
    MultipleRecipientNotificationBuilder builder       = new MultipleRecipientNotificationBuilder(context, OpenchatServicePreferences.getNotificationPrivacy(context));
    List<NotificationItem>               notifications = notificationState.getNotifications();

    builder.setMessageCount(notificationState.getMessageCount(), notificationState.getThreadCount());
    builder.setMostRecentSender(notifications.get(0).getIndividualRecipient());

    long timestamp = notifications.get(0).getTimestamp();
    if (timestamp != 0) builder.setWhen(timestamp);

    builder.addActions(notificationState.getMarkAsReadIntent(context));

    ListIterator<NotificationItem> iterator = notifications.listIterator(notifications.size());

    while(iterator.hasPrevious()) {
      NotificationItem item = iterator.previous();
      builder.addMessageBody(item.getIndividualRecipient(), item.getText());
    }

    if (openchat) {
      builder.setAlarms(notificationState.getRingtone(), notificationState.getVibrate());
      builder.setTicker(notifications.get(0).getText());
    }

    ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE))
      .notify(NOTIFICATION_ID, builder.build());
  }

  private static void sendInThreadNotification(Context context, Recipients recipients) {
    try {
      if (!OpenchatServicePreferences.isInThreadNotifications(context)) {
        return;
      }

      Uri uri = recipients != null ? recipients.getRingtone() : null;

      if (uri == null) {
        String ringtone = OpenchatServicePreferences.getNotificationRingtone(context);

        if (ringtone == null) {
          Log.w(TAG, "ringtone preference was null.");
          return;
        } else {
          uri = Uri.parse(ringtone);
        }
      }

      if (uri == null) {
        Log.w(TAG, "couldn't parse ringtone uri " + OpenchatServicePreferences.getNotificationRingtone(context));
        return;
      }

      MediaPlayer player = new MediaPlayer();
      player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
      player.setDataSource(context, uri);
      player.setLooping(false);
      player.setVolume(0.25f, 0.25f);
      player.prepare();

      final AudioManager audioManager = ((AudioManager)context.getSystemService(Context.AUDIO_SERVICE));

      audioManager.requestAudioFocus(null, AudioManager.STREAM_NOTIFICATION,
                                     AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

      player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
          audioManager.abandonAudioFocus(null);
        }
      });

      player.start();
    } catch (IOException ioe) {
      Log.w("MessageNotifier", ioe);
    }
  }

  private static void appendPushNotificationState(@NonNull Context context,
                                                  @NonNull NotificationState notificationState,
                                                  @NonNull Cursor cursor)
  {
    PushDatabase.Reader reader = null;
    OpenchatServiceEnvelope envelope;

    try {
      reader = DatabaseFactory.getPushDatabase(context).readerFor(cursor);

      while ((envelope = reader.getNext()) != null) {
        Recipients      recipients = RecipientFactory.getRecipientsFromString(context, envelope.getSource(), false);
        Recipient       recipient  = recipients.getPrimaryRecipient();
        long            threadId   = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipients);
        SpannableString body       = new SpannableString(context.getString(R.string.MessageNotifier_locked_message));
        body.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, body.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (!recipients.isMuted()) {
          notificationState.addNotification(new NotificationItem(recipient, recipients, null, threadId, body, 0));
        }
      }
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  private static NotificationState constructNotificationState(@NonNull  Context context,
                                                              @Nullable MasterSecret masterSecret,
                                                              @NonNull  Cursor cursor)
  {
    NotificationState notificationState = new NotificationState();
    MessageRecord record;
    MmsSmsDatabase.Reader reader;

    if (masterSecret == null) reader = DatabaseFactory.getMmsSmsDatabase(context).readerFor(cursor);
    else                      reader = DatabaseFactory.getMmsSmsDatabase(context).readerFor(cursor, masterSecret);

    while ((record = reader.getNext()) != null) {
      Recipient       recipient        = record.getIndividualRecipient();
      Recipients      recipients       = record.getRecipients();
      long            threadId         = record.getThreadId();
      CharSequence    body             = record.getDisplayBody();
      Recipients      threadRecipients = null;
      long            timestamp;

      if (record.isPush()) timestamp = record.getDateSent();
      else                 timestamp = record.getDateReceived();

      if (threadId != -1) {
        threadRecipients = DatabaseFactory.getThreadDatabase(context).getRecipientsForThreadId(threadId);
      }

      if (SmsDatabase.Types.isDecryptInProgressType(record.getType()) || !record.getBody().isPlaintext()) {
        body = SpanUtil.italic(context.getString(R.string.MessageNotifier_locked_message));
      } else if (record.isMms() && TextUtils.isEmpty(body)) {
        body = SpanUtil.italic(context.getString(R.string.MessageNotifier_media_message));
      } else if (record.isMms() && !record.isMmsNotification()) {
        String message      = context.getString(R.string.MessageNotifier_media_message_with_text, body);
        int    italicLength = message.length() - body.length();
        body = SpanUtil.italic(message, italicLength);
      }

      if (threadRecipients == null || !threadRecipients.isMuted()) {
        notificationState.addNotification(new NotificationItem(recipient, recipients, threadRecipients, threadId, body, timestamp));
      }
    }

    reader.close();
    return notificationState;
  }

  private static void updateBadge(Context context, int count) {
    try {
      ShortcutBadger.setBadge(context.getApplicationContext(), count);
    } catch (Throwable t) {
      Log.w("MessageNotifier", t);
    }
  }

  private static void scheduleReminder(Context context, int count) {
    if (count >= OpenchatServicePreferences.getRepeatAlertsCount(context)) {
      return;
    }

    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent       alarmIntent  = new Intent(ReminderReceiver.REMINDER_ACTION);
    alarmIntent.putExtra("reminder_count", count);

    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    long          timeout       = TimeUnit.MINUTES.toMillis(2);

    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeout, pendingIntent);
  }

  private static void clearReminder(Context context) {
    Intent        alarmIntent   = new Intent(ReminderReceiver.REMINDER_ACTION);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    AlarmManager  alarmManager  = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarmManager.cancel(pendingIntent);
  }

  public static class ReminderReceiver extends BroadcastReceiver {

    public static final String REMINDER_ACTION = "com.openchat.secureim.MessageNotifier.REMINDER_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
      MasterSecret masterSecret  = KeyCachingService.getMasterSecret(context);
      int          reminderCount = intent.getIntExtra("reminder_count", 0);
      MessageNotifier.updateNotification(context, masterSecret, true, reminderCount + 1);
    }
  }

  public static class DeleteReceiver extends BroadcastReceiver {

    public static final String DELETE_REMINDER_ACTION = "com.openchat.secureim.MessageNotifier.DELETE_REMINDER_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
      clearReminder(context);
    }
  }
}
