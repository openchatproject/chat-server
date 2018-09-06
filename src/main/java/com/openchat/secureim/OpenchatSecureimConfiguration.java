package com.openchat.secureim;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.openchat.secureim.configuration.ApnConfiguration;
import com.openchat.secureim.configuration.FederationConfiguration;
import com.openchat.secureim.configuration.GcmConfiguration;
import com.openchat.secureim.configuration.GraphiteConfiguration;
import com.openchat.secureim.configuration.MemcacheConfiguration;
import com.openchat.secureim.configuration.MetricsConfiguration;
import com.openchat.secureim.configuration.NexmoConfiguration;
import com.openchat.secureim.configuration.RateLimitsConfiguration;
import com.openchat.secureim.configuration.RedPhoneConfiguration;
import com.openchat.secureim.configuration.RedisConfiguration;
import com.openchat.secureim.configuration.S3Configuration;
import com.openchat.secureim.configuration.TwilioConfiguration;
import com.openchat.secureim.configuration.WebsocketConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class OpenChatSecureimConfiguration extends Configuration {

  @NotNull
  @Valid
  @JsonProperty
  private TwilioConfiguration twilio;

  @JsonProperty
  private NexmoConfiguration nexmo;

  @NotNull
  @Valid
  @JsonProperty
  private GcmConfiguration gcm;

  @NotNull
  @Valid
  @JsonProperty
  private S3Configuration s3;

  @NotNull
  @Valid
  @JsonProperty
  private MemcacheConfiguration memcache;

  @NotNull
  @Valid
  @JsonProperty
  private RedisConfiguration redis;

  @JsonProperty
  private ApnConfiguration apn = new ApnConfiguration();

  @Valid
  @JsonProperty
  private FederationConfiguration federation = new FederationConfiguration();

  @Valid
  @NotNull
  @JsonProperty
  private DataSourceFactory database = new DataSourceFactory();

  @Valid
  @NotNull
  @JsonProperty
  private RateLimitsConfiguration limits = new RateLimitsConfiguration();

  @Valid
  @JsonProperty
  private GraphiteConfiguration graphite = new GraphiteConfiguration();

  @Valid
  @JsonProperty
  private MetricsConfiguration viz = new MetricsConfiguration();

  @Valid
  @JsonProperty
  private WebsocketConfiguration websocket = new WebsocketConfiguration();

  @JsonProperty
  private RedPhoneConfiguration redphone = new RedPhoneConfiguration();

  public WebsocketConfiguration getWebsocketConfiguration() {
    return websocket;
  }

  public TwilioConfiguration getTwilioConfiguration() {
    return twilio;
  }

  public NexmoConfiguration getNexmoConfiguration() {
    return nexmo;
  }

  public GcmConfiguration getGcmConfiguration() {
    return gcm;
  }

  public ApnConfiguration getApnConfiguration() {
    return apn;
  }

  public S3Configuration getS3Configuration() {
    return s3;
  }

  public MemcacheConfiguration getMemcacheConfiguration() {
    return memcache;
  }

  public RedisConfiguration getRedisConfiguration() {
    return redis;
  }

  public DataSourceFactory getDataSourceFactory() {
    return database;
  }

  public RateLimitsConfiguration getLimitsConfiguration() {
    return limits;
  }

  public FederationConfiguration getFederationConfiguration() {
    return federation;
  }

  public GraphiteConfiguration getGraphiteConfiguration() {
    return graphite;
  }

  public MetricsConfiguration getMetricsConfiguration() {
    return viz;
  }

  public RedPhoneConfiguration getRedphoneConfiguration() {
    return redphone;
  }
}
