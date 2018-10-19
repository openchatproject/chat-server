package com.openchat.dropwizard.simpleauth;


import com.google.common.base.Optional;

import io.dropwizard.auth.AuthenticationException;


public interface Authenticator<C, P> {
  
  Optional<P> authenticate(C credentials) throws AuthenticationException;
}
