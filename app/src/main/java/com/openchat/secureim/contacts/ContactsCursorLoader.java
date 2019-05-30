package com.openchat.secureim.contacts;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.Log;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.util.NumberUtil;

import java.util.ArrayList;

public class ContactsCursorLoader extends CursorLoader {

  private static final String TAG = ContactsCursorLoader.class.getSimpleName();

  private final String  filter;
  private       boolean includeSmsContacts;

  public ContactsCursorLoader(Context context, boolean includeSmsContacts, String filter) {
    super(context);

    this.filter   = filter;
    this.includeSmsContacts = includeSmsContacts;
  }

  @Override
  public Cursor loadInBackground() {
    ContactsDatabase  contactsDatabase = DatabaseFactory.getContactsDatabase(getContext());
    ArrayList<Cursor> cursorList       = new ArrayList<>(3);

    cursorList.add(contactsDatabase.queryOpenchatServiceContacts(filter));

    if (includeSmsContacts) {
      cursorList.add(contactsDatabase.querySystemContacts(filter));
    }

    if (!TextUtils.isEmpty(filter) && NumberUtil.isValidSmsOrEmail(filter)) {
      cursorList.add(contactsDatabase.getNewNumberCursor(filter));
    }

    return new MergeCursor(cursorList.toArray(new Cursor[0]));
  }
}
