package com.openchat.websocket.setup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import org.eclipse.jetty.server.RequestLog;
import com.openchat.websocket.auth.WebSocketAuthenticator;
import com.openchat.websocket.messages.WebSocketMessageFactory;
import com.openchat.websocket.messages.protobuf.ProtobufWebSocketMessageFactory;

import javax.validation.Validator;

import io.dropwizard.Configuration;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.server.AbstractServerFactory;
import io.dropwizard.setup.Environment;

public class WebSocketEnvironment {

  private final JerseyContainerHolder jerseyServletContainer;
  private final JerseyEnvironment     jerseyEnvironment;
  private final ObjectMapper          objectMapper;
  private final Validator             validator;
  private final RequestLog            requestLog;

  private WebSocketAuthenticator authenticator;
  private WebSocketMessageFactory   messageFactory;
  private WebSocketConnectListener  connectListener;

  public WebSocketEnvironment(Environment environment, Configuration configuration) {
    DropwizardResourceConfig jerseyConfig = new DropwizardResourceConfig(environment.metrics());

    this.objectMapper           = environment.getObjectMapper();
    this.validator              = environment.getValidator();
    this.requestLog             = ((AbstractServerFactory)configuration.getServerFactory()).getRequestLogFactory().build("websocket");
    this.jerseyServletContainer = new JerseyContainerHolder(new ServletContainer(jerseyConfig)  );
    this.jerseyEnvironment      = new JerseyEnvironment(jerseyServletContainer, jerseyConfig);
    this.messageFactory         = new ProtobufWebSocketMessageFactory();
  }

  public JerseyEnvironment jersey() {
    return jerseyEnvironment;
  }

  public WebSocketAuthenticator getAuthenticator() {
    return authenticator;
  }

  public void setAuthenticator(WebSocketAuthenticator authenticator) {
    this.authenticator = authenticator;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public RequestLog getRequestLog() {
    return requestLog;
  }

  public Validator getValidator() {
    return validator;
  }

  public ServletContainer getJerseyServletContainer() {
    return jerseyServletContainer.getContainer();
  }

  public WebSocketMessageFactory getMessageFactory() {
    return messageFactory;
  }

  public void setMessageFactory(WebSocketMessageFactory messageFactory) {
    this.messageFactory = messageFactory;
  }

  public WebSocketConnectListener getConnectListener() {
    return connectListener;
  }

  public void setConnectListener(WebSocketConnectListener connectListener) {
    this.connectListener = connectListener;
  }
}
