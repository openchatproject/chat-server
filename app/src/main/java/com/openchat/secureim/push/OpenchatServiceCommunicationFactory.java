package com.openchat.secureim.push;

import android.content.Context;

import com.openchat.secureim.BuildConfig;
import com.openchat.secureim.crypto.SecurityEvent;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.OpenchatServiceAccountManager;
import com.openchat.imservice.api.OpenchatServiceMessageReceiver;
import com.openchat.imservice.api.OpenchatServiceMessageSender;

import static com.openchat.imservice.api.OpenchatServiceMessageSender.EventListener;

public class OpenchatServiceCommunicationFactory {

  public static OpenchatServiceAccountManager createManager(Context context) {
    return new OpenchatServiceAccountManager(BuildConfig.PUSH_URL,
                                        new OpenchatServicePushTrustStore(context),
                                        OpenchatServicePreferences.getLocalNumber(context),
                                        OpenchatServicePreferences.getPushServerPassword(context));
  }

  public static OpenchatServiceAccountManager createManager(Context context, String number, String password) {
    return new OpenchatServiceAccountManager(BuildConfig.PUSH_URL, new OpenchatServicePushTrustStore(context),
                                        number, password);
  }

}
