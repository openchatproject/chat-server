package com.openchat.push.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openchat.push.auth.Server;

import javax.validation.Valid;
import java.util.List;

public class AuthenticationConfiguration {

  @JsonProperty
  @Valid
  private List<Server> servers;


  public List<Server> getServers() {
    return servers;
  }
}
