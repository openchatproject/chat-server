package com.openchat.secureim.contacts;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

public class ContactsCursorLoader extends CursorLoader {

  private final Context          context;
  private final String           filter;
  private final boolean          pushOnly;
  private       ContactsDatabase db;

  public ContactsCursorLoader(Context context, String filter, boolean pushOnly) {
    super(context);
    this.context  = context;
    this.filter   = filter;
    this.pushOnly = pushOnly;
  }

  @Override
  public Cursor loadInBackground() {
    ContactsDatabase.destroyInstance();
    db = ContactsDatabase.getInstance(context);
    return db.query(filter, pushOnly);
  }

  @Override
  public void onReset() {
    super.onReset();
  }
}
