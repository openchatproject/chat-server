package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountCount {

  @JsonProperty
  private int count;

  public AccountCount(int count) {
    this.count = count;
  }

  public AccountCount() {}

  public int getCount() {
    return count;
  }
}
