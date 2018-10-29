package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class GcmRegistrationId {

  @JsonProperty
  @NotEmpty
  private String gcmRegistrationId;

  @JsonProperty
  private boolean webSocketChannel;

  public String getGcmRegistrationId() {
    return gcmRegistrationId;
  }

  public boolean isWebSocketChannel() {
    return webSocketChannel;
  }
}

