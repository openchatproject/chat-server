package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PreKeyStatus {

  @JsonProperty
  private int count;

  public PreKeyStatus() {}

  public int getCount() {
    return count;
  }
}
