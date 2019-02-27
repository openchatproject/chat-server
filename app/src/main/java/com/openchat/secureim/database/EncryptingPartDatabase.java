package com.openchat.secureim.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.openchat.secureim.crypto.DecryptingPartInputStream;
import com.openchat.secureim.crypto.EncryptingPartOutputStream;
import com.openchat.imservice.crypto.MasterSecret;

import ws.com.google.android.mms.pdu.PduPart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class EncryptingPartDatabase extends PartDatabase {

  private final MasterSecret masterSecret;

  public EncryptingPartDatabase(Context context, SQLiteOpenHelper databaseHelper, MasterSecret masterSecret) {
    super(context, databaseHelper);
    this.masterSecret = masterSecret;
  }

  @Override
  protected FileInputStream getPartInputStream(File path, PduPart part) throws FileNotFoundException {
    Log.w("EncryptingPartDatabase", "Getting part at: " + path.getAbsolutePath());
    if (!part.getEncrypted())
      return super.getPartInputStream(path, part);

    return new DecryptingPartInputStream(path, masterSecret);
  }

  @Override
  protected FileOutputStream getPartOutputStream(File path, PduPart part) throws FileNotFoundException {
    Log.w("EncryptingPartDatabase", "Writing part to: " + path.getAbsolutePath());
    part.setEncrypted(true);
    return new EncryptingPartOutputStream(path, masterSecret);
  }
}
