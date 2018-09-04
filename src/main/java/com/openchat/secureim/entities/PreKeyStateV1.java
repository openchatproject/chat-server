package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PreKeyStateV1 {

  @JsonProperty
  @NotNull
  @Valid
  private PreKeyV1 lastResortKey;

  @JsonProperty
  @NotNull
  @Valid
  private List<PreKeyV1> keys;

  public List<PreKeyV1> getKeys() {
    return keys;
  }

  @VisibleForTesting
  public void setKeys(List<PreKeyV1> keys) {
    this.keys = keys;
  }

  public PreKeyV1 getLastResortKey() {
    return lastResortKey;
  }

  @VisibleForTesting
  public void setLastResortKey(PreKeyV1 lastResortKey) {
    this.lastResortKey = lastResortKey;
  }
}
