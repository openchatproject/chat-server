package com.openchat.secureim.jobs;

import android.content.Context;
import android.telephony.SmsMessage;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUnion;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.protocol.WirePrefix;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.sms.IncomingEncryptedMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.jobqueue.JobParameters;
import com.openchat.protocal.util.guava.Optional;

import java.util.LinkedList;
import java.util.List;

public class SmsReceiveJob extends ContextJob {

  private static final String TAG = SmsReceiveJob.class.getSimpleName();

  private final Object[] pdus;

  public SmsReceiveJob(Context context, Object[] pdus) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withWakeLock(true)
                                .create());

    this.pdus = pdus;
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() {
    Optional<IncomingTextMessage> message      = assembleMessageFragments(pdus);
    MasterSecret                  masterSecret = KeyCachingService.getMasterSecret(context);

    MasterSecretUnion masterSecretUnion;

    if (masterSecret == null) {
      masterSecretUnion = new MasterSecretUnion(MasterSecretUtil.getAsymmetricMasterSecret(context, null));
    } else {
      masterSecretUnion = new MasterSecretUnion(masterSecret);
    }

    if (message.isPresent() && !isBlocked(message.get())) {
      Pair<Long, Long> messageAndThreadId = storeMessage(masterSecretUnion, message.get());
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    } else if (message.isPresent()) {
      Log.w(TAG, "*** Received blocked SMS, ignoring...");
    }
  }

  @Override
  public void onCanceled() {

  }

  @Override
  public boolean onShouldRetry(Exception exception) {
    return false;
  }

  private boolean isBlocked(IncomingTextMessage message) {
    if (message.getSender() != null) {
      Recipients recipients = RecipientFactory.getRecipientsFromString(context, message.getSender(), false);
      return recipients.isBlocked();
    }

    return false;
  }

  private Pair<Long, Long> storeMessage(MasterSecretUnion masterSecret, IncomingTextMessage message) {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);

    Pair<Long, Long> messageAndThreadId;

    if (message.isSecureMessage()) {
      IncomingTextMessage placeholder = new IncomingTextMessage(message, "");
      messageAndThreadId = database.insertMessageInbox(placeholder);
      database.markAsLegacyVersion(messageAndThreadId.first);
    } else {
      messageAndThreadId = database.insertMessageInbox(masterSecret, message);
    }

    return messageAndThreadId;
  }

  private Optional<IncomingTextMessage> assembleMessageFragments(Object[] pdus) {
    List<IncomingTextMessage> messages = new LinkedList<>();

    for (Object pdu : pdus) {
      messages.add(new IncomingTextMessage(SmsMessage.createFromPdu((byte[])pdu)));
    }

    if (messages.isEmpty()) {
      return Optional.absent();
    }

    IncomingTextMessage message =  new IncomingTextMessage(messages);

    if (WirePrefix.isEncryptedMessage(message.getMessageBody()) ||
        WirePrefix.isKeyExchange(message.getMessageBody())      ||
        WirePrefix.isPreKeyBundle(message.getMessageBody())     ||
        WirePrefix.isEndSession(message.getMessageBody()))
    {
      return Optional.<IncomingTextMessage>of(new IncomingEncryptedMessage(message, message.getMessageBody()));
    } else {
      return Optional.of(message);
    }
  }
}
