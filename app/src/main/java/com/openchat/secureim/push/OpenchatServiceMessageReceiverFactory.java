package com.openchat.secureim.push;

import android.content.Context;

import com.openchat.secureim.Release;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.api.OpenchatServiceMessageReceiver;

public class OpenchatServiceMessageReceiverFactory {
  public static OpenchatServiceMessageReceiver create(Context context, MasterSecret masterSecret) {
    return new OpenchatServiceMessageReceiver(context,
                                         OpenchatServicePreferences.getOpenchatingKey(context),
                                         Release.PUSH_URL,
                                         new OpenchatServicePushTrustStore(context),
                                         OpenchatServicePreferences.getLocalNumber(context),
                                         OpenchatServicePreferences.getPushServerPassword(context),
                                         new OpenchatServiceOpenchatStore(context, masterSecret));
  }
}
