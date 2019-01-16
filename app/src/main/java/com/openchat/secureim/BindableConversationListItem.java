package com.openchat.secureim;

import android.support.annotation.NonNull;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.model.ThreadRecord;
import com.openchat.secureim.mms.GlideRequests;

import java.util.Locale;
import java.util.Set;

public interface BindableConversationListItem extends Unbindable {

  public void bind(@NonNull MasterSecret masterSecret, @NonNull ThreadRecord thread,
                   @NonNull GlideRequests glideRequests, @NonNull Locale locale,
                   @NonNull Set<Long> selectedThreads, boolean batchMode);
}
