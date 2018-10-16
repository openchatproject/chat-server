package com.openchat.secureim.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.dispatch.io.RedisPubSubConnectionFactory;
import com.openchat.dispatch.redis.PubSubConnection;
import com.openchat.secureim.redis.ReplicatedJedisPool;
import com.openchat.secureim.util.Util;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class RedisClientFactory implements RedisPubSubConnectionFactory {

  private final Logger logger = LoggerFactory.getLogger(RedisClientFactory.class);

  private final String    host;
  private final int       port;
  private final ReplicatedJedisPool jedisPool;

  public RedisClientFactory(String url, List<String> replicaUrls) throws URISyntaxException {
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setTestOnBorrow(true);

    URI redisURI = new URI(url);

    this.host      = redisURI.getHost();
    this.port      = redisURI.getPort();

    JedisPool       masterPool   = new JedisPool(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null);
    List<JedisPool> replicaPools = new LinkedList<>();

    for (String replicaUrl : replicaUrls) {
      URI replicaURI = new URI(replicaUrl);

      replicaPools.add(new JedisPool(poolConfig, replicaURI.getHost(), replicaURI.getPort(),
                                     500, Protocol.DEFAULT_TIMEOUT, null,
                                     Protocol.DEFAULT_DATABASE, null, false, null ,
                                     null, null));
    }

    this.jedisPool = new ReplicatedJedisPool(masterPool, replicaPools);
  }

  public ReplicatedJedisPool getRedisClientPool() {
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
