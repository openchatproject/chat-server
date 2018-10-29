package com.openchat.secureim.mappers;

import com.openchat.secureim.controllers.RateLimitExceededException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RateLimitExceededExceptionMapper implements ExceptionMapper<RateLimitExceededException> {
  @Override
  public Response toResponse(RateLimitExceededException e) {
    return Response.status(413).build();
  }
}
