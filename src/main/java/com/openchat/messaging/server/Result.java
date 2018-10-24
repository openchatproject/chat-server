package com.openchat.messaging.server;

public class Result {

  private final Object context;
  private final String canonicalRegistrationId;
  private final String messageId;
  private final String error;

  public Result(Object context, String canonicalRegistrationId, String messageId, String error) {
    this.context                 = context;
    this.canonicalRegistrationId = canonicalRegistrationId;
    this.messageId               = messageId;
    this.error                   = error;
  }

  public String getCanonicalRegistrationId() {
    return canonicalRegistrationId;
  }

  public boolean hasCanonicalRegistrationId() {
    return canonicalRegistrationId != null && !canonicalRegistrationId.isEmpty();
  }

  public String getMessageId() {
    return messageId;
  }

  public String getError() {
    return error;
  }

  public boolean isSuccess() {
    return messageId != null && !messageId.isEmpty() && (error == null || error.isEmpty());
  }

  public boolean isUnregistered() {
    return "NotRegistered".equals(error);
  }

  public boolean isThrottled() {
    return "DeviceMessageRateExceeded".equals(error);
  }

  public boolean isInvalidRegistrationId() {
    return "InvalidRegistration".equals(error);
  }

  public Object getContext() {
    return context;
  }
}
