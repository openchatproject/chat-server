package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;

public class UnstructuredPreKeyList {
  @JsonProperty
  @NotNull
  @Valid
  private List<PreKey> keys;

  public UnstructuredPreKeyList(List<PreKey> preKeys) {
    this.keys = preKeys;
  }

  public List<PreKey> getKeys() {
    return keys;
  }

  @VisibleForTesting public boolean equals(Object o) {
    if (!(o instanceof UnstructuredPreKeyList) ||
        ((UnstructuredPreKeyList) o).keys.size() != keys.size())
      return false;
    Iterator<PreKey> otherKeys = ((UnstructuredPreKeyList) o).keys.iterator();
    for (PreKey key : keys) {
      if (!otherKeys.next().equals(key))
        return false;
    }
    return true;
  }
}
