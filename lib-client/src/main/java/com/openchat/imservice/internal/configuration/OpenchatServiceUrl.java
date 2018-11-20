package com.openchat.imservice.internal.configuration;

import com.openchat.imservice.api.push.TrustStore;

import okhttp3.ConnectionSpec;

public class OpenchatServiceUrl extends OpenchatUrl {

  public OpenchatServiceUrl(String url, TrustStore trustStore) {
    super(url, trustStore);
  }

  public OpenchatServiceUrl(String url, String hostHeader, TrustStore trustStore, ConnectionSpec connectionSpec) {
    super(url, hostHeader, trustStore, connectionSpec);
  }
}
