package com.openchat.secureim.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.mms.IncomingMediaMessage;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.push.IncomingPushMessage;
import com.openchat.imservice.push.PushServiceSocket;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ws.com.google.android.mms.MmsException;
import ws.com.google.android.mms.pdu.GenericPdu;
import ws.com.google.android.mms.pdu.NotificationInd;
import ws.com.google.android.mms.pdu.PduHeaders;
import ws.com.google.android.mms.pdu.PduParser;

public class MmsReceiver {

  private final Context context;

  public MmsReceiver(Context context) {
    this.context = context;
  }

  public void process(MasterSecret masterSecret, Intent intent) {
    if (intent.getAction().equals(SendReceiveService.RECEIVE_MMS_ACTION)) {
      handleMmsNotification(intent);
    }
  }

  private void handleMmsNotification(Intent intent) {
    byte[] mmsData   = intent.getByteArrayExtra("data");
    PduParser parser = new PduParser(mmsData);
    GenericPdu pdu   = parser.parse();

    if (pdu != null && pdu.getMessageType() == PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND) {
      MmsDatabase database                = DatabaseFactory.getMmsDatabase(context);
      Pair<Long, Long> messageAndThreadId = database.insertMessageInbox((NotificationInd)pdu);

      Log.w("MmsReceiver", "Inserted received MMS notification...");
      scheduleDownload((NotificationInd)pdu, messageAndThreadId.first, messageAndThreadId.second);
    }
  }

  private void scheduleDownload(NotificationInd pdu, long messageId, long threadId) {
    Intent intent = new Intent(SendReceiveService.DOWNLOAD_MMS_ACTION, null, context, SendReceiveService.class);
    intent.putExtra("content_location", new String(pdu.getContentLocation()));
    intent.putExtra("message_id", messageId);
    intent.putExtra("transaction_id", pdu.getTransactionId());
    intent.putExtra("thread_id", threadId);
    intent.putExtra("automatic", true);

    context.startService(intent);
  }

}
