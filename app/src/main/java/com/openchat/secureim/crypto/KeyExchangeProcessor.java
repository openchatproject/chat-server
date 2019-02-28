package com.openchat.secureim.crypto;

import android.content.Context;
import android.content.Intent;

import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.service.PreKeyService;
import com.openchat.secureim.sms.OutgoingKeyExchangeMessage;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.SessionBuilder;
import com.openchat.protocal.StaleKeyExchangeException;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.protocol.KeyExchangeMessage;
import com.openchat.protocal.protocol.PreKeyOpenchatMessage;
import com.openchat.protocal.state.DeviceKeyStore;
import com.openchat.protocal.state.IdentityKeyStore;
import com.openchat.protocal.state.PreKeyBundle;
import com.openchat.protocal.state.PreKeyStore;
import com.openchat.protocal.state.SessionStore;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.push.PreKeyEntity;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.storage.OpenchatServicePreKeyStore;
import com.openchat.imservice.storage.OpenchatServiceSessionStore;
import com.openchat.imservice.util.Base64;

public class KeyExchangeProcessor {

  public static final String SECURITY_UPDATE_EVENT = "com.openchat.secureim.KEY_EXCHANGE_UPDATE";

  private Context         context;
  private RecipientDevice recipientDevice;
  private MasterSecret    masterSecret;
  private SessionBuilder  sessionBuilder;

  public KeyExchangeProcessor(Context context, MasterSecret masterSecret, RecipientDevice recipientDevice)
  {
    this.context         = context;
    this.recipientDevice = recipientDevice;
    this.masterSecret    = masterSecret;

    IdentityKeyStore identityKeyStore = new OpenchatServiceIdentityKeyStore(context, masterSecret);
    PreKeyStore      preKeyStore      = new OpenchatServicePreKeyStore(context, masterSecret);
    DeviceKeyStore   deviceKeyStore   = new OpenchatServicePreKeyStore(context, masterSecret);
    SessionStore     sessionStore     = new OpenchatServiceSessionStore(context, masterSecret);

    this.sessionBuilder = new SessionBuilder(sessionStore, preKeyStore, deviceKeyStore,
                                             identityKeyStore, recipientDevice.getRecipientId(),
                                             recipientDevice.getDeviceId());
  }

  public void processKeyExchangeMessage(PreKeyOpenchatMessage message)
      throws InvalidKeyIdException, InvalidKeyException, UntrustedIdentityException
  {
    sessionBuilder.process(message);
    PreKeyService.initiateRefresh(context, masterSecret);
  }

  public void processKeyExchangeMessage(PreKeyBundle bundle, long threadId)
      throws InvalidKeyException, UntrustedIdentityException
  {
    sessionBuilder.process(bundle);

    if (threadId != -1) {
      broadcastSecurityUpdateEvent(context, threadId);
    }
  }

  public OutgoingKeyExchangeMessage processKeyExchangeMessage(KeyExchangeMessage message, long threadId)
      throws InvalidKeyException, UntrustedIdentityException, StaleKeyExchangeException
  {
    KeyExchangeMessage responseMessage = sessionBuilder.process(message);
    Recipient          recipient       = RecipientFactory.getRecipientsForIds(context,
                                                                              String.valueOf(recipientDevice.getRecipientId()),
                                                                              false)
                                                         .getPrimaryRecipient();

    DecryptingQueue.scheduleRogueMessages(context, masterSecret, recipient);

    broadcastSecurityUpdateEvent(context, threadId);

    if (responseMessage != null) {
      String serializedResponse = Base64.encodeBytesWithoutPadding(responseMessage.serialize());
      return new OutgoingKeyExchangeMessage(recipient, serializedResponse);
    } else {
      return null;
    }
  }

  public static void broadcastSecurityUpdateEvent(Context context, long threadId) {
    Intent intent = new Intent(SECURITY_UPDATE_EVENT);
    intent.putExtra("thread_id", threadId);
    intent.setPackage(context.getPackageName());
    context.sendBroadcast(intent, KeyCachingService.KEY_PERMISSION);
  }

}
