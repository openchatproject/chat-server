package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

public class PreKeyResponseItemV2 {

  @JsonProperty
  private long deviceId;

  @JsonProperty
  private int registrationId;

  @JsonProperty
  private DeviceKey deviceKey;

  @JsonProperty
  private PreKeyV2 preKey;

  public PreKeyResponseItemV2() {}

  public PreKeyResponseItemV2(long deviceId, int registrationId, DeviceKey deviceKey, PreKeyV2 preKey) {
    this.deviceId       = deviceId;
    this.registrationId = registrationId;
    this.deviceKey      = deviceKey;
    this.preKey         = preKey;
  }

  @VisibleForTesting
  public DeviceKey getDeviceKey() {
    return deviceKey;
  }

  @VisibleForTesting
  public PreKeyV2 getPreKey() {
    return preKey;
  }

  @VisibleForTesting
  public int getRegistrationId() {
    return registrationId;
  }

  @VisibleForTesting
  public long getDeviceId() {
    return deviceId;
  }
}
