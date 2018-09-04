package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PreKeyResponseV1 {

  @JsonProperty
  @NotNull
  @Valid
  private List<PreKeyV1> keys;

  @VisibleForTesting
  public PreKeyResponseV1() {}

  public PreKeyResponseV1(PreKeyV1 preKey) {
    this.keys = new LinkedList<>();
    this.keys.add(preKey);
  }

  public PreKeyResponseV1(List<PreKeyV1> preKeys) {
    this.keys = preKeys;
  }

  public List<PreKeyV1> getKeys() {
    return keys;
  }

  @VisibleForTesting
  public boolean equals(Object o) {
    if (!(o instanceof PreKeyResponseV1) ||
        ((PreKeyResponseV1) o).keys.size() != keys.size())
      return false;
    Iterator<PreKeyV1> otherKeys = ((PreKeyResponseV1) o).keys.iterator();
    for (PreKeyV1 key : keys) {
      if (!otherKeys.next().equals(key))
        return false;
    }
    return true;
  }

  public int hashCode() {
    int ret = 0xFBA4C795 * keys.size();
    for (PreKeyV1 key : keys)
      ret ^= key.getPublicKey().hashCode();
    return ret;
  }
}
