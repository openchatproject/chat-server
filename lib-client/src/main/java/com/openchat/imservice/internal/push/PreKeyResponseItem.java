package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.openchat.imservice.api.push.SignedPreKeyEntity;

public class PreKeyResponseItem {

  @JsonProperty
  private int                deviceId;

  @JsonProperty
  private int                registrationId;

  @JsonProperty
  private SignedPreKeyEntity signedPreKey;

  @JsonProperty
  private PreKeyEntity       preKey;

  public int getDeviceId() {
    return deviceId;
  }

  public int getRegistrationId() {
    return registrationId;
  }

  public SignedPreKeyEntity getSignedPreKey() {
    return signedPreKey;
  }

  public PreKeyEntity getPreKey() {
    return preKey;
  }

}
