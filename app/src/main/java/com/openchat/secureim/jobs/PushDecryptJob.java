package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.SecurityEvent;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceSessionStore;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.PushDatabase;
import com.openchat.secureim.groups.GroupMessageProcessor;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.mms.IncomingMediaMessage;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.push.OpenchatServiceMessageReceiverFactory;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.sms.IncomingEncryptedMessage;
import com.openchat.secureim.sms.IncomingEndSessionMessage;
import com.openchat.secureim.sms.IncomingPreKeyBundleMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.state.SessionStore;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.OpenchatServiceMessageReceiver;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;
import com.openchat.imservice.push.IncomingPushMessage;
import com.openchat.imservice.util.Base64;

import ws.com.google.android.mms.MmsException;

public class PushDecryptJob extends MasterSecretJob {

  public static final String TAG = PushDecryptJob.class.getSimpleName();

  private final long messageId;

  public PushDecryptJob(Context context, long messageId) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withRequirement(new MasterSecretRequirement(context))
                                .create());
    this.messageId = messageId;
  }

  @Override
  public void onAdded() {
    if (KeyCachingService.getMasterSecret(context) == null) {
      MessageNotifier.updateNotification(context, null);
    }
  }

  @Override
  public void onRun() throws RequirementNotMetException {
    try {
      MasterSecret        masterSecret = getMasterSecret();
      PushDatabase        database     = DatabaseFactory.getPushDatabase(context);
      IncomingPushMessage push         = database.get(messageId);

      handleMessage(masterSecret, push);
      database.delete(messageId);

    } catch (PushDatabase.NoSuchMessageException e) {
      Log.w(TAG, e);
    }
  }

  @Override
  public void onCanceled() {

  }

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    if (throwable instanceof RequirementNotMetException) return true;
    return false;
  }

  private void handleMessage(MasterSecret masterSecret, IncomingPushMessage push) {
    try {
      Recipients                recipients      = RecipientFactory.getRecipientsFromMessage(context, push, false);
      long                      recipientId     = recipients.getPrimaryRecipient().getRecipientId();
      OpenchatServiceMessageReceiver messageReceiver = OpenchatServiceMessageReceiverFactory.create(context, masterSecret);

      OpenchatServiceMessage message = messageReceiver.receiveMessage(recipientId, push);

      if      (message.isEndSession())               handleEndSessionMessage(masterSecret, recipientId, push, message);
      else if (message.isGroupUpdate())              handleGroupMessage(masterSecret, push, message);
      else if (message.getAttachments().isPresent()) handleMediaMessage(masterSecret, push, message);
      else                                           handleTextMessage(masterSecret, push, message);
    } catch (InvalidVersionException e) {
      Log.w(TAG, e);
      handleInvalidVersionMessage(masterSecret, push);
    } catch (InvalidMessageException | InvalidKeyIdException | InvalidKeyException | MmsException e) {
      Log.w(TAG, e);
      handleCorruptMessage(masterSecret, push);
    } catch (NoSessionException e) {
      Log.w(TAG, e);
      handleNoSessionMessage(masterSecret, push);
    } catch (LegacyMessageException e) {
      Log.w(TAG, e);
      handleLegacyMessage(masterSecret, push);
    } catch (DuplicateMessageException e) {
      Log.w(TAG, e);
      handleDuplicateMessage(masterSecret, push);
    } catch (UntrustedIdentityException e) {
      Log.w(TAG, e);
      handleUntrustedIdentityMessage(masterSecret, push);
    }
  }

  private void handleEndSessionMessage(MasterSecret masterSecret, long recipientId, IncomingPushMessage push, OpenchatServiceMessage message) {
    IncomingTextMessage incomingTextMessage = new IncomingTextMessage(push.getSource(),
                                                                      push.getSourceDevice(),
                                                                      message.getTimestamp(),
                                                                      "", Optional.<OpenchatServiceGroup>absent());

    IncomingEndSessionMessage incomingEndSessionMessage = new IncomingEndSessionMessage(incomingTextMessage);
    EncryptingSmsDatabase     database                  = DatabaseFactory.getEncryptingSmsDatabase(context);
    Pair<Long, Long>          messageAndThreadId        = database.insertMessageInbox(masterSecret, incomingEndSessionMessage);

    SessionStore sessionStore = new OpenchatServiceSessionStore(context, masterSecret);
    sessionStore.deleteAllSessions(recipientId);

    SecurityEvent.broadcastSecurityUpdateEvent(context, messageAndThreadId.second);
    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleGroupMessage(MasterSecret masterSecret, IncomingPushMessage push, OpenchatServiceMessage message) {
    GroupMessageProcessor.process(context, masterSecret, push, message);
  }

  private void handleMediaMessage(MasterSecret masterSecret, IncomingPushMessage openchat, OpenchatServiceMessage message)
      throws MmsException
  {
    String               localNumber  = OpenchatServicePreferences.getLocalNumber(context);
    MmsDatabase          database     = DatabaseFactory.getMmsDatabase(context);
    IncomingMediaMessage mediaMessage = new IncomingMediaMessage(masterSecret, openchat.getSource(),
                                                                 localNumber, message.getTimestamp(),
                                                                 Optional.fromNullable(openchat.getRelay()),
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

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleTextMessage(MasterSecret masterSecret, IncomingPushMessage openchat, OpenchatServiceMessage message) {
    EncryptingSmsDatabase database    = DatabaseFactory.getEncryptingSmsDatabase(context);
    String                body        = message.getBody().isPresent() ? message.getBody().get() : "";
    IncomingTextMessage   textMessage = new IncomingTextMessage(openchat.getSource(),
                                                                openchat.getSourceDevice(),
                                                                message.getTimestamp(), body,
                                                                message.getGroupInfo());

    if (message.isSecure()) {
      textMessage = new IncomingEncryptedMessage(textMessage, body);
    }

    Pair<Long, Long> messageAndThreadId = database.insertMessageInbox(masterSecret, textMessage);

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleInvalidVersionMessage(MasterSecret masterSecret, IncomingPushMessage push) {
    Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, push);
    DatabaseFactory.getEncryptingSmsDatabase(context).markAsInvalidVersionKeyExchange(messageAndThreadId.first);

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleCorruptMessage(MasterSecret masterSecret, IncomingPushMessage push) {
    Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, push);
    DatabaseFactory.getEncryptingSmsDatabase(context).markAsDecryptFailed(messageAndThreadId.first);

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleNoSessionMessage(MasterSecret masterSecret, IncomingPushMessage push) {
    Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, push);
    DatabaseFactory.getEncryptingSmsDatabase(context).markAsNoSession(messageAndThreadId.first);

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleLegacyMessage(MasterSecret masterSecret, IncomingPushMessage push) {
    Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, push);
    DatabaseFactory.getEncryptingSmsDatabase(context).markAsLegacyVersion(messageAndThreadId.first);

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleDuplicateMessage(MasterSecret masterSecret, IncomingPushMessage push) {
    Pair<Long, Long> messageAndThreadId = insertPlaceholder(masterSecret, push);
    DatabaseFactory.getEncryptingSmsDatabase(context).markAsDecryptDuplicate(messageAndThreadId.first);

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private void handleUntrustedIdentityMessage(MasterSecret masterSecret, IncomingPushMessage push) {
    String              encoded     = Base64.encodeBytes(push.getBody());
    IncomingTextMessage textMessage = new IncomingTextMessage(push.getSource(), push.getSourceDevice(),
                                                              push.getTimestampMillis(), encoded,
                                                              Optional.<OpenchatServiceGroup>absent());

    IncomingPreKeyBundleMessage bundleMessage      = new IncomingPreKeyBundleMessage(textMessage, encoded);
    Pair<Long, Long>            messageAndThreadId = DatabaseFactory.getEncryptingSmsDatabase(context)
                                                                    .insertMessageInbox(masterSecret, bundleMessage);

    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private Pair<Long, Long> insertPlaceholder(MasterSecret masterSecret, IncomingPushMessage push) {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);

    IncomingTextMessage textMessage = new IncomingTextMessage(push.getSource(), push.getSourceDevice(),
                                                              push.getTimestampMillis(), "",
                                                              Optional.<OpenchatServiceGroup>absent());

    textMessage = new IncomingEncryptedMessage(textMessage, "");

    return database.insertMessageInbox(masterSecret, textMessage);
  }
}
