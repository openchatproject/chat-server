package com.openchat.secureim.mms;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.BitmapDecodingException;

import java.io.IOException;

import ws.com.google.android.mms.pdu.PduPart;

public class GifSlide extends ImageSlide {
  public GifSlide(Context context, MasterSecret masterSecret, PduPart part) {
    super(context, masterSecret, part);
  }

  public GifSlide(Context context, MasterSecret masterSecret, Uri uri)
      throws IOException, BitmapDecodingException, MediaTooLargeException
  {
    super(context, masterSecret, uri);
    assertMediaSize();
  }

  private void assertMediaSize() throws MediaTooLargeException, IOException {
    assertMediaSize(context, getPart().getDataUri(), MediaConstraints.PUSH_CONSTRAINTS.getGifMaxSize());
    if (!MediaConstraints.PUSH_CONSTRAINTS.isSatisfied(context, masterSecret, part)) {
      throw new MediaTooLargeException("Media exceeds maximum message size.");
    }
  }

  @Override public Uri getThumbnailUri() {
    return getPart().getDataUri();
  }
}
