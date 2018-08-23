package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class GcmRegistrationId {

  @JsonProperty
  @NotEmpty
  private String gcmRegistrationId;

  public String getGcmRegistrationId() {
    return gcmRegistrationId;
  }

}

