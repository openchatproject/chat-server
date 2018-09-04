package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

public class PreKeyResponseItemV2 {

  @JsonProperty
  private long deviceId;

  @JsonProperty
  private int registrationId;

  @JsonProperty
  private SignedPreKey signedPreKey;

  @JsonProperty
  private PreKeyV2 preKey;

  public PreKeyResponseItemV2() {}

  public PreKeyResponseItemV2(long deviceId, int registrationId, SignedPreKey signedPreKey, PreKeyV2 preKey) {
    this.deviceId       = deviceId;
    this.registrationId = registrationId;
    this.signedPreKey   = signedPreKey;
    this.preKey         = preKey;
  }

  @VisibleForTesting
  public SignedPreKey getSignedPreKey() {
    return signedPreKey;
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
