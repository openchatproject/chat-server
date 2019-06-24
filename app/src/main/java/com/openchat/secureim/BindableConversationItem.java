package com.openchat.secureim;

import android.support.annotation.NonNull;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.model.MessageRecord;

import java.util.Locale;
import java.util.Set;

public interface BindableConversationItem extends Unbindable {
  void bind(@NonNull MasterSecret masterSecret,
            @NonNull MessageRecord messageRecord,
            @NonNull Locale locale,
            @NonNull Set<MessageRecord> batchSelected,
            boolean groupThread, boolean pushDestination);
}
