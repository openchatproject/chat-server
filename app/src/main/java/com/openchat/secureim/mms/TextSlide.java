package com.openchat.secureim.mms;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.LRUCache;

import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Map;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.CharacterSets;
import ws.com.google.android.mms.pdu.PduPart;

public class TextSlide extends Slide {

  private static final int MAX_CACHE_SIZE = 10;
  private static final Map<Uri, SoftReference<String>> textCache =
      Collections.synchronizedMap(new LRUCache<Uri, SoftReference<String>>(MAX_CACHE_SIZE));

  public TextSlide(Context context, MasterSecret masterSecret, PduPart part) {
    super(context, masterSecret, part);
  }

  public TextSlide(Context context, String message) {
    super(context, getPartForMessage(message));
  }

  private static PduPart getPartForMessage(String message) {
    PduPart part = new PduPart();

    try {
      part.setData(message.getBytes(CharacterSets.MIMENAME_UTF_8));

      if (part.getData().length == 0)
        throw new AssertionError("Part data should not be zero!");
            
    } catch (UnsupportedEncodingException e) {
      Log.w("TextSlide", "ISO_8859_1 must be supported!", e);
      part.setData("Unsupported character set!".getBytes());
    }

    part.setCharset(CharacterSets.UTF_8);
    part.setContentType(ContentType.TEXT_PLAIN.getBytes());
    part.setContentId((System.currentTimeMillis()+"").getBytes());
    part.setName(("Text"+System.currentTimeMillis()).getBytes());
        
    return part;
  }
}
