package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

import java.util.List;

public class PreKeyResponseV2 {

  @JsonProperty
  private String identityKey;

  @JsonProperty
  private List<PreKeyResponseItemV2> devices;

  public PreKeyResponseV2() {}

  public PreKeyResponseV2(String identityKey, List<PreKeyResponseItemV2> devices) {
    this.identityKey = identityKey;
    this.devices     = devices;
  }

  @VisibleForTesting
  public String getIdentityKey() {
    return identityKey;
  }

  @VisibleForTesting
  public List<PreKeyResponseItemV2> getDevices() {
    return devices;
  }
}
