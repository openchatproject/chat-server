package com.openchat.secureim.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.PartDatabase;
import com.openchat.secureim.mms.PartUriParser;
import com.openchat.secureim.service.KeyCachingService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PartProvider extends ContentProvider {
  private static final String TAG = PartProvider.class.getSimpleName();

  private static final String CONTENT_URI_STRING = "content://com.openchat.secureim.provider/part";
  private static final Uri    CONTENT_URI        = Uri.parse(CONTENT_URI_STRING);
  private static final int    SINGLE_ROW         = 1;

  private static final UriMatcher uriMatcher;

  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI("com.openchat.secureim.provider", "part/*/#", SINGLE_ROW);
  }

  @Override
  public boolean onCreate() {
    Log.w(TAG, "onCreate()");
    return true;
  }

  public static Uri getContentUri(PartDatabase.PartId partId) {
    Uri uri = Uri.withAppendedPath(CONTENT_URI, String.valueOf(partId.getUniqueId()));
    return ContentUris.withAppendedId(uri, partId.getRowId());
  }

  private File copyPartToTemporaryFile(MasterSecret masterSecret, PartDatabase.PartId partId) throws IOException {
    InputStream in        = DatabaseFactory.getPartDatabase(getContext()).getPartStream(masterSecret, partId);
    File tmpDir           = getContext().getDir("tmp", 0);
    File tmpFile          = File.createTempFile("test", ".jpg", tmpDir);
    FileOutputStream fout = new FileOutputStream(tmpFile);

    byte[] buffer         = new byte[512];
    int read;

    while ((read = in.read(buffer)) != -1)
      fout.write(buffer, 0, read);

    in.close();

    return tmpFile;
  }

  @Override
  public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
    MasterSecret masterSecret = KeyCachingService.getMasterSecret(getContext());
    Log.w(TAG, "openFile() called!");

    if (masterSecret == null) {
      Log.w(TAG, "masterSecret was null, abandoning.");
      return null;
    }

    switch (uriMatcher.match(uri)) {
    case SINGLE_ROW:
      Log.w(TAG, "Parting out a single row...");
      try {
        PartUriParser        partUri = new PartUriParser(uri);
        File                 tmpFile = copyPartToTemporaryFile(masterSecret, partUri.getPartId());
        ParcelFileDescriptor pdf     = ParcelFileDescriptor.open(tmpFile, ParcelFileDescriptor.MODE_READ_ONLY);

        if (!tmpFile.delete()) {
          Log.w(TAG, "Failed to delete temp file.");
        }

        return pdf;
      } catch (IOException ioe) {
        Log.w(TAG, ioe);
        throw new FileNotFoundException("Error opening file");
      }
    }

    throw new FileNotFoundException("Request for bad part.");
  }

  @Override
  public int delete(Uri arg0, String arg1, String[] arg2) {
    return 0;
  }

  @Override
  public String getType(Uri arg0) {
    return null;
  }

  @Override
  public Uri insert(Uri arg0, ContentValues arg1) {
    return null;
  }

  @Override
  public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {
    return null;
  }

  @Override
  public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
    return 0;
  }
}
