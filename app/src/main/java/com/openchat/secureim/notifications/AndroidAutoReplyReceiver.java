package com.openchat.secureim.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.RemoteInput;

import com.openchat.secureim.attachments.Attachment;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MessagingDatabase.MarkedMessageInfo;
import com.openchat.secureim.mms.OutgoingMediaMessage;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingTextMessage;
import com.openchat.libim.logging.Log;

import java.util.LinkedList;
import java.util.List;

public class AndroidAutoReplyReceiver extends MasterSecretBroadcastReceiver {

  public static final String TAG             = AndroidAutoReplyReceiver.class.getSimpleName();
  public static final String REPLY_ACTION    = "com.openchat.securesms.notifications.ANDROID_AUTO_REPLY";
  public static final String ADDRESS_EXTRA   = "car_address";
  public static final String VOICE_REPLY_KEY = "car_voice_reply_key";
  public static final String THREAD_ID_EXTRA = "car_reply_thread_id";

  @Override
  protected void onReceive(final Context context, Intent intent,
                           final @Nullable MasterSecret masterSecret)
  {
    if (!REPLY_ACTION.equals(intent.getAction())) return;

    Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

    if (remoteInput == null) return;

    final Address      address      = intent.getParcelableExtra(ADDRESS_EXTRA);
    final long         threadId     = intent.getLongExtra(THREAD_ID_EXTRA, -1);
    final CharSequence responseText = getMessageText(intent);
    final Recipient    recipient    = Recipient.from(context, address, false);

    if (responseText != null) {
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {

          long replyThreadId;

          int  subscriptionId = recipient.getDefaultSubscriptionId().or(-1);
          long expiresIn      = recipient.getExpireMessages() * 1000;

          if (recipient.isGroupRecipient()) {
            Log.w("AndroidAutoReplyReceiver", "GroupRecipient, Sending media message");
            OutgoingMediaMessage reply = new OutgoingMediaMessage(recipient, responseText.toString(), new LinkedList<Attachment>(), System.currentTimeMillis(), subscriptionId, expiresIn, 0);
            replyThreadId = MessageSender.send(context, masterSecret, reply, threadId, false, null);
          } else {
            Log.w("AndroidAutoReplyReceiver", "Sending regular message ");
            OutgoingTextMessage reply = new OutgoingTextMessage(recipient, responseText.toString(), expiresIn, subscriptionId);
            replyThreadId = MessageSender.send(context, masterSecret, reply, threadId, false, null);
          }

          List<MarkedMessageInfo> messageIds = DatabaseFactory.getThreadDatabase(context).setRead(replyThreadId, true);

          MessageNotifier.updateNotification(context, masterSecret);
          MarkReadReceiver.process(context, messageIds);

          return null;
        }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
  }

  private CharSequence getMessageText(Intent intent) {
    Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
    if (remoteInput != null) {
      return remoteInput.getCharSequence(VOICE_REPLY_KEY);
    }
    return null;
  }

}
