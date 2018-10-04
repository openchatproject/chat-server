package com.openchat.secureim;

import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.common.base.Optional;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.client.ClientProperties;
import org.skife.jdbi.v2.DBI;
import com.openchat.dispatch.DispatchChannel;
import com.openchat.dispatch.DispatchManager;
import com.openchat.dropwizard.simpleauth.AuthDynamicFeature;
import com.openchat.dropwizard.simpleauth.AuthValueFactoryProvider;
import com.openchat.dropwizard.simpleauth.BasicCredentialAuthFilter;
import com.openchat.secureim.auth.AccountAuthenticator;
import com.openchat.secureim.auth.FederatedPeerAuthenticator;
import com.openchat.secureim.auth.TurnTokenGenerator;
import com.openchat.secureim.controllers.AccountController;
import com.openchat.secureim.controllers.AttachmentController;
import com.openchat.secureim.controllers.DeviceController;
import com.openchat.secureim.controllers.DirectoryController;
import com.openchat.secureim.controllers.FederationControllerV1;
import com.openchat.secureim.controllers.FederationControllerV2;
import com.openchat.secureim.controllers.KeepAliveController;
import com.openchat.secureim.controllers.KeysControllerV1;
import com.openchat.secureim.controllers.KeysControllerV2;
import com.openchat.secureim.controllers.MessageController;
import com.openchat.secureim.controllers.ProvisioningController;
import com.openchat.secureim.controllers.ReceiptController;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.federation.FederatedPeer;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.liquibase.NameableMigrationsBundle;
import com.openchat.secureim.mappers.DeviceLimitExceededExceptionMapper;
import com.openchat.secureim.mappers.IOExceptionMapper;
import com.openchat.secureim.mappers.InvalidWebsocketAddressExceptionMapper;
import com.openchat.secureim.mappers.RateLimitExceededExceptionMapper;
import com.openchat.secureim.metrics.CpuUsageGauge;
import com.openchat.secureim.metrics.FileDescriptorGauge;
import com.openchat.secureim.metrics.FreeMemoryGauge;
import com.openchat.secureim.metrics.NetworkReceivedGauge;
import com.openchat.secureim.metrics.NetworkSentGauge;
import com.openchat.secureim.providers.RedisClientFactory;
import com.openchat.secureim.providers.RedisHealthCheck;
import com.openchat.secureim.providers.TimeProvider;
import com.openchat.secureim.push.ApnFallbackManager;
import com.openchat.secureim.push.FeedbackHandler;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.push.PushServiceClient;
import com.openchat.secureim.push.ReceiptSender;
import com.openchat.secureim.push.WebsocketSender;
import com.openchat.secureim.sms.SmsSender;
import com.openchat.secureim.sms.TwilioSmsSender;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Accounts;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.DirectoryManager;
import com.openchat.secureim.storage.Keys;
import com.openchat.secureim.storage.Messages;
import com.openchat.secureim.storage.MessagesManager;
import com.openchat.secureim.storage.PendingAccounts;
import com.openchat.secureim.storage.PendingAccountsManager;
import com.openchat.secureim.storage.PendingDevices;
import com.openchat.secureim.storage.PendingDevicesManager;
import com.openchat.secureim.storage.PubSubManager;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.util.UrlSigner;
import com.openchat.secureim.websocket.AuthenticatedConnectListener;
import com.openchat.secureim.websocket.DeadLetterHandler;
import com.openchat.secureim.websocket.ProvisioningConnectListener;
import com.openchat.secureim.websocket.WebSocketAccountAuthenticator;
import com.openchat.secureim.workers.DirectoryCommand;
import com.openchat.secureim.workers.PeriodicStatsCommand;
import com.openchat.secureim.workers.TrimMessagesCommand;
import com.openchat.secureim.workers.VacuumCommand;
import com.openchat.websocket.WebSocketResourceProviderFactory;
import com.openchat.websocket.setup.WebSocketEnvironment;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletRegistration;
import javax.ws.rs.client.Client;
import java.security.Security;
import java.util.EnumSet;

import static com.codahale.metrics.MetricRegistry.name;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;

