package com.openchat.websocket.auth;

import org.eclipse.jetty.websocket.api.UpgradeRequest;

import java.util.Optional;

public interface WebSocketAuthenticator<T> {
  public Optional<T> authenticate(UpgradeRequest request) throws AuthenticationException;
}
