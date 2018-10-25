package com.openchat.push.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;


public class ApnConfiguration {

  @NotEmpty
  @JsonProperty
  private String certificate;

  @NotEmpty
  @JsonProperty
  private String key;

  @JsonProperty
  private boolean feedback = true;

  public String getCertificate() {
    return certificate;
  }

  public String getKey() {
    return key;
  }

  public boolean isFeedbackEnabled() {
    return feedback;
  }
}
