package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PreKeyList {

  @JsonProperty
  @NotNull
  @Valid
  private PreKey lastResortKey;

  @JsonProperty
  @NotNull
  @Valid
  private List<PreKey> keys;

  public List<PreKey> getKeys() {
    return keys;
  }

  @VisibleForTesting
  public void setKeys(List<PreKey> keys) {
    this.keys = keys;
  }

  public PreKey getLastResortKey() {
    return lastResortKey;
  }

  @VisibleForTesting
  public void setLastResortKey(PreKey lastResortKey) {
    this.lastResortKey = lastResortKey;
  }
}
