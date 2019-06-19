package com.openchat.secureim.mms;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.MediaUtil;
import com.openchat.secureim.util.Util;

import java.io.IOException;
import java.io.InputStream;

import ws.com.google.android.mms.pdu.PduPart;

public abstract class Slide {

  protected final PduPart part;
  protected final Context context;

  public Slide(Context context, @NonNull PduPart part) {
    this.part    = part;
    this.context = context;
  }

  public String getContentType() {
    return new String(part.getContentType());
  }

  public Uri getUri() {
    return part.getDataUri();
  }

  public boolean hasImage() {
    return false;
  }

  public boolean hasVideo() {
    return false;
  }

  public boolean hasAudio() {
    return false;
  }

  public PduPart getPart() {
    return part;
  }

  public Uri getThumbnailUri() {
    return null;
  }

  public boolean isInProgress() {
    return part.isInProgress();
  }

  public long getTransferProgress() {
    return part.getTransferProgress();
  }

  public @DrawableRes int getPlaceholderRes(Theme theme) {
    throw new AssertionError("getPlaceholderRes() called for non-drawable slide");
  }

  public boolean isDraft() {
    return !getPart().getPartId().isValid();
  }

  protected static PduPart constructPartFromUri(@NonNull  Context      context,
                                                @NonNull  Uri          uri,
                                                @NonNull  String       defaultMime,
                                                          long         dataSize)
      throws IOException
  {
    final PduPart part            = new PduPart();
    final String  mimeType        = MediaUtil.getMimeType(context, uri);
    final String  derivedMimeType = mimeType != null ? mimeType : defaultMime;

    part.setDataSize(dataSize);
    part.setDataUri(uri);
    part.setContentType(derivedMimeType.getBytes());
    part.setContentId((System.currentTimeMillis()+"").getBytes());
    part.setName((MediaUtil.getDiscreteMimeType(derivedMimeType) + System.currentTimeMillis()).getBytes());

    return part;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Slide)) return false;

    Slide that = (Slide)other;

    return Util.equals(this.getContentType(), that.getContentType()) &&
           this.hasAudio() == that.hasAudio()                        &&
           this.hasImage() == that.hasImage()                        &&
           this.hasVideo() == that.hasVideo()                        &&
           this.isDraft() == that.isDraft()                          &&
           this.getTransferProgress() == that.getTransferProgress()  &&
           Util.equals(this.getUri(), that.getUri())                 &&
           Util.equals(this.getThumbnailUri(), that.getThumbnailUri());
  }

  @Override
  public int hashCode() {
    return Util.hashCode(getContentType(), hasAudio(), hasImage(),
                         hasVideo(), isDraft(), getUri(), getThumbnailUri(), getTransferProgress());
  }
}
