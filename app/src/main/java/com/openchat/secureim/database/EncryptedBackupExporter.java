package com.openchat.secureim.database;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class EncryptedBackupExporter {

  public static void exportToSd(Context context) throws NoExternalStorageException, IOException {
    verifyExternalStorageForExport();
    exportDirectory(context, "");
  }

  public static void importFromSd(Context context) throws NoExternalStorageException, IOException {
    verifyExternalStorageForImport();
    importDirectory(context, "");
  }

  private static String getExportDirectoryPath() {
    File sdDirectory  = Environment.getExternalStorageDirectory();
    return sdDirectory.getAbsolutePath() + File.separator + "OpenchatServiceExport";
  }

  private static void verifyExternalStorageForExport() throws NoExternalStorageException {
    if (!Environment.getExternalStorageDirectory().canWrite())
      throw new NoExternalStorageException();

    String exportDirectoryPath = getExportDirectoryPath();
    File exportDirectory       = new File(exportDirectoryPath);

    if (!exportDirectory.exists())
      exportDirectory.mkdir();
  }

  private static void verifyExternalStorageForImport() throws NoExternalStorageException {
    if (!Environment.getExternalStorageDirectory().canRead() ||
        !(new File(getExportDirectoryPath()).exists()))
        throw new NoExternalStorageException();
  }

  private static void migrateFile(File from, File to) {
    try {
      if (from.exists()) {
        FileChannel source      = new FileInputStream(from).getChannel();
        FileChannel destination = new FileOutputStream(to).getChannel();

        destination.transferFrom(source, 0, source.size());
        source.close();
        destination.close();
      }
    } catch (IOException ioe) {
      Log.w("EncryptedBackupExporter", ioe);
    }
  }

  private static void exportDirectory(Context context, String directoryName) throws IOException {
    File directory       = new File(context.getFilesDir().getParent() + File.separatorChar + directoryName);
    File exportDirectory = new File(getExportDirectoryPath() + File.separatorChar + directoryName);

    if (directory.exists()) {
      exportDirectory.mkdirs();

      File[] contents = directory.listFiles();

      for (int i=0;i<contents.length;i++) {
        File localFile = contents[i];

        if (localFile.isFile()) {
          File exportedFile = new File(exportDirectory.getAbsolutePath() + File.separator + localFile.getName());
          migrateFile(localFile, exportedFile);
        } else {
          exportDirectory(context, directoryName + File.separator + localFile.getName());
        }
      }
    } else {
      Log.w("EncryptedBackupExporter", "Could not find directory: " + directory.getAbsolutePath());
    }
  }

  private static void importDirectory(Context context, String directoryName) throws IOException {
    File directory       = new File(getExportDirectoryPath() + File.separator + directoryName);
    File importDirectory = new File(context.getFilesDir().getParent() + File.separator + directoryName);

    if (directory.exists() && directory.isDirectory()) {
      importDirectory.mkdirs();

      File[] contents = directory.listFiles();

      for (File exportedFile : contents) {
        if (exportedFile.isFile()) {
          File localFile = new File(importDirectory.getAbsolutePath() + File.separator + exportedFile.getName());
          migrateFile(exportedFile, localFile);
        } else if (exportedFile.isDirectory()) {
          importDirectory(context, directoryName + File.separator + exportedFile.getName());
        }
      }
    }
  }
}
