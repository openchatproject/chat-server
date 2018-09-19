package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;

public class AccountAttributes {

  @JsonProperty
  @NotEmpty
  private String signalingKey;

  @JsonProperty
  private boolean fetchesMessages;

  @JsonProperty
  private int registrationId;

  @JsonProperty
  @Length(max = 50, message = "This field must be less than 50 characters")
  private String name;

  public AccountAttributes() {}

  @VisibleForTesting
  public AccountAttributes(String signalingKey, boolean fetchesMessages, int registrationId) {
    this(signalingKey, fetchesMessages, registrationId, null);
  }

  @VisibleForTesting
  public AccountAttributes(String signalingKey, boolean fetchesMessages, int registrationId, String name) {
    this.signalingKey    = signalingKey;
    this.fetchesMessages = fetchesMessages;
    this.registrationId  = registrationId;
    this.name            = name;
  }

  public String getSignalingKey() {
    return signalingKey;
  }

  public boolean getFetchesMessages() {
    return fetchesMessages;
  }

  public int getRegistrationId() {
    return registrationId;
  }

  public String getName() {
    return name;
  }
}
