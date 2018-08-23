package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PreKeyList {

  @JsonProperty
  @NotNull
  private PreKey lastResortKey;

  @JsonProperty
  @NotNull
  @Valid
  private List<PreKey> keys;

  public List<PreKey> getKeys() {
    return keys;
  }

  public PreKey getLastResortKey() {
    return lastResortKey;
  }
}
