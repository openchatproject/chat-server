package com.openchat.secureim.mms;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.CharacterSets;
import ws.com.google.android.mms.pdu.PduPart;

public class TextSlide extends Slide {

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
