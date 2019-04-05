package com.openchat.secureim.mms;

import java.io.IOException;

import com.openchat.secureim.R;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.SmilUtil;
import com.openchat.secureim.util.ThemeUtil;
import org.w3c.dom.smil.SMILDocument;
import org.w3c.dom.smil.SMILMediaElement;
import org.w3c.dom.smil.SMILRegionElement;
import org.w3c.dom.smil.SMILRegionMediaElement;

import ws.com.google.android.mms.pdu.PduPart;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore.Audio;

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
  public SMILRegionElement getSmilRegion(SMILDocument document) {
    return null;
  }

  @Override
  public SMILMediaElement getMediaElement(SMILDocument document) {
    return SmilUtil.createMediaElement("audio", document, new String(getPart().getName()));
  }

  @Override
  public Drawable getThumbnail(Context context, int maxWidth, int maxHeight) {
    return ThemeUtil.resolveIcon(context, R.attr.conversation_icon_attach_audio);
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
