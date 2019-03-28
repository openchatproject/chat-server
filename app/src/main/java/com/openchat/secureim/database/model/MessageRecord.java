package com.openchat.secureim.database.model;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;

import com.openchat.secureim.R;
import com.openchat.secureim.database.MmsSmsColumns;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.GroupUtil;

public abstract class MessageRecord extends DisplayRecord {

  public static final int DELIVERY_STATUS_NONE     = 0;
  public static final int DELIVERY_STATUS_RECEIVED = 1;
  public static final int DELIVERY_STATUS_PENDING  = 2;
  public static final int DELIVERY_STATUS_FAILED   = 3;

  private final Recipient individualRecipient;
  private final int       recipientDeviceId;
  private final long      id;
  private final int       deliveryStatus;
  private final int       receiptCount;

  MessageRecord(Context context, long id, Body body, Recipients recipients,
                Recipient individualRecipient, int recipientDeviceId,
                long dateSent, long dateReceived, long threadId,
                int deliveryStatus, int receiptCount, long type)
  {
    super(context, body, recipients, dateSent, dateReceived, threadId, type);
    this.id                  = id;
    this.individualRecipient = individualRecipient;
    this.recipientDeviceId   = recipientDeviceId;
    this.deliveryStatus      = deliveryStatus;
    this.receiptCount        = receiptCount;
  }

  public abstract boolean isMms();
  public abstract boolean isMmsNotification();

  public boolean isFailed() {
    return
        MmsSmsColumns.Types.isFailedMessageType(type) ||
        getDeliveryStatus() == DELIVERY_STATUS_FAILED;
  }

  public boolean isOutgoing() {
    return MmsSmsColumns.Types.isOutgoingMessageType(type);
  }

  public boolean isPending() {
    return MmsSmsColumns.Types.isPendingMessageType(type);
  }

  public boolean isSecure() {
    return MmsSmsColumns.Types.isSecureType(type);
  }

  public boolean isLegacyMessage() {
    return MmsSmsColumns.Types.isLegacyType(type);
  }

  public boolean isAsymmetricEncryption() {
    return MmsSmsColumns.Types.isAsymmetricEncryption(type);
  }

  @Override
  public SpannableString getDisplayBody() {
    if (isGroupUpdate() && isOutgoing()) {
      return emphasisAdded(context.getString(R.string.MessageRecord_updated_group));
    } else if (isGroupUpdate()) {
      return emphasisAdded(GroupUtil.getDescription(context, getBody().getBody()));
    } else if (isGroupQuit() && isOutgoing()) {
      return emphasisAdded(context.getString(R.string.MessageRecord_left_group));
    } else if (isGroupQuit()) {
      return emphasisAdded(context.getString(R.string.ConversationItem_group_action_left, getIndividualRecipient().toShortString()));
    }

    return new SpannableString(getBody().getBody());
  }

  public long getId() {
    return id;
  }

  public int getDeliveryStatus() {
    return deliveryStatus;
  }

  public boolean isDelivered() {
    return getDeliveryStatus() == DELIVERY_STATUS_RECEIVED || receiptCount > 0;
  }

  public boolean isPush() {
    return SmsDatabase.Types.isPushType(type);
  }

  public boolean isForcedSms() {
    return SmsDatabase.Types.isForcedSms(type);
  }

  public boolean isStaleKeyExchange() {
    return SmsDatabase.Types.isStaleKeyExchange(type);
  }

  public boolean isProcessedKeyExchange() {
    return SmsDatabase.Types.isProcessedKeyExchange(type);
  }

  public boolean isPendingSmsFallback() {
    return SmsDatabase.Types.isPendingSmsFallbackType(type);
  }

  public boolean isPendingSecureSmsFallback() {
    return SmsDatabase.Types.isPendingSecureSmsFallbackType(type);
  }

  public boolean isPendingInsecureSmsFallback() {
    return SmsDatabase.Types.isPendingInsecureSmsFallbackType(type);
  }

  public boolean isBundleKeyExchange() {
    return SmsDatabase.Types.isBundleKeyExchange(type);
  }

  public boolean isIdentityUpdate() {
    return SmsDatabase.Types.isIdentityUpdate(type);
  }

  public boolean isCorruptedKeyExchange() {
    return SmsDatabase.Types.isCorruptedKeyExchange(type);
  }

  public boolean isInvalidVersionKeyExchange() {
    return SmsDatabase.Types.isInvalidVersionKeyExchange(type);
  }

  public Recipient getIndividualRecipient() {
    return individualRecipient;
  }

  public int getRecipientDeviceId() {
    return recipientDeviceId;
  }

  public long getType() {
    return type;
  }

  protected SpannableString emphasisAdded(String sequence) {
    SpannableString spannable = new SpannableString(sequence);
    spannable.setSpan(new TextAppearanceSpan(context, android.R.style.TextAppearance_Small), 0, sequence.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    spannable.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, sequence.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    return spannable;
  }

  public boolean equals(Object other) {
    return other != null                              &&
           other instanceof MessageRecord             &&
           ((MessageRecord) other).getId() == getId() &&
           ((MessageRecord) other).isMms() == isMms();
  }

  public int hashCode() {
    return (int)getId();
  }
}
