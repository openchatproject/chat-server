package com.openchat.imservice.internal.configuration;

public class OpenchatServiceConfiguration {

  private final OpenchatServiceUrl[]          openchatServiceUrls;
  private final OpenchatCdnUrl[]              openchatCdnUrls;
  private final OpenchatContactDiscoveryUrl[] openchatContactDiscoveryUrls;

  public OpenchatServiceConfiguration(OpenchatServiceUrl[] openchatServiceUrls, OpenchatCdnUrl[] openchatCdnUrls, OpenchatContactDiscoveryUrl[] openchatContactDiscoveryUrls) {
    this.openchatServiceUrls          = openchatServiceUrls;
    this.openchatCdnUrls              = openchatCdnUrls;
    this.openchatContactDiscoveryUrls = openchatContactDiscoveryUrls;
  }

  public OpenchatServiceUrl[] getOpenchatServiceUrls() {
    return openchatServiceUrls;
  }

  public OpenchatCdnUrl[] getOpenchatCdnUrls() {
    return openchatCdnUrls;
  }

  public OpenchatContactDiscoveryUrl[] getOpenchatContactDiscoveryUrls() {
    return openchatContactDiscoveryUrls;
  }
}
