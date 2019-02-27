package com.openchat.secureim.transport;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.mms.MmsSendResult;
import com.openchat.secureim.push.PushServiceSocketFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.sms.IncomingGroupMessage;
import com.openchat.secureim.sms.IncomingIdentityUpdateMessage;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.secureim.util.Util;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.directory.Directory;
import com.openchat.imservice.directory.NotInDirectoryException;
import com.openchat.imservice.push.ContactTokenDetails;
import com.openchat.imservice.push.PushServiceSocket;
import com.openchat.imservice.push.UnregisteredUserException;
import com.openchat.imservice.storage.Session;
import com.openchat.imservice.util.DirectoryUtil;
import com.openchat.imservice.util.InvalidNumberException;

import java.io.IOException;

import ws.com.google.android.mms.pdu.SendReq;

public class UniversalTransport {

  private final Context       context;
  private final MasterSecret  masterSecret;
  private final PushTransport pushTransport;
  private final SmsTransport  smsTransport;
  private final MmsTransport  mmsTransport;

  public UniversalTransport(Context context, MasterSecret masterSecret) {
    this.context       = context;
    this.masterSecret  = masterSecret;
    this.pushTransport = new PushTransport(context, masterSecret);
    this.smsTransport  = new SmsTransport(context, masterSecret);
    this.mmsTransport  = new MmsTransport(context, masterSecret);
  }

  public void deliver(SmsMessageRecord message)
      throws UndeliverableMessageException, UntrustedIdentityException, RetryLaterException,
             SecureFallbackApprovalException, InsecureFallbackApprovalException
  {
    if (message.isForcedSms()) {
      smsTransport.deliver(message);
      return;
    }

    if (!OpenchatServicePreferences.isPushRegistered(context)) {
      deliverDirectSms(message);
      return;
    }

    try {
      Recipient recipient = message.getIndividualRecipient();
      String    number    = Util.canonicalizeNumber(context, recipient.getNumber());

      if (isPushTransport(number) && !message.isKeyExchange()) {
        boolean isSmsFallbackSupported = isSmsFallbackSupported(number);

        try {
          Log.w("UniversalTransport", "Using GCM as transport...");
          pushTransport.deliver(message);
        } catch (UnregisteredUserException uue) {
          Log.w("UniversalTransport", uue);
          if (isSmsFallbackSupported) fallbackOrAskApproval(message, number);
          else                        throw new UndeliverableMessageException(uue);
        } catch (IOException ioe) {
          Log.w("UniversalTransport", ioe);
          if (isSmsFallbackSupported) fallbackOrAskApproval(message, number);
          else                        throw new RetryLaterException(ioe);
        }
      } else {
        Log.w("UniversalTransport", "Using SMS as transport...");
        deliverDirectSms(message);
      }
    } catch (InvalidNumberException e) {
      Log.w("UniversalTransport", e);
      deliverDirectSms(message);
    }
  }

  public MmsSendResult deliver(SendReq mediaMessage, long threadId)
      throws UndeliverableMessageException, RetryLaterException, UntrustedIdentityException,
             SecureFallbackApprovalException, InsecureFallbackApprovalException
  {
    if (MmsDatabase.Types.isForcedSms(mediaMessage.getDatabaseMessageBox())) {
      return mmsTransport.deliver(mediaMessage);
    }

    if (Util.isEmpty(mediaMessage.getTo())) {
      return deliverDirectMms(mediaMessage);
    }

    if (GroupUtil.isEncodedGroup(mediaMessage.getTo()[0].getString())) {
      return deliverGroupMessage(mediaMessage, threadId);
    }

    if (!OpenchatServicePreferences.isPushRegistered(context)) {
      return deliverDirectMms(mediaMessage);
    }

    if (isMultipleRecipients(mediaMessage)) {
      return deliverDirectMms(mediaMessage);
    }

    try {
      String destination = Util.canonicalizeNumber(context, mediaMessage.getTo()[0].getString());

      if (isPushTransport(destination)) {
        boolean isSmsFallbackSupported = isSmsFallbackSupported(destination);

        try {
          Log.w("UniversalTransport", "Using GCM as transport...");
          pushTransport.deliver(mediaMessage, threadId);
          return new MmsSendResult("push".getBytes("UTF-8"), 0, true, true);
        } catch (IOException ioe) {
          Log.w("UniversalTransport", ioe);
          if (isSmsFallbackSupported) return fallbackOrAskApproval(mediaMessage, destination);
          else                        throw new RetryLaterException(ioe);
        } catch (RecipientFormattingException e) {
          Log.w("UniversalTransport", e);
          if (isSmsFallbackSupported) return fallbackOrAskApproval(mediaMessage, destination);
          else                        throw new UndeliverableMessageException(e);
        } catch (EncapsulatedExceptions ee) {
          Log.w("UniversalTransport", ee);
          if (!ee.getUnregisteredUserExceptions().isEmpty()) {
            if (isSmsFallbackSupported) return mmsTransport.deliver(mediaMessage);
            else                        throw new UndeliverableMessageException(ee);
          } else {
            throw new UntrustedIdentityException(ee.getUntrustedIdentityExceptions().get(0));
          }
        }
      } else {
        Log.w("UniversalTransport", "Delivering media message with MMS...");
        return deliverDirectMms(mediaMessage);
      }
    } catch (InvalidNumberException ine) {
      Log.w("UniversalTransport", ine);
      return deliverDirectMms(mediaMessage);
    }
  }

