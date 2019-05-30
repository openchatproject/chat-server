package com.openchat.secureim.crypto.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.openchat.secureim.crypto.MasterCipher;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.util.Conversions;
import com.openchat.protocal.OpenchatAddress;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.state.SessionRecord;
import com.openchat.protocal.state.SessionState;
import com.openchat.protocal.state.SessionStore;
import com.openchat.imservice.api.push.OpenchatServiceAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

import static com.openchat.protocal.state.StorageProtos.SessionStructure;

public class OpenchatServiceSessionStore implements SessionStore {

  private static final String TAG                   = OpenchatServiceSessionStore.class.getSimpleName();
  private static final String SESSIONS_DIRECTORY_V2 = "sessions-v2";
  private static final Object FILE_LOCK             = new Object();

  private static final int SINGLE_STATE_VERSION   = 1;
  private static final int ARCHIVE_STATES_VERSION = 2;
  private static final int PLAINTEXT_VERSION      = 3;
  private static final int CURRENT_VERSION        = 3;

  @NonNull  private final Context      context;
  @Nullable private final MasterSecret masterSecret;

  public OpenchatServiceSessionStore(@NonNull Context context) {
    this(context, null);
  }

  public OpenchatServiceSessionStore(@NonNull Context context, @Nullable MasterSecret masterSecret) {
    this.context      = context.getApplicationContext();
    this.masterSecret = masterSecret;
  }

  @Override
  public SessionRecord loadSession(@NonNull OpenchatAddress address) {
    synchronized (FILE_LOCK) {
      try {
        FileInputStream in            = new FileInputStream(getSessionFile(address));
        int             versionMarker = readInteger(in);

        if (versionMarker > CURRENT_VERSION) {
          throw new AssertionError("Unknown version: " + versionMarker);
        }

        byte[] serialized = readBlob(in);
        in.close();

        if (versionMarker < PLAINTEXT_VERSION && masterSecret != null) {
          serialized = new MasterCipher(masterSecret).decryptBytes(serialized);
        } else if (versionMarker < PLAINTEXT_VERSION) {
          throw new AssertionError("Session didn't get migrated: (" + versionMarker + "," + address + ")");
        }

        if (versionMarker == SINGLE_STATE_VERSION) {
          SessionStructure sessionStructure = SessionStructure.parseFrom(serialized);
          SessionState     sessionState     = new SessionState(sessionStructure);
          return new SessionRecord(sessionState);
        } else if (versionMarker >= ARCHIVE_STATES_VERSION) {
          return new SessionRecord(serialized);
        } else {
          throw new AssertionError("Unknown version: " + versionMarker);
        }
      } catch (InvalidMessageException | IOException e) {
        Log.w(TAG, "No existing session information found.");
        return new SessionRecord();
      }
    }
  }

  @Override
  public void storeSession(@NonNull OpenchatAddress address, @NonNull SessionRecord record) {
    synchronized (FILE_LOCK) {
      try {
        RandomAccessFile sessionFile  = new RandomAccessFile(getSessionFile(address), "rw");
        FileChannel      out          = sessionFile.getChannel();

        out.position(0);
        writeInteger(CURRENT_VERSION, out);
        writeBlob(record.serialize(), out);
        out.truncate(out.position());

        sessionFile.close();
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
  }

  @Override
  public boolean containsSession(OpenchatAddress address) {
    return getSessionFile(address).exists() &&
           loadSession(address).getSessionState().hasSenderChain();
  }

  @Override
  public void deleteSession(OpenchatAddress address) {
    getSessionFile(address).delete();
  }

  @Override
  public void deleteAllSessions(String name) {
    List<Integer> devices = getSubDeviceSessions(name);

    deleteSession(new OpenchatAddress(name, OpenchatServiceAddress.DEFAULT_DEVICE_ID));

    for (int device : devices) {
      deleteSession(new OpenchatAddress(name, device));
    }
  }

  @Override
  public List<Integer> getSubDeviceSessions(String name) {
    long          recipientId = RecipientFactory.getRecipientsFromString(context, name, true).getPrimaryRecipient().getRecipientId();
    List<Integer> results     = new LinkedList<>();
    File          parent      = getSessionDirectory();
    String[]      children    = parent.list();

    if (children == null) return results;

    for (String child : children) {
      try {
        String[] parts              = child.split("[.]", 2);
        long     sessionRecipientId = Long.parseLong(parts[0]);

        if (sessionRecipientId == recipientId && parts.length > 1) {
          results.add(Integer.parseInt(parts[1]));
        }
      } catch (NumberFormatException e) {
        Log.w(TAG, e);
      }
    }

    return results;
  }

  public void migrateSessions() {
    synchronized (FILE_LOCK) {
      File directory = getSessionDirectory();

      for (File session : directory.listFiles()) {
        if (session.isFile()) {
          OpenchatAddress address = getAddressName(session);

          if (address != null) {
            SessionRecord sessionRecord = loadSession(address);
            storeSession(address, sessionRecord);
          }
        }
      }
    }
  }

  private File getSessionFile(OpenchatAddress address) {
    return new File(getSessionDirectory(), getSessionName(address));
  }

  private File getSessionDirectory() {
    File directory = new File(context.getFilesDir(), SESSIONS_DIRECTORY_V2);

    if (!directory.exists()) {
      if (!directory.mkdirs()) {
        Log.w(TAG, "Session directory creation failed!");
      }
    }

    return directory;
  }

  private String getSessionName(OpenchatAddress axolotlAddress) {
    Recipient recipient   = RecipientFactory.getRecipientsFromString(context, axolotlAddress.getName(), true)
                                          .getPrimaryRecipient();
    long      recipientId = recipient.getRecipientId();
    int       deviceId    = axolotlAddress.getDeviceId();

    return recipientId + (deviceId == OpenchatServiceAddress.DEFAULT_DEVICE_ID ? "" : "." + deviceId);
  }

  private @Nullable OpenchatAddress getAddressName(File sessionFile) {
    try {
      String[]  parts     = sessionFile.getName().split("[.]");
      Recipient recipient = RecipientFactory.getRecipientForId(context, Integer.valueOf(parts[0]), true);

      int deviceId;

      if (parts.length > 1) deviceId = Integer.parseInt(parts[1]);
      else                  deviceId = OpenchatServiceAddress.DEFAULT_DEVICE_ID;

      return new OpenchatAddress(recipient.getNumber(), deviceId);
    } catch (NumberFormatException e) {
      Log.w(TAG, e);
      return null;
    }
  }

  private byte[] readBlob(FileInputStream in) throws IOException {
    int length       = readInteger(in);
    byte[] blobBytes = new byte[length];

    in.read(blobBytes, 0, blobBytes.length);
    return blobBytes;
  }

  private void writeBlob(byte[] blobBytes, FileChannel out) throws IOException {
    writeInteger(blobBytes.length, out);
    out.write(ByteBuffer.wrap(blobBytes));
  }

  private int readInteger(FileInputStream in) throws IOException {
    byte[] integer = new byte[4];
    in.read(integer, 0, integer.length);
    return Conversions.byteArrayToInt(integer);
  }

  private void writeInteger(int value, FileChannel out) throws IOException {
    byte[] valueBytes = Conversions.intToByteArray(value);
    out.write(ByteBuffer.wrap(valueBytes));
  }

}
