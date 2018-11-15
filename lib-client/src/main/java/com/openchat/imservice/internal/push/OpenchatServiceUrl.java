package com.openchat.imservice.internal.push;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.push.TrustStore;

import okhttp3.ConnectionSpec;

public class OpenchatServiceUrl {

  private final String                   url;
  private final Optional<String>         hostHeader;
  private final Optional<ConnectionSpec> connectionSpec;
  private       TrustStore               trustStore;

  public OpenchatServiceUrl(String url, TrustStore trustStore) {
    this(url, null, trustStore, null);
  }

  public OpenchatServiceUrl(String url, String hostHeader,
                          TrustStore trustStore,
                          ConnectionSpec connectionSpec)
  {
    this.url            = url;
    this.hostHeader     = Optional.fromNullable(hostHeader);
    this.trustStore     = trustStore;
    this.connectionSpec = Optional.fromNullable(connectionSpec);
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

  public Optional<ConnectionSpec> getConnectionSpec() {
    return connectionSpec;
  }
}
