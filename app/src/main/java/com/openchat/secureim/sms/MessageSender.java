package com.openchat.secureim.sms;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUnion;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.RecipientDatabase;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.database.ThreadDatabase;
import com.openchat.secureim.database.model.MessageRecord;
import com.openchat.secureim.jobs.MmsSendJob;
import com.openchat.secureim.jobs.PushGroupSendJob;
import com.openchat.secureim.jobs.PushMediaSendJob;
import com.openchat.secureim.jobs.PushTextSendJob;
import com.openchat.secureim.jobs.SmsSendJob;
import com.openchat.secureim.mms.MmsException;
import com.openchat.secureim.mms.OutgoingMediaMessage;
import com.openchat.secureim.push.AccountManagerFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.service.ExpiringMessageManager;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.secureim.util.Util;
import com.openchat.jobqueue.JobManager;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.openchatServiceAccountManager;
import com.openchat.imservice.api.push.ContactTokenDetails;

import java.io.IOException;

public class MessageSender {

  private static final String TAG = MessageSender.class.getSimpleName();

  public static long send(final Context context,
                          final MasterSecret masterSecret,
                          final OutgoingTextMessage message,
                          final long threadId,
                          final boolean forceSms,
                          final SmsDatabase.InsertListener insertListener)
  {
    EncryptingSmsDatabase database    = DatabaseFactory.getEncryptingSmsDatabase(context);
    Recipient             recipient   = message.getRecipient();
    boolean               keyExchange = message.isKeyExchange();

    long allocatedThreadId;

    if (threadId == -1) {
      allocatedThreadId = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipient);
    } else {
      allocatedThreadId = threadId;
    }

    long messageId = database.insertMessageOutbox(new MasterSecretUnion(masterSecret), allocatedThreadId,
                                                  message, forceSms, System.currentTimeMillis(), insertListener);

    sendTextMessage(context, recipient, forceSms, keyExchange, messageId, message.getExpiresIn());

