package com.openchat.secureim.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class IOExceptionMapper implements ExceptionMapper<IOException> {

  private final Logger logger = LoggerFactory.getLogger(IOExceptionMapper.class);

  @Override
  public Response toResponse(IOException e) {
    logger.warn("IOExceptionMapper", e);
    return Response.status(503).build();
  }
}
