package com.openchat.imservice.internal.configuration;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.push.TrustStore;
import com.openchat.imservice.internal.util.BlacklistingTrustManager;

import javax.net.ssl.TrustManager;

import okhttp3.ConnectionSpec;

public class OpenchatUrl {

  private final String                   url;
  private final Optional<String>         hostHeader;
  private final Optional<ConnectionSpec> connectionSpec;
  private       TrustManager[]           trustManagers;

  public OpenchatUrl(String url, TrustStore trustStore) {
    this(url, null, trustStore, null);
  }

  public OpenchatUrl(String url, String hostHeader,
                   TrustStore trustStore,
                   ConnectionSpec connectionSpec)
  {
    this.url            = url;
    this.hostHeader     = Optional.fromNullable(hostHeader);
    this.trustManagers  = BlacklistingTrustManager.createFor(trustStore);
    this.connectionSpec = Optional.fromNullable(connectionSpec);
  }

  public Optional<String> getHostHeader() {
    return hostHeader;
  }

  public String getUrl() {
    return url;
  }

  public TrustManager[] getTrustManagers() {
    return trustManagers;
  }

  public Optional<ConnectionSpec> getConnectionSpec() {
    return connectionSpec;
  }

}
