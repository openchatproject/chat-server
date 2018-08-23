package com.openchat.secureim.storage;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.openchat.secureim.entities.ClientContact;
import com.openchat.secureim.util.IterablePair;
import com.openchat.secureim.util.Util;

import java.util.LinkedList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class DirectoryManager {

  private static final byte[] DIRECTORY_KEY = {'d', 'i', 'r', 'e', 'c', 't', 'o', 'r', 'y'};

  private final JedisPool redisPool;

  public DirectoryManager(JedisPool redisPool) {
    this.redisPool = redisPool;
  }

  public void remove(String number) {
    remove(Util.getContactToken(number));
  }

  public void remove(BatchOperationHandle handle, String number) {
    remove(handle, Util.getContactToken(number));
  }

  public void remove(byte[] token) {
    Jedis jedis = redisPool.getResource();
    jedis.hdel(DIRECTORY_KEY, token);
    redisPool.returnResource(jedis);
  }

  public void remove(BatchOperationHandle handle, byte[] token) {
    Pipeline pipeline = handle.pipeline;
    pipeline.hdel(DIRECTORY_KEY, token);
  }

  public void add(ClientContact contact) {
    TokenValue tokenValue = new TokenValue(contact.getRelay(), contact.isSupportsSms());
    Jedis      jedis      = redisPool.getResource();

    jedis.hset(DIRECTORY_KEY, contact.getToken(), new Gson().toJson(tokenValue).getBytes());
    redisPool.returnResource(jedis);
  }

  public void add(BatchOperationHandle handle, ClientContact contact) {
    Pipeline   pipeline   = handle.pipeline;
    TokenValue tokenValue = new TokenValue(contact.getRelay(), contact.isSupportsSms());

    pipeline.hset(DIRECTORY_KEY, contact.getToken(), new Gson().toJson(tokenValue).getBytes());
  }

  public Optional<ClientContact> get(byte[] token) {
    Jedis jedis = redisPool.getResource();

    try {
      byte[] result = jedis.hget(DIRECTORY_KEY, token);

      if (result == null) {
        return Optional.absent();
      }

      TokenValue tokenValue = new Gson().fromJson(new String(result), TokenValue.class);
      return Optional.of(new ClientContact(token, tokenValue.relay, tokenValue.supportsSms));
    } finally {
      redisPool.returnResource(jedis);
    }
  }

  public List<ClientContact> get(List<byte[]> tokens) {
    Jedis jedis = redisPool.getResource();

    try {
      Pipeline               pipeline = jedis.pipelined();
      List<Response<byte[]>> futures  = new LinkedList<>();
      List<ClientContact>    results  = new LinkedList<>();

      try {
        for (byte[] token : tokens) {
          futures.add(pipeline.hget(DIRECTORY_KEY, token));
        }
      } finally {
        pipeline.sync();
      }

      IterablePair<byte[], Response<byte[]>> lists = new IterablePair<>(tokens, futures);

      for (IterablePair.Pair<byte[], Response<byte[]>> pair : lists) {
        if (pair.second().get() != null) {
          TokenValue    tokenValue    = new Gson().fromJson(new String(pair.second().get()), TokenValue.class);
          ClientContact clientContact = new ClientContact(pair.first(), tokenValue.relay, tokenValue.supportsSms);

          results.add(clientContact);
        }
      }

      return results;
    } finally {
      redisPool.returnResource(jedis);
    }
  }

  public BatchOperationHandle startBatchOperation() {
    Jedis jedis = redisPool.getResource();
    return new BatchOperationHandle(jedis, jedis.pipelined());
  }

  public void stopBatchOperation(BatchOperationHandle handle) {
    Pipeline pipeline = handle.pipeline;
    Jedis    jedis    = handle.jedis;

    pipeline.sync();
    redisPool.returnResource(jedis);
  }

  public static class BatchOperationHandle {

    public final Pipeline pipeline;
    public final Jedis    jedis;

    public BatchOperationHandle(Jedis jedis, Pipeline pipeline) {
      this.pipeline = pipeline;
      this.jedis    = jedis;
    }
  }

  private static class TokenValue {
    @SerializedName("r")
    private String  relay;

    @SerializedName("s")
    private boolean supportsSms;

    public TokenValue(String relay, boolean supportsSms) {
      this.relay       = relay;
      this.supportsSms = supportsSms;
    }
  }
}
