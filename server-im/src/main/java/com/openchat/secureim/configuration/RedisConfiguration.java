package com.openchat.secureim.configuration;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import java.util.List;

public class RedisConfiguration {

  @JsonProperty
  @NotEmpty
  private String url;

  @JsonProperty
  @NotNull
  private List<String> replicaUrls;

  public String getUrl() {
    return url;
  }

  public List<String> getReplicaUrls() {
    return replicaUrls;
  }
}
