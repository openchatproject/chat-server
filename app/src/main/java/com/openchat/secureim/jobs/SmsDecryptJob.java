package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.AsymmetricMasterCipher;
import com.openchat.secureim.crypto.AsymmetricMasterSecret;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.sms.IncomingEncryptedMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.jobqueue.JobParameters;
import com.openchat.protocal.InvalidMessageException;
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
  public void onAdded() {}

  @Override
  public void onRun(MasterSecret masterSecret) throws NoSuchMessageException {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);

    try {
      SmsMessageRecord    record    = database.getMessage(masterSecret, messageId);
      IncomingTextMessage message   = createIncomingTextMessage(masterSecret, record);
      long                messageId = record.getId();

      if (message.isSecureMessage()) {
        database.markAsLegacyVersion(messageId);
      } else {
        database.updateMessageBody(masterSecret, messageId, message.getMessageBody());
      }

      MessageNotifier.updateNotification(context, masterSecret);
    } catch (InvalidMessageException e) {
      Log.w(TAG, e);
      database.markAsDecryptFailed(messageId);
    }
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    return false;
  }

  @Override
  public void onCanceled() {
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
    IncomingTextMessage message = new IncomingTextMessage(record.getRecipients().getPrimaryRecipient().getNumber(),
                                                          record.getRecipientDeviceId(),
                                                          record.getDateSent(),
                                                          record.getBody().getBody(),
                                                          Optional.<OpenchatServiceGroup>absent());

    if (record.isAsymmetricEncryption()) {
      String plaintextBody = getAsymmetricDecryptedBody(masterSecret, record.getBody().getBody());
      return new IncomingTextMessage(message, plaintextBody);
    } else {
      return new IncomingEncryptedMessage(message, message.getMessageBody());
    }
  }
}