  private MmsSendResult fallbackOrAskApproval(SendReq mediaMessage, String destination)
      throws SecureFallbackApprovalException, UndeliverableMessageException, InsecureFallbackApprovalException
  {
    try {
      Recipient recipient                     = RecipientFactory.getRecipientsFromString(context, destination, false).getPrimaryRecipient();
      boolean   isSmsFallbackApprovalRequired = isSmsFallbackApprovalRequired(destination);

      if (!isSmsFallbackApprovalRequired) {
        Log.w("UniversalTransport", "Falling back to MMS");
        DatabaseFactory.getMmsDatabase(context).markAsForcedSms(mediaMessage.getDatabaseMessageId());
        return mmsTransport.deliver(mediaMessage);
      } else if (!Session.hasEncryptCapableSession(context, masterSecret, recipient)) {
        Log.w("UniversalTransport", "Marking message as pending insecure SMS fallback");
        throw new InsecureFallbackApprovalException("Pending user approval for fallback to insecure SMS");
      } else {
        Log.w("UniversalTransport", "Marking message as pending secure SMS fallback");
        throw new SecureFallbackApprovalException("Pending user approval for fallback secure to SMS");
      }
    } catch (RecipientFormattingException rfe) {
      throw new UndeliverableMessageException(rfe);
    }
  }

  private void fallbackOrAskApproval(SmsMessageRecord smsMessage, String destination)
      throws SecureFallbackApprovalException, UndeliverableMessageException, InsecureFallbackApprovalException
  {
    Recipient recipient                     = smsMessage.getIndividualRecipient();
    boolean   isSmsFallbackApprovalRequired = isSmsFallbackApprovalRequired(destination);

    if (!isSmsFallbackApprovalRequired) {
      Log.w("UniversalTransport", "Falling back to SMS");
      DatabaseFactory.getSmsDatabase(context).markAsForcedSms(smsMessage.getId());
      smsTransport.deliver(smsMessage);
    } else if (!Session.hasEncryptCapableSession(context, masterSecret, recipient)) {
      Log.w("UniversalTransport", "Marking message as pending insecure fallback.");
      throw new InsecureFallbackApprovalException("Pending user approval for fallback to insecure SMS");
    } else {
      Log.w("UniversalTransport", "Marking message as pending secure fallback.");
      throw new SecureFallbackApprovalException("Pending user approval for fallback to secure SMS");
    }
  }

