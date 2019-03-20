package com.openchat.secureim.database.loaders;

import android.content.Context;
import android.database.Cursor;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.util.AbstractCursorLoader;

public class ConversationLoader extends AbstractCursorLoader {
  private final long                     threadId;

  public ConversationLoader(Context context, long threadId) {
    super(context);
    this.threadId = threadId;
  }

  @Override
  public Cursor getCursor() {
    return DatabaseFactory.getMmsSmsDatabase(context).getConversation(threadId);
  }
}
