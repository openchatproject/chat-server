package com.openchat.secureim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NexmoConfiguration {

  @JsonProperty
  private String apiKey;

  @JsonProperty
  private String apiSecret;

  @JsonProperty
  private String number;

  public String getApiKey() {
    return apiKey;
  }

  public String getApiSecret() {
    return apiSecret;
  }

  public String getNumber() {
    return number;
  }
}
