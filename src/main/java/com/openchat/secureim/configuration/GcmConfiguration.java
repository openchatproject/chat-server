package com.openchat.secureim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class GcmConfiguration {

  @NotNull
  @JsonProperty
  private long senderId;

  @NotEmpty
  @JsonProperty
  private String apiKey;

  public String getApiKey() {
    return apiKey;
  }

  public long getSenderId() {
    return senderId;
  }

}
