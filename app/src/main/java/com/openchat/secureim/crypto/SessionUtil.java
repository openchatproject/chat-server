package com.openchat.secureim.crypto;

import android.content.Context;
import android.support.annotation.NonNull;

import com.openchat.secureim.crypto.storage.OpenchatServiceSessionStore;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.protocal.OpenchatAddress;
import com.openchat.protocal.state.SessionStore;
import com.openchat.imservice.api.push.OpenchatServiceAddress;

public class SessionUtil {

  public static boolean hasSession(Context context, MasterSecret masterSecret, Recipient recipient) {
    return hasSession(context, masterSecret, recipient.getNumber());
  }

  public static boolean hasSession(Context context, MasterSecret masterSecret, @NonNull String number) {
    SessionStore   sessionStore   = new OpenchatServiceSessionStore(context, masterSecret);
    OpenchatAddress axolotlAddress = new OpenchatAddress(number, OpenchatServiceAddress.DEFAULT_DEVICE_ID);

    return sessionStore.containsSession(axolotlAddress);
  }
}
