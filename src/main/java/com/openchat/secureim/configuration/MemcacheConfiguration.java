package com.openchat.secureim.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class MemcacheConfiguration {

  @NotEmpty
  @JsonProperty
  private String servers;

  @JsonProperty
  private String user;

  @JsonProperty
  private String password;


  public String getServers() {
    return servers;
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }
}
