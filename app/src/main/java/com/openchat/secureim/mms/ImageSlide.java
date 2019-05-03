package com.openchat.secureim.mms;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.support.annotation.DrawableRes;

import com.openchat.secureim.R;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.BitmapDecodingException;

import java.io.IOException;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.PduPart;

public class ImageSlide extends Slide {
  private static final String TAG = ImageSlide.class.getSimpleName();

  public ImageSlide(Context context, MasterSecret masterSecret, PduPart part) {
    super(context, masterSecret, part);
  }

  public ImageSlide(Context context, Uri uri) throws IOException, BitmapDecodingException {
    super(context, constructPartFromUri(uri));
  }

  @Override
  public Uri getThumbnailUri() {
    if (!getPart().isPendingPush() && getPart().getDataUri() != null) {
      return isDraft()
             ? getPart().getDataUri()
             : PartAuthority.getThumbnailUri(getPart().getPartId());
    }

    return null;
  }

  @Override
  public @DrawableRes int getPlaceholderRes(Theme theme) {
    return R.drawable.ic_missing_thumbnail_picture;
  }

  @Override
  public boolean hasImage() {
    return true;
  }

  private static PduPart constructPartFromUri(Uri uri)
      throws IOException, BitmapDecodingException
  {
    PduPart part = new PduPart();

    part.setDataUri(uri);
    part.setContentType(ContentType.IMAGE_JPEG.getBytes());
    part.setContentId((System.currentTimeMillis()+"").getBytes());
    part.setName(("Image" + System.currentTimeMillis()).getBytes());

    return part;
  }

}
