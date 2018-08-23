package com.openchat.secureim.providers;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import com.openchat.secureim.configuration.MemcacheConfiguration;
import com.openchat.secureim.util.Util;

import java.io.IOException;

public class MemcachedClientFactory {

  private final MemcachedClient client;

  public MemcachedClientFactory(MemcacheConfiguration config) throws IOException {
    ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
    builder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);

    if (!Util.isEmpty(config.getUser())) {
      AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" },
                                             new PlainCallbackHandler(config.getUser(),
                                                                      config.getPassword()));

      builder.setAuthDescriptor(ad);
    }


    this.client = new MemcachedClient(builder.build(),
                                      AddrUtil.getAddresses(config.getServers()));
  }


  public MemcachedClient getClient() {
    return client;
  }
}
