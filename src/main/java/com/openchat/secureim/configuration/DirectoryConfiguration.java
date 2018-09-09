package com.openchat.secureim.configuration;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public class DirectoryConfiguration {

  @JsonProperty
  @NotEmpty
  private String url;

  public String getUrl() {
    return url;
  }
}
