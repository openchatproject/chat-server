package com.openchat.secureim.database.model;

import android.content.Context;
import android.text.SpannableString;

import com.openchat.secureim.R;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;

public class NotificationMmsMessageRecord extends MessageRecord {

  private final byte[] contentLocation;
  private final long messageSize;
  private final long expiry;
  private final int status;
  private final byte[] transactionId;

  public NotificationMmsMessageRecord(Context context, long id, Recipients recipients,
                                      Recipient individualRecipient, int recipientDeviceId,
                                      long dateSent, long dateReceived, long threadId,
                                      byte[] contentLocation, long messageSize, long expiry,
                                      int status, byte[] transactionId, long mailbox)
  {
    super(context, id, new Body("", true), recipients, individualRecipient, recipientDeviceId,
          dateSent, dateReceived, threadId, DELIVERY_STATUS_NONE, mailbox);

    this.contentLocation = contentLocation;
    this.messageSize     = messageSize;
    this.expiry          = expiry;
    this.status          = status;
    this.transactionId   = transactionId;
  }

  public byte[] getTransactionId() {
    return transactionId;
  }

  public int getStatus() {
    return this.status;
  }

  public byte[] getContentLocation() {
    return contentLocation;
  }

  public long getMessageSize() {
    return (messageSize + 1023) / 1024;
  }

  public long getExpiration() {
    return expiry * 1000;
  }

  @Override
  public boolean isOutgoing() {
    return false;
  }

  @Override
  public boolean isFailed() {
    return MmsDatabase.Status.isHardError(status);
  }

  @Override
  public boolean isSecure() {
    return false;
  }

  @Override
  public boolean isPending() {
    return false;
  }

  @Override
  public boolean isMms() {
    return true;
  }

  @Override
  public boolean isMmsNotification() {
    return true;
  }

  @Override
  public SpannableString getDisplayBody() {
    return emphasisAdded(context.getString(R.string.NotificationMmsMessageRecord_multimedia_message));
  }
}
