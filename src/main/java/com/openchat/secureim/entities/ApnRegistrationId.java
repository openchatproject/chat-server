package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class ApnRegistrationId {

  @JsonProperty
  @NotEmpty
  private String apnRegistrationId;

  @JsonProperty
  private String voipRegistrationId;

  public String getApnRegistrationId() {
    return apnRegistrationId;
  }

  public String getVoipRegistrationId() {
    return voipRegistrationId;
  }
}
