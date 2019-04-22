package com.openchat.secureim.mms;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.support.annotation.DrawableRes;

import com.openchat.secureim.R;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.ResUtil;

import java.io.IOException;

import ws.com.google.android.mms.pdu.PduPart;

public class AudioSlide extends Slide {

  public AudioSlide(Context context, Uri uri) throws IOException, MediaTooLargeException {
    super(context, constructPartFromUri(context, uri));
  }

  public AudioSlide(Context context, MasterSecret masterSecret, PduPart part) {
    super(context, masterSecret, part);
  }

  @Override
    public boolean hasImage() {
    return true;
  }

  @Override
    public boolean hasAudio() {
    return true;
  }

  @Override
  public @DrawableRes int getPlaceholderRes(Theme theme) {
    return ResUtil.getDrawableRes(theme, R.attr.conversation_icon_attach_audio);
  }

  public static PduPart constructPartFromUri(Context context, Uri uri) throws IOException, MediaTooLargeException {
    PduPart part = new PduPart();

    assertMediaSize(context, uri);

    Cursor cursor = null;

    try {
      cursor = context.getContentResolver().query(uri, new String[]{Audio.Media.MIME_TYPE}, null, null, null);

      if (cursor != null && cursor.moveToFirst())
        part.setContentType(cursor.getString(0).getBytes());
      else
        throw new IOException("Unable to query content type.");
    } finally {
      if (cursor != null)
        cursor.close();
    } 

    part.setDataUri(uri);
    part.setContentId((System.currentTimeMillis()+"").getBytes());
    part.setName(("Audio" + System.currentTimeMillis()).getBytes());

    return part;
  }
}
