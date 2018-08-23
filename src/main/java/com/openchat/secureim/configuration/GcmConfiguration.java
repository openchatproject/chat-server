package com.openchat.secureim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class GcmConfiguration {

  @NotEmpty
  @JsonProperty
  private String apiKey;

  public String getApiKey() {
    return apiKey;
  }
}
