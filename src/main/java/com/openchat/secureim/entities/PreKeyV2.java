package com.openchat.secureim.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class PreKeyV2 implements PreKeyBase {

  @JsonProperty
  @NotNull
  private long    keyId;

  @JsonProperty
  @NotEmpty
  private String  publicKey;

  public PreKeyV2() {}

  public PreKeyV2(long keyId, String publicKey)
  {
    this.keyId     = keyId;
    this.publicKey = publicKey;
  }

  @Override
  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  @Override
  public long getKeyId() {
    return keyId;
  }

  public void setKeyId(long keyId) {
    this.keyId = keyId;
  }

  @Override
  public boolean equals(Object object) {
    if (object == null || !(object instanceof PreKeyV2)) return false;
    PreKeyV2 that = (PreKeyV2)object;

    if (publicKey == null) {
      return this.keyId == that.keyId && that.publicKey == null;
    } else {
      return this.keyId == that.keyId && this.publicKey.equals(that.publicKey);
    }
  }

  @Override
  public int hashCode() {
    if (publicKey == null) {
      return (int)this.keyId;
    } else {
      return ((int)this.keyId) ^ publicKey.hashCode();
    }
  }

}
