package com.openchat.secureim.workers;

import com.yammer.dropwizard.cli.ConfiguredCommand;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.jdbi.ImmutableListContainerFactory;
import com.yammer.dropwizard.jdbi.ImmutableSetContainerFactory;
import com.yammer.dropwizard.jdbi.OptionalContainerFactory;
import com.yammer.dropwizard.jdbi.args.OptionalArgumentFactory;
import net.sourceforge.argparse4j.inf.Namespace;
import net.spy.memcached.MemcachedClient;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.OpenChatSecureimConfiguration;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.providers.MemcachedClientFactory;
import com.openchat.secureim.providers.RedisClientFactory;
import com.openchat.secureim.storage.Accounts;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.DirectoryManager;

import redis.clients.jedis.JedisPool;

public class DirectoryCommand extends ConfiguredCommand<OpenChatSecureimConfiguration> {

  private final Logger logger = LoggerFactory.getLogger(DirectoryCommand.class);

  public DirectoryCommand() {
    super("directory", "Update directory from DB and peers.");
  }

  @Override
  protected void run(Bootstrap<OpenChatSecureimConfiguration> bootstrap,
                     Namespace namespace,
                     OpenChatSecureimConfiguration config)
      throws Exception
  {
    try {
      DatabaseConfiguration dbConfig = config.getDatabaseConfiguration();
      DBI                   dbi      = new DBI(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());

      dbi.registerArgumentFactory(new OptionalArgumentFactory(dbConfig.getDriverClass()));
      dbi.registerContainerFactory(new ImmutableListContainerFactory());
      dbi.registerContainerFactory(new ImmutableSetContainerFactory());
      dbi.registerContainerFactory(new OptionalContainerFactory());

      Accounts               accounts               = dbi.onDemand(Accounts.class);
      MemcachedClient        memcachedClient        = new MemcachedClientFactory(config.getMemcacheConfiguration()).getClient();
      JedisPool              redisClient            = new RedisClientFactory(config.getRedisConfiguration()).getRedisClientPool();
      DirectoryManager       directory              = new DirectoryManager(redisClient);
      AccountsManager        accountsManager        = new AccountsManager(accounts, directory, memcachedClient);
      FederatedClientManager federatedClientManager = new FederatedClientManager(config.getFederationConfiguration());

      DirectoryUpdater update = new DirectoryUpdater(accountsManager, federatedClientManager, directory);

      update.updateFromLocalDatabase();
      update.updateFromPeers();
    } catch (Exception ex) {
      logger.warn("Directory Exception", ex);
      throw new RuntimeException(ex);
    } finally {
      Thread.sleep(3000);
      System.exit(0);
    }
  }
}
