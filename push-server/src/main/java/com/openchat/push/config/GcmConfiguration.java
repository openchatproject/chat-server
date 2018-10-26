package com.openchat.push.config;

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

  @JsonProperty
  private boolean xmpp = false;

  public String getApiKey() {
    return apiKey;
  }

  public long getSenderId() {
    return senderId;
  }

  public boolean isXmpp() {
    return xmpp;
  }
}
