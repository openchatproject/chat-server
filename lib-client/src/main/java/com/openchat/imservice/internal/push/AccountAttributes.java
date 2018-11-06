package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountAttributes {

  @JsonProperty
  private String  openchatingKey;

  @JsonProperty
  private boolean supportsSms;

  @JsonProperty
  private int     registrationId;

  public AccountAttributes(String openchatingKey, boolean supportsSms, int registrationId) {
    this.openchatingKey   = openchatingKey;
    this.supportsSms    = supportsSms;
    this.registrationId = registrationId;
  }

  public AccountAttributes() {}

  public String getOpenchatingKey() {
    return openchatingKey;
  }

  public boolean isSupportsSms() {
    return supportsSms;
  }

  public int getRegistrationId() {
    return registrationId;
  }
}
