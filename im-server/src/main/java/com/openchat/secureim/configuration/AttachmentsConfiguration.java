package com.openchat.secureim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class AttachmentsConfiguration {

  @NotEmpty
  @JsonProperty
  private String accessKey;

  @NotEmpty
  @JsonProperty
  private String accessSecret;

  @NotEmpty
  @JsonProperty
  private String bucket;

  public String getAccessKey() {
    return accessKey;
  }

  public String getAccessSecret() {
    return accessSecret;
  }

  public String getBucket() {
    return bucket;
  }
}
