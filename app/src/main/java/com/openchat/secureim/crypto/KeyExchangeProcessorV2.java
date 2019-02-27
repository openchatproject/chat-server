package com.openchat.secureim.crypto;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.protocol.KeyExchangeMessage;
import com.openchat.secureim.crypto.protocol.KeyExchangeMessageV2;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.service.PreKeyService;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingKeyExchangeMessage;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.crypto.IdentityKey;
import com.openchat.imservice.crypto.IdentityKeyPair;
import com.openchat.imservice.crypto.InvalidKeyException;
import com.openchat.imservice.crypto.InvalidMessageException;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.crypto.ecc.Curve;
import com.openchat.imservice.crypto.ecc.ECKeyPair;
import com.openchat.imservice.crypto.ecc.ECPublicKey;
import com.openchat.imservice.crypto.protocol.PreKeyOpenchatMessage;
import com.openchat.imservice.crypto.ratchet.RatchetingSession;
import com.openchat.imservice.push.PreKeyEntity;
import com.openchat.imservice.storage.InvalidKeyIdException;
import com.openchat.imservice.storage.PreKeyRecord;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.storage.Session;
import com.openchat.imservice.storage.SessionRecordV2;
import com.openchat.imservice.util.Medium;

public class KeyExchangeProcessorV2 extends KeyExchangeProcessor {

  private Context         context;
  private RecipientDevice recipientDevice;
  private MasterSecret    masterSecret;
  private SessionRecordV2 sessionRecord;

