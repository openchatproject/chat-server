package com.openchat.secureim.workers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.common.base.Optional;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.glassfish.jersey.client.ClientProperties;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.OpenChatSecureimConfiguration;
import com.openchat.secureim.entities.ApnMessage;
import com.openchat.secureim.entities.GcmMessage;
import com.openchat.secureim.providers.RedisClientFactory;
import com.openchat.secureim.push.ApnFallbackManager;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.push.PushServiceClient;
import com.openchat.secureim.push.TransientPushFailureException;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Accounts;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.DirectoryManager;
import com.openchat.secureim.storage.Messages;
import com.openchat.secureim.util.Pair;
import com.openchat.secureim.util.Util;

import javax.ws.rs.client.Client;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;

public class PushCommand extends EnvironmentCommand<OpenChatSecureimConfiguration> {

  private final Logger logger = LoggerFactory.getLogger(DirectoryCommand.class);

  private static final int LIMIT = 1000;

  public PushCommand() {
    super(new Application<OpenChatSecureimConfiguration>() {
      @Override
      public void run(OpenChatSecureimConfiguration configuration, Environment environment)
          throws Exception
      {

      }
    }, "push", "send pushes");
  }

  @Override
  public void configure(Subparser subparser) {
    super.configure(subparser);
    subparser.addArgument("-t", "--time")
             .dest("timestamp")
             .type(Long.class)
             .required(true)
             .help("The starting timestamp to notify users from");

    subparser.addArgument("-o", "--offset")
             .dest("offset")
             .type(Integer.class)
             .required(true)
             .help("The starting offset in the user query");
  }

  @Override
  protected void run(Environment environment, Namespace namespace,
                     OpenChatSecureimConfiguration configuration)
      throws Exception
  {
    try {
      long timestampStart = namespace.getLong("timestamp");
      int  offset         = namespace.getInt("offset");

      environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      DBIFactory dbiFactory = new DBIFactory();
      DBI        database   = dbiFactory.build(environment, configuration.getDataSourceFactory(), "accountdb"        );
      DBI        messagedb  = dbiFactory.build(environment, configuration.getMessageStoreConfiguration(), "messagedb");

      Accounts accounts = database.onDemand(Accounts.class);
      Messages messages = messagedb.onDemand(Messages.class);

      JedisPool        cacheClient     = new RedisClientFactory(configuration.getCacheConfiguration().getUrl()).getRedisClientPool();
      JedisPool        redisClient     = new RedisClientFactory(configuration.getDirectoryConfiguration().getUrl()).getRedisClientPool();
      DirectoryManager directory       = new DirectoryManager(redisClient);
      AccountsManager  accountsManager = new AccountsManager(accounts, directory, cacheClient);

      Client            httpClient        = initializeHttpClient(environment, configuration);
      PushServiceClient pushServiceClient = new PushServiceClient(httpClient, configuration.getPushConfiguration());
      
      while (true) {
        List<Pair<String, Integer>> pendingDestinations = messages.getPendingDestinations(timestampStart, offset, LIMIT);

        if (pendingDestinations == null || pendingDestinations.size() == 0) {
          break;
        }

        for (Pair<String, Integer> pendingDestination : pendingDestinations) {
          Optional<Account> account = accountsManager.get(pendingDestination.first());

          if (account.isPresent()) {
            Optional<Device> device = account.get().getDevice(pendingDestination.second());

            if (device.isPresent()) {
              if (device.get().getGcmId() != null) {
                sendGcm(pushServiceClient, account.get(), device.get());
              } else if (device.get().getApnId() != null) {
                sendApn(pushServiceClient, account.get(), device.get());
              }
            } else {
              logger.warn("No device found: "  + pendingDestination.first() + ", " + pendingDestination.second());
            }
          } else {
            logger.warn("No account found: " + pendingDestination.first());
          }
        }

        logger.warn("Processed " + LIMIT + "...");
        offset += LIMIT;
      }

      logger.warn("Finished!");

    } catch (Exception ex) {
      logger.warn("Exception", ex);
    }
  }

  private void sendGcm(PushServiceClient pushServiceClient, Account account, Device device) {
    try {
      GcmMessage gcmMessage = new GcmMessage(device.getGcmId(), account.getNumber(),
                                             (int)device.getId(), "", false, true);

      logger.warn("Sending GCM: " + account.getNumber());
      pushServiceClient.send(gcmMessage);
    } catch (TransientPushFailureException e) {
      logger.warn("Push failure", e);
    }
  }

  private void sendApn(PushServiceClient pushServiceClient, Account account, Device device) {
    if (!Util.isEmpty(device.getVoipApnId())) {
      try {
        ApnMessage apnMessage = new ApnMessage(device.getVoipApnId(), account.getNumber(), (int)device.getId(),
                                               String.format(PushSender.APN_PAYLOAD, 1),
                                               true, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(ApnFallbackManager.FALLBACK_DURATION));

        logger.warn("Sending APN: " + account.getNumber());
        pushServiceClient.send(apnMessage);
      } catch (TransientPushFailureException e) {
        logger.warn("SILENT PUSH LOSS", e);
      }
    }
  }

  private Client initializeHttpClient(Environment environment, OpenChatSecureimConfiguration config) {
    Client httpClient = new JerseyClientBuilder(environment).using(config.getJerseyClientConfiguration())
                                                            .build(getName());

    httpClient.property(ClientProperties.CONNECT_TIMEOUT, 1000);
    httpClient.property(ClientProperties.READ_TIMEOUT, 1000);

    return httpClient;
  }

}
