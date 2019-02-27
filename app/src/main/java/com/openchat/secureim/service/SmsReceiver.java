package com.openchat.secureim.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.crypto.DecryptingQueue;
import com.openchat.secureim.crypto.KeyExchangeProcessor;
import com.openchat.secureim.crypto.KeyExchangeProcessorV2;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.crypto.protocol.KeyExchangeMessage;
import com.openchat.imservice.crypto.LegacyMessageException;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.protocol.WirePrefix;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.sms.IncomingEncryptedMessage;
import com.openchat.secureim.sms.IncomingKeyExchangeMessage;
import com.openchat.secureim.sms.IncomingPreKeyBundleMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.sms.MultipartSmsMessageHandler;
import com.openchat.secureim.sms.SmsTransportDetails;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.imservice.crypto.InvalidKeyException;
import com.openchat.imservice.crypto.InvalidMessageException;
import com.openchat.imservice.crypto.InvalidVersionException;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.crypto.protocol.PreKeyOpenchatMessage;
import com.openchat.imservice.crypto.protocol.OpenchatMessage;
import com.openchat.imservice.storage.InvalidKeyIdException;
import com.openchat.imservice.storage.RecipientDevice;

import java.io.IOException;
import java.util.List;

public class SmsReceiver {

  private MultipartSmsMessageHandler multipartMessageHandler = new MultipartSmsMessageHandler();

  private final Context context;

  public SmsReceiver(Context context) {
    this.context = context;
  }

  private IncomingTextMessage assembleMessageFragments(List<IncomingTextMessage> messages) {
    IncomingTextMessage message = new IncomingTextMessage(messages);

    if (WirePrefix.isEncryptedMessage(message.getMessageBody()) ||
        WirePrefix.isKeyExchange(message.getMessageBody())      ||
        WirePrefix.isPreKeyBundle(message.getMessageBody())     ||
        WirePrefix.isEndSession(message.getMessageBody()))
    {
      return multipartMessageHandler.processPotentialMultipartMessage(message);
    } else {
      return message;
    }
  }

  private Pair<Long, Long> storeSecureMessage(MasterSecret masterSecret, IncomingTextMessage message) {
    Pair<Long, Long> messageAndThreadId = DatabaseFactory.getEncryptingSmsDatabase(context)
                                                         .insertMessageInbox(masterSecret, message);

    if (masterSecret != null) {
      DecryptingQueue.scheduleDecryption(context, masterSecret, messageAndThreadId.first,
                                         messageAndThreadId.second,
                                         message.getSender(), message.getSenderDeviceId(),
                                         message.getMessageBody(), message.isSecureMessage(),
                                         message.isKeyExchange(), message.isEndSession());
    }

    return messageAndThreadId;
  }

  private Pair<Long, Long> storeStandardMessage(MasterSecret masterSecret, IncomingTextMessage message) {
    EncryptingSmsDatabase encryptingDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);
    SmsDatabase           plaintextDatabase  = DatabaseFactory.getSmsDatabase(context);

