package com.openchat.secureim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;


public class ApnConfiguration {

  @NotEmpty
  @JsonProperty
  private String pushCertificate;

  @NotEmpty
  @JsonProperty
  private String pushKey;

  @NotEmpty
  @JsonProperty
  private String bundleId;

  @JsonProperty
  private boolean sandbox = false;

  public String getPushCertificate() {
    return pushCertificate;
  }

  public String getPushKey() {
    return pushKey;
  }

  public String getBundleId() {
    return bundleId;
  }

  public boolean isSandboxEnabled() {
    return sandbox;
  }
}
