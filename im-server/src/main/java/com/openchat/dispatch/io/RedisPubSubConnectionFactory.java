package com.openchat.dispatch.io;

import com.openchat.dispatch.redis.PubSubConnection;

public interface RedisPubSubConnectionFactory {

  public PubSubConnection connect();

}
