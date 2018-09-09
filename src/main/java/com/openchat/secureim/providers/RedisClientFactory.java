package com.openchat.secureim.providers;

import com.openchat.secureim.configuration.DirectoryConfiguration;
import com.openchat.secureim.util.Util;

import java.net.URI;
import java.net.URISyntaxException;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisClientFactory {

  private final JedisPool jedisPool;

  public RedisClientFactory(String url) throws URISyntaxException {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setTestOnBorrow(true);

    URI    redisURI      = new URI(url);
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