public class OpenChatSecureimService extends Application<OpenChatSecureimConfiguration> {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  @Override
  public void initialize(Bootstrap<OpenChatSecureimConfiguration> bootstrap) {
    bootstrap.addCommand(new DirectoryCommand());
    bootstrap.addCommand(new VacuumCommand());
    bootstrap.addCommand(new TrimMessagesCommand());
    bootstrap.addCommand(new PeriodicStatsCommand());
    bootstrap.addBundle(new NameableMigrationsBundle<OpenChatSecureimConfiguration>("accountdb", "accountsdb.xml") {
      @Override
      public DataSourceFactory getDataSourceFactory(OpenChatSecureimConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    });

    bootstrap.addBundle(new NameableMigrationsBundle<OpenChatSecureimConfiguration>("messagedb", "messagedb.xml") {
      @Override
      public DataSourceFactory getDataSourceFactory(OpenChatSecureimConfiguration configuration) {
        return configuration.getMessageStoreConfiguration();
      }
    });
  }

  @Override
  public String getName() {
    return "openchat-secureim";
  }

  @Override
  public void run(OpenChatSecureimConfiguration config, Environment environment)
      throws Exception
  {
    SharedMetricRegistries.add(Constants.METRICS_NAME, environment.metrics());
    environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    environment.getObjectMapper().setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    environment.getObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    DBIFactory dbiFactory = new DBIFactory();
    DBI        database   = dbiFactory.build(environment, config.getDataSourceFactory(), "accountdb");
    DBI        messagedb  = dbiFactory.build(environment, config.getMessageStoreConfiguration(), "messagedb");

    Accounts        accounts        = database.onDemand(Accounts.class);
    PendingAccounts pendingAccounts = database.onDemand(PendingAccounts.class);
    PendingDevices  pendingDevices  = database.onDemand(PendingDevices.class);
    Keys            keys            = database.onDemand(Keys.class);
    Messages        messages        = messagedb.onDemand(Messages.class);

    RedisClientFactory cacheClientFactory = new RedisClientFactory(config.getCacheConfiguration().getUrl());
    JedisPool          cacheClient        = cacheClientFactory.getRedisClientPool();
    JedisPool          directoryClient    = new RedisClientFactory(config.getDirectoryConfiguration().getUrl()).getRedisClientPool();
    Client             httpClient         = initializeHttpClient(environment, config);

    DirectoryManager           directory                  = new DirectoryManager(directoryClient);
    PendingAccountsManager     pendingAccountsManager     = new PendingAccountsManager(pendingAccounts, cacheClient);
    PendingDevicesManager      pendingDevicesManager      = new PendingDevicesManager (pendingDevices, cacheClient );
    AccountsManager            accountsManager            = new AccountsManager(accounts, directory, cacheClient);
    FederatedClientManager     federatedClientManager     = new FederatedClientManager(environment, config.getJerseyClientConfiguration(), config.getFederationConfiguration());
    MessagesManager            messagesManager            = new MessagesManager(messages);
    DeadLetterHandler          deadLetterHandler          = new DeadLetterHandler(messagesManager);
    DispatchManager            dispatchManager            = new DispatchManager(cacheClientFactory, Optional.<DispatchChannel>of(deadLetterHandler));
    PubSubManager              pubSubManager              = new PubSubManager(cacheClient, dispatchManager);
    PushServiceClient          pushServiceClient          = new PushServiceClient(httpClient, config.getPushConfiguration());
    WebsocketSender            websocketSender            = new WebsocketSender(messagesManager, pubSubManager);
    AccountAuthenticator       deviceAuthenticator        = new AccountAuthenticator(accountsManager                 );
    FederatedPeerAuthenticator federatedPeerAuthenticator = new FederatedPeerAuthenticator(config.getFederationConfiguration());
    RateLimiters               rateLimiters               = new RateLimiters(config.getLimitsConfiguration(), cacheClient);

    ApnFallbackManager       apnFallbackManager  = new ApnFallbackManager(pushServiceClient, pubSubManager);
    TwilioSmsSender          twilioSmsSender     = new TwilioSmsSender(config.getTwilioConfiguration());
    SmsSender                smsSender           = new SmsSender(twilioSmsSender);
    UrlSigner                urlSigner           = new UrlSigner(config.getS3Configuration());
    PushSender               pushSender          = new PushSender(apnFallbackManager, pushServiceClient, websocketSender, config.getPushConfiguration().getQueueSize());
    ReceiptSender            receiptSender       = new ReceiptSender(accountsManager, pushSender, federatedClientManager);
    FeedbackHandler          feedbackHandler     = new FeedbackHandler(pushServiceClient, accountsManager);
    TurnTokenGenerator       turnTokenGenerator  = new TurnTokenGenerator(config.getTurnConfiguration());
    Optional<byte[]>         authorizationKey    = config.getRedphoneConfiguration().getAuthorizationKey();

    environment.lifecycle().manage(apnFallbackManager);
    environment.lifecycle().manage(pubSubManager);
    environment.lifecycle().manage(feedbackHandler);
    environment.lifecycle().manage(pushSender);

    AttachmentController attachmentController = new AttachmentController(rateLimiters, federatedClientManager, urlSigner);
    KeysControllerV1     keysControllerV1     = new KeysControllerV1(rateLimiters, keys, accountsManager, federatedClientManager);
    KeysControllerV2     keysControllerV2     = new KeysControllerV2(rateLimiters, keys, accountsManager, federatedClientManager);
    MessageController    messageController    = new MessageController(rateLimiters, pushSender, receiptSender, accountsManager, messagesManager, federatedClientManager);

    environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<Account>()
                                                             .setAuthenticator(deviceAuthenticator)
                                                             .setPrincipal(Account.class)
                                                             .buildAuthFilter(),
                                                         new BasicCredentialAuthFilter.Builder<FederatedPeer>()
                                                             .setAuthenticator(federatedPeerAuthenticator)
                                                             .setPrincipal(FederatedPeer.class)
                                                             .buildAuthFilter()));
    environment.jersey().register(new AuthValueFactoryProvider.Binder());

