package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class AccountAttributes {

  @JsonProperty
  @NotEmpty
  private String signalingKey;

  @JsonProperty
  private boolean supportsSms;

  public AccountAttributes() {}

  public AccountAttributes(String signalingKey, boolean supportsSms) {
    this.signalingKey = signalingKey;
    this.supportsSms  = supportsSms;
  }

  public String getSignalingKey() {
    return signalingKey;
  }

  public boolean getSupportsSms() {
    return supportsSms;
  }

}
