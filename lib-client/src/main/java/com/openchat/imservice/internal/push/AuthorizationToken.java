package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationToken {

  @JsonProperty
  private String token;

  public String getToken() {
    return token;
  }
}
