package com.openchat.secureim.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.widget.ImageView;

import com.makeramen.RoundedDrawable;

import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;

import java.lang.ref.WeakReference;

public class BitmapWorkerRunnable implements Runnable {
  private final static String TAG = BitmapWorkerRunnable.class.getSimpleName();

  private final WeakReference<ImageView> imageViewReference;
  private final Context                  context;
  private final int                      size;
  public final  String                   number;

  public BitmapWorkerRunnable(Context context, ImageView imageView, String number, int size) {
    this.imageViewReference = new WeakReference<>(imageView);
    this.context = context;
    this.size = size;
    this.number = number;
  }

  @Override
  public void run() {
    final Recipient recipient    = RecipientFactory.getRecipientsFromString(context, number, false).getPrimaryRecipient();
    final Drawable  contactPhoto = recipient.getContactPhoto();

    if (contactPhoto != null) {
      final ImageView imageView                  = imageViewReference.get();
      final TaggedFutureTask<?> bitmapWorkerTask = AsyncDrawable.getBitmapWorkerTask(imageView);

      if (bitmapWorkerTask.getTag().equals(number) && imageView != null) {
        imageView.post(new Runnable() {
          @Override
          public void run() {
            imageView.setImageDrawable(contactPhoto);
          }
        });
      }
    }
  }

  public static class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<TaggedFutureTask<?>> bitmapWorkerTaskReference;

    public AsyncDrawable(TaggedFutureTask<?> bitmapWorkerTask) {
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
