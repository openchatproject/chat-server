package com.openchat.imservice.api.util;

public interface CredentialsProvider {
  public String getUser();
  public String getPassword();
  public String getOpenchatingKey();
}