  private MmsSendResult deliverGroupMessage(SendReq mediaMessage, long threadId)
      throws RetryLaterException, UndeliverableMessageException
  {
    if (!OpenchatServicePreferences.isPushRegistered(context)) {
      throw new UndeliverableMessageException("Not push registered!");
    }

    try {
      pushTransport.deliver(mediaMessage, threadId);
      return new MmsSendResult("push".getBytes("UTF-8"), 0, true, true);
    } catch (IOException e) {
      Log.w("UniversalTransport", e);
      throw new RetryLaterException(e);
    } catch (RecipientFormattingException e) {
      throw new UndeliverableMessageException(e);
    } catch (InvalidNumberException e) {
      throw new UndeliverableMessageException(e);
    } catch (EncapsulatedExceptions ee) {
      Log.w("UniversalTransport", ee);
      try {
        for (UnregisteredUserException unregistered : ee.getUnregisteredUserExceptions()) {
          IncomingGroupMessage quitMessage = IncomingGroupMessage.createForQuit(mediaMessage.getTo()[0].getString(), unregistered.getE164Number());
          DatabaseFactory.getEncryptingSmsDatabase(context).insertMessageInbox(masterSecret, quitMessage);
          DatabaseFactory.getGroupDatabase(context).remove(GroupUtil.getDecodedId(mediaMessage.getTo()[0].getString()), unregistered.getE164Number());
        }

        for (UntrustedIdentityException untrusted : ee.getUntrustedIdentityExceptions()) {
          IncomingIdentityUpdateMessage identityMessage = IncomingIdentityUpdateMessage.createFor(untrusted.getE164Number(), untrusted.getIdentityKey(), mediaMessage.getTo()[0].getString());
          DatabaseFactory.getEncryptingSmsDatabase(context).insertMessageInbox(masterSecret, identityMessage);
        }

        return new MmsSendResult("push".getBytes("UTF-8"), 0, true, true);
      } catch (IOException ioe) {
        throw new AssertionError(ioe);
      }
    }
  }

  private void deliverDirectSms(SmsMessageRecord message)
      throws InsecureFallbackApprovalException, UndeliverableMessageException
  {
    if (OpenchatServicePreferences.isDirectSmsAllowed(context)) {
      smsTransport.deliver(message);
    } else {
      throw new UndeliverableMessageException("Direct SMS delivery is disabled!");
    }
  }

  private MmsSendResult deliverDirectMms(SendReq message)
      throws InsecureFallbackApprovalException, UndeliverableMessageException
  {
    if (OpenchatServicePreferences.isDirectSmsAllowed(context)) {
      return mmsTransport.deliver(message);
    } else {
      throw new UndeliverableMessageException("Direct MMS delivery is disabled!");
    }
  }

  public boolean isMultipleRecipients(SendReq mediaMessage) {
    int recipientCount = 0;

    if (mediaMessage.getTo() != null) {
      recipientCount += mediaMessage.getTo().length;
    }

    if (mediaMessage.getCc() != null) {
      recipientCount += mediaMessage.getCc().length;
    }

    if (mediaMessage.getBcc() != null) {
      recipientCount += mediaMessage.getBcc().length;
    }

    return recipientCount > 1;
  }

  private boolean isSmsFallbackApprovalRequired(String destination) {
    return (isSmsFallbackSupported(destination) && OpenchatServicePreferences.isFallbackSmsAskRequired(context));
  }

  private boolean isSmsFallbackSupported(String destination) {
    if (GroupUtil.isEncodedGroup(destination)) {
      return false;
    }

    if (OpenchatServicePreferences.isPushRegistered(context) &&
        !OpenchatServicePreferences.isFallbackSmsAllowed(context))
    {
      return false;
    }

    Directory directory = Directory.getInstance(context);
    return directory.isSmsFallbackSupported(destination);
  }

  private boolean isPushTransport(String destination) {
    if (GroupUtil.isEncodedGroup(destination)) {
      return true;
    }

    Directory directory = Directory.getInstance(context);

    try {
      return directory.isActiveNumber(destination);
    } catch (NotInDirectoryException e) {
      try {
        PushServiceSocket   socket         = PushServiceSocketFactory.create(context);
        String              contactToken   = DirectoryUtil.getDirectoryServerToken(destination);
        ContactTokenDetails registeredUser = socket.getContactTokenDetails(contactToken);

        if (registeredUser == null) {
          registeredUser = new ContactTokenDetails();
          registeredUser.setNumber(destination);
          directory.setNumber(registeredUser, false);
          return false;
        } else {
          registeredUser.setNumber(destination);
          directory.setNumber(registeredUser, true);
          return true;
        }
      } catch (IOException e1) {
        Log.w("UniversalTransport", e1);
        return false;
      }
    }
  }
}
