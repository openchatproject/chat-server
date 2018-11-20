package com.openchat.imservice.internal.configuration;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.push.TrustStore;
import com.openchat.imservice.internal.util.BlacklistingTrustManager;

import java.util.Collections;
import java.util.List;

import javax.net.ssl.TrustManager;

import okhttp3.ConnectionSpec;

public class OpenchatUrl {

  private final String                   url;
  private final Optional<String>         hostHeader;
  private final Optional<ConnectionSpec> connectionSpec;
  private       TrustStore               trustStore;

  public OpenchatUrl(String url, TrustStore trustStore) {
    this(url, null, trustStore, null);
  }

  public OpenchatUrl(String url, String hostHeader,
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

  public Optional<List<ConnectionSpec>> getConnectionSpecs() {
    return connectionSpec.isPresent() ? Optional.of(Collections.singletonList(connectionSpec.get())) : Optional.<List<ConnectionSpec>>absent();
  }

}
