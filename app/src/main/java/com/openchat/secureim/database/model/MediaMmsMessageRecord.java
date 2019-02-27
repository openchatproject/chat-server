package com.openchat.secureim.database.model;

import android.content.Context;
import android.text.SpannableString;

import com.openchat.secureim.R;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.mms.SlideDeck;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.imservice.util.ListenableFutureTask;

public class MediaMmsMessageRecord extends MessageRecord {

  private final Context context;
  private final int partCount;
  private final ListenableFutureTask<SlideDeck> slideDeck;

  public MediaMmsMessageRecord(Context context, long id, Recipients recipients,
                               Recipient individualRecipient, int recipientDeviceId,
                               long dateSent, long dateReceived, long threadId, Body body,
                               ListenableFutureTask<SlideDeck> slideDeck,
                               int partCount, long mailbox)
  {
    super(context, id, body, recipients, individualRecipient, recipientDeviceId,
          dateSent, dateReceived, threadId, DELIVERY_STATUS_NONE, mailbox);

    this.context   = context.getApplicationContext();
    this.partCount = partCount;
    this.slideDeck = slideDeck;
  }

  public ListenableFutureTask<SlideDeck> getSlideDeck() {
    return slideDeck;
  }

  public int getPartCount() {
    return partCount;
  }

  @Override
  public boolean isMms() {
    return true;
  }

  @Override
  public boolean isMmsNotification() {
    return false;
  }

  @Override
  public SpannableString getDisplayBody() {
    if (MmsDatabase.Types.isDecryptInProgressType(type)) {
      return emphasisAdded(context.getString(R.string.MmsMessageRecord_decrypting_mms_please_wait));
    } else if (MmsDatabase.Types.isFailedDecryptType(type)) {
      return emphasisAdded(context.getString(R.string.MmsMessageRecord_bad_encrypted_mms_message));
    } else if (MmsDatabase.Types.isDuplicateMessageType(type)) {
      return emphasisAdded(context.getString(R.string.SmsMessageRecord_duplicate_message));
    } else if (MmsDatabase.Types.isNoRemoteSessionType(type)) {
      return emphasisAdded(context.getString(R.string.MmsMessageRecord_mms_message_encrypted_for_non_existing_session));
    } else if (isLegacyMessage()) {
      return emphasisAdded(context.getString(R.string.MessageRecord_message_encrypted_with_a_legacy_protocol_version_that_is_no_longer_supported));
    } else if (!getBody().isPlaintext()) {
      return emphasisAdded(context.getString(R.string.MessageNotifier_encrypted_message));
    }

    return super.getDisplayBody();
  }
}
