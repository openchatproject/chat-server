package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountAttributes {

  @JsonProperty
  private String  openchatingKey;

  @JsonProperty
  private int     registrationId;

  public AccountAttributes(String openchatingKey, int registrationId) {
    this.openchatingKey   = openchatingKey;
    this.registrationId = registrationId;
  }

  public AccountAttributes() {}

  public String getOpenchatingKey() {
    return openchatingKey;
  }

  public int getRegistrationId() {
    return registrationId;
  }

}
