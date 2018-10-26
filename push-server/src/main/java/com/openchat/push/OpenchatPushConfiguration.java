package com.openchat.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openchat.push.config.ApnConfiguration;
import com.openchat.push.config.AuthenticationConfiguration;
import com.openchat.push.config.GcmConfiguration;
import com.openchat.push.config.RedisConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;

public class OpenchatPushConfiguration extends Configuration {

  @JsonProperty
  @Valid
  @NotNull
  private AuthenticationConfiguration authentication;

  @JsonProperty
  @Valid
  @NotNull
  private RedisConfiguration redis;

  @JsonProperty
  @Valid
  @NotNull
  private ApnConfiguration apn;

  @JsonProperty
  @Valid
  @NotNull
  private GcmConfiguration gcm;
  
  public AuthenticationConfiguration getAuthenticationConfiguration() {
    return authentication;
  }

  public RedisConfiguration getRedisConfiguration() {
    return redis;
  }

  public ApnConfiguration getApnConfiguration() {
    return apn;
  }

  public GcmConfiguration getGcmConfiguration() {
    return gcm;
  }
}
