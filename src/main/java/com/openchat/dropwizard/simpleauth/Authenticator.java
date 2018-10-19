package com.openchat.dropwizard.simpleauth;


import java.util.Optional;

import io.dropwizard.auth.AuthenticationException;


public interface Authenticator<C, P> {
  
  Optional<P> authenticate(C credentials) throws AuthenticationException;
}
