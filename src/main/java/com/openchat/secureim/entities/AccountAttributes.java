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

  public AccountAttributes() {}

  public AccountAttributes(String signalingKey, boolean supportsSms, boolean fetchesMessages) {
    this.signalingKey = signalingKey;
    this.supportsSms  = supportsSms;
    this.fetchesMessages = fetchesMessages;
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

}
