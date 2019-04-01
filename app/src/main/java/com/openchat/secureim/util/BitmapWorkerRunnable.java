package com.openchat.secureim.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.makeramen.RoundedDrawable;

import com.openchat.secureim.R;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;

import java.lang.ref.WeakReference;

public class BitmapWorkerRunnable implements Runnable {
  private final static String TAG = BitmapWorkerRunnable.class.getSimpleName();

  private final Bitmap defaultPhoto;

  private final WeakReference<ImageView> imageViewReference;
  private final Context                  context;
  private final int                      size;
  public final  String                   number;

  public BitmapWorkerRunnable(Context context, ImageView imageView, Bitmap defaultPhoto, String number, int size) {
    this.imageViewReference = new WeakReference<>(imageView);
    this.context = context;
    this.defaultPhoto = defaultPhoto;
    this.size = size;
    this.number = number;
  }

  @Override
  public void run() {
    try {
      final Recipient recipient = RecipientFactory.getRecipientsFromString(context, number, false).getPrimaryRecipient();
      final Bitmap contactPhoto = recipient.getContactPhoto();
      if (defaultPhoto == contactPhoto) {
        return;
      }
      if (recipient.getContactPhoto() != null) {
        final ImageView imageView                  = imageViewReference.get();
        final TaggedFutureTask<?> bitmapWorkerTask = AsyncDrawable.getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask.getTag().equals(number) && imageView != null) {
          final BitmapDrawable drawable = new BitmapDrawable(context.getResources(), recipient.getContactPhoto());
          imageView.post(new Runnable() {
            @Override
            public void run() {
              imageView.setImageDrawable(drawable);
            }
          });
        }
      }
    } catch (RecipientFormattingException rfe) {
      Log.w(TAG, "Couldn't get recipient from string", rfe);
    }
  }

  public static class AsyncDrawable extends RoundedDrawable {
    private final WeakReference<TaggedFutureTask<?>> bitmapWorkerTaskReference;

    public AsyncDrawable(Bitmap bitmap, TaggedFutureTask<?> bitmapWorkerTask) {
      super(bitmap);
      bitmapWorkerTaskReference =
          new WeakReference<TaggedFutureTask<?>>(bitmapWorkerTask);
    }

    public TaggedFutureTask<?> getBitmapWorkerTask() {
      return bitmapWorkerTaskReference.get();
    }

    public static TaggedFutureTask<?> getBitmapWorkerTask(ImageView imageView) {
      if (imageView != null) {
        final Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AsyncDrawable) {
          final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
          return asyncDrawable.getBitmapWorkerTask();
        }
      }
      return null;
    }
  }
}
