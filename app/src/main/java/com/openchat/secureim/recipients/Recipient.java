package com.openchat.secureim.recipients;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.openchat.secureim.contacts.ContactPhotoFactory;
import com.openchat.secureim.recipients.RecipientProvider.RecipientDetails;
import com.openchat.secureim.util.FutureTaskListener;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.secureim.util.ListenableFutureTask;

import java.util.HashSet;

public class Recipient {

  private final static String TAG = Recipient.class.getSimpleName();

  private final HashSet<RecipientModifiedListener> listeners = new HashSet<>();

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
          HashSet<RecipientModifiedListener> localListeners;

          synchronized (Recipient.this) {
            Recipient.this.name                      = result.name;
            Recipient.this.number                    = result.number;
            Recipient.this.contactUri                = result.contactUri;
            Recipient.this.contactPhoto              = result.avatar;

            localListeners                           = (HashSet<RecipientModifiedListener>) listeners.clone();
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

  public synchronized void setContactPhoto(Drawable bitmap) {
    this.contactPhoto = bitmap;
    notifyListeners();
  }

  public synchronized void setName(String name) {
    this.name = name;
    notifyListeners();
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

  public void notifyListeners() {
    HashSet<RecipientModifiedListener> localListeners;

    synchronized (this) {
      localListeners = (HashSet<RecipientModifiedListener>)listeners.clone();
    }

    for (RecipientModifiedListener listener : localListeners) {
      listener.onModified(this);
    }
  }

  public synchronized String toShortString() {
    return (name == null ? number : name);
  }

  public synchronized Drawable getContactPhoto() {
    return contactPhoto;
  }

  public static Recipient getUnknownRecipient(Context context) {
    return new Recipient("Unknown", "Unknown", -1, null,
                         ContactPhotoFactory.getDefaultContactPhoto(null));
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
