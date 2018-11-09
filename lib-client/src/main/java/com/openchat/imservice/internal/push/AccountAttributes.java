package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountAttributes {

  @JsonProperty
  private String  openchatingKey;

  @JsonProperty
  private int     registrationId;

  @JsonProperty
  private boolean voice;

  public AccountAttributes(String openchatingKey, int registrationId, boolean voice) {
    this.openchatingKey   = openchatingKey;
    this.registrationId = registrationId;
    this.voice          = voice;
  }

  public AccountAttributes() {}

  public String getOpenchatingKey() {
    return openchatingKey;
  }

  public int getRegistrationId() {
    return registrationId;
  }

  public boolean isVoice() {
    return voice;
  }
}
