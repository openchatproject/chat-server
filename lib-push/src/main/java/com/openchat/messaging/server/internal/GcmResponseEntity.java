package com.openchat.messaging.server.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GcmResponseEntity {

  @JsonProperty(value = "message_id")
  private String messageId;

  @JsonProperty(value = "registration_id")
  private String canonicalRegistrationId;

  @JsonProperty
  private String error;

  public String getMessageId() {
    return messageId;
  }

  public String getCanonicalRegistrationId() {
    return canonicalRegistrationId;
  }

  public String getError() {
    return error;
  }
}
