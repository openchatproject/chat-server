package com.openchat.secureim.database.model;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import com.openchat.secureim.R;
import com.openchat.secureim.database.MmsSmsColumns;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.GroupUtil;

public class ThreadRecord extends DisplayRecord {

  private final Context context;
  private final long count;
  private final boolean read;
  private final int distributionType;

  public ThreadRecord(Context context, Body body, Recipients recipients, long date,
                      long count, boolean read, long threadId, long snippetType,
                      int distributionType)
  {
    super(context, body, recipients, date, date, threadId, snippetType);
    this.context          = context.getApplicationContext();
    this.count            = count;
    this.read             = read;
    this.distributionType = distributionType;
  }

  @Override
  public SpannableString getDisplayBody() {
    if (SmsDatabase.Types.isDecryptInProgressType(type)) {
      return emphasisAdded(context.getString(R.string.MessageDisplayHelper_decrypting_please_wait));
    } else if (isGroupUpdate()) {
      return emphasisAdded(GroupUtil.getDescription(getBody().getBody()));
    } else if (isGroupQuit()) {
      return emphasisAdded(context.getString(R.string.ThreadRecord_left_the_group));
    } else if (isKeyExchange()) {
      return emphasisAdded(context.getString(R.string.ConversationListItem_key_exchange_message));
    } else if (SmsDatabase.Types.isFailedDecryptType(type)) {
      return emphasisAdded(context.getString(R.string.MessageDisplayHelper_bad_encrypted_message));
    } else if (SmsDatabase.Types.isNoRemoteSessionType(type)) {
      return emphasisAdded(context.getString(R.string.MessageDisplayHelper_message_encrypted_for_non_existing_session));
    } else if (!getBody().isPlaintext()) {
      return emphasisAdded(context.getString(R.string.MessageNotifier_encrypted_message));
    } else if (SmsDatabase.Types.isEndSessionType(type)) {
      return emphasisAdded(context.getString(R.string.TheadRecord_secure_session_ended));
    } else if (MmsSmsColumns.Types.isLegacyType(type)) {
      return emphasisAdded(context.getString(R.string.MessageRecord_message_encrypted_with_a_legacy_protocol_version_that_is_no_longer_supported));
    } else {
      if (TextUtils.isEmpty(getBody().getBody())) {
        return new SpannableString(context.getString(R.string.MessageNotifier_no_subject));
      } else {
        return new SpannableString(getBody().getBody());
      }
    }
  }

  private SpannableString emphasisAdded(String sequence) {
    SpannableString spannable = new SpannableString(sequence);
    spannable.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0,
                      sequence.length(),
                      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }

  public long getCount() {
    return count;
  }

  public boolean isRead() {
    return read;
  }

  public long getDate() {
    return getDateReceived();
  }

  public int getDistributionType() {
    return distributionType;
  }
}