    environment.jersey().register(new AccountController(pendingAccountsManager, accountsManager, rateLimiters, smsSender, messagesManager, new TimeProvider(), authorizationKey, turnTokenGenerator, config.getTestDevices()));
    environment.jersey().register(new DeviceController(pendingDevicesManager, accountsManager, messagesManager, rateLimiters));
    environment.jersey().register(new DirectoryController(rateLimiters, directory));
    environment.jersey().register(new FederationControllerV1(accountsManager, attachmentController, messageController, keysControllerV1));
    environment.jersey().register(new FederationControllerV2(accountsManager, attachmentController, messageController, keysControllerV2));
    environment.jersey().register(new ReceiptController(receiptSender));
    environment.jersey().register(new ProvisioningController(rateLimiters, pushSender));
    environment.jersey().register(attachmentController);
    environment.jersey().register(keysControllerV1);
    environment.jersey().register(keysControllerV2);
    environment.jersey().register(messageController);

    if (config.getWebsocketConfiguration().isEnabled()) {
      WebSocketEnvironment webSocketEnvironment = new WebSocketEnvironment(environment, config, 90000);
      webSocketEnvironment.setAuthenticator(new WebSocketAccountAuthenticator(deviceAuthenticator));
      webSocketEnvironment.setConnectListener(new AuthenticatedConnectListener(accountsManager, pushSender, receiptSender, messagesManager, pubSubManager));
      webSocketEnvironment.jersey().register(new KeepAliveController(pubSubManager));

      WebSocketEnvironment provisioningEnvironment = new WebSocketEnvironment(environment, config);
      provisioningEnvironment.setConnectListener(new ProvisioningConnectListener(pubSubManager));
      provisioningEnvironment.jersey().register(new KeepAliveController(pubSubManager));
      
      WebSocketResourceProviderFactory webSocketServlet    = new WebSocketResourceProviderFactory(webSocketEnvironment   );
      WebSocketResourceProviderFactory provisioningServlet = new WebSocketResourceProviderFactory(provisioningEnvironment);

      ServletRegistration.Dynamic websocket    = environment.servlets().addServlet("WebSocket", webSocketServlet      );
      ServletRegistration.Dynamic provisioning = environment.servlets().addServlet("Provisioning", provisioningServlet);

      websocket.addMapping("/v1/websocket/");
      websocket.setAsyncSupported(true);

      provisioning.addMapping("/v1/websocket/provisioning/");
      provisioning.setAsyncSupported(true);

      webSocketServlet.start();
      provisioningServlet.start();

      FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
