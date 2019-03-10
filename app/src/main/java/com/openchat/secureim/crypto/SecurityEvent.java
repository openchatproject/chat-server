package com.openchat.secureim.crypto;

import android.content.Context;
import android.content.Intent;

import com.openchat.secureim.crypto.storage.OpenchatServiceIdentityKeyStore;
import com.openchat.secureim.crypto.storage.OpenchatServicePreKeyStore;
import com.openchat.secureim.crypto.storage.OpenchatServiceSessionStore;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.sms.OutgoingKeyExchangeMessage;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.SessionBuilder;
import com.openchat.protocal.StaleKeyExchangeException;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.protocol.KeyExchangeMessage;
import com.openchat.protocal.state.SignedPreKeyStore;
import com.openchat.protocal.state.IdentityKeyStore;
import com.openchat.protocal.state.PreKeyBundle;
import com.openchat.protocal.state.PreKeyStore;
import com.openchat.protocal.state.SessionStore;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.util.Base64;

public class SecurityEvent {

  public static final String SECURITY_UPDATE_EVENT = "com.openchat.secureim.KEY_EXCHANGE_UPDATE";

  public static void broadcastSecurityUpdateEvent(Context context, long threadId) {
    Intent intent = new Intent(SECURITY_UPDATE_EVENT);
    intent.putExtra("thread_id", threadId);
    intent.setPackage(context.getPackageName());
    context.sendBroadcast(intent, KeyCachingService.KEY_PERMISSION);
  }

}
