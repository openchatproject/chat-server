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
import com.openchat.secureim.mms.OutgoingMediaMessage;
import com.openchat.secureim.mms.OutgoingSecureMediaMessage;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.sms.IncomingEncryptedMessage;
import com.openchat.secureim.sms.IncomingEndSessionMessage;
import com.openchat.secureim.sms.IncomingPreKeyBundleMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.sms.OutgoingTextMessage;
import com.openchat.secureim.util.Base64;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobParameters;
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
import com.openchat.imservice.api.messages.OpenchatServiceContent;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceDataMessage;
import com.openchat.imservice.api.messages.multidevice.SentTranscriptMessage;
import com.openchat.imservice.api.messages.multidevice.OpenchatServiceSyncMessage;
import com.openchat.imservice.api.push.OpenchatServiceAddress;

import java.util.concurrent.TimeUnit;

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
                                .withWakeLock(true, 5, TimeUnit.SECONDS)
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
    PushDatabase       database             = DatabaseFactory.getPushDatabase(context);
    OpenchatServiceEnvelope envelope             = database.get(messageId);
    Optional<Long>     optionalSmsMessageId = smsMessageId > 0 ? Optional.of(smsMessageId) :
                                                                 Optional.<Long>absent();

    handleMessage(masterSecret, envelope, optionalSmsMessageId);
    database.delete(messageId);
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    return false;
  }

  @Override
  public void onCanceled() {

  }

  private void handleMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, Optional<Long> smsMessageId) {
    try {
      OpenchatStore      axolotlStore = new OpenchatServiceOpenchatStore(context, masterSecret);
      OpenchatServiceAddress localAddress = new OpenchatServiceAddress(OpenchatServicePreferences.getLocalNumber(context));
      OpenchatServiceCipher  cipher       = new OpenchatServiceCipher(localAddress, axolotlStore);

      OpenchatServiceContent content = cipher.decrypt(envelope);

      if (content.getDataMessage().isPresent()) {
        OpenchatServiceDataMessage message = content.getDataMessage().get();

        if      (message.isEndSession())               handleEndSessionMessage(masterSecret, envelope, message, smsMessageId);
        else if (message.isGroupUpdate())              handleGroupMessage(masterSecret, envelope, message, smsMessageId);
        else if (message.getAttachments().isPresent()) handleMediaMessage(masterSecret, envelope, message, smsMessageId);
        else                                           handleTextMessage(masterSecret, envelope, message, smsMessageId);
      } else if (content.getSyncMessage().isPresent()) {
        OpenchatServiceSyncMessage syncMessage = content.getSyncMessage().get();

        if (syncMessage.getSent().isPresent()) handleSynchronizeSentMessage(masterSecret, syncMessage.getSent().get(), smsMessageId);
      }

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
                                       OpenchatServiceDataMessage message, Optional<Long> smsMessageId)
  {
    EncryptingSmsDatabase smsDatabase         = DatabaseFactory.getEncryptingSmsDatabase(context);
    IncomingTextMessage   incomingTextMessage = new IncomingTextMessage(envelope.getSource(),
                                                                        envelope.getSourceDevice(),
                                                                        message.getTimestamp(),
                                                                        "", Optional.<OpenchatServiceGroup>absent());

    long threadId;

    if (!smsMessageId.isPresent()) {
      IncomingEndSessionMessage incomingEndSessionMessage = new IncomingEndSessionMessage(incomingTextMessage);
      Pair<Long, Long>          messageAndThreadId        = smsDatabase.insertMessageInbox(masterSecret, incomingEndSessionMessage);
      threadId = messageAndThreadId.second;
    } else {
      smsDatabase.markAsEndSession(smsMessageId.get());
      threadId = smsDatabase.getThreadIdForMessage(smsMessageId.get());
    }

    SessionStore sessionStore = new OpenchatServiceSessionStore(context, masterSecret);
    sessionStore.deleteAllSessions(envelope.getSource());

    SecurityEvent.broadcastSecurityUpdateEvent(context, threadId);
    MessageNotifier.updateNotification(context, masterSecret, threadId);
  }

  private void handleGroupMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, OpenchatServiceDataMessage message, Optional<Long> smsMessageId) {
    GroupMessageProcessor.process(context, masterSecret, envelope, message);

    if (smsMessageId.isPresent()) {
      DatabaseFactory.getSmsDatabase(context).deleteMessage(smsMessageId.get());
    }
  }

  private void handleSynchronizeSentMessage(MasterSecret masterSecret, SentTranscriptMessage message, Optional<Long> smsMessageId)
      throws MmsException
  {
    if (message.getMessage().getAttachments().isPresent()) {
      handleSynchronizeSentMediaMessage(masterSecret, message, smsMessageId);
    } else {
      handleSynchronizeSentTextMessage(masterSecret, message, smsMessageId);
    }
  }

  private void handleMediaMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope,
                                  OpenchatServiceDataMessage message, Optional<Long> smsMessageId)
      throws MmsException
  {
    MmsDatabase          database     = DatabaseFactory.getMmsDatabase(context);
    String               localNumber  = OpenchatServicePreferences.getLocalNumber(context);
    IncomingMediaMessage mediaMessage = new IncomingMediaMessage(masterSecret, envelope.getSource(),
                                                                 localNumber, message.getTimestamp(),
                                                                 Optional.fromNullable(envelope.getRelay()),
                                                                 message.getBody(),
                                                                 message.getGroupInfo(),
                                                                 message.getAttachments());

    Pair<Long, Long> messageAndThreadId =  database.insertSecureDecryptedMessageInbox(masterSecret, mediaMessage, -1);

    ApplicationContext.getInstance(context)
                      .getJobManager()
                      .add(new AttachmentDownloadJob(context, messageAndThreadId.first));

    if (smsMessageId.isPresent()) {
      DatabaseFactory.getSmsDatabase(context).deleteMessage(smsMessageId.get());
    }

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleSynchronizeSentMediaMessage(MasterSecret masterSecret,
                                                 SentTranscriptMessage message,
                                                 Optional<Long> smsMessageId)
      throws MmsException
  {
    MmsDatabase           database     = DatabaseFactory.getMmsDatabase(context);
    Recipients            recipients   = getSyncMessageDestination(message);
    OutgoingMediaMessage  mediaMessage = new OutgoingMediaMessage(context, masterSecret, recipients,
                                                                  message.getMessage().getAttachments().get(),
                                                                  message.getMessage().getBody().orNull());

    mediaMessage = new OutgoingSecureMediaMessage(mediaMessage);

    long threadId  = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipients);
    long messageId = database.insertMessageOutbox(masterSecret, mediaMessage, threadId, false, message.getTimestamp());

    database.markAsSent(messageId, "push".getBytes(), 0);
    database.markAsPush(messageId);

    ApplicationContext.getInstance(context)
                      .getJobManager()
                      .add(new AttachmentDownloadJob(context, messageId));

    if (smsMessageId.isPresent()) {
      DatabaseFactory.getSmsDatabase(context).deleteMessage(smsMessageId.get());
    }
  }

  private void handleTextMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope,
                                 OpenchatServiceDataMessage message, Optional<Long> smsMessageId)
  {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);
    String                body     = message.getBody().isPresent() ? message.getBody().get() : "";

    Pair<Long, Long> messageAndThreadId;

    if (smsMessageId.isPresent()) {
      messageAndThreadId = database.updateBundleMessageBody(masterSecret, smsMessageId.get(), body);
    } else {
      IncomingTextMessage textMessage = new IncomingTextMessage(envelope.getSource(),
                                                                envelope.getSourceDevice(),
                                                                message.getTimestamp(), body,
                                                                message.getGroupInfo());

      textMessage = new IncomingEncryptedMessage(textMessage, body);

      messageAndThreadId = database.insertMessageInbox(masterSecret, textMessage);
    }

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleSynchronizeSentTextMessage(MasterSecret masterSecret,
                                                SentTranscriptMessage message,
                                                Optional<Long> smsMessageId)
  {
    EncryptingSmsDatabase database            = DatabaseFactory.getEncryptingSmsDatabase(context);
    Recipients            recipients          = getSyncMessageDestination(message);
    String                body                = message.getMessage().getBody().or("");
    OutgoingTextMessage   outgoingTextMessage = new OutgoingTextMessage(recipients, body);

    long threadId  = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipients);
    long messageId = database.insertMessageOutbox(masterSecret, threadId, outgoingTextMessage, false, message.getTimestamp());

    database.markAsSent(messageId);
    database.markAsPush(messageId);
    database.markAsSecure(messageId);

    if (smsMessageId.isPresent()) {
      database.deleteMessage(smsMessageId.get());
    }
  }

  private void handleInvalidVersionMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, Optional<Long> smsMessageId) {
    EncryptingSmsDatabase smsDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (!smsMessageId.isPresent()) {
      Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, envelope);
      smsDatabase.markAsInvalidVersionKeyExchange(messageAndThreadId.first);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    } else {
      smsDatabase.markAsInvalidVersionKeyExchange(smsMessageId.get());
    }
  }

  private void handleCorruptMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, Optional<Long> smsMessageId) {
    EncryptingSmsDatabase smsDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (!smsMessageId.isPresent()) {
      Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, envelope);
      smsDatabase.markAsDecryptFailed(messageAndThreadId.first);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    } else {
      smsDatabase.markAsDecryptFailed(smsMessageId.get());
    }
  }

  private void handleNoSessionMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, Optional<Long> smsMessageId) {
    EncryptingSmsDatabase smsDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (!smsMessageId.isPresent()) {
      Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, envelope);
      smsDatabase.markAsNoSession(messageAndThreadId.first);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    } else {
      smsDatabase.markAsNoSession(smsMessageId.get());
    }
  }

  private void handleLegacyMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, Optional<Long> smsMessageId) {
    EncryptingSmsDatabase smsDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (!smsMessageId.isPresent()) {
      Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, envelope);
      smsDatabase.markAsLegacyVersion(messageAndThreadId.first);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    } else {
      smsDatabase.markAsLegacyVersion(smsMessageId.get());
    }
  }

  private void handleDuplicateMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, Optional<Long> smsMessageId) {
  }

  private void handleUntrustedIdentityMessage(MasterSecret masterSecret, OpenchatServiceEnvelope envelope, Optional<Long> smsMessageId) {
    try {
      EncryptingSmsDatabase database       = DatabaseFactory.getEncryptingSmsDatabase(context);
      Recipients            recipients     = RecipientFactory.getRecipientsFromString(context, envelope.getSource(), false);
      long                  recipientId    = recipients.getPrimaryRecipient().getRecipientId();
      PreKeyOpenchatMessage  openchatMessage = new PreKeyOpenchatMessage(envelope.getLegacyMessage());
      IdentityKey           identityKey    = openchatMessage.getIdentityKey();
      String                encoded        = Base64.encodeBytes(envelope.getLegacyMessage());
      IncomingTextMessage   textMessage    = new IncomingTextMessage(envelope.getSource(), envelope.getSourceDevice(),
                                                                     envelope.getTimestamp(), encoded,
                                                                     Optional.<OpenchatServiceGroup>absent());

      if (!smsMessageId.isPresent()) {
        IncomingPreKeyBundleMessage bundleMessage      = new IncomingPreKeyBundleMessage(textMessage, encoded);
        Pair<Long, Long>            messageAndThreadId = database.insertMessageInbox(masterSecret, bundleMessage);

        database.setMismatchedIdentity(messageAndThreadId.first, recipientId, identityKey);
        MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
      } else {
        database.updateMessageBody(masterSecret, smsMessageId.get(), encoded);
        database.markAsPreKeyBundle(smsMessageId.get());
        database.setMismatchedIdentity(smsMessageId.get(), recipientId, identityKey);
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

  private Recipients getSyncMessageDestination(SentTranscriptMessage message) {
    if (message.getMessage().getGroupInfo().isPresent()) {
      return RecipientFactory.getRecipientsFromString(context, GroupUtil.getEncodedId(message.getMessage().getGroupInfo().get().getGroupId()), false);
    } else {
      return RecipientFactory.getRecipientsFromString(context, message.getDestination().get(), false);
    }
  }
}
