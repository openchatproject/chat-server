package com.openchat.secureim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class TwilioConfiguration {

  @NotEmpty
  @JsonProperty
  private String accountId;

  @NotEmpty
  @JsonProperty
  private String accountToken;

  @NotNull
  @JsonProperty
  private List<String> numbers;

  @NotEmpty
  @JsonProperty
  private String localDomain;

  public String getAccountId() {
    return accountId;
  }

  public String getAccountToken() {
    return accountToken;
  }

  public List<String> getNumbers() {
    return numbers;
  }

  public String getLocalDomain() {
    return localDomain;
  }
}
