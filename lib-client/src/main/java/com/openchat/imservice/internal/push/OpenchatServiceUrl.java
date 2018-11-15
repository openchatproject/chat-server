package com.openchat.imservice.internal.push;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.push.TrustStore;

public class OpenchatServiceUrl {

  private final String           url;
  private final Optional<String> hostHeader;
  private       TrustStore       trustStore;

  public OpenchatServiceUrl(String url, TrustStore trustStore) {
    this(url, null, trustStore);
  }

  public OpenchatServiceUrl(String url, String hostHeader, TrustStore trustStore) {
    this.url        = url;
    this.hostHeader = Optional.fromNullable(hostHeader);
    this.trustStore = trustStore;
  }

  public Optional<String> getHostHeader() {
    return hostHeader;
  }

  public String getUrl() {
    return url;
  }

  public TrustStore getTrustStore() {
    return trustStore;
  }
}