  public KeyExchangeProcessorV2(Context context, MasterSecret masterSecret, RecipientDevice recipientDevice)
  {
    this.context         = context;
    this.recipientDevice = recipientDevice;
    this.masterSecret    = masterSecret;
    this.sessionRecord   = new SessionRecordV2(context, masterSecret, recipientDevice);
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

  public boolean isStale(KeyExchangeMessage m) {
    KeyExchangeMessageV2 message = (KeyExchangeMessageV2)m;
    return
        message.isResponse() &&
            (!sessionRecord.getSessionState().hasPendingKeyExchange() ||
              sessionRecord.getSessionState().getPendingKeyExchangeSequence() != message.getSequence()) &&
        !message.isResponseForSimultaneousInitiate();
  }

  public void processKeyExchangeMessage(PreKeyOpenchatMessage message)
      throws InvalidKeyIdException, InvalidKeyException
  {
    int         preKeyId          = message.getPreKeyId();
    ECPublicKey theirBaseKey      = message.getBaseKey();
    ECPublicKey theirEphemeralKey = message.getOpenchatMessage().getSenderEphemeral();
    IdentityKey theirIdentityKey  = message.getIdentityKey();

    Log.w("KeyExchangeProcessor", "Received pre-key with local key ID: " + preKeyId);

    if (!PreKeyRecord.hasRecord(context, preKeyId) && SessionRecordV2.hasSession(context, masterSecret, recipientDevice)) {
      Log.w("KeyExchangeProcessor", "We've already processed the prekey part, letting bundled message fall through...");
      return;
    }

    if (!PreKeyRecord.hasRecord(context, preKeyId))
      throw new InvalidKeyIdException("No such prekey: " + preKeyId);

    PreKeyRecord    preKeyRecord         = new PreKeyRecord(context, masterSecret, preKeyId);
    ECKeyPair       ourBaseKey           = preKeyRecord.getKeyPair();
    ECKeyPair       ourEphemeralKey      = ourBaseKey;
    IdentityKeyPair ourIdentityKey       = IdentityKeyUtil.getIdentityKeyPair(context, masterSecret);
    boolean         simultaneousInitiate = sessionRecord.getSessionState().hasPendingPreKey();

    if (!simultaneousInitiate) sessionRecord.clear();
    else                       sessionRecord.archiveCurrentState();

    RatchetingSession.initializeSession(sessionRecord.getSessionState(),
                                        ourBaseKey, theirBaseKey,
                                        ourEphemeralKey, theirEphemeralKey,
                                        ourIdentityKey, theirIdentityKey);

    Session.clearV1SessionFor(context, recipientDevice.getRecipient());
    sessionRecord.getSessionState().setLocalRegistrationId(OpenchatServicePreferences.getLocalRegistrationId(context));
    sessionRecord.getSessionState().setRemoteRegistrationId(message.getRegistrationId());

    if (simultaneousInitiate) sessionRecord.getSessionState().setNeedsRefresh(true);

    sessionRecord.save();

    if (preKeyId != Medium.MAX_VALUE) {
      PreKeyRecord.delete(context, preKeyId);
    }

    PreKeyService.initiateRefresh(context, masterSecret);

    DatabaseFactory.getIdentityDatabase(context)
                   .saveIdentity(masterSecret, recipientDevice.getRecipientId(), theirIdentityKey);
  }

  public void processKeyExchangeMessage(PreKeyEntity message, long threadId)
      throws InvalidKeyException
  {
    ECKeyPair       ourBaseKey        = Curve.generateKeyPair(true);
    ECKeyPair       ourEphemeralKey   = Curve.generateKeyPair(true);
    ECPublicKey     theirBaseKey      = message.getPublicKey();
    ECPublicKey     theirEphemeralKey = theirBaseKey;
    IdentityKey     theirIdentityKey  = message.getIdentityKey();
    IdentityKeyPair ourIdentityKey    = IdentityKeyUtil.getIdentityKeyPair(context, masterSecret);

    if (sessionRecord.getSessionState().getNeedsRefresh()) sessionRecord.archiveCurrentState();
    else                                                   sessionRecord.clear();

    RatchetingSession.initializeSession(sessionRecord.getSessionState(),
                                        ourBaseKey, theirBaseKey, ourEphemeralKey,
                                        theirEphemeralKey, ourIdentityKey, theirIdentityKey);

    sessionRecord.getSessionState().setPendingPreKey(message.getKeyId(), ourBaseKey.getPublicKey());
    sessionRecord.getSessionState().setLocalRegistrationId(OpenchatServicePreferences.getLocalRegistrationId(context));
    sessionRecord.getSessionState().setRemoteRegistrationId(message.getRegistrationId());

    sessionRecord.save();

    DatabaseFactory.getIdentityDatabase(context)
                   .saveIdentity(masterSecret, recipientDevice.getRecipientId(), message.getIdentityKey());

    if (threadId != -1) {
      broadcastSecurityUpdateEvent(context, threadId);
    }
  }

  @Override
  public void processKeyExchangeMessage(KeyExchangeMessage _message, long threadId)
      throws InvalidMessageException
  {
    try {
      KeyExchangeMessageV2 message = (KeyExchangeMessageV2)_message;
      Recipient recipient = RecipientFactory.getRecipientsForIds(context,
                                                                 String.valueOf(recipientDevice.getRecipientId()),
                                                                 false)
                                            .getPrimaryRecipient();

      Log.w("KeyExchangeProcessorV2", "Received key exchange with sequence: " + message.getSequence());

      if (message.isInitiate()) {
        ECKeyPair       ourBaseKey, ourEphemeralKey;
        IdentityKeyPair ourIdentityKey;

        int flags = KeyExchangeMessageV2.RESPONSE_FLAG;

        Log.w("KeyExchangeProcessorV2", "KeyExchange is an initiate.");

        if (!sessionRecord.getSessionState().hasPendingKeyExchange()) {
          Log.w("KeyExchangeProcessorV2", "We don't have a pending initiate...");
          ourBaseKey      = Curve.generateKeyPair(true);
          ourEphemeralKey = Curve.generateKeyPair(true);
          ourIdentityKey  = IdentityKeyUtil.getIdentityKeyPair(context, masterSecret);

          sessionRecord.getSessionState().setPendingKeyExchange(message.getSequence(), ourBaseKey,
                                                                ourEphemeralKey, ourIdentityKey);
        } else {
          Log.w("KeyExchangeProcessorV2", "We alredy have a pending initiate, responding as simultaneous initiate...");
          ourBaseKey      = sessionRecord.getSessionState().getPendingKeyExchangeBaseKey();
          ourEphemeralKey = sessionRecord.getSessionState().getPendingKeyExchangeEphemeralKey();
          ourIdentityKey  = sessionRecord.getSessionState().getPendingKeyExchangeIdentityKey();
          flags          |= KeyExchangeMessageV2.SIMULTAENOUS_INITIATE_FLAG;

          sessionRecord.getSessionState().setPendingKeyExchange(message.getSequence(), ourBaseKey,
                                                                ourEphemeralKey, ourIdentityKey);
        }

        KeyExchangeMessageV2 ourMessage = new KeyExchangeMessageV2(message.getSequence(),
                                                                   flags, ourBaseKey.getPublicKey(),
                                                                   ourEphemeralKey.getPublicKey(),
                                                                   ourIdentityKey.getPublicKey());

        OutgoingKeyExchangeMessage textMessage = new OutgoingKeyExchangeMessage(recipient,
                                                                                ourMessage.serialize());
        MessageSender.send(context, masterSecret, textMessage, threadId, false);
      }

      if (message.getSequence() != sessionRecord.getSessionState().getPendingKeyExchangeSequence()) {
        Log.w("KeyExchangeProcessorV2", "No matching sequence for response. " +
            "Is simultaneous initiate response: " + message.isResponseForSimultaneousInitiate());
        return;
      }

      ECKeyPair       ourBaseKey      = sessionRecord.getSessionState().getPendingKeyExchangeBaseKey();
      ECKeyPair       ourEphemeralKey = sessionRecord.getSessionState().getPendingKeyExchangeEphemeralKey();
      IdentityKeyPair ourIdentityKey  = sessionRecord.getSessionState().getPendingKeyExchangeIdentityKey();

      sessionRecord.clear();

      RatchetingSession.initializeSession(sessionRecord.getSessionState(),
                                          ourBaseKey, message.getBaseKey(),
                                          ourEphemeralKey, message.getEphemeralKey(),
                                          ourIdentityKey, message.getIdentityKey());

      sessionRecord.getSessionState().setSessionVersion(message.getVersion());
      Session.clearV1SessionFor(context, recipientDevice.getRecipient());
      sessionRecord.save();

      DatabaseFactory.getIdentityDatabase(context)
                     .saveIdentity(masterSecret, recipientDevice.getRecipientId(), message.getIdentityKey());

      DecryptingQueue.scheduleRogueMessages(context, masterSecret, recipient);

      broadcastSecurityUpdateEvent(context, threadId);
    } catch (InvalidKeyException e) {
      throw new InvalidMessageException(e);
    }
  }

}
