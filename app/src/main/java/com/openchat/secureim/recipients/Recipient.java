package com.openchat.secureim.recipients;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.openchat.secureim.contacts.ContactPhotoFactory;
import com.openchat.secureim.recipients.RecipientProvider.RecipientDetails;
import com.openchat.secureim.util.FutureTaskListener;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.secureim.util.ListenableFutureTask;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

public class Recipient {

  private final static String TAG = Recipient.class.getSimpleName();

  private final Set<RecipientModifiedListener> listeners = Collections.newSetFromMap(new WeakHashMap<RecipientModifiedListener, Boolean>());

  private final long recipientId;

  private String number;
  private String name;

  private Drawable contactPhoto;
  private Uri      contactUri;

  Recipient(String number, Drawable contactPhoto,
            long recipientId, ListenableFutureTask<RecipientDetails> future)
  {
    this.number                     = number;
    this.contactPhoto               = contactPhoto;
    this.recipientId                = recipientId;

    future.addListener(new FutureTaskListener<RecipientDetails>() {
      @Override
      public void onSuccess(RecipientDetails result) {
        if (result != null) {
          Set<RecipientModifiedListener> localListeners;

          synchronized (Recipient.this) {
            Recipient.this.name                      = result.name;
            Recipient.this.number                    = result.number;
            Recipient.this.contactUri                = result.contactUri;
            Recipient.this.contactPhoto              = result.avatar;

            localListeners                           = new HashSet<>(listeners);
            listeners.clear();
          }

          for (RecipientModifiedListener listener : localListeners)
            listener.onModified(Recipient.this);
        }
      }

      @Override
      public void onFailure(Throwable error) {
        Log.w("Recipient", error);
      }
    });
  }

  Recipient(String name, String number, long recipientId, Uri contactUri, Drawable contactPhoto) {
    this.number                     = number;
    this.recipientId                = recipientId;
    this.contactUri                 = contactUri;
    this.name                       = name;
    this.contactPhoto               = contactPhoto;
  }

  public synchronized Uri getContactUri() {
    return this.contactUri;
  }

  public synchronized String getName() {
    return this.name;
  }

  public String getNumber() {
    return number;
  }

  public long getRecipientId() {
    return recipientId;
  }

  public boolean isGroupRecipient() {
    return GroupUtil.isEncodedGroup(number);
  }

  public synchronized void addListener(RecipientModifiedListener listener) {
    listeners.add(listener);
  }

  public synchronized void removeListener(RecipientModifiedListener listener) {
    listeners.remove(listener);
  }

  public synchronized String toShortString() {
    return (name == null ? number : name);
  }

  public synchronized Drawable getContactPhoto() {
    return contactPhoto;
  }

  public static Recipient getUnknownRecipient(Context context) {
    return new Recipient("Unknown", "Unknown", -1, null,
                         ContactPhotoFactory.getDefaultContactPhoto(context, null));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof Recipient)) return false;

    Recipient that = (Recipient) o;

    return this.recipientId == that.recipientId;
  }

  @Override
  public int hashCode() {
    return 31 + (int)this.recipientId;
  }

  public interface RecipientModifiedListener {
    public void onModified(Recipient recipient);
  }
}
