package com.openchat.secureim.entities;

import java.util.List;

public class MessageResponse {
  private List<String> success;
  private List<String> failure;

  public MessageResponse(List<String> success, List<String> failure) {
    this.success = success;
    this.failure = failure;
  }

  public MessageResponse() {}

  public List<String> getSuccess() {
    return success;
  }

  public List<String> getFailure() {
    return failure;
  }

}
