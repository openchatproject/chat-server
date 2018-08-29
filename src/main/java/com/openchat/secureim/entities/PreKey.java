package com.openchat.secureim.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PreKey {

  @JsonIgnore
  private long    id;

  @JsonIgnore
  private String  number;

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
  private boolean lastResort;

  public PreKey() {}

  public PreKey(long id, String number, long deviceId, long keyId,
                String publicKey, String identityKey,
                boolean lastResort)
  {
    this.id          = id;
    this.number      = number;
    this.deviceId    = deviceId;
    this.keyId       = keyId;
    this.publicKey   = publicKey;
    this.identityKey = identityKey;
    this.lastResort  = lastResort;
  }

  @XmlTransient
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @XmlTransient
  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public long getKeyId() {
    return keyId;
  }

  public void setKeyId(long keyId) {
    this.keyId = keyId;
  }

  public String getIdentityKey() {
    return identityKey;
  }

  public void setIdentityKey(String identityKey) {
    this.identityKey = identityKey;
  }

  @XmlTransient
  public boolean isLastResort() {
    return lastResort;
  }

  public void setLastResort(boolean lastResort) {
    this.lastResort = lastResort;
  }

  public void setDeviceId(long deviceId) {
    this.deviceId = deviceId;
  }

  public long getDeviceId() {
    return deviceId;
  }
}
