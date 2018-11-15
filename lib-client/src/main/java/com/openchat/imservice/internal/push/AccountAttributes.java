package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountAttributes {

  @JsonProperty
  private String  openchatingKey;

  @JsonProperty
  private int     registrationId;

  @JsonProperty
  private boolean voice;

  @JsonProperty
  private boolean video;

  public AccountAttributes(String openchatingKey, int registrationId, boolean voice, boolean video) {
    this.openchatingKey   = openchatingKey;
    this.registrationId = registrationId;
    this.voice          = voice;
    this.video          = video;
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

  public boolean isVideo() {
    return video;
  }
}
