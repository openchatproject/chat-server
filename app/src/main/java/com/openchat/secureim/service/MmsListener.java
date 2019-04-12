package com.openchat.secureim.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.jobs.MmsReceiveJob;
import com.openchat.secureim.protocol.WirePrefix;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.secureim.util.Util;

import ws.com.google.android.mms.pdu.GenericPdu;
import ws.com.google.android.mms.pdu.NotificationInd;
import ws.com.google.android.mms.pdu.PduHeaders;
import ws.com.google.android.mms.pdu.PduParser;

public class MmsListener extends BroadcastReceiver {

  private boolean isRelevant(Context context, Intent intent) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.DONUT) {
      return false;
    }

    if (!ApplicationMigrationService.isDatabaseImported(context)) {
      return false;
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
        Telephony.Sms.Intents.WAP_PUSH_RECEIVED_ACTION.equals(intent.getAction()) &&
        Util.isDefaultSmsProvider(context))
    {
      return false;
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT &&
        OpenchatServicePreferences.isInterceptAllMmsEnabled(context))
    {
      return true;
    }

    byte[] mmsData   = intent.getByteArrayExtra("data");
    PduParser parser = new PduParser(mmsData);
    GenericPdu pdu   = parser.parse();

    if (pdu.getMessageType() != PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND)
      return false;

    NotificationInd notificationPdu = (NotificationInd)pdu;

    if (notificationPdu.getSubject() == null)
      return false;

    return WirePrefix.isEncryptedMmsSubject(notificationPdu.getSubject().getString());
  }

  @Override
    public void onReceive(Context context, Intent intent) {
    Log.w("MmsListener", "Got MMS broadcast..." + intent.getAction());

    if ((Telephony.Sms.Intents.WAP_PUSH_DELIVER_ACTION.equals(intent.getAction())  &&
        Util.isDefaultSmsProvider(context))                                        ||
        (Telephony.Sms.Intents.WAP_PUSH_RECEIVED_ACTION.equals(intent.getAction()) &&
         isRelevant(context, intent)))
    {
      Log.w("MmsListener", "Relevant!");
      ApplicationContext.getInstance(context)
                        .getJobManager()
                        .add(new MmsReceiveJob(context, intent.getByteArrayExtra("data")));

      abortBroadcast();
    }
  }

}
