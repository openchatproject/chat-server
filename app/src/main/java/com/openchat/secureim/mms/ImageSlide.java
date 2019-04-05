package com.openchat.secureim.mms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.openchat.secureim.R;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.util.BitmapDecodingException;
import com.openchat.secureim.util.LRUCache;
import com.openchat.secureim.util.MediaUtil;
import com.openchat.secureim.util.MediaUtil.ThumbnailData;
import com.openchat.secureim.util.SmilUtil;
import com.openchat.secureim.util.Util;
import org.w3c.dom.smil.SMILDocument;
import org.w3c.dom.smil.SMILMediaElement;
import org.w3c.dom.smil.SMILRegionElement;
import com.openchat.secureim.crypto.MasterSecret;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.PduPart;

public class ImageSlide extends Slide {
  private static final String TAG = ImageSlide.class.getSimpleName();

  private static final int MAX_CACHE_SIZE = 10;
  private static final Map<Uri, SoftReference<Drawable>> thumbnailCache =
      Collections.synchronizedMap(new LRUCache<Uri, SoftReference<Drawable>>(MAX_CACHE_SIZE));

  public ImageSlide(Context context, MasterSecret masterSecret, PduPart part) {
    super(context, masterSecret, part);
  }

  public ImageSlide(Context context, Uri uri) throws IOException, BitmapDecodingException {
    super(context, constructPartFromUri(uri));
  }

  @Override
  public Drawable getThumbnail(Context context, int maxWidth, int maxHeight) {
    Drawable thumbnail = getCachedThumbnail();

    if (thumbnail != null) {
      return thumbnail;
    }

    if (part.isPendingPush()) {
      return context.getResources().getDrawable(R.drawable.stat_sys_download);
    }

    try {
      Bitmap thumbnailBitmap;
      long startDecode = System.currentTimeMillis();

      if (part.getDataUri() != null && part.getId() > -1) {
        thumbnailBitmap = BitmapFactory.decodeStream(DatabaseFactory.getPartDatabase(context)
                                                                    .getThumbnailStream(masterSecret, part.getId()));
      } else if (part.getDataUri() != null) {
        Log.w(TAG, "generating thumbnail for new part");
        ThumbnailData thumbnailData = MediaUtil.generateThumbnail(context, masterSecret,
                                                                  part.getDataUri(), Util.toIsoString(part.getContentType()));
        thumbnailBitmap = thumbnailData.getBitmap();
        part.setThumbnail(thumbnailBitmap);
      } else {
        throw new FileNotFoundException("no data location specified");
      }

      Log.w(TAG, "thumbnail decode/generate time: " + (System.currentTimeMillis() - startDecode) + "ms");

      thumbnail = new BitmapDrawable(context.getResources(), thumbnailBitmap);
      thumbnailCache.put(part.getDataUri(), new SoftReference<>(thumbnail));

      return thumbnail;
    } catch (IOException | BitmapDecodingException e) {
      Log.w(TAG, e);
      return context.getResources().getDrawable(R.drawable.ic_missing_thumbnail_picture);
    }
  }

  @Override
  public void setThumbnailOn(Context context, ImageView imageView) {
    setThumbnailOn(context, imageView, imageView.getWidth(), imageView.getHeight(), new ColorDrawable(Color.TRANSPARENT));
  }

  @Override
  public void setThumbnailOn(Context context, ImageView imageView, final int width, final int height, final Drawable placeholder) {
    Drawable thumbnail = getCachedThumbnail();

    if (thumbnail != null) {
      Log.w("ImageSlide", "Setting cached thumbnail...");
      setThumbnailOn(imageView, thumbnail, true);
      return;
    }

    final WeakReference<Context>   weakContext   = new WeakReference<>(context);
    final WeakReference<ImageView> weakImageView = new WeakReference<>(imageView);
    final Handler handler                        = new Handler();

    imageView.setImageDrawable(placeholder);

    if (width == 0 || height == 0)
      return;

    MmsDatabase.slideResolver.execute(new Runnable() {
      @Override
      public void run() {
        final Context context = weakContext.get();
        if (context == null) {
          Log.w(TAG, "context SoftReference was null, leaving");
          return;
        }

        final Drawable bitmap = getThumbnail(context, width, height);
        final ImageView destination = weakImageView.get();

        Log.w(TAG, "slide resolved, destination available? " + (destination == null));
        if (destination != null && destination.getDrawable() == placeholder) {
          handler.post(new Runnable() {
            @Override
            public void run() {
              setThumbnailOn(destination, bitmap, false);
            }
          });
        }
      }
    });
  }

  private void setThumbnailOn(ImageView imageView, Drawable thumbnail, boolean fromMemory) {
    if (fromMemory) {
      imageView.setImageDrawable(thumbnail);
    } else if (thumbnail instanceof AnimationDrawable) {
      imageView.setImageDrawable(thumbnail);
      ((AnimationDrawable)imageView.getDrawable()).start();
    } else {
      TransitionDrawable fadingResult = new TransitionDrawable(new Drawable[]{imageView.getDrawable(), thumbnail});
      imageView.setImageDrawable(fadingResult);
      fadingResult.startTransition(300);
    }
  }

  private Drawable getCachedThumbnail() {
    synchronized (thumbnailCache) {
      SoftReference<Drawable> bitmapReference = thumbnailCache.get(part.getDataUri());
      Log.w("ImageSlide", "Got soft reference: " + bitmapReference);

      if (bitmapReference != null) {
        Drawable bitmap = bitmapReference.get();
        Log.w("ImageSlide", "Got cached bitmap: " + bitmap);
        if (bitmap != null) return bitmap;
        else                thumbnailCache.remove(part.getDataUri());
      }
    }

    return null;
  }

  @Override
  public boolean hasImage() {
    return true;
  }

  @Override
  public SMILRegionElement getSmilRegion(SMILDocument document) {
    SMILRegionElement region = (SMILRegionElement) document.createElement("region");
    region.setId("Image");
    region.setLeft(0);
    region.setTop(0);
    region.setWidth(SmilUtil.ROOT_WIDTH);
    region.setHeight(SmilUtil.ROOT_HEIGHT);
    region.setFit("meet");
    return region;
  }

  @Override
  public SMILMediaElement getMediaElement(SMILDocument document) {
    return SmilUtil.createMediaElement("img", document, new String(getPart().getName()));
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
