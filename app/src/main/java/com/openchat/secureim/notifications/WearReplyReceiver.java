package com.openchat.secureim.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.RemoteInput;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.mms.OutgoingMediaMessage;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingTextMessage;

import ws.com.google.android.mms.pdu.PduBody;

public class WearReplyReceiver extends MasterSecretBroadcastReceiver {

  public static final String TAG                 = WearReplyReceiver.class.getSimpleName();
  public static final String REPLY_ACTION        = "com.openchat.secureim.notifications.WEAR_REPLY";
  public static final String RECIPIENT_IDS_EXTRA = "recipient_ids";

  @Override
  protected void onReceive(final Context context, Intent intent,
                           final @Nullable MasterSecret masterSecret)
  {
    if (!REPLY_ACTION.equals(intent.getAction())) return;

    Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

    if (remoteInput == null) return;

    final long[]       recipientIds = intent.getLongArrayExtra(RECIPIENT_IDS_EXTRA);
    final CharSequence responseText = remoteInput.getCharSequence(MessageNotifier.EXTRA_VOICE_REPLY);
    final Recipients   recipients   = RecipientFactory.getRecipientsForIds(context, recipientIds, false);

    if (masterSecret != null && responseText != null) {
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
          long threadId;

          if (recipients.isGroupRecipient()) {
            OutgoingMediaMessage reply = new OutgoingMediaMessage(context, recipients, new PduBody(), responseText.toString(), 0);
            threadId = MessageSender.send(context, masterSecret, reply, -1, false);
          } else {
            OutgoingTextMessage reply = new OutgoingTextMessage(recipients, responseText.toString());
            threadId = MessageSender.send(context, masterSecret, reply, -1, false);
          }

          DatabaseFactory.getThreadDatabase(context).setRead(threadId);
          MessageNotifier.updateNotification(context, masterSecret);

          return null;
        }
      }.execute();
    }

  }
}
