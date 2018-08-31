package com.openchat.secureim.providers;

import com.yammer.metrics.core.HealthCheck;
import com.yammer.metrics.core.HealthCheck.Result;
import net.spy.memcached.MemcachedClient;

import java.security.SecureRandom;

public class MemcacheHealthCheck extends HealthCheck {

  private final MemcachedClient client;

  public MemcacheHealthCheck(MemcachedClient client) {
    super("memcached");
    this.client = client;
  }

  @Override
  protected Result check() throws Exception {
    if (client == null) {
      return Result.unhealthy("not configured");
    }

    int random = SecureRandom.getInstance("SHA1PRNG").nextInt();
    int value  = SecureRandom.getInstance("SHA1PRNG").nextInt();

    this.client.set("HEALTH" + random, 2000, String.valueOf(value));
    String result = (String)this.client.get("HEALTH" + random);

    if (result == null || Integer.parseInt(result) != value) {
      return Result.unhealthy("Fetch failed");
    }

    return Result.healthy();
  }

}
