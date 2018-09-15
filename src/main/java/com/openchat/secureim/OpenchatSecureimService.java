package com.openchat.secureim;

import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.google.common.base.Optional;
import com.sun.jersey.api.client.Client;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;
import com.openchat.dispatch.DispatchChannel;
import com.openchat.dispatch.DispatchManager;
import com.openchat.secureim.auth.AccountAuthenticator;
import com.openchat.secureim.auth.FederatedPeerAuthenticator;
import com.openchat.secureim.auth.MultiBasicAuthProvider;
import com.openchat.secureim.configuration.NexmoConfiguration;
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
import com.openchat.secureim.mappers.IOExceptionMapper;
import com.openchat.secureim.mappers.RateLimitExceededExceptionMapper;
import com.openchat.secureim.metrics.CpuUsageGauge;
import com.openchat.secureim.metrics.FreeMemoryGauge;
import com.openchat.secureim.metrics.NetworkReceivedGauge;
import com.openchat.secureim.metrics.NetworkSentGauge;
import com.openchat.secureim.providers.RedisClientFactory;
import com.openchat.secureim.providers.RedisHealthCheck;
import com.openchat.secureim.providers.TimeProvider;
import com.openchat.secureim.push.FeedbackHandler;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.push.PushServiceClient;
import com.openchat.secureim.push.WebsocketSender;
import com.openchat.secureim.sms.NexmoSmsSender;
import com.openchat.secureim.sms.SmsSender;
import com.openchat.secureim.sms.TwilioSmsSender;
import com.openchat.secureim.storage.Accounts;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;
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
import com.openchat.secureim.workers.VacuumCommand;
import com.openchat.websocket.WebSocketResourceProviderFactory;
import com.openchat.websocket.setup.WebSocketEnvironment;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletRegistration;
import java.security.Security;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.metrics.graphite.GraphiteReporterFactory;
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
    Client             httpClient         = new JerseyClientBuilder(environment).using(config.getJerseyClientConfiguration())
                                                                                .build(getName());

    DirectoryManager       directory              = new DirectoryManager(directoryClient);
    PendingAccountsManager pendingAccountsManager = new PendingAccountsManager(pendingAccounts, cacheClient);
    PendingDevicesManager  pendingDevicesManager  = new PendingDevicesManager (pendingDevices, cacheClient);
    AccountsManager        accountsManager        = new AccountsManager(accounts, directory, cacheClient);
    FederatedClientManager federatedClientManager = new FederatedClientManager(config.getFederationConfiguration());
    MessagesManager        messagesManager        = new MessagesManager(messages);
    DeadLetterHandler      deadLetterHandler      = new DeadLetterHandler(messagesManager);
    DispatchManager        dispatchManager        = new DispatchManager(cacheClientFactory, Optional.<DispatchChannel>of(deadLetterHandler));
    PubSubManager          pubSubManager          = new PubSubManager(cacheClient, dispatchManager);
    PushServiceClient      pushServiceClient      = new PushServiceClient(httpClient, config.getPushConfiguration());
    WebsocketSender        websocketSender        = new WebsocketSender(messagesManager, pubSubManager);
    AccountAuthenticator   deviceAuthenticator    = new AccountAuthenticator(accountsManager);
    RateLimiters           rateLimiters           = new RateLimiters(config.getLimitsConfiguration(), cacheClient);

    TwilioSmsSender          twilioSmsSender     = new TwilioSmsSender(config.getTwilioConfiguration());
    Optional<NexmoSmsSender> nexmoSmsSender      = initializeNexmoSmsSender(config.getNexmoConfiguration());
    SmsSender                smsSender           = new SmsSender(twilioSmsSender, nexmoSmsSender, config.getTwilioConfiguration().isInternational());
    UrlSigner                urlSigner           = new UrlSigner(config.getS3Configuration());
    PushSender               pushSender          = new PushSender(pushServiceClient, websocketSender);
    FeedbackHandler          feedbackHandler     = new FeedbackHandler(pushServiceClient, accountsManager);
    Optional<byte[]>         authorizationKey    = config.getRedphoneConfiguration().getAuthorizationKey();

    environment.lifecycle().manage(pubSubManager);
    environment.lifecycle().manage(feedbackHandler);

    AttachmentController attachmentController = new AttachmentController(rateLimiters, federatedClientManager, urlSigner);
    KeysControllerV1     keysControllerV1     = new KeysControllerV1(rateLimiters, keys, accountsManager, federatedClientManager);
    KeysControllerV2     keysControllerV2     = new KeysControllerV2(rateLimiters, keys, accountsManager, federatedClientManager);
    MessageController    messageController    = new MessageController(rateLimiters, pushSender, accountsManager, federatedClientManager);

    environment.jersey().register(new MultiBasicAuthProvider<>(new FederatedPeerAuthenticator(config.getFederationConfiguration()),
                                                               FederatedPeer.class,
                                                               deviceAuthenticator,
                                                               Device.class, "OpenchatSecureimServer"));

    environment.jersey().register(new AccountController(pendingAccountsManager, accountsManager, rateLimiters, smsSender, messagesManager, new TimeProvider(), authorizationKey));
    environment.jersey().register(new DeviceController(pendingDevicesManager, accountsManager, rateLimiters));
    environment.jersey().register(new DirectoryController(rateLimiters, directory));
    environment.jersey().register(new FederationControllerV1(accountsManager, attachmentController, messageController, keysControllerV1));
    environment.jersey().register(new FederationControllerV2(accountsManager, attachmentController, messageController, keysControllerV2));
    environment.jersey().register(new ReceiptController(accountsManager, federatedClientManager, pushSender));
    environment.jersey().register(new ProvisioningController(rateLimiters, pushSender));
    environment.jersey().register(attachmentController);
    environment.jersey().register(keysControllerV1);
    environment.jersey().register(keysControllerV2);
    environment.jersey().register(messageController);

    if (config.getWebsocketConfiguration().isEnabled()) {
      WebSocketEnvironment webSocketEnvironment = new WebSocketEnvironment(environment, config, 90000);
      webSocketEnvironment.setAuthenticator(new WebSocketAccountAuthenticator(deviceAuthenticator));
      webSocketEnvironment.setConnectListener(new AuthenticatedConnectListener(accountsManager, pushSender, messagesManager, pubSubManager));
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
