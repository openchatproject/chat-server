package com.openchat.secureim.mms;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import com.openchat.secureim.R;
import com.openchat.secureim.crypto.DecryptingPartInputStream;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.BitmapDecodingException;
import com.openchat.secureim.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.PduPart;

public class ImageSlide extends Slide {
  private static final String TAG = ImageSlide.class.getSimpleName();
  private boolean encrypted = false;

  public ImageSlide(Context context, MasterSecret masterSecret, PduPart part) {
    super(context, masterSecret, part);
  }

  public ImageSlide(Context context, Uri uri) throws IOException, BitmapDecodingException {
    this(context, null, uri);
  }

  public ImageSlide(Context context, MasterSecret masterSecret, Uri uri) throws IOException, BitmapDecodingException {
    super(context, masterSecret, constructPartFromByteArrayAndUri(uri, decryptContent(uri, masterSecret), masterSecret != null));
    encrypted = masterSecret != null;
  }

  @Override
  public Uri getThumbnailUri() {
    if (getPart().getDataUri() != null) {
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

  @Override
  public boolean isEncrypted() {
    return encrypted;
  }

  private static byte[] decryptContent(Uri uri, MasterSecret masterSecret) {
    try {
      if (masterSecret != null) {
        InputStream inputStream = new DecryptingPartInputStream(new File(uri.getPath()), masterSecret);
        return Util.readFully(inputStream);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static PduPart constructPartFromByteArrayAndUri(Uri uri, @Nullable byte[] data, boolean encrypted)
      throws IOException, BitmapDecodingException
  {
    PduPart part = new PduPart();

    part.setDataUri(uri);
    if (data != null)
      part.setData(data);
    part.setEncrypted(encrypted);
    part.setContentType(ContentType.IMAGE_JPEG.getBytes());
    part.setContentId((System.currentTimeMillis()+"").getBytes());
    part.setName(("Image" + System.currentTimeMillis()).getBytes());

    return part;
  }

}
