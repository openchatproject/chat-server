package com.openchat.imservice.internal.util;

import com.openchat.imservice.api.util.CredentialsProvider;

public class StaticCredentialsProvider implements CredentialsProvider {

  private final String user;
  private final String password;
  private final String openchatingKey;

  public StaticCredentialsProvider(String user, String password, String openchatingKey) {
    this.user         = user;
    this.password     = password;
    this.openchatingKey = openchatingKey;
  }

  @Override
  public String getUser() {
    return user;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getOpenchatingKey() {
    return openchatingKey;
  }
}
