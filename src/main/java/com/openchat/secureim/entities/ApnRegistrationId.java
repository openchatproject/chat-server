package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class ApnRegistrationId {

  @JsonProperty
  @NotEmpty
  private String apnRegistrationId;

  public String getApnRegistrationId() {
    return apnRegistrationId;
  }
}
