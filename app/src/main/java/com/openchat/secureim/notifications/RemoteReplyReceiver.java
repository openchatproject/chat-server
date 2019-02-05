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

import java.util.LinkedList;
import java.util.List;

/**
 * Get the response text from the Wearable Device and sends an message as a reply
 */
public class RemoteReplyReceiver extends MasterSecretBroadcastReceiver {

  public static final String TAG           = RemoteReplyReceiver.class.getSimpleName();
  public static final String REPLY_ACTION  = "com.openchat.securesms.notifications.WEAR_REPLY";
  public static final String ADDRESS_EXTRA = "address";

  @Override
  protected void onReceive(final Context context, Intent intent,
                           final @Nullable MasterSecret masterSecret)
  {
    if (!REPLY_ACTION.equals(intent.getAction())) return;

    Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

    if (remoteInput == null) return;

    final Address      address      = intent.getParcelableExtra(ADDRESS_EXTRA);
    final CharSequence responseText = remoteInput.getCharSequence(MessageNotifier.EXTRA_REMOTE_REPLY);

    if (masterSecret != null && responseText != null) {
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
          long threadId;

          Recipient recipient = Recipient.from(context, address, false);
          int  subscriptionId = recipient.getDefaultSubscriptionId().or(-1);
          long expiresIn      = recipient.getExpireMessages() * 1000;

          if (recipient.isGroupRecipient()) {
            OutgoingMediaMessage reply = new OutgoingMediaMessage(recipient, responseText.toString(), new LinkedList<Attachment>(), System.currentTimeMillis(), subscriptionId, expiresIn, 0);
            threadId = MessageSender.send(context, masterSecret, reply, -1, false, null);
          } else {
            OutgoingTextMessage reply = new OutgoingTextMessage(recipient, responseText.toString(), expiresIn, subscriptionId);
            threadId = MessageSender.send(context, masterSecret, reply, -1, false, null);
          }

          List<MarkedMessageInfo> messageIds = DatabaseFactory.getThreadDatabase(context).setRead(threadId, true);

          MessageNotifier.updateNotification(context, masterSecret);
          MarkReadReceiver.process(context, messageIds);

          return null;
        }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

  }
}
