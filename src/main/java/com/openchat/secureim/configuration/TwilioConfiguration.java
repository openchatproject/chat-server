package com.openchat.secureim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class TwilioConfiguration {

  @NotEmpty
  @JsonProperty
  private String accountId;

  @NotEmpty
  @JsonProperty
  private String accountToken;

  @NotEmpty
  @JsonProperty
  private String number;

  @NotEmpty
  @JsonProperty
  private String localDomain;

  public String getAccountId() {
    return accountId;
  }

  public String getAccountToken() {
    return accountToken;
  }

  public String getNumber() {
    return number;
  }

  public String getLocalDomain() {
    return localDomain;
  }
}
