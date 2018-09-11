package com.openchat.secureim.workers;

import net.sourceforge.argparse4j.inf.Namespace;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.OpenChatSecureimConfiguration;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.providers.RedisClientFactory;
import com.openchat.secureim.storage.Accounts;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.DirectoryManager;

import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.ImmutableListContainerFactory;
import io.dropwizard.jdbi.ImmutableSetContainerFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
import io.dropwizard.jdbi.args.OptionalArgumentFactory;
import io.dropwizard.setup.Bootstrap;
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
      DataSourceFactory dbConfig = config.getDataSourceFactory();
      DBI               dbi      = new DBI(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());

      dbi.registerArgumentFactory(new OptionalArgumentFactory(dbConfig.getDriverClass()));
      dbi.registerContainerFactory(new ImmutableListContainerFactory());
      dbi.registerContainerFactory(new ImmutableSetContainerFactory());
      dbi.registerContainerFactory(new OptionalContainerFactory());

      Accounts               accounts               = dbi.onDemand(Accounts.class);
      JedisPool              cacheClient            = new RedisClientFactory(config.getCacheConfiguration().getUrl()).getRedisClientPool();
      JedisPool              redisClient            = new RedisClientFactory(config.getDirectoryConfiguration().getUrl()).getRedisClientPool();
      DirectoryManager       directory              = new DirectoryManager(redisClient);
      AccountsManager        accountsManager        = new AccountsManager(accounts, directory, cacheClient);
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
