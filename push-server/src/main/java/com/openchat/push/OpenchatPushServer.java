package com.openchat.push;

import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.openchat.push.auth.Server;
import com.openchat.push.auth.ServerAuthenticator;
import com.openchat.push.controllers.FeedbackController;
import com.openchat.push.controllers.PushController;
import com.openchat.push.providers.RedisClientFactory;
import com.openchat.push.providers.RedisHealthCheck;
import com.openchat.push.senders.APNSender;
import com.openchat.push.senders.GCMSender;
import com.openchat.push.senders.UnregisteredQueue;
import com.openchat.push.util.Constants;

import java.util.List;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;

public class OpenchatPushServer extends Application<OpenchatPushConfiguration> {

  @Override
  public void initialize(Bootstrap<OpenchatPushConfiguration> bootstrap) {

  }

  @Override
  public void run(OpenchatPushConfiguration config, Environment environment) throws Exception {
    SharedMetricRegistries.add(Constants.METRICS_NAME, environment.metrics());
    environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    JedisPool           redisClient         = new RedisClientFactory(config.getRedisConfiguration()).getRedisClientPool();
    ServerAuthenticator serverAuthenticator = new ServerAuthenticator(config.getAuthenticationConfiguration());
    List<Server>        servers             = config.getAuthenticationConfiguration().getServers();
    UnregisteredQueue   apnQueue            = new UnregisteredQueue(redisClient, environment.getObjectMapper(), servers, "apn");
    UnregisteredQueue   gcmQueue            = new UnregisteredQueue(redisClient, environment.getObjectMapper(), servers, "gcm");

    APNSender apnSender = new APNSender(redisClient, apnQueue,
                                        config.getApnConfiguration().getCertificate(),
                                        config.getApnConfiguration().getKey());
    GCMSender gcmSender = new GCMSender(gcmQueue,
                                        config.getGcmConfiguration().getSenderId(),
                                        config.getGcmConfiguration().getApiKey());

    environment.lifecycle().manage(apnSender);
    environment.lifecycle().manage(gcmSender);

    environment.jersey().register(new BasicAuthProvider<>(serverAuthenticator, "OpenchatPushServer"));
    environment.jersey().register(new PushController(apnSender, gcmSender));
    environment.jersey().register(new FeedbackController(gcmQueue, apnQueue));

    environment.healthChecks().register("Redis", new RedisHealthCheck(redisClient));
  }
}
