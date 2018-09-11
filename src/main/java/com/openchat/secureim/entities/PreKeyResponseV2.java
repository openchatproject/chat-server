package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @JsonIgnore
  public PreKeyResponseItemV2 getDevice(int deviceId) {
    for (PreKeyResponseItemV2 device : devices) {
      if (device.getDeviceId() == deviceId) return device;
    }

    return null;
  }

  @VisibleForTesting
  @JsonIgnore
  public int getDevicesCount() {
    return devices.size();
  }

}
