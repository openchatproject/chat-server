package com.openchat.secureim.database.loaders;

import android.content.Context;
import android.database.Cursor;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.util.AbstractCursorLoader;

public class BlockedContactsLoader extends AbstractCursorLoader {

  public BlockedContactsLoader(Context context) {
    super(context);
  }

  @Override
  public Cursor getCursor() {
    return DatabaseFactory.getRecipientDatabase(getContext())
                          .getBlocked();
  }

}
