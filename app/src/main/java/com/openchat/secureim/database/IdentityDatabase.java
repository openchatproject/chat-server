package com.openchat.secureim.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.Base64;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;

import java.io.IOException;

public class IdentityDatabase extends Database {

  private static final Uri CHANGE_URI = Uri.parse("content://openchatservice/identities");

  private static final String TABLE_NAME    = "identities";
  private static final String ID            = "_id";
  public  static final String RECIPIENT     = "recipient";
  public  static final String IDENTITY_KEY  = "key";

  public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
      " (" + ID + " INTEGER PRIMARY KEY, " +
      RECIPIENT + " INTEGER UNIQUE, " +
      IDENTITY_KEY + " TEXT);";

  public IdentityDatabase(Context context, SQLiteOpenHelper databaseHelper) {
    super(context, databaseHelper);
  }

  public Cursor getIdentities() {
    SQLiteDatabase database = databaseHelper.getReadableDatabase();
    Cursor cursor           = database.query(TABLE_NAME, null, null, null, null, null, null);

    if (cursor != null)
      cursor.setNotificationUri(context.getContentResolver(), CHANGE_URI);

    return cursor;
  }

  public boolean isValidIdentity(long recipientId,
                                 IdentityKey theirIdentity)
  {
    SQLiteDatabase database = databaseHelper.getReadableDatabase();
    Cursor         cursor   = null;

    try {
      cursor = database.query(TABLE_NAME, null, RECIPIENT + " = ?",
                              new String[] {recipientId+""}, null, null,null);

      if (cursor != null && cursor.moveToFirst()) {
        String      serializedIdentity = cursor.getString(cursor.getColumnIndexOrThrow(IDENTITY_KEY));
        IdentityKey ourIdentity        = new IdentityKey(Base64.decode(serializedIdentity), 0);

        return ourIdentity.equals(theirIdentity);
      } else {
        return true;
      }
    } catch (IOException e) {
      Log.w("IdentityDatabase", e);
      return false;
    } catch (InvalidKeyException e) {
      Log.w("IdentityDatabase", e);
      return false;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  public void saveIdentity(long recipientId, IdentityKey identityKey)
  {
    SQLiteDatabase database          = databaseHelper.getWritableDatabase();
    String         identityKeyString = Base64.encodeBytes(identityKey.serialize());

    ContentValues contentValues = new ContentValues();
    contentValues.put(RECIPIENT, recipientId);
    contentValues.put(IDENTITY_KEY, identityKeyString);

    database.replace(TABLE_NAME, null, contentValues);

    context.getContentResolver().notifyChange(CHANGE_URI, null);
  }

  public void deleteIdentity(long id) {
    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.delete(TABLE_NAME, ID_WHERE, new String[] {id+""});

    context.getContentResolver().notifyChange(CHANGE_URI, null);
  }

  public Reader readerFor(Cursor cursor) {
    return new Reader(cursor);
  }

  public class Reader {
    private final Cursor cursor;

    public Reader(Cursor cursor) {
      this.cursor = cursor;
    }

    public Identity getCurrent() {
      long       recipientId = cursor.getLong(cursor.getColumnIndexOrThrow(RECIPIENT));
      Recipients recipients  = RecipientFactory.getRecipientsForIds(context, new long[]{recipientId}, true);

      try {
        String      identityKeyString = cursor.getString(cursor.getColumnIndexOrThrow(IDENTITY_KEY));
        IdentityKey identityKey       = new IdentityKey(Base64.decode(identityKeyString), 0);

        return new Identity(recipients, identityKey);
      } catch (IOException e) {
        Log.w("IdentityDatabase", e);
        return new Identity(recipients, null);
      } catch (InvalidKeyException e) {
        Log.w("IdentityDatabase", e);
        return new Identity(recipients, null);
      }
    }
  }

  public static class Identity {
    private final Recipients  recipients;
    private final IdentityKey identityKey;

    public Identity(Recipients recipients, IdentityKey identityKey) {
      this.recipients  = recipients;
      this.identityKey = identityKey;
    }

    public Recipients getRecipients() {
      return recipients;
    }

    public IdentityKey getIdentityKey() {
      return identityKey;
    }
  }

}
