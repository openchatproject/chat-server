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

  @JsonProperty
  private boolean fetchesMessages;

  @JsonProperty
  private String pin;

  public AccountAttributes(String openchatingKey, int registrationId, boolean fetchesMessages, String pin) {
    this.openchatingKey    = openchatingKey;
    this.registrationId  = registrationId;
    this.voice           = true;
    this.video           = true;
    this.fetchesMessages = fetchesMessages;
    this.pin             = pin;
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

  public boolean isFetchesMessages() {
    return fetchesMessages;
  }

  public String getPin() {
    return pin;
  }
}
