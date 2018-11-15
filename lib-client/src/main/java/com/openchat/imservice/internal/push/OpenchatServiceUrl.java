package com.openchat.imservice.internal.push;

import com.openchat.protocal.util.guava.Optional;

public class OpenchatServiceUrl {

  private final Optional<String> hostHeader;
  private final String           url;

  public OpenchatServiceUrl(String url) {
    this(url, null);
  }

  public OpenchatServiceUrl(String url, String hostHeader) {
    this.url        = url;
    this.hostHeader = Optional.fromNullable(hostHeader);
  }

  public Optional<String> getHostHeader() {
    return hostHeader;
  }

  public String getUrl() {
    return url;
  }
}
