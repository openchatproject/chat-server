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
import com.openchat.secureim.crypto.MasterCipher;
import com.openchat.secureim.crypto.MasterSecret;

import java.io.IOException;

public class IdentityDatabase extends Database {

  private static final Uri CHANGE_URI = Uri.parse("content://openchatservice/identities");

  private static final String TABLE_NAME    = "identities";
  private static final String ID            = "_id";
  public  static final String RECIPIENT     = "recipient";
  public  static final String IDENTITY_KEY  = "key";
  public  static final String MAC           = "mac";

  public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
      " (" + ID + " INTEGER PRIMARY KEY, " +
      RECIPIENT + " INTEGER UNIQUE, " +
      IDENTITY_KEY + " TEXT, " +
      MAC + " TEXT);";

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

  public boolean isValidIdentity(MasterSecret masterSecret,
                                 long recipientId,
                                 IdentityKey theirIdentity)
  {
    SQLiteDatabase database   = databaseHelper.getReadableDatabase();
    MasterCipher masterCipher = new MasterCipher(masterSecret);
    Cursor cursor             = null;

    try {
      cursor = database.query(TABLE_NAME, null, RECIPIENT + " = ?",
                              new String[] {recipientId+""}, null, null,null);

      if (cursor != null && cursor.moveToFirst()) {
        String serializedIdentity = cursor.getString(cursor.getColumnIndexOrThrow(IDENTITY_KEY));
        String mac                = cursor.getString(cursor.getColumnIndexOrThrow(MAC));

        if (!masterCipher.verifyMacFor(recipientId + serializedIdentity, Base64.decode(mac))) {
          Log.w("IdentityDatabase", "MAC failed");
          return false;
        }

        IdentityKey ourIdentity = new IdentityKey(Base64.decode(serializedIdentity), 0);

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

  public void saveIdentity(MasterSecret masterSecret, long recipientId, IdentityKey identityKey)
  {
    SQLiteDatabase database   = databaseHelper.getWritableDatabase();
    MasterCipher masterCipher = new MasterCipher(masterSecret);
    String identityKeyString  = Base64.encodeBytes(identityKey.serialize());
    String macString          = Base64.encodeBytes(masterCipher.getMacFor(recipientId +
                                                                              identityKeyString));

    ContentValues contentValues = new ContentValues();
    contentValues.put(RECIPIENT, recipientId);
    contentValues.put(IDENTITY_KEY, identityKeyString);
    contentValues.put(MAC, macString);

    database.replace(TABLE_NAME, null, contentValues);

    context.getContentResolver().notifyChange(CHANGE_URI, null);
  }

  public void deleteIdentity(long id) {
    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.delete(TABLE_NAME, ID_WHERE, new String[] {id+""});

    context.getContentResolver().notifyChange(CHANGE_URI, null);
  }

  public Reader readerFor(MasterSecret masterSecret, Cursor cursor) {
    return new Reader(masterSecret, cursor);
  }

  public class Reader {
    private final Cursor cursor;
    private final MasterCipher cipher;

    public Reader(MasterSecret masterSecret, Cursor cursor) {
      this.cursor = cursor;
      this.cipher = new MasterCipher(masterSecret);
    }

    public Identity getCurrent() {
      long       recipientId = cursor.getLong(cursor.getColumnIndexOrThrow(RECIPIENT));
      Recipients recipients  = RecipientFactory.getRecipientsForIds(context, new long[]{recipientId}, true);

      try {
        String identityKeyString = cursor.getString(cursor.getColumnIndexOrThrow(IDENTITY_KEY));
        String mac               = cursor.getString(cursor.getColumnIndexOrThrow(MAC));

        if (!cipher.verifyMacFor(recipientId + identityKeyString, Base64.decode(mac))) {
          return new Identity(recipients, null);
        }

        IdentityKey identityKey = new IdentityKey(Base64.decode(identityKeyString), 0);
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

  public class Identity {
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
