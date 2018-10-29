package com.openchat.push.providers;

import com.openchat.push.config.RedisConfiguration;
import com.openchat.push.util.Util;

import java.net.URI;
import java.net.URISyntaxException;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisClientFactory {

  private final JedisPool jedisPool;

  public RedisClientFactory(RedisConfiguration redisConfig) throws URISyntaxException {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setTestOnBorrow(true);

    URI    redisURI      = new URI(redisConfig.getUrl());
    String redisHost     = redisURI.getHost();
    int    redisPort     = redisURI.getPort();
    String redisPassword = null;

    if (!Util.isEmpty(redisURI.getUserInfo())) {
      redisPassword = redisURI.getUserInfo().split(":",2)[1];
    }

    this.jedisPool = new JedisPool(poolConfig, redisHost, redisPort,
                                   Protocol.DEFAULT_TIMEOUT, redisPassword);
  }

  public JedisPool getRedisClientPool() {
    return jedisPool;
  }

}
