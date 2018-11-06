package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StaleDevices {

  @JsonProperty
  private List<Integer> staleDevices;

  public List<Integer> getStaleDevices() {
    return staleDevices;
  }
}
