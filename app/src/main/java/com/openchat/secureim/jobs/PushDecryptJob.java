package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.SecurityEvent;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.crypto.storage.OpenchatServiceSessionStore;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.database.PushDatabase;
import com.openchat.secureim.groups.GroupMessageProcessor;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.mms.IncomingMediaMessage;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.sms.IncomingEncryptedMessage;
import com.openchat.secureim.sms.IncomingEndSessionMessage;
import com.openchat.secureim.sms.IncomingPreKeyBundleMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.util.Base64;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.protocal.OpenchatAddress;
import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.protocol.PreKeyOpenchatMessage;
import com.openchat.protocal.state.OpenchatStore;
import com.openchat.protocal.state.SessionStore;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.crypto.OpenchatServiceCipher;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;

import ws.com.google.android.mms.MmsException;

public class PushDecryptJob extends MasterSecretJob {

  public static final String TAG = PushDecryptJob.class.getSimpleName();

  private final long messageId;
  private final long smsMessageId;

  public PushDecryptJob(Context context, long pushMessageId, String sender) {
    this(context, pushMessageId, -1, sender);
  }

  public PushDecryptJob(Context context, long pushMessageId, long smsMessageId, String sender) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withRequirement(new MasterSecretRequirement(context))
                                .withGroupId(sender)
                                .create());
    this.messageId    = pushMessageId;
    this.smsMessageId = smsMessageId;
  }

  @Override
  public void onAdded() {
    if (KeyCachingService.getMasterSecret(context) == null) {
      MessageNotifier.updateNotification(context, null, -2);
    }
  }

  @Override
  public void onRun(MasterSecret masterSecret) throws NoSuchMessageException {
    PushDatabase       database = DatabaseFactory.getPushDatabase(context);
    OpenchatServiceEnvelope envelope = database.get(messageId);

    handleMessage(masterSecret, envelope, smsMessageId);
    database.delete(messageId);
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    return false;
  }

  @Override
  public void onCanceled() {

  }

  private void handleMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, long smsMessageId) {
    try {
      OpenchatStore     axolotlStore = new OpenchatServiceOpenchatStore(context, masterSecret);
      OpenchatServiceCipher cipher       = new OpenchatServiceCipher(axolotlStore);

      OpenchatServiceMessage message = cipher.decrypt(envelope);

      if      (message.isEndSession())               handleEndSessionMessage(masterSecret, envelope, message, smsMessageId);
      else if (message.isGroupUpdate())              handleGroupMessage(masterSecret, envelope, message, smsMessageId);
      else if (message.getAttachments().isPresent()) handleMediaMessage(masterSecret, envelope, message, smsMessageId);
      else                                           handleTextMessage(masterSecret, envelope, message, smsMessageId);

      if (envelope.isPreKeyOpenchatMessage()) {
        ApplicationContext.getInstance(context).getJobManager().add(new RefreshPreKeysJob(context));
      }
    } catch (InvalidVersionException e) {
      Log.w(TAG, e);
      handleInvalidVersionMessage(masterSecret, envelope, smsMessageId);
    } catch (InvalidMessageException | InvalidKeyIdException | InvalidKeyException | MmsException e) {
      Log.w(TAG, e);
      handleCorruptMessage(masterSecret, envelope, smsMessageId);
    } catch (NoSessionException e) {
      Log.w(TAG, e);
      handleNoSessionMessage(masterSecret, envelope, smsMessageId);
    } catch (LegacyMessageException e) {
      Log.w(TAG, e);
      handleLegacyMessage(masterSecret, envelope, smsMessageId);
    } catch (DuplicateMessageException e) {
      Log.w(TAG, e);
      handleDuplicateMessage(masterSecret, envelope, smsMessageId);
    } catch (UntrustedIdentityException e) {
      Log.w(TAG, e);
      handleUntrustedIdentityMessage(masterSecret, envelope, smsMessageId);
    }
  }

  private void handleEndSessionMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope,
                                       OpenchatServiceMessage message, long smsMessageId)
  {
    EncryptingSmsDatabase smsDatabase         = DatabaseFactory.getEncryptingSmsDatabase(context);
    IncomingTextMessage   incomingTextMessage = new IncomingTextMessage(envelope.getSource(),
                                                                        envelope.getSourceDevice(),
                                                                        message.getTimestamp(),
                                                                        "", Optional.<OpenchatServiceGroup>absent());

    long threadId;

    if (smsMessageId <= 0) {
      IncomingEndSessionMessage incomingEndSessionMessage = new IncomingEndSessionMessage(incomingTextMessage);
      Pair<Long, Long>          messageAndThreadId        = smsDatabase.insertMessageInbox(masterSecret, incomingEndSessionMessage);
      threadId = messageAndThreadId.second;
    } else {
      smsDatabase.markAsEndSession(smsMessageId);
      threadId = smsDatabase.getThreadIdForMessage(smsMessageId);
    }

    SessionStore sessionStore = new OpenchatServiceSessionStore(context, masterSecret);
    sessionStore.deleteAllSessions(envelope.getSource());

    SecurityEvent.broadcastSecurityUpdateEvent(context, threadId);
    MessageNotifier.updateNotification(context, masterSecret, threadId);
  }

  private void handleGroupMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, OpenchatServiceMessage message, long smsMessageId) {
    GroupMessageProcessor.process(context, masterSecret, envelope, message);

    if (smsMessageId > 0) {
      DatabaseFactory.getSmsDatabase(context).deleteMessage(smsMessageId);
    }
  }

  private void handleMediaMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, OpenchatServiceMessage message, long smsMessageId)
      throws MmsException
  {
    String               localNumber  = OpenchatServicePreferences.getLocalNumber(context);
    MmsDatabase          database     = DatabaseFactory.getMmsDatabase(context);
    IncomingMediaMessage mediaMessage = new IncomingMediaMessage(masterSecret, envelope.getSource(),
                                                                 localNumber, message.getTimestamp(),
                                                                 Optional.fromNullable(envelope.getRelay()),
                                                                 message.getBody(),
                                                                 message.getGroupInfo(),
                                                                 message.getAttachments());

    Pair<Long, Long> messageAndThreadId;

    if (message.isSecure()) {
      messageAndThreadId = database.insertSecureDecryptedMessageInbox(masterSecret, mediaMessage, -1);
    } else {
      messageAndThreadId = database.insertMessageInbox(masterSecret, mediaMessage, null, -1);
    }

    ApplicationContext.getInstance(context)
                      .getJobManager()
                      .add(new AttachmentDownloadJob(context, messageAndThreadId.first));

    if (smsMessageId >= 0) {
      DatabaseFactory.getSmsDatabase(context).deleteMessage(smsMessageId);
    }

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleTextMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope,
                                 OpenchatServiceMessage message, long smsMessageId)
  {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);
    String                body     = message.getBody().isPresent() ? message.getBody().get() : "";

    if (smsMessageId > 0) {
      database.updateBundleMessageBody(masterSecret, smsMessageId, body);
    } else {
      IncomingTextMessage textMessage = new IncomingTextMessage(envelope.getSource(),
                                                                envelope.getSourceDevice(),
                                                                message.getTimestamp(), body,
                                                                message.getGroupInfo());

      if (message.isSecure()) {
        textMessage = new IncomingEncryptedMessage(textMessage, body);
      }

      Pair<Long, Long> messageAndThreadId = database.insertMessageInbox(masterSecret, textMessage);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    }
  }

  private void handleInvalidVersionMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, long smsMessageId) {
    EncryptingSmsDatabase smsDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (smsMessageId <= 0) {
      Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, envelope);
      smsDatabase.markAsInvalidVersionKeyExchange(messageAndThreadId.first);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    } else {
      smsDatabase.markAsInvalidVersionKeyExchange(smsMessageId);
    }
  }

  private void handleCorruptMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, long smsMessageId) {
    EncryptingSmsDatabase smsDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (smsMessageId <= 0) {
      Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, envelope);
      smsDatabase.markAsDecryptFailed(messageAndThreadId.first);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    } else {
      smsDatabase.markAsDecryptFailed(smsMessageId);
    }
  }

  private void handleNoSessionMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, long smsMessageId) {
    EncryptingSmsDatabase smsDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (smsMessageId <= 0) {
      Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, envelope);
      smsDatabase.markAsNoSession(messageAndThreadId.first);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    } else {
      smsDatabase.markAsNoSession(smsMessageId);
    }
  }

  private void handleLegacyMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, long smsMessageId) {
    EncryptingSmsDatabase smsDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (smsMessageId <= 0) {
      Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, envelope);
      smsDatabase.markAsLegacyVersion(messageAndThreadId.first);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    } else {
      smsDatabase.markAsLegacyVersion(smsMessageId);
    }
  }

  private void handleDuplicateMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, long smsMessageId) {
  }

  private void handleUntrustedIdentityMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, long smsMessageId) {
    try {
      EncryptingSmsDatabase database       = DatabaseFactory.getEncryptingSmsDatabase(context);
      Recipients            recipients     = RecipientFactory.getRecipientsFromString(context, envelope.getSource(), false);
      long                  recipientId    = recipients.getPrimaryRecipient().getRecipientId();
      PreKeyOpenchatMessage  openchatMessage = new PreKeyOpenchatMessage(envelope.getMessage());
      IdentityKey           identityKey    = openchatMessage.getIdentityKey();
      String                encoded        = Base64.encodeBytes(envelope.getMessage());
      IncomingTextMessage   textMessage    = new IncomingTextMessage(envelope.getSource(), envelope.getSourceDevice(),
                                                                     envelope.getTimestamp(), encoded,
                                                                     Optional.<OpenchatServiceGroup>absent());

      if (smsMessageId <= 0) {
        IncomingPreKeyBundleMessage bundleMessage      = new IncomingPreKeyBundleMessage(textMessage, encoded);
        Pair<Long, Long>            messageAndThreadId = database.insertMessageInbox(masterSecret, bundleMessage);

        database.addMismatchedIdentity(messageAndThreadId.first, recipientId, identityKey);
        MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
      } else {
        database.updateMessageBody(masterSecret, smsMessageId, encoded);
        database.markAsPreKeyBundle(smsMessageId);
        database.addMismatchedIdentity(smsMessageId, recipientId, identityKey);
      }
    } catch (InvalidMessageException | InvalidVersionException e) {
      throw new AssertionError(e);
    }
  }

  private Pair<Long, Long> insertPlaceholder(MasterSecret masterSecret, OpenchatServiceEnvelope envelope) {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);

    IncomingTextMessage textMessage = new IncomingTextMessage(envelope.getSource(), envelope.getSourceDevice(),
                                                              envelope.getTimestamp(), "",
                                                              Optional.<OpenchatServiceGroup>absent());

    textMessage = new IncomingEncryptedMessage(textMessage, "");

    return database.insertMessageInbox(masterSecret, textMessage);
  }
}
