package com.openchat.imservice.api.profiles;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenchatServiceProfile {

  @JsonProperty
  private String identityKey;

  @JsonProperty
  private String name;

  @JsonProperty
  private String avatar;

  public OpenchatServiceProfile() {}

  public String getIdentityKey() {
    return identityKey;
  }

  public String getName() {
    return name;
  }

  public String getAvatar() {
    return avatar;
  }
}
