package com.openchat.secureim.recipients;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Patterns;

import com.openchat.secureim.recipients.Recipient.RecipientModifiedListener;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.secureim.util.NumberUtil;
import com.openchat.secureim.util.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Recipients implements Parcelable {

  public static final Parcelable.Creator<Recipients> CREATOR = new Parcelable.Creator<Recipients>() {
    public Recipients createFromParcel(Parcel in) {
      return new Recipients(in);
    }

    public Recipients[] newArray(int size) {
      return new Recipients[size];
    }
  };

  private List<Recipient> recipients;

  public Recipients(List<Recipient> recipients) {
    this.recipients = recipients;
  }

  public Recipients(final Recipient recipient) {
    this.recipients = new LinkedList<Recipient>() {{
      add(recipient);
    }};
  }

  public Recipients(Parcel in) {
    this.recipients = new ArrayList<Recipient>();
    in.readTypedList(recipients, Recipient.CREATOR);
  }

  public void append(Recipients recipients) {
    this.recipients.addAll(recipients.getRecipientsList());
  }

  public void addListener(RecipientModifiedListener listener) {
    for (Recipient recipient : recipients) {
      recipient.addListener(listener);
    }
  }

  public void removeListener(RecipientModifiedListener listener) {
    for (Recipient recipient : recipients) {
      recipient.removeListener(listener);
    }
  }

  public boolean isEmailRecipient() {
    for (Recipient recipient : recipients) {
      if (NumberUtil.isValidEmail(recipient.getNumber()))
        return true;
    }

    return false;
  }

  public boolean isGroupRecipient() {
    return isSingleRecipient() && GroupUtil.isEncodedGroup(recipients.get(0).getNumber());
  }

  public boolean isEmpty() {
    return this.recipients.isEmpty();
  }

  public boolean isSingleRecipient() {
    return this.recipients.size() == 1;
  }

  public Recipient getPrimaryRecipient() {
    if (!isEmpty())
      return this.recipients.get(0);
    else
      return null;
  }

  public List<Recipient> getRecipientsList() {
    return this.recipients;
  }

  public String toIdString() {
    List<String> ids = new LinkedList<String>();

    for (Recipient recipient : recipients) {
      ids.add(String.valueOf(recipient.getRecipientId()));
    }

    return Util.join(ids, " ");
  }

  public String[] toNumberStringArray(boolean scrub) {
    String[] recipientsArray     = new String[recipients.size()];
    Iterator<Recipient> iterator = recipients.iterator();
    int i                        = 0;

    while (iterator.hasNext()) {
      String number = iterator.next().getNumber();

      if (scrub && number != null &&
          !Patterns.EMAIL_ADDRESS.matcher(number).matches() &&
          !GroupUtil.isEncodedGroup(number))
      {
        number = number.replaceAll("[^0-9+]", "");
      }

      recipientsArray[i++] = number;
    }

    return recipientsArray;
  }

  public String toShortString() {
    String fromString = "";

    for (int i=0;i<recipients.size();i++) {
      fromString += recipients.get(i).toShortString();

      if (i != recipients.size() -1 )
        fromString += ", ";
    }

    return fromString;
  }

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(recipients);
  }
}
