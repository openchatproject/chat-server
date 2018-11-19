package com.openchat.imservice.api.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenchatServiceProfile {

  @JsonProperty
  private String identityKey;

  public OpenchatServiceProfile() {}

  public String getIdentityKey() {
    return identityKey;
  }
}
