package com.openchat.push;

import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.push.auth.Server;
import com.openchat.push.auth.ServerAuthenticator;
import com.openchat.push.config.ApnConfiguration;
import com.openchat.push.config.GcmConfiguration;
import com.openchat.push.controllers.FeedbackController;
import com.openchat.push.controllers.PushController;
import com.openchat.push.metrics.JsonMetricsReporter;
import com.openchat.push.providers.RedisClientFactory;
import com.openchat.push.providers.RedisHealthCheck;
import com.openchat.push.senders.APNSender;
import com.openchat.push.senders.GCMSender;
import com.openchat.push.senders.HttpGCMSender;
import com.openchat.push.senders.XmppGCMSender;
import com.openchat.push.senders.UnregisteredQueue;
import com.openchat.push.util.Constants;

import java.security.Security;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;

public class OpenchatPushServer extends Application<OpenchatPushConfiguration> {

  private final Logger logger = LoggerFactory.getLogger(OpenchatPushServer.class);

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  @Override
  public void initialize(Bootstrap<OpenchatPushConfiguration> bootstrap) {}

  @Override
  public void run(OpenchatPushConfiguration config, Environment environment) throws Exception {
    SharedMetricRegistries.add(Constants.METRICS_NAME, environment.metrics());
    environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    JedisPool           redisClient         = new RedisClientFactory(config.getRedisConfiguration()).getRedisClientPool();
    ServerAuthenticator serverAuthenticator = new ServerAuthenticator(config.getAuthenticationConfiguration());
    List<Server>        servers             = config.getAuthenticationConfiguration().getServers();
    UnregisteredQueue   apnQueue            = new UnregisteredQueue(redisClient, environment.getObjectMapper(), servers, "apn");
    UnregisteredQueue   gcmQueue            = new UnregisteredQueue(redisClient, environment.getObjectMapper(), servers, "gcm");

    APNSender apnSender = initializeApnSender(redisClient, apnQueue, config.getApnConfiguration());
    GCMSender gcmSender = initializeGcmSender(gcmQueue, config.getGcmConfiguration());

    environment.lifecycle().manage(apnSender);
    environment.lifecycle().manage(gcmSender);

    environment.jersey().register(new BasicAuthProvider<>(serverAuthenticator, "OpenchatPushServer"));
    environment.jersey().register(new PushController(apnSender, gcmSender));
    environment.jersey().register(new FeedbackController(gcmQueue, apnQueue));

    environment.healthChecks().register("Redis", new RedisHealthCheck(redisClient));
  }

  private APNSender initializeApnSender(JedisPool redisClient,
                                        UnregisteredQueue apnQueue,
                                        ApnConfiguration configuration)
  {
    return new APNSender(redisClient, apnQueue,
                         configuration.getCertificate(),
                         configuration.getKey(),
                         configuration.isFeedbackEnabled());
  }

  private GCMSender initializeGcmSender(UnregisteredQueue gcmQueue,
                                        GcmConfiguration configuration)
  {
    if (configuration.isXmpp()) {
      logger.info("Using XMPP GCM Interface.");
      return new XmppGCMSender(gcmQueue, configuration.getSenderId(), configuration.getApiKey());
    } else {
      logger.info("Using HTTP GCM Interface.");
      return new HttpGCMSender(gcmQueue, configuration.getApiKey());
    }
  }

  public static void main(String[] args) throws Exception {
    new OpenchatPushServer().run(args);
  }
}
