package com.openchat.secureim.transport;

import android.app.PendingIntent;
import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.SmsCipher;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.sms.MultipartSmsMessageHandler;
import com.openchat.secureim.sms.OutgoingTextMessage;
import com.openchat.secureim.util.NumberUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.protocal.NoSessionException;

import java.util.ArrayList;

public class SmsTransport extends BaseTransport {

  private final Context context;
  private final MasterSecret masterSecret;

  public SmsTransport(Context context, MasterSecret masterSecret) {
    this.context      = context.getApplicationContext();
    this.masterSecret = masterSecret;
  }

  public void deliver(SmsMessageRecord message) throws UndeliverableMessageException,
                                                       InsecureFallbackApprovalException
  {
    if (!NumberUtil.isValidSmsOrEmail(message.getIndividualRecipient().getNumber())) {
      throw new UndeliverableMessageException("Not a valid SMS destination! " + message.getIndividualRecipient().getNumber());
    }

    if (message.isSecure() || message.isKeyExchange() || message.isEndSession()) {
      deliverSecureMessage(message);
    } else {
      deliverPlaintextMessage(message);
    }
  }

  private void deliverSecureMessage(SmsMessageRecord message) throws UndeliverableMessageException,
                                                                     InsecureFallbackApprovalException
  {
    MultipartSmsMessageHandler multipartMessageHandler = new MultipartSmsMessageHandler();
    OutgoingTextMessage transportMessage               = OutgoingTextMessage.from(message);

    if (message.isSecure() || message.isEndSession()) {
      transportMessage = getAsymmetricEncrypt(masterSecret, transportMessage);
    }

    ArrayList<String> messages                = multipartMessageHandler.divideMessage(transportMessage);
    ArrayList<PendingIntent> sentIntents      = constructSentIntents(message.getId(), message.getType(), messages, message.isSecure());
    ArrayList<PendingIntent> deliveredIntents = constructDeliveredIntents(message.getId(), message.getType(), messages);

    Log.w("SmsTransport", "Secure divide into message parts: " + messages.size());

    for (int i=0;i<messages.size();i++) {
      try {
        SmsManager.getDefault().sendTextMessage(message.getIndividualRecipient().getNumber(), null, messages.get(i),
                                                sentIntents.get(i),
                                                deliveredIntents == null ? null : deliveredIntents.get(i));
      } catch (NullPointerException npe) {
        Log.w("SmsTransport", npe);
        Log.w("SmsTransport", "Recipient: " + message.getIndividualRecipient().getNumber());
        Log.w("SmsTransport", "Message Total Parts/Current: " + messages.size() + "/" + i);
        Log.w("SmsTransport", "Message Part Length: " + messages.get(i).getBytes().length);
        throw new UndeliverableMessageException(npe);
      } catch (IllegalArgumentException iae) {
        Log.w("SmsTransport", iae);
        throw new UndeliverableMessageException(iae);
      }
    }

  }

  private void deliverPlaintextMessage(SmsMessageRecord message)
      throws UndeliverableMessageException
  {
    ArrayList<String> messages                = SmsManager.getDefault().divideMessage(message.getBody().getBody());
    ArrayList<PendingIntent> sentIntents      = constructSentIntents(message.getId(), message.getType(), messages, false);
    ArrayList<PendingIntent> deliveredIntents = constructDeliveredIntents(message.getId(), message.getType(), messages);
    String recipient                          = message.getIndividualRecipient().getNumber();

    try {
      SmsManager.getDefault().sendMultipartTextMessage(recipient, null, messages, sentIntents, deliveredIntents);
    } catch (NullPointerException npe) {
      Log.w("SmsTransport", npe);
      Log.w("SmsTransport", "Recipient: " + recipient);
      Log.w("SmsTransport", "Message Parts: " + messages.size());

      try {
        for (int i=0;i<messages.size();i++) {
          SmsManager.getDefault().sendTextMessage(recipient, null, messages.get(i),
                                                  sentIntents.get(i),
                                                  deliveredIntents == null ? null : deliveredIntents.get(i));
        }
      } catch (NullPointerException npe2) {
        Log.w("SmsTransport", npe);
        throw new UndeliverableMessageException(npe2);
      }
    }
  }

  private ArrayList<PendingIntent> constructSentIntents(long messageId, long type,
                                                        ArrayList<String> messages, boolean secure)
  {
    ArrayList<PendingIntent> sentIntents = new ArrayList<>(messages.size());

    for (String ignored : messages) {
      sentIntents.add(PendingIntent.getBroadcast(context, 0,
                                                 constructSentIntent(context, messageId, type, secure, false),
                                                 0));
    }

    return sentIntents;
  }

  private ArrayList<PendingIntent> constructDeliveredIntents(long messageId, long type, ArrayList<String> messages) {
    if (!OpenchatServicePreferences.isSmsDeliveryReportsEnabled(context)) {
      return null;
    }

    ArrayList<PendingIntent> deliveredIntents = new ArrayList<>(messages.size());

    for (String ignored : messages) {
      deliveredIntents.add(PendingIntent.getBroadcast(context, 0,
                                                      constructDeliveredIntent(context, messageId, type),
                                                      0));
    }

    return deliveredIntents;
  }

  private OutgoingTextMessage getAsymmetricEncrypt(MasterSecret masterSecret,
                                                   OutgoingTextMessage message)
      throws InsecureFallbackApprovalException
  {
    try {
      return new SmsCipher(new OpenchatServiceOpenchatStore(context, masterSecret)).encrypt(message);
    } catch (NoSessionException e) {
      throw new InsecureFallbackApprovalException(e);
    }
  }
}