    if (masterSecret != null) {
      return encryptingDatabase.insertMessageInbox(masterSecret, message);
    } else if (MasterSecretUtil.hasAsymmericMasterSecret(context)) {
      return encryptingDatabase.insertMessageInbox(MasterSecretUtil.getAsymmetricMasterSecret(context, null), message);
    } else {
      return plaintextDatabase.insertMessageInbox(message);
    }
  }

  private Pair<Long, Long> storePreKeyOpenchatMessage(MasterSecret masterSecret,
                                                     IncomingPreKeyBundleMessage message)
  {
    Log.w("SmsReceiver", "Processing prekey message...");

    try {
      Recipient              recipient        = RecipientFactory.getRecipientsFromString(context, message.getSender(), false).getPrimaryRecipient();
      RecipientDevice        recipientDevice  = new RecipientDevice(recipient.getRecipientId(), message.getSenderDeviceId());
      KeyExchangeProcessorV2 processor        = new KeyExchangeProcessorV2(context, masterSecret, recipientDevice);
      SmsTransportDetails    transportDetails = new SmsTransportDetails();
      byte[]                 rawMessage       = transportDetails.getDecodedMessage(message.getMessageBody().getBytes());
      PreKeyOpenchatMessage   preKeyExchange   = new PreKeyOpenchatMessage(rawMessage);

      if (processor.isTrusted(preKeyExchange)) {
        processor.processKeyExchangeMessage(preKeyExchange);

        OpenchatMessage           ciphertextMessage  = preKeyExchange.getOpenchatMessage();
        String                   bundledMessageBody = new String(transportDetails.getEncodedMessage(ciphertextMessage.serialize()));
        IncomingEncryptedMessage bundledMessage     = new IncomingEncryptedMessage(message, bundledMessageBody);
        Pair<Long, Long>         messageAndThreadId = storeSecureMessage(masterSecret, bundledMessage);

        Intent intent = new Intent(KeyExchangeProcessorV2.SECURITY_UPDATE_EVENT);
        intent.putExtra("thread_id", messageAndThreadId.second);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent, KeyCachingService.KEY_PERMISSION);

        return messageAndThreadId;
      }
    } catch (InvalidKeyException e) {
      Log.w("SmsReceiver", e);
      message.setCorrupted(true);
    } catch (InvalidVersionException e) {
      Log.w("SmsReceiver", e);
      message.setInvalidVersion(true);
    } catch (InvalidKeyIdException e) {
      Log.w("SmsReceiver", e);
      message.setStale(true);
    } catch (IOException e) {
      Log.w("SmsReceive", e);
      message.setCorrupted(true);
    } catch (InvalidMessageException e) {
      Log.w("SmsReceiver", e);
      message.setCorrupted(true);
    } catch (RecipientFormattingException e) {
      Log.w("SmsReceiver", e);
      message.setCorrupted(true);
    }

    return storeStandardMessage(masterSecret, message);
  }

  private Pair<Long, Long> storeKeyExchangeMessage(MasterSecret masterSecret,
                                                   IncomingKeyExchangeMessage message)
  {
    if (masterSecret != null && OpenchatServicePreferences.isAutoRespondKeyExchangeEnabled(context)) {
      try {
        Recipient            recipient       = RecipientFactory.getRecipientsFromString(context, message.getSender(), false).getPrimaryRecipient();
        RecipientDevice      recipientDevice = new RecipientDevice(recipient.getRecipientId(), message.getSenderDeviceId());
        KeyExchangeMessage   exchangeMessage = KeyExchangeMessage.createFor(message.getMessageBody());
        KeyExchangeProcessor processor       = KeyExchangeProcessor.createFor(context, masterSecret, recipientDevice, exchangeMessage);

        if (processor.isStale(exchangeMessage)) {
          message.setStale(true);
        } else if (processor.isTrusted(exchangeMessage)) {
          message.setProcessed(true);

          Pair<Long, Long> messageAndThreadId = storeStandardMessage(masterSecret, message);
          processor.processKeyExchangeMessage(exchangeMessage, messageAndThreadId.second);

          return messageAndThreadId;
        }
      } catch (InvalidVersionException e) {
        Log.w("SmsReceiver", e);
        message.setInvalidVersion(true);
      } catch (InvalidKeyException e) {
        Log.w("SmsReceiver", e);
        message.setCorrupted(true);
      } catch (InvalidMessageException e) {
        Log.w("SmsReceiver", e);
        message.setCorrupted(true);
      } catch (RecipientFormattingException e) {
        Log.w("SmsReceiver", e);
        message.setCorrupted(true);
      } catch (LegacyMessageException e) {
        Log.w("SmsReceiver", e);
        message.setLegacyVersion(true);
      }
    }

    return storeStandardMessage(masterSecret, message);
  }

  private Pair<Long, Long> storeMessage(MasterSecret masterSecret, IncomingTextMessage message) {
    if      (message.isSecureMessage()) return storeSecureMessage(masterSecret, message);
    else if (message.isPreKeyBundle())  return storePreKeyOpenchatMessage(masterSecret, (IncomingPreKeyBundleMessage) message);
    else if (message.isKeyExchange())   return storeKeyExchangeMessage(masterSecret, (IncomingKeyExchangeMessage) message);
    else if (message.isEndSession())    return storeSecureMessage(masterSecret, message);
    else                                return storeStandardMessage(masterSecret, message);
  }

  private void handleReceiveMessage(MasterSecret masterSecret, Intent intent) {
    if (intent.getExtras() == null) return;

    List<IncomingTextMessage> messagesList = intent.getExtras().getParcelableArrayList("text_messages");
    IncomingTextMessage       message      = assembleMessageFragments(messagesList);

    if (message != null) {
      Pair<Long, Long> messageAndThreadId = storeMessage(masterSecret, message);
      MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
    }
  }

  public void process(MasterSecret masterSecret, Intent intent) {
    if (SendReceiveService.RECEIVE_SMS_ACTION.equals(intent.getAction())) {
      handleReceiveMessage(masterSecret, intent);
    }
  }
}
