package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MismatchedDevices {
  @JsonProperty
  private List<Integer> missingDevices;

  @JsonProperty
  private List<Integer> extraDevices;

  public List<Integer> getMissingDevices() {
    return missingDevices;
  }

  public List<Integer> getExtraDevices() {
    return extraDevices;
  }
}
