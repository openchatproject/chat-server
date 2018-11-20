package com.openchat.imservice.internal.configuration;

public class OpenchatServiceConfiguration {

  private final OpenchatServiceUrl[] openchatServiceUrls;
  private final OpenchatCdnUrl[]     openchatCdnUrls;

  public OpenchatServiceConfiguration(OpenchatServiceUrl[] openchatServiceUrls, OpenchatCdnUrl[] openchatCdnUrls) {
    this.openchatServiceUrls = openchatServiceUrls;
    this.openchatCdnUrls     = openchatCdnUrls;
  }

  public OpenchatServiceUrl[] getOpenchatServiceUrls() {
    return openchatServiceUrls;
  }

  public OpenchatCdnUrl[] getOpenchatCdnUrls() {
    return openchatCdnUrls;
  }
}
