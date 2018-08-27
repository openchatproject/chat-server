package com.openchat.secureim.entities;

import java.util.List;

public class MessageResponse {
  private List<String> success;
  private List<String> failure;
  private List<String> missingDeviceIds;

  public MessageResponse(List<String> success, List<String> failure, List<String> missingDeviceIds) {
    this.success          = success;
    this.failure          = failure;
    this.missingDeviceIds = missingDeviceIds;
  }

  public MessageResponse() {}

  public List<String> getSuccess() {
    return success;
  }

  public List<String> getFailure() {
    return failure;
  }

  public List<String> getNumbersMissingDevices() {
    return missingDeviceIds;
  }
}
