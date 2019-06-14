package com.openchat.secureim.mms;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.openchat.secureim.R;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.dom.smil.parser.SmilXmlSerializer;
import com.openchat.secureim.util.ListenableFutureTask;
import com.openchat.secureim.util.MediaUtil;
import com.openchat.secureim.util.SmilUtil;
import com.openchat.secureim.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.CharacterSets;
import ws.com.google.android.mms.pdu.PduBody;
import ws.com.google.android.mms.pdu.PduPart;

public class SlideDeck {

  private final List<Slide> slides = new LinkedList<>();

  public SlideDeck(SlideDeck copy) {
    this.slides.addAll(copy.getSlides());
  }

  public SlideDeck(Context context, MasterSecret masterSecret, PduBody body) {
    for (int i=0;i<body.getPartsNum();i++) {
      String contentType = Util.toIsoString(body.getPart(i).getContentType());
      Slide  slide       = MediaUtil.getSlideForPart(context, masterSecret, body.getPart(i), contentType);
      if (slide != null) slides.add(slide);
    }
  }

  public SlideDeck() {
  }

  public void clear() {
    slides.clear();
  }

  public PduBody toPduBody() {
    PduBody body = new PduBody();

    for (Slide slide : slides) {
      PduPart part = slide.getPart();
      body.addPart(part);
    }

    return body;
  }

  public void addSlide(Slide slide) {
    slides.add(slide);
  }

  public List<Slide> getSlides() {
    return slides;
  }

  public boolean containsMediaSlide() {
    for (Slide slide : slides) {
      if (slide.hasImage() || slide.hasVideo() || slide.hasAudio()) {
        return true;
      }
    }
    return false;
  }

  public @Nullable Slide getThumbnailSlide() {
    for (Slide slide : slides) {
      if (slide.hasImage()) {
        return slide;
      }
    }
    return null;
  }
}
