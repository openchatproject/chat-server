package com.openchat.secureim.contacts;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import junit.framework.Assert;

import java.util.concurrent.Semaphore;

public class ContactsCursorLoader extends CursorLoader {
  private static final String TAG        = ContactsCursorLoader.class.getSimpleName();
  private static final int    DB_PERMITS = 100;

  private final Context          context;
  private final String           filter;
  private final boolean          pushOnly;
  private final Semaphore        dbSemaphore = new Semaphore(DB_PERMITS);
  private       ContactsDatabase db;

  public ContactsCursorLoader(Context context, String filter, boolean pushOnly) {
    super(context);
    this.context  = context;
    this.filter   = filter;
    this.pushOnly = pushOnly;
    this.db       = new ContactsDatabase(context);
  }

  @Override
  public Cursor loadInBackground() {
    try {
      dbSemaphore.acquire();
      return db.query(filter, pushOnly);
    } catch (InterruptedException ie) {
      throw new AssertionError(ie);
    } finally {
      dbSemaphore.release();
    }
  }

  @Override
  public void onReset() {
    Log.w(TAG, "onReset()");
    try {
      dbSemaphore.acquire(DB_PERMITS);
      db.close();
      db = new ContactsDatabase(context);
    } catch (InterruptedException ie) {
      throw new AssertionError(ie);
    } finally {
      dbSemaphore.release(DB_PERMITS);
    }
    super.onReset();
  }
}
