package com.openchat.secureim.push;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.configuration.ApnConfiguration;
import com.openchat.secureim.push.RetryingApnsClient.ApnResult;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.dropwizard.lifecycle.Managed;

public class APNSender implements Managed {

  private final Logger logger = LoggerFactory.getLogger(APNSender.class);

  private ExecutorService    executor;
  private ApnFallbackManager fallbackManager;

  private final AccountsManager    accountsManager;
  private final String             bundleId;
  private final boolean            sandbox;
  private final RetryingApnsClient apnsClient;

  public APNSender(AccountsManager accountsManager, ApnConfiguration configuration)
      throws IOException
  {
    this.accountsManager = accountsManager;
    this.bundleId        = configuration.getBundleId();
    this.sandbox         = configuration.isSandboxEnabled();
    this.apnsClient      = new RetryingApnsClient(configuration.getPushCertificate(),
                                                  configuration.getPushKey(),
                                                  10);
  }

  @VisibleForTesting
  public APNSender(ExecutorService executor, AccountsManager accountsManager, RetryingApnsClient apnsClient, String bundleId, boolean sandbox) {
    this.executor        = executor;
    this.accountsManager = accountsManager;
    this.apnsClient      = apnsClient;
    this.sandbox         = sandbox;
    this.bundleId        = bundleId;
  }

  public ListenableFuture<ApnResult> sendMessage(final ApnMessage message) {
    String topic = bundleId;

    if (message.isVoip()) {
      topic = topic + ".voip";
    }
    
    ListenableFuture<ApnResult> future = apnsClient.send(message.getApnId(), topic,
                                                         message.getMessage(),
                                                         new Date(message.getExpirationTime()));

    Futures.addCallback(future, new FutureCallback<ApnResult>() {
      @Override
      public void onSuccess(@Nullable ApnResult result) {
        if (result == null) {
          logger.warn("*** RECEIVED NULL APN RESULT ***");
        } else if (result.getStatus() == ApnResult.Status.NO_SUCH_USER) {
          handleUnregisteredUser(message.getApnId(), message.getNumber(), message.getDeviceId());
        } else if (result.getStatus() == ApnResult.Status.GENERIC_FAILURE) {
          logger.warn("*** Got APN generic failure: " + result.getReason() + ", " + message.getNumber());
        }
      }

      @Override
      public void onFailure(@Nullable Throwable t) {
        logger.warn("Got fatal APNS exception", t);
      }
    }, executor);

    return future;
  }

  @Override
  public void start() throws Exception {
    this.executor = Executors.newSingleThreadExecutor();
    this.apnsClient.connect(sandbox);
  }

  @Override
  public void stop() throws Exception {
    this.executor.shutdown();
    this.apnsClient.disconnect();
  }

  public void setApnFallbackManager(ApnFallbackManager fallbackManager) {
    this.fallbackManager = fallbackManager;
  }

  private void handleUnregisteredUser(String registrationId, String number, long deviceId) {
//    logger.info("Got APN Unregistered: " + number + "," + deviceId);

    Optional<Account> account = accountsManager.get(number);

    if (!account.isPresent()) {
      logger.info("No account found: " + number);
      return;
    }

    Optional<Device> device = account.get().getDevice(deviceId);

    if (!device.isPresent()) {
      logger.info("No device found: " + number);
      return;
    }

    if (!registrationId.equals(device.get().getApnId()) &&
        !registrationId.equals(device.get().getVoipApnId()))
    {
      logger.info("Registration ID does not match: " + registrationId + ", " + device.get().getApnId() + ", " + device.get().getVoipApnId());
      return;
    }

//    if (registrationId.equals(device.get().getApnId())) {
//      logger.info("APN Unregister APN ID matches! " + number + ", " + deviceId);
//    } else if (registrationId.equals(device.get().getVoipApnId())) {
//      logger.info("APN Unregister VoIP ID matches! " + number + ", " + deviceId);
//    }

    long tokenTimestamp = device.get().getPushTimestamp();

    if (tokenTimestamp != 0 && System.currentTimeMillis() < tokenTimestamp + TimeUnit.SECONDS.toMillis(10))
    {
      logger.info("APN Unregister push timestamp is more recent: " + tokenTimestamp + ", " + number);
      return;
    }

//    logger.info("APN Unregister timestamp matches: " + device.get().getApnId() + ", " + device.get().getVoipApnId());
//    device.get().setApnId(null);
//    device.get().setVoipApnId(null);
//    device.get().setFetchesMessages(false);
//    accountsManager.update(account.get());

//    if (fallbackManager != null) {
//      fallbackManager.cancel(new WebsocketAddress(number, deviceId));
//    }

    if (fallbackManager != null) {
      fallbackManager.cancel(account.get(), device.get());
    }
  }
}
