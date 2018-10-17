package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DirectoryReconciliationRequest {

  @JsonProperty
  private String fromNumber;

  @JsonProperty
  private String toNumber;

  @JsonProperty
  private List<String> numbers;

  public DirectoryReconciliationRequest() {
  }

  public DirectoryReconciliationRequest(String fromNumber, String toNumber, List<String> numbers) {
    this.fromNumber = fromNumber;
    this.toNumber   = toNumber;
    this.numbers    = numbers;
  }

  public String getFromNumber() {
    return fromNumber;
  }

  public String getToNumber() {
    return toNumber;
  }

  public List<String> getNumbers() {
    return numbers;
  }

}
