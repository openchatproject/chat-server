package com.openchat.protocal.util;

import com.openchat.protocal.IdentityKey;

import java.util.Comparator;

public class IdentityKeyComparator extends ByteArrayComparator implements Comparator<IdentityKey> {

  @Override
  public int compare(IdentityKey first, IdentityKey second) {
    return compare(first.getPublicKey().serialize(), second.getPublicKey().serialize());
  }
}
