package com.openchat.secureim;

import android.support.annotation.NonNull;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.model.MessageRecord;
import com.openchat.secureim.mms.GlideRequests;
import com.openchat.secureim.recipients.Recipient;

import java.util.Locale;
import java.util.Set;

public interface BindableConversationItem extends Unbindable {
  void bind(@NonNull MasterSecret masterSecret,
            @NonNull MessageRecord messageRecord,
            @NonNull GlideRequests glideRequests,
            @NonNull Locale locale,
            @NonNull Set<MessageRecord> batchSelected,
            @NonNull Recipient recipients);

  MessageRecord getMessageRecord();
}
