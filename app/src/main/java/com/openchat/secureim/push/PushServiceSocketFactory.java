package com.openchat.secureim.push;

import android.content.Context;

import com.openchat.secureim.Release;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.push.PushServiceSocket;

public class PushServiceSocketFactory {

  public static PushServiceSocket create(Context context, String number, String password) {
    return new PushServiceSocket(context, Release.PUSH_URL, new OpenchatServicePushTrustStore(context),
                                 number, password);
  }

  public static PushServiceSocket create(Context context) {
    return create(context,
                  OpenchatServicePreferences.getLocalNumber(context),
                  OpenchatServicePreferences.getPushServerPassword(context));
  }

}
