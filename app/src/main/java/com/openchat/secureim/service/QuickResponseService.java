package com.openchat.secureim.service;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.openchat.secureim.R;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingTextMessage;
import com.openchat.secureim.util.Rfc5724Uri;

import java.net.URISyntaxException;
import java.net.URLDecoder;

public class QuickResponseService extends MasterSecretIntentService {

  private static final String TAG = QuickResponseService.class.getSimpleName();

  public QuickResponseService() {
    super("QuickResponseService");
  }

  @Override
  protected void onHandleIntent(Intent intent, @Nullable MasterSecret masterSecret) {
    if (!TelephonyManager.ACTION_RESPOND_VIA_MESSAGE.equals(intent.getAction())) {
      Log.w(TAG, "Received unknown intent: " + intent.getAction());
      return;
    }

    if (masterSecret == null) {
      Log.w(TAG, "Got quick response request when locked...");
      Toast.makeText(this, R.string.QuickResponseService_quick_response_unavailable_when_openchat_is_locked, Toast.LENGTH_LONG).show();
      return;
    }

    try {
      Rfc5724Uri uri        = new Rfc5724Uri(intent.getDataString());
      String     content    = intent.getStringExtra(Intent.EXTRA_TEXT);
      String     number     = uri.getPath();

      if (number.contains("%")){
        number = URLDecoder.decode(number);
      }

      Address   address        = Address.fromExternal(this, number);
      Recipient recipient      = Recipient.from(this, address, false);
      int       subscriptionId = recipient.getDefaultSubscriptionId().or(-1);
      long      expiresIn      = recipient.getExpireMessages() * 1000;

      if (!TextUtils.isEmpty(content)) {
        MessageSender.send(this, masterSecret, new OutgoingTextMessage(recipient, content, expiresIn, subscriptionId), -1, false, null);
      }
    } catch (URISyntaxException e) {
      Toast.makeText(this, R.string.QuickResponseService_problem_sending_message, Toast.LENGTH_LONG).show();
      Log.w(TAG, e);
    }
  }
}
