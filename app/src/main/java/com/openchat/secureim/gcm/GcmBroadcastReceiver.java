package com.openchat.secureim.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.openchat.secureim.service.SendReceiveService;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.imservice.directory.Directory;
import com.openchat.imservice.directory.NotInDirectoryException;
import com.openchat.imservice.push.ContactTokenDetails;
import com.openchat.imservice.push.IncomingEncryptedPushMessage;
import com.openchat.imservice.push.IncomingPushMessage;
import com.openchat.imservice.util.Util;

import java.io.IOException;

public class GcmBroadcastReceiver extends BroadcastReceiver {

  private static final String TAG = GcmBroadcastReceiver.class.getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {
    GoogleCloudMessaging gcm         = GoogleCloudMessaging.getInstance(context);
    String               messageType = gcm.getMessageType(intent);

    if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
      Log.w(TAG, "GCM message...");

      try {
        String data = intent.getStringExtra("message");

        if (Util.isEmpty(data))
          return;

        if (!OpenchatServicePreferences.isPushRegistered(context)) {
          Log.w(TAG, "Not push registered!");
          return;
        }

        String                       sessionKey       = OpenchatServicePreferences.getOpenchatingKey(context);
        IncomingEncryptedPushMessage encryptedMessage = new IncomingEncryptedPushMessage(data, sessionKey);
        IncomingPushMessage          message          = encryptedMessage.getIncomingPushMessage();

        if (!isActiveNumber(context, message.getSource())) {
          Directory directory           = Directory.getInstance(context);
          ContactTokenDetails contactTokenDetails = new ContactTokenDetails();
          contactTokenDetails.setNumber(message.getSource());

          directory.setNumber(contactTokenDetails, true);
        }

        Intent service = new Intent(context, SendReceiveService.class);
        service.setAction(SendReceiveService.RECEIVE_PUSH_ACTION);
        service.putExtra("message", message);
        context.startService(service);
      } catch (IOException e) {
        Log.w(TAG, e);
      } catch (InvalidVersionException e) {
        Log.w(TAG, e);
      }
    }
  }

  private boolean isActiveNumber(Context context, String e164number) {
    boolean isActiveNumber;

    try {
      isActiveNumber = Directory.getInstance(context).isActiveNumber(e164number);
    } catch (NotInDirectoryException e) {
      isActiveNumber = false;
    }

    return isActiveNumber;
  }
}
