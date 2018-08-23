package com.openchat.secureim.providers;

import com.yammer.metrics.core.HealthCheck;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisHealthCheck extends HealthCheck {

  private final JedisPool clientPool;

  public RedisHealthCheck(JedisPool clientPool) {
    super("redis");
    this.clientPool = clientPool;
  }

  @Override
  protected Result check() throws Exception {
    Jedis client = clientPool.getResource();

    try {
      client.set("HEALTH", "test");

      if (!"test".equals(client.get("HEALTH"))) {
        return Result.unhealthy("fetch failed");
      }

      return Result.healthy();
    } finally {
      clientPool.returnResource(client);
    }
  }
}
