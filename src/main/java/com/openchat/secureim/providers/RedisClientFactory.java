package com.openchat.secureim.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.dispatch.io.RedisPubSubConnectionFactory;
import com.openchat.dispatch.redis.PubSubConnection;
import com.openchat.secureim.util.Util;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisClientFactory implements RedisPubSubConnectionFactory {

  private final Logger logger = LoggerFactory.getLogger(RedisClientFactory.class);

  private final String    host;
  private final int       port;
  private final JedisPool jedisPool;

  public RedisClientFactory(String url) throws URISyntaxException {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setTestOnBorrow(true);

    URI redisURI = new URI(url);

    this.host      = redisURI.getHost();
    this.port      = redisURI.getPort();
    this.jedisPool = new JedisPool(poolConfig, host, port,
                                   Protocol.DEFAULT_TIMEOUT, null);
  }

  public JedisPool getRedisClientPool() {
    return jedisPool;
  }

  @Override
  public PubSubConnection connect() {
    while (true) {
      try {
        Socket socket = new Socket(host, port);
        return new PubSubConnection(socket);
      } catch (IOException e) {
        logger.warn("Error connecting", e);
        Util.sleep(200);
      }
    }
  }
}
