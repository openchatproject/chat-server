package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.AsymmetricMasterCipher;
import com.openchat.secureim.crypto.AsymmetricMasterSecret;
import com.openchat.secureim.crypto.SecurityEvent;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.crypto.SmsCipher;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.sms.IncomingEncryptedMessage;
import com.openchat.secureim.sms.IncomingEndSessionMessage;
import com.openchat.secureim.sms.IncomingKeyExchangeMessage;
import com.openchat.secureim.sms.IncomingPreKeyBundleMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingKeyExchangeMessage;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.StaleKeyExchangeException;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;

import java.io.IOException;

public class SmsDecryptJob extends MasterSecretJob {

  private static final String TAG = SmsDecryptJob.class.getSimpleName();

  private final long messageId;

  public SmsDecryptJob(Context context, long messageId) {
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
  public void onRun() throws RequirementNotMetException, NoSuchMessageException {
    MasterSecret          masterSecret = getMasterSecret();
    EncryptingSmsDatabase database     = DatabaseFactory.getEncryptingSmsDatabase(context);

    try {
      SmsMessageRecord    record    = database.getMessage(masterSecret, messageId);
      IncomingTextMessage message   = createIncomingTextMessage(masterSecret, record);
      long                messageId = record.getId();
      long                threadId  = record.getThreadId();

      if      (message.isSecureMessage()) handleSecureMessage(masterSecret, messageId, message);
      else if (message.isPreKeyBundle())  handlePreKeyOpenchatMessage(masterSecret, messageId, threadId, (IncomingPreKeyBundleMessage) message);
      else if (message.isKeyExchange())   handleKeyExchangeMessage(masterSecret, messageId, threadId, (IncomingKeyExchangeMessage) message);
      else if (message.isEndSession())    handleSecureMessage(masterSecret, messageId, message);
      else                                database.updateMessageBody(masterSecret, messageId, message.getMessageBody());

      MessageNotifier.updateNotification(context, masterSecret);
    } catch (LegacyMessageException e) {
      Log.w(TAG, e);
      database.markAsLegacyVersion(messageId);
    } catch (InvalidMessageException e) {
      Log.w(TAG, e);
      database.markAsDecryptFailed(messageId);
    } catch (DuplicateMessageException e) {
      Log.w(TAG, e);
      database.markAsDecryptDuplicate(messageId);
    } catch (NoSessionException e) {
      Log.w(TAG, e);
      database.markAsNoSession(messageId);
    }
  }

  @Override
  public void onCanceled() {
  }

  private void handleSecureMessage(MasterSecret masterSecret, long messageId, IncomingTextMessage message)
      throws NoSessionException, DuplicateMessageException,
      InvalidMessageException, LegacyMessageException
  {
    EncryptingSmsDatabase database  = DatabaseFactory.getEncryptingSmsDatabase(context);
    SmsCipher             cipher    = new SmsCipher(new OpenchatServiceOpenchatStore(context, masterSecret));
    IncomingTextMessage   plaintext = cipher.decrypt(context, message);

    database.updateMessageBody(masterSecret, messageId, plaintext.getMessageBody());
  }

  private void handlePreKeyOpenchatMessage(MasterSecret masterSecret, long messageId, long threadId,
                                          IncomingPreKeyBundleMessage message)
      throws NoSessionException, DuplicateMessageException,
      InvalidMessageException, LegacyMessageException
  {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);

    try {
      SmsCipher                smsCipher = new SmsCipher(new OpenchatServiceOpenchatStore(context, masterSecret));
      IncomingEncryptedMessage plaintext = smsCipher.decrypt(context, message);

      database.updateBundleMessageBody(masterSecret, messageId, plaintext.getMessageBody());

      SecurityEvent.broadcastSecurityUpdateEvent(context, threadId);
    } catch (InvalidVersionException e) {
      Log.w(TAG, e);
      database.markAsInvalidVersionKeyExchange(messageId);
    } catch (UntrustedIdentityException e) {
      Log.w(TAG, e);
    }
  }

  private void handleKeyExchangeMessage(MasterSecret masterSecret, long messageId, long threadId,
                                        IncomingKeyExchangeMessage message)
  {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (OpenchatServicePreferences.isAutoRespondKeyExchangeEnabled(context)) {
      try {
        SmsCipher                  cipher   = new SmsCipher(new OpenchatServiceOpenchatStore(context, masterSecret));
        OutgoingKeyExchangeMessage response = cipher.process(context, message);

        database.markAsProcessedKeyExchange(messageId);

        if (response != null) {
          MessageSender.send(context, masterSecret, response, threadId, true);
        }
      } catch (InvalidVersionException e) {
        Log.w(TAG, e);
        database.markAsInvalidVersionKeyExchange(messageId);
      } catch (InvalidMessageException e) {
        Log.w(TAG, e);
        database.markAsCorruptKeyExchange(messageId);
      } catch (LegacyMessageException e) {
        Log.w(TAG, e);
        database.markAsLegacyVersion(messageId);
      } catch (StaleKeyExchangeException e) {
        Log.w(TAG, e);
        database.markAsStaleKeyExchange(messageId);
      } catch (UntrustedIdentityException e) {
        Log.w(TAG, e);
      }
    }
  }

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    if (throwable instanceof RequirementNotMetException) return true;
    return false;
  }

  private String getAsymmetricDecryptedBody(MasterSecret masterSecret, String body)
      throws InvalidMessageException
  {
    try {
      AsymmetricMasterSecret asymmetricMasterSecret = MasterSecretUtil.getAsymmetricMasterSecret(context, masterSecret);
      AsymmetricMasterCipher asymmetricMasterCipher = new AsymmetricMasterCipher(asymmetricMasterSecret);

      return asymmetricMasterCipher.decryptBody(body);
    } catch (IOException e) {
      throw new InvalidMessageException(e);
    }
  }

  private IncomingTextMessage createIncomingTextMessage(MasterSecret masterSecret, SmsMessageRecord record)
      throws InvalidMessageException
  {
    String plaintextBody = record.getBody().getBody();

    if (record.isAsymmetricEncryption()) {
      plaintextBody = getAsymmetricDecryptedBody(masterSecret, record.getBody().getBody());
    }

    IncomingTextMessage message = new IncomingTextMessage(record.getRecipients().getPrimaryRecipient().getNumber(),
                                                          record.getRecipientDeviceId(),
                                                          record.getDateSent(),
                                                          plaintextBody,
                                                          Optional.<OpenchatServiceGroup>absent());

    if (record.isEndSession()) {
      return new IncomingEndSessionMessage(message);
    } else if (record.isBundleKeyExchange()) {
      return new IncomingPreKeyBundleMessage(message, message.getMessageBody());
    } else if (record.isKeyExchange()) {
      return new IncomingKeyExchangeMessage(message, message.getMessageBody());
    } else if (record.isSecure()) {
      return new IncomingEncryptedMessage(message, message.getMessageBody());
    }

    return message;
  }
}
