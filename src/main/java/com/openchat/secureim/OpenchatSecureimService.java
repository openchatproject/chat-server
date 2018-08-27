package com.openchat.secureim;

import com.google.common.base.Optional;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import com.yammer.dropwizard.jdbi.DBIFactory;
import com.yammer.dropwizard.migrations.MigrationsBundle;
import com.yammer.metrics.reporting.GraphiteReporter;
import net.spy.memcached.MemcachedClient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.skife.jdbi.v2.DBI;
import com.openchat.secureim.auth.AccountAuthenticator;
import com.openchat.secureim.auth.FederatedPeerAuthenticator;
import com.openchat.secureim.auth.MultiBasicAuthProvider;
import com.openchat.secureim.configuration.NexmoConfiguration;
import com.openchat.secureim.controllers.AccountController;
import com.openchat.secureim.controllers.DeviceController;
import com.openchat.secureim.controllers.AttachmentController;
import com.openchat.secureim.controllers.DirectoryController;
import com.openchat.secureim.controllers.FederationController;
import com.openchat.secureim.controllers.KeysController;
import com.openchat.secureim.controllers.MessageController;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.federation.FederatedPeer;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.mappers.IOExceptionMapper;
import com.openchat.secureim.mappers.RateLimitExceededExceptionMapper;
import com.openchat.secureim.providers.MemcacheHealthCheck;
import com.openchat.secureim.providers.MemcachedClientFactory;
import com.openchat.secureim.providers.RedisClientFactory;
import com.openchat.secureim.providers.RedisHealthCheck;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.sms.NexmoSmsSender;
import com.openchat.secureim.sms.SmsSender;
import com.openchat.secureim.sms.TwilioSmsSender;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Accounts;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.DirectoryManager;
import com.openchat.secureim.storage.Keys;
import com.openchat.secureim.storage.PendingAccounts;
import com.openchat.secureim.storage.PendingAccountsManager;
import com.openchat.secureim.storage.PendingDeviceRegistrations;
import com.openchat.secureim.storage.PendingDevicesManager;
import com.openchat.secureim.storage.StoredMessageManager;
import com.openchat.secureim.storage.StoredMessages;
import com.openchat.secureim.util.UrlSigner;
import com.openchat.secureim.workers.DirectoryCommand;

import java.security.Security;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.JedisPool;

public class OpenChatSecureimService extends Service<OpenChatSecureimConfiguration> {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  @Override
  public void initialize(Bootstrap<OpenChatSecureimConfiguration> bootstrap) {
    bootstrap.setName("openchat-secureim");
    bootstrap.addCommand(new DirectoryCommand());
    bootstrap.addBundle(new MigrationsBundle<OpenChatSecureimConfiguration>() {
      @Override
      public DatabaseConfiguration getDatabaseConfiguration(OpenChatSecureimConfiguration configuration) {
        return configuration.getDatabaseConfiguration();
      }
    });
  }

  @Override
  public void run(OpenChatSecureimConfiguration config, Environment environment)
      throws Exception
  {
    DBIFactory dbiFactory = new DBIFactory();
    DBI        jdbi       = dbiFactory.build(environment, config.getDatabaseConfiguration(), "postgresql");

    Accounts                   accounts        = jdbi.onDemand(Accounts.class);
    PendingAccounts            pendingAccounts = jdbi.onDemand(PendingAccounts.class);
    PendingDeviceRegistrations pendingDevices  = jdbi.onDemand(PendingDeviceRegistrations.class);
    Keys                       keys            = jdbi.onDemand(Keys.class);
    StoredMessages             storedMessages  = jdbi.onDemand(StoredMessages.class);

    MemcachedClient memcachedClient = new MemcachedClientFactory(config.getMemcacheConfiguration()).getClient();
    JedisPool       redisClient     = new RedisClientFactory(config.getRedisConfiguration()).getRedisClientPool();

    DirectoryManager         directory              = new DirectoryManager(redisClient);
    PendingAccountsManager   pendingAccountsManager = new PendingAccountsManager(pendingAccounts, memcachedClient);
    PendingDevicesManager    pendingDevicesManager  = new PendingDevicesManager(pendingDevices, memcachedClient);
    AccountsManager          accountsManager        = new AccountsManager(accounts, directory, memcachedClient);
    AccountAuthenticator     accountAuthenticator   = new AccountAuthenticator(accountsManager                     );
    FederatedClientManager   federatedClientManager = new FederatedClientManager(config.getFederationConfiguration());
    StoredMessageManager     storedMessageManager   = new StoredMessageManager(storedMessages);
    RateLimiters             rateLimiters           = new RateLimiters(config.getLimitsConfiguration(), memcachedClient);
    TwilioSmsSender          twilioSmsSender        = new TwilioSmsSender(config.getTwilioConfiguration());
    Optional<NexmoSmsSender> nexmoSmsSender         = initializeNexmoSmsSender(config.getNexmoConfiguration());
    SmsSender                smsSender              = new SmsSender(twilioSmsSender, nexmoSmsSender, config.getTwilioConfiguration().isInternational());
    UrlSigner                urlSigner              = new UrlSigner(config.getS3Configuration());
    PushSender               pushSender             = new PushSender(config.getGcmConfiguration(),
                                                                     config.getApnConfiguration(),
                                                                     storedMessageManager,
                                                                     accountsManager, directory);

    environment.addProvider(new MultiBasicAuthProvider<>(new FederatedPeerAuthenticator(config.getFederationConfiguration()),
                                                         FederatedPeer.class,
                                                         accountAuthenticator,
                                                         Account.class, "OpenchatSecureimServer"));

    environment.addResource(new AccountController(pendingAccountsManager, accountsManager, rateLimiters, smsSender));
    environment.addResource(new DeviceController(pendingDevicesManager, accountsManager, rateLimiters));
    environment.addResource(new DirectoryController(rateLimiters, directory));
    environment.addResource(new AttachmentController(rateLimiters, federatedClientManager, urlSigner));
    environment.addResource(new KeysController(rateLimiters, keys, accountsManager, federatedClientManager));
    environment.addResource(new FederationController(keys, accountsManager, pushSender, urlSigner));

    environment.addServlet(new MessageController(rateLimiters, accountAuthenticator,
                                                 pushSender, federatedClientManager),
                           MessageController.PATH);

    environment.addHealthCheck(new RedisHealthCheck(redisClient));
    environment.addHealthCheck(new MemcacheHealthCheck(memcachedClient));

    environment.addProvider(new IOExceptionMapper());
    environment.addProvider(new RateLimitExceededExceptionMapper());

    if (config.getGraphiteConfiguration().isEnabled()) {
      GraphiteReporter.enable(15, TimeUnit.SECONDS,
                              config.getGraphiteConfiguration().getHost(),
                              config.getGraphiteConfiguration().getPort());
    }
  }

  private Optional<NexmoSmsSender> initializeNexmoSmsSender(NexmoConfiguration configuration) {
    if (configuration == null) {
      return Optional.absent();
    } else {
      return Optional.of(new NexmoSmsSender(configuration));
    }
  }

  public static void main(String[] args) throws Exception {
    new OpenChatSecureimService().run(args);
  }

}
