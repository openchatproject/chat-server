package com.openchat.imservice.internal.configuration;

import com.openchat.imservice.api.push.TrustStore;

import okhttp3.ConnectionSpec;

public class OpenchatCdnUrl extends OpenchatUrl {
  public OpenchatCdnUrl(String url, TrustStore trustStore) {
    super(url, trustStore);
  }

  public OpenchatCdnUrl(String url, String hostHeader, TrustStore trustStore, ConnectionSpec connectionSpec) {
    super(url, hostHeader, trustStore, connectionSpec);
  }
}
