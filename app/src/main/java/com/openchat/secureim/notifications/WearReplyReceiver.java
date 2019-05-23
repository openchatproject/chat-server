package com.openchat.secureim.notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientProvider;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingTextMessage;

public class WearReplyReceiver extends BroadcastReceiver {

  public static final String TAG = WearReplyReceiver.class.getSimpleName();
  public static final String REPLY_ACTION = "com.openchat.secureim.notifications.WEAR_REPLY";

  @Override
  public void onReceive(final Context context, Intent intent) {
    if (!intent.getAction().equals(REPLY_ACTION))
      return;

    Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
    if (remoteInput == null)
      return;

    final long[] threadIds = intent.getLongArrayExtra("thread_ids");
    final MasterSecret masterSecret = intent.getParcelableExtra("master_secret");
    final long recipientId = intent.getLongExtra("recipient_id", -1);
    final CharSequence responseText = remoteInput.getCharSequence(MessageNotifier.EXTRA_VOICE_REPLY);

    final Recipients recipients = RecipientFactory.getRecipientsForIds(context, new long[]{recipientId}, false);

    if (threadIds != null && masterSecret != null) {

      ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
              .cancel(MessageNotifier.NOTIFICATION_ID);

      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
          for (long threadId : threadIds) {
            Log.w(TAG, "Marking as read: " + threadId);
            DatabaseFactory.getThreadDatabase(context).setRead(threadId);
          }

          OutgoingTextMessage reply = new OutgoingTextMessage(recipients, responseText.toString());
          MessageSender.send(context, masterSecret, reply, threadIds[0], false);

          MessageNotifier.updateNotification(context, masterSecret);
          return null;
        }
      }.execute();
    }
  }
}
