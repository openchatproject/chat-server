package com.openchat.secureim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class DirectoryServerConfiguration {

  @NotEmpty
  @JsonProperty
  private String replicationUrl;

  @NotEmpty
  @JsonProperty
  private String replicationPassword;

  @NotEmpty
  @JsonProperty
  private String replicationCaCertificate;

  @JsonProperty
  private int reconciliationChunkSize = 1000;

  @JsonProperty
  private long reconciliationChunkIntervalMs = 8000L;

  public String getReplicationUrl() {
    return replicationUrl;
  }

  public String getReplicationPassword() {
    return replicationPassword;
  }

  public String getReplicationCaCertificate() {
    return replicationCaCertificate;
  }

  public int getReconciliationChunkSize() {
    return reconciliationChunkSize;
  }

  public long getReconciliationChunkIntervalMs() {
    return reconciliationChunkIntervalMs;
  }
}
