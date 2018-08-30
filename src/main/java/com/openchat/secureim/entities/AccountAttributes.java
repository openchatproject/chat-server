package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class AccountAttributes {

  @JsonProperty
  @NotEmpty
  private String signalingKey;

  @JsonProperty
  private boolean supportsSms;

  @JsonProperty
  private boolean fetchesMessages;

  @JsonProperty
  private int registrationId;

  public AccountAttributes() {}

  public AccountAttributes(String signalingKey, boolean supportsSms, boolean fetchesMessages, int registrationId) {
    this.signalingKey    = signalingKey;
    this.supportsSms     = supportsSms;
    this.fetchesMessages = fetchesMessages;
    this.registrationId  = registrationId;
  }

  public String getSignalingKey() {
    return signalingKey;
  }

  public boolean getSupportsSms() {
    return supportsSms;
  }

  public boolean getFetchesMessages() {
    return fetchesMessages;
  }

  public int getRegistrationId() {
    return registrationId;
  }
}
