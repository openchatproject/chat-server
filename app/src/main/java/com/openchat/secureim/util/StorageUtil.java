package com.openchat.secureim.util;

import android.os.Environment;

import com.openchat.secureim.database.NoExternalStorageException;

import java.io.File;

public class StorageUtil
{
  private static File getopenchatStorageDir() throws NoExternalStorageException {
    final File storage = Environment.getExternalStorageDirectory();

    if (!storage.canWrite()) {
      throw new NoExternalStorageException();
    }

    return storage;
  }

  public static boolean canWriteInopenchatStorageDir() {
    File storage;

    try {
      storage = getopenchatStorageDir();
    } catch (NoExternalStorageException e) {
      return false;
    }

    return storage.canWrite();
  }

  public static File getBackupDir() throws NoExternalStorageException {
    return getopenchatStorageDir();
  }

  public static File getVideoDir() throws NoExternalStorageException {
    return new File(getopenchatStorageDir(), Environment.DIRECTORY_MOVIES);
  }

  public static File getAudioDir() throws NoExternalStorageException {
    return new File(getopenchatStorageDir(), Environment.DIRECTORY_MUSIC);
  }

  public static File getImageDir() throws NoExternalStorageException {
    return new File(getopenchatStorageDir(), Environment.DIRECTORY_PICTURES);
  }

  public static File getDownloadDir() throws NoExternalStorageException {
    return new File(getopenchatStorageDir(), Environment.DIRECTORY_DOWNLOADS);
  }
}
