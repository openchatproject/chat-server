package com.openchat.websocket.auth;

import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.UpgradeRequest;

import java.util.Optional;

public interface WebSocketAuthenticator<T> {
  AuthenticationResult<T> authenticate(UpgradeRequest request) throws AuthenticationException;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public class AuthenticationResult<T> {
    private final Optional<T> user;
    private final boolean     required;

    public AuthenticationResult(Optional<T> user, boolean required) {
      this.user     = user;
      this.required = required;
    }

    public Optional<T> getUser() {
      return user;
    }

    public boolean isRequired() {
      return required;
    }
  }
}
