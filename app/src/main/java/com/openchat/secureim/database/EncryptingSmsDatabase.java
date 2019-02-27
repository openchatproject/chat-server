package com.openchat.secureim.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.crypto.AsymmetricMasterCipher;
import com.openchat.secureim.crypto.AsymmetricMasterSecret;
import com.openchat.secureim.database.model.DisplayRecord;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.sms.OutgoingTextMessage;
import com.openchat.secureim.util.LRUCache;
import com.openchat.imservice.crypto.InvalidMessageException;
import com.openchat.imservice.crypto.MasterCipher;
import com.openchat.imservice.crypto.MasterSecret;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EncryptingSmsDatabase extends SmsDatabase {

  private final PlaintextCache plaintextCache = new PlaintextCache();

  public EncryptingSmsDatabase(Context context, SQLiteOpenHelper databaseHelper) {
    super(context, databaseHelper);
  }

  private String getAsymmetricEncryptedBody(AsymmetricMasterSecret masterSecret, String body) {
    AsymmetricMasterCipher bodyCipher = new AsymmetricMasterCipher(masterSecret);
    return bodyCipher.encryptBody(body);
  }

  private String getEncryptedBody(MasterSecret masterSecret, String body) {
    MasterCipher bodyCipher = new MasterCipher(masterSecret);
    String ciphertext       = bodyCipher.encryptBody(body);
    plaintextCache.put(ciphertext, body);

    return ciphertext;
  }

  public List<Long> insertMessageOutbox(MasterSecret masterSecret, long threadId,
                                        OutgoingTextMessage message, boolean forceSms)
  {
    long type = Types.BASE_OUTBOX_TYPE;
    message   = message.withBody(getEncryptedBody(masterSecret, message.getMessageBody()));
    type     |= Types.ENCRYPTION_SYMMETRIC_BIT;

    return insertMessageOutbox(threadId, message, type, forceSms);
  }

  public Pair<Long, Long> insertMessageInbox(MasterSecret masterSecret,
                                             IncomingTextMessage message)
  {
    long type = Types.BASE_INBOX_TYPE;

    if (!message.isSecureMessage() && !message.isEndSession()) {
      type |= Types.ENCRYPTION_SYMMETRIC_BIT;
      message = message.withMessageBody(getEncryptedBody(masterSecret, message.getMessageBody()));
    }

    return insertMessageInbox(message, type);
  }

  public Pair<Long, Long> insertMessageInbox(AsymmetricMasterSecret masterSecret,
                                             IncomingTextMessage message)
  {
    long type = Types.BASE_INBOX_TYPE;

    if (message.isSecureMessage()) {
      type |= Types.ENCRYPTION_REMOTE_BIT;
    } else {
      message = message.withMessageBody(getAsymmetricEncryptedBody(masterSecret, message.getMessageBody()));
      type   |= Types.ENCRYPTION_ASYMMETRIC_BIT;
    }

    return insertMessageInbox(message, type);
  }

  public void updateBundleMessageBody(MasterSecret masterSecret, long messageId, String body) {
    updateMessageBodyAndType(messageId, body, Types.TOTAL_MASK,
                             Types.BASE_INBOX_TYPE | Types.ENCRYPTION_REMOTE_BIT | Types.SECURE_MESSAGE_BIT);
  }

  public void updateMessageBody(MasterSecret masterSecret, long messageId, String body) {
    String encryptedBody = getEncryptedBody(masterSecret, body);
    updateMessageBodyAndType(messageId, encryptedBody, Types.ENCRYPTION_MASK,
                             Types.ENCRYPTION_SYMMETRIC_BIT);
  }

  public Reader getMessages(MasterSecret masterSecret, int skip, int limit) {
    Cursor cursor = super.getMessages(skip, limit);
    return new DecryptingReader(masterSecret, cursor);
  }

  public Reader getOutgoingMessages(MasterSecret masterSecret) {
    Cursor cursor = super.getOutgoingMessages();
    return new DecryptingReader(masterSecret, cursor);
  }

  public Reader getMessage(MasterSecret masterSecret, long messageId) {
    Cursor cursor = super.getMessage(messageId);
    return new DecryptingReader(masterSecret, cursor);
  }

  public Reader getDecryptInProgressMessages(MasterSecret masterSecret) {
    Cursor cursor = super.getDecryptInProgressMessages();
    return new DecryptingReader(masterSecret, cursor);
  }

  public Reader readerFor(MasterSecret masterSecret, Cursor cursor) {
    return new DecryptingReader(masterSecret, cursor);
  }

  public class DecryptingReader extends SmsDatabase.Reader {

    private final MasterCipher masterCipher;

    public DecryptingReader(MasterSecret masterSecret, Cursor cursor) {
      super(cursor);
      this.masterCipher = new MasterCipher(masterSecret);
    }

    @Override
    protected DisplayRecord.Body getBody(Cursor cursor) {
      long type         = cursor.getLong(cursor.getColumnIndexOrThrow(SmsDatabase.TYPE));
      String ciphertext = cursor.getString(cursor.getColumnIndexOrThrow(SmsDatabase.BODY));

      if (ciphertext == null) {
        return new DisplayRecord.Body("", true);
      }

      try {
        if (SmsDatabase.Types.isSymmetricEncryption(type)) {
          String plaintext = plaintextCache.get(ciphertext);

          if (plaintext != null)
            return new DisplayRecord.Body(plaintext, true);

          plaintext = masterCipher.decryptBody(ciphertext);

          plaintextCache.put(ciphertext, plaintext);
          return new DisplayRecord.Body(plaintext, true);
        } else {
          return new DisplayRecord.Body(ciphertext, true);
        }
      } catch (InvalidMessageException e) {
        Log.w("EncryptingSmsDatabase", e);
        return new DisplayRecord.Body("Error decrypting message.", true);
      }
    }
  }

  private static class PlaintextCache {
    private static final int MAX_CACHE_SIZE = 2000;
    private static final Map<String, SoftReference<String>> decryptedBodyCache =
        Collections.synchronizedMap(new LRUCache<String, SoftReference<String>>(MAX_CACHE_SIZE));

    public void put(String ciphertext, String plaintext) {
      decryptedBodyCache.put(ciphertext, new SoftReference<String>(plaintext));
    }

    public String get(String ciphertext) {
      SoftReference<String> plaintextReference = decryptedBodyCache.get(ciphertext);

      if (plaintextReference != null) {
        String plaintext = plaintextReference.get();

        if (plaintext != null) {
          return plaintext;
        }
      }

      return null;
    }
  }
}