    return allocatedThreadId;
  }

  public static long send(final Context context,
                          final MasterSecret masterSecret,
                          final OutgoingMediaMessage message,
                          final long threadId,
                          final boolean forceSms,
                          final SmsDatabase.InsertListener insertListener)
  {
    try {
      ThreadDatabase threadDatabase = DatabaseFactory.getThreadDatabase(context);
      MmsDatabase    database       = DatabaseFactory.getMmsDatabase(context);

      long allocatedThreadId;

      if (threadId == -1) {
        allocatedThreadId = threadDatabase.getThreadIdFor(message.getRecipient(), message.getDistributionType());
      } else {
        allocatedThreadId = threadId;
      }

      Recipient recipient = message.getRecipient();
      long      messageId = database.insertMessageOutbox(new MasterSecretUnion(masterSecret), message, allocatedThreadId, forceSms, insertListener);

      sendMediaMessage(context, masterSecret, recipient, forceSms, messageId, message.getExpiresIn());

      return allocatedThreadId;
    } catch (MmsException e) {
      Log.w(TAG, e);
      return threadId;
    }
  }

  public static void resendGroupMessage(Context context, MessageRecord messageRecord, Address filterAddress) {
    if (!messageRecord.isMms()) throw new AssertionError("Not Group");
    sendGroupPush(context, messageRecord.getRecipient(), messageRecord.getId(), filterAddress);
  }

  public static void resend(Context context, MasterSecret masterSecret, MessageRecord messageRecord) {
    try {
      long       messageId   = messageRecord.getId();
      boolean    forceSms    = messageRecord.isForcedSms();
      boolean    keyExchange = messageRecord.isKeyExchange();
      long       expiresIn   = messageRecord.getExpiresIn();
      Recipient  recipient   = messageRecord.getRecipient();

      if (messageRecord.isMms()) {
        sendMediaMessage(context, masterSecret, recipient, forceSms, messageId, expiresIn);
      } else {
        sendTextMessage(context, recipient, forceSms, keyExchange, messageId, expiresIn);
      }
    } catch (MmsException e) {
      Log.w(TAG, e);
    }
  }

  private static void sendMediaMessage(Context context, MasterSecret masterSecret,
                                       Recipient recipient, boolean forceSms,
                                       long messageId, long expiresIn)
      throws MmsException
  {
    if (!forceSms && isSelfSend(context, recipient)) {
      sendMediaSelf(context, masterSecret, messageId, expiresIn);
    } else if (isGroupPushSend(recipient)) {
      sendGroupPush(context, recipient, messageId, null);
    } else if (!forceSms && isPushMediaSend(context, recipient)) {
      sendMediaPush(context, recipient, messageId);
    } else {
      sendMms(context, messageId);
    }
  }

  private static void sendTextMessage(Context context, Recipient recipient,
                                      boolean forceSms, boolean keyExchange,
                                      long messageId, long expiresIn)
  {
    if (!forceSms && isSelfSend(context, recipient)) {
      sendTextSelf(context, messageId, expiresIn);
    } else if (!forceSms && isPushTextSend(context, recipient, keyExchange)) {
      sendTextPush(context, recipient, messageId);
    } else {
      sendSms(context, recipient, messageId);
    }
  }

  private static void sendTextSelf(Context context, long messageId, long expiresIn) {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);

    database.markAsSent(messageId, true);

    Pair<Long, Long> messageAndThreadId = database.copyMessageInbox(messageId);
    database.markAsPush(messageAndThreadId.first);

    if (expiresIn > 0) {
      ExpiringMessageManager expiringMessageManager = ApplicationContext.getInstance(context).getExpiringMessageManager();

      database.markExpireStarted(messageId);
      expiringMessageManager.scheduleDeletion(messageId, false, expiresIn);
    }
  }

  private static void sendMediaSelf(Context context, MasterSecret masterSecret,
                                    long messageId, long expiresIn)
      throws MmsException
  {
    ExpiringMessageManager expiringMessageManager = ApplicationContext.getInstance(context).getExpiringMessageManager();
    MmsDatabase            database               = DatabaseFactory.getMmsDatabase(context);

    database.markAsSent(messageId, true);
    database.copyMessageInbox(masterSecret, messageId);

    if (expiresIn > 0) {
      database.markExpireStarted(messageId);
      expiringMessageManager.scheduleDeletion(messageId, true, expiresIn);
    }
  }

  private static void sendTextPush(Context context, Recipient recipient, long messageId) {
    JobManager jobManager = ApplicationContext.getInstance(context).getJobManager();
    jobManager.add(new PushTextSendJob(context, messageId, recipient.getAddress()));
  }

  private static void sendMediaPush(Context context, Recipient recipient, long messageId) {
    JobManager jobManager = ApplicationContext.getInstance(context).getJobManager();
    jobManager.add(new PushMediaSendJob(context, messageId, recipient.getAddress()));
  }

  private static void sendGroupPush(Context context, Recipient recipient, long messageId, Address filterAddress) {
    JobManager jobManager = ApplicationContext.getInstance(context).getJobManager();
    jobManager.add(new PushGroupSendJob(context, messageId, recipient.getAddress(), filterAddress));
  }

  private static void sendSms(Context context, Recipient recipient, long messageId) {
    JobManager jobManager = ApplicationContext.getInstance(context).getJobManager();
    jobManager.add(new SmsSendJob(context, messageId, recipient.getName()));
  }

  private static void sendMms(Context context, long messageId) {
    JobManager jobManager = ApplicationContext.getInstance(context).getJobManager();
    jobManager.add(new MmsSendJob(context, messageId));
  }

  private static boolean isPushTextSend(Context context, Recipient recipient, boolean keyExchange) {
    if (!TextSecurePreferences.isPushRegistered(context)) {
      return false;
    }

    if (keyExchange) {
      return false;
    }

    return isPushDestination(context, recipient);
  }

  private static boolean isPushMediaSend(Context context, Recipient recipient) {
    if (!TextSecurePreferences.isPushRegistered(context)) {
      return false;
    }

    if (recipient.isGroupRecipient()) {
      return false;
    }

    return isPushDestination(context, recipient);
  }

  private static boolean isGroupPushSend(Recipient recipient) {
    return recipient.getAddress().isGroup() &&
           !recipient.getAddress().isMmsGroup();
  }

  private static boolean isSelfSend(Context context, Recipient recipient) {
    if (!TextSecurePreferences.isPushRegistered(context)) {
      return false;
    }

    if (recipient.isGroupRecipient()) {
      return false;
    }

    return Util.isOwnNumber(context, recipient.getAddress());
  }

  private static boolean isPushDestination(Context context, Recipient destination) {
    if (destination.resolve().getRegistered() == RecipientDatabase.RegisteredState.REGISTERED) {
      return true;
    } else if (destination.resolve().getRegistered() == RecipientDatabase.RegisteredState.NOT_REGISTERED) {
      return false;
    } else {
      try {
        openchatServiceAccountManager   accountManager = AccountManagerFactory.createManager(context);
        Optional<ContactTokenDetails> registeredUser = accountManager.getContact(destination.getAddress().serialize());

        if (!registeredUser.isPresent()) {
          DatabaseFactory.getRecipientDatabase(context).setRegistered(destination, RecipientDatabase.RegisteredState.NOT_REGISTERED);
          return false;
        } else {
          DatabaseFactory.getRecipientDatabase(context).setRegistered(destination, RecipientDatabase.RegisteredState.REGISTERED);
          return true;
        }
      } catch (IOException e1) {
        Log.w(TAG, e1);
        return false;
      }
    }
  }

}