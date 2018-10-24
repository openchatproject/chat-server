package com.openchat.messaging.server.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GcmResponseListEntity {

  @JsonProperty
  private List<GcmResponseEntity> results;

  public List<GcmResponseEntity> getResults() {
    return results;
  }
}
