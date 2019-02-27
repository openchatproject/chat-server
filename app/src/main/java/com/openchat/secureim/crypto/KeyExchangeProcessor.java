package com.openchat.secureim.crypto;

import android.content.Context;
import android.content.Intent;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.service.PreKeyService;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingKeyExchangeMessage;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.SessionBuilder;
import com.openchat.protocal.protocol.KeyExchangeMessage;
import com.openchat.protocal.protocol.PreKeyOpenchatMessage;
import com.openchat.protocal.state.IdentityKeyStore;
import com.openchat.protocal.state.PreKeyStore;
import com.openchat.protocal.state.SessionRecord;
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
  private SessionStore    sessionStore;

  public KeyExchangeProcessor(Context context, MasterSecret masterSecret, RecipientDevice recipientDevice)
  {
    this.context         = context;
    this.recipientDevice = recipientDevice;
    this.masterSecret    = masterSecret;

    IdentityKeyStore identityKeyStore = new OpenchatServiceIdentityKeyStore(context, masterSecret);
    PreKeyStore      preKeyStore      = new OpenchatServicePreKeyStore(context, masterSecret);

    this.sessionStore   = new OpenchatServiceSessionStore(context, masterSecret);
    this.sessionBuilder = new SessionBuilder(sessionStore, preKeyStore, identityKeyStore,
                                             recipientDevice.getRecipientId(),
                                             recipientDevice.getDeviceId());
  }

  public boolean isTrusted(PreKeyOpenchatMessage message) {
    return isTrusted(message.getIdentityKey());
  }

  public boolean isTrusted(PreKeyEntity entity) {
    return isTrusted(entity.getIdentityKey());
  }

  public boolean isTrusted(KeyExchangeMessage message) {
    return message.hasIdentityKey() && isTrusted(message.getIdentityKey());
  }

  public boolean isTrusted(IdentityKey identityKey) {
    return DatabaseFactory.getIdentityDatabase(context).isValidIdentity(masterSecret,
                                                                        recipientDevice.getRecipientId(),
                                                                        identityKey);
  }

  public boolean isStale(KeyExchangeMessage message) {
    SessionRecord sessionRecord = sessionStore.load(recipientDevice.getRecipientId(),
                                                    recipientDevice.getDeviceId());

    return
        message.isResponse() &&
            (!sessionRecord.getSessionState().hasPendingKeyExchange() ||
              sessionRecord.getSessionState().getPendingKeyExchangeSequence() != message.getSequence()) &&
        !message.isResponseForSimultaneousInitiate();
  }

  public void processKeyExchangeMessage(PreKeyOpenchatMessage message)
      throws InvalidKeyIdException, InvalidKeyException
  {
    sessionBuilder.process(message);
    PreKeyService.initiateRefresh(context, masterSecret);
  }

  public void processKeyExchangeMessage(PreKeyEntity message, long threadId)
      throws InvalidKeyException
  {
    sessionBuilder.process(message);

    if (threadId != -1) {
      broadcastSecurityUpdateEvent(context, threadId);
    }
  }

  public void processKeyExchangeMessage(KeyExchangeMessage message, long threadId)
      throws InvalidKeyException
  {
    KeyExchangeMessage responseMessage = sessionBuilder.process(message);
    Recipient            recipient     = RecipientFactory.getRecipientsForIds(context,
                                                                              String.valueOf(recipientDevice.getRecipientId()),
                                                                              false)
                                                         .getPrimaryRecipient();

    if (responseMessage != null) {
      String                     serializedResponse = Base64.encodeBytesWithoutPadding(responseMessage.serialize());
      OutgoingKeyExchangeMessage textMessage        = new OutgoingKeyExchangeMessage(recipient, serializedResponse);
      MessageSender.send(context, masterSecret, textMessage, threadId, true);
    }

    DecryptingQueue.scheduleRogueMessages(context, masterSecret, recipient);

    broadcastSecurityUpdateEvent(context, threadId);
  }

  public static void broadcastSecurityUpdateEvent(Context context, long threadId) {
    Intent intent = new Intent(SECURITY_UPDATE_EVENT);
    intent.putExtra("thread_id", threadId);
    intent.setPackage(context.getPackageName());
    context.sendBroadcast(intent, KeyCachingService.KEY_PERMISSION);
  }

}
