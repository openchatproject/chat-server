package com.openchat.imservice.api.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactTokenDetails {

  @JsonProperty
  private String  token;

  @JsonProperty
  private String  relay;

  @JsonProperty
  private String  number;

  @JsonProperty
  private boolean voice;

  public ContactTokenDetails() {}

  
  public String getToken() {
    return token;
  }

  
  public String getRelay() {
    return relay;
  }

  
  public boolean isVoice() {
    return voice;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  
  public String getNumber() {
    return number;
  }
}
