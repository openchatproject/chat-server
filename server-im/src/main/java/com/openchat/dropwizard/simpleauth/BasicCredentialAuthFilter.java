package com.openchat.dropwizard.simpleauth;

import com.google.common.base.Optional;
import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;

@Priority(Priorities.AUTHENTICATION)
public class BasicCredentialAuthFilter<P> extends AuthFilter<BasicCredentials, P> {

  private static final Logger LOGGER = LoggerFactory.getLogger(BasicCredentialAuthFilter.class);

  private BasicCredentialAuthFilter() {}

  @Override
  public void filter(final ContainerRequestContext requestContext) throws IOException {
    final String header = requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    try {
      if (header != null) {
        final int space = header.indexOf(' ');
        if (space > 0) {
          final String method = header.substring(0, space);
          if (prefix.equalsIgnoreCase(method)) {
            final String decoded = new String(
                BaseEncoding.base64().decode(header.substring(space + 1)),
                StandardCharsets.UTF_8);
            final int i = decoded.indexOf(':');
            if (i > 0) {
              final String username = decoded.substring(0, i);
              final String password = decoded.substring(i + 1);
              final BasicCredentials credentials = new BasicCredentials(username, password);
              try {
                Optional<P> principal = authenticator.authenticate(credentials);

                if (principal.isPresent()) {
                  requestContext.setSecurityContext(new AuthSecurityContext<P>(principal.get(), false));
                  return;
                }
              } catch (AuthenticationException e) {
                LOGGER.warn("Error authenticating credentials", e);
                throw new InternalServerErrorException();
              }
            }
          }
        }
      }
    } catch (IllegalArgumentException e) {
      LOGGER.warn("Error decoding credentials", e);
    }

    throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
  }

  public static class Builder<P> extends AuthFilter.AuthFilterBuilder<BasicCredentials, P, BasicCredentialAuthFilter<P>> {

    @Override
    protected BasicCredentialAuthFilter<P> newInstance() {
      return new BasicCredentialAuthFilter<>();
    }
  }

}