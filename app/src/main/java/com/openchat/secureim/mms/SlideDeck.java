package com.openchat.secureim.mms;

import android.content.Context;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.dom.smil.parser.SmilXmlSerializer;
import com.openchat.secureim.util.SmilUtil;

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
    try {
      for (int i=0;i<body.getPartsNum();i++) {
        String contentType = new String(body.getPart(i).getContentType(), CharacterSets.MIMENAME_ISO_8859_1);
        if (ContentType.isImageType(contentType))
          slides.add(new ImageSlide(context, masterSecret, body.getPart(i)));
        else if (ContentType.isVideoType(contentType))
          slides.add(new VideoSlide(context, body.getPart(i)));
        else if (ContentType.isAudioType(contentType))
          slides.add(new AudioSlide(context, body.getPart(i)));
        else if (ContentType.isTextType(contentType))
          slides.add(new TextSlide(context, masterSecret, body.getPart(i)));
      }
    } catch (UnsupportedEncodingException uee) {
      throw new AssertionError(uee);
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
      body.addPart(slide.getPart());
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SmilXmlSerializer.serialize(SmilUtil.createSmilDocument(this), out);
    PduPart smilPart = new PduPart();
    smilPart.setContentId("smil".getBytes());
    smilPart.setContentLocation("smil.xml".getBytes());
    smilPart.setContentType(ContentType.APP_SMIL.getBytes());
    smilPart.setData(out.toByteArray());
    body.addPart(0, smilPart);

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

}
