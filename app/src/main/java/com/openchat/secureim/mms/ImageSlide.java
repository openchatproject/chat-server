package com.openchat.secureim.mms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.R;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.util.BitmapDecodingException;
import com.openchat.secureim.util.LRUCache;
import com.openchat.secureim.util.ListenableFutureTask;
import com.openchat.secureim.util.MediaUtil;
import com.openchat.secureim.util.ResUtil;
import com.openchat.secureim.util.SmilUtil;
import org.w3c.dom.smil.SMILDocument;
import org.w3c.dom.smil.SMILMediaElement;
import org.w3c.dom.smil.SMILRegionElement;
import com.openchat.secureim.crypto.MasterSecret;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;

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
  public ListenableFutureTask<Pair<Drawable,Boolean>> getThumbnail(Context context) {
    if (getPart().isPendingPush()) {
      return new ListenableFutureTask<>(new Pair<>(context.getResources().getDrawable(R.drawable.stat_sys_download), true));
    }

    Drawable thumbnail = getCachedThumbnail();
    if (thumbnail != null) {
      Log.w(TAG, "getThumbnail() returning cached thumbnail");
      return new ListenableFutureTask<>(new Pair<>(thumbnail, true));
    }

    Log.w(TAG, "getThumbnail() resolving thumbnail, as it wasn't cached");
    return resolveThumbnail(context);
  }

  private ListenableFutureTask<Pair<Drawable,Boolean>> resolveThumbnail(Context context) {
    final WeakReference<Context> weakContext = new WeakReference<>(context);

    Callable<Pair<Drawable,Boolean>> slideCallable = new Callable<Pair<Drawable, Boolean>>() {
      @Override
      public Pair<Drawable, Boolean> call() throws Exception {
        final Context context = weakContext.get();
        if (context == null) {
          Log.w(TAG, "context SoftReference was null, leaving");
          return null;
        }

        try {
          final long     startDecode     = System.currentTimeMillis();
          final Bitmap   thumbnailBitmap = MediaUtil.getOrGenerateThumbnail(context, masterSecret, part);
          final Drawable thumbnail       = new BitmapDrawable(context.getResources(), thumbnailBitmap);
          Log.w(TAG, "thumbnail decode/generate time: " + (System.currentTimeMillis() - startDecode) + "ms");

          thumbnailCache.put(part.getDataUri(), new SoftReference<>(thumbnail));
          return new Pair<>(thumbnail, false);
        } catch (IOException | BitmapDecodingException e) {
          Log.w(TAG, e);
          return new Pair<>(context.getResources().getDrawable(R.drawable.ic_missing_thumbnail_picture), false);
        }
      }
    };
    ListenableFutureTask<Pair<Drawable,Boolean>> futureTask = new ListenableFutureTask<>(slideCallable);
    MmsDatabase.slideResolver.execute(futureTask);
    return futureTask;
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
