package com.openchat.secureim.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PreKeyV1 implements PreKeyBase {

  @JsonProperty
  private long deviceId;

  @JsonProperty
  @NotNull
  private long    keyId;

  @JsonProperty
  @NotNull
  private String  publicKey;

  @JsonProperty
  @NotNull
  private String  identityKey;

  @JsonProperty
  private int registrationId;

  public PreKeyV1() {}

  public PreKeyV1(long deviceId, long keyId, String publicKey, String identityKey, int registrationId)
  {
    this.deviceId       = deviceId;
    this.keyId          = keyId;
    this.publicKey      = publicKey;
    this.identityKey    = identityKey;
    this.registrationId = registrationId;
  }

  @VisibleForTesting
  public PreKeyV1(long deviceId, long keyId, String publicKey, String identityKey)
  {
    this.deviceId    = deviceId;
    this.keyId       = keyId;
    this.publicKey   = publicKey;
    this.identityKey = identityKey;
  }

  @Override
  public String getPublicKey() {
    return publicKey;
  }

  @Override
  public long getKeyId() {
    return keyId;
  }

  public String getIdentityKey() {
    return identityKey;
  }

  public void setDeviceId(long deviceId) {
    this.deviceId = deviceId;
  }

  public long getDeviceId() {
    return deviceId;
  }

  public int getRegistrationId() {
    return registrationId;
  }

  public void setRegistrationId(int registrationId) {
    this.registrationId = registrationId;
  }
}
