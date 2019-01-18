package com.openchat.secureim.crypto;

import android.os.Parcel;
import android.os.Parcelable;

import com.openchat.libim.IdentityKey;
import com.openchat.libim.InvalidKeyException;

public class IdentityKeyParcelable implements Parcelable {

  public static final Parcelable.Creator<IdentityKeyParcelable> CREATOR = new Parcelable.Creator<IdentityKeyParcelable>() {
    public IdentityKeyParcelable createFromParcel(Parcel in) {
      try {
        return new IdentityKeyParcelable(in);
      } catch (InvalidKeyException e) {
        throw new AssertionError(e);
      }
    }

    public IdentityKeyParcelable[] newArray(int size) {
      return new IdentityKeyParcelable[size];
    }
  };

  private final IdentityKey identityKey;

  public IdentityKeyParcelable(IdentityKey identityKey) {
    this.identityKey = identityKey;
  }

  public IdentityKeyParcelable(Parcel in) throws InvalidKeyException {
    int    serializedLength = in.readInt();
    byte[] serialized       = new byte[serializedLength];

    in.readByteArray(serialized);
    this.identityKey = new IdentityKey(serialized, 0);
  }

  public IdentityKey get() {
    return identityKey;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(identityKey.serialize().length);
    dest.writeByteArray(identityKey.serialize());
  }
}
