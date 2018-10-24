package com.openchat.websocket.setup;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.RequestLog;
import org.glassfish.jersey.servlet.ServletContainer;
import com.openchat.websocket.auth.WebSocketAuthenticator;
import com.openchat.websocket.configuration.WebSocketConfiguration;
import com.openchat.websocket.messages.WebSocketMessageFactory;
import com.openchat.websocket.messages.protobuf.ProtobufWebSocketMessageFactory;

import javax.servlet.http.HttpServlet;
import javax.validation.Validator;

import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;

public class WebSocketEnvironment {

  private final JerseyContainerHolder jerseyServletContainer;
  private final JerseyEnvironment     jerseyEnvironment;
  private final ObjectMapper          objectMapper;
  private final Validator             validator;
  private final RequestLog            requestLog;
  private final long                  idleTimeoutMillis;

  private WebSocketAuthenticator   authenticator;
  private WebSocketMessageFactory  messageFactory;
  private WebSocketConnectListener connectListener;

  public WebSocketEnvironment(Environment environment, WebSocketConfiguration configuration) {
    this(environment, configuration, 60000);
  }

  public WebSocketEnvironment(Environment environment, WebSocketConfiguration configuration, long idleTimeoutMillis) {
    this(environment, configuration.getRequestLog().build("websocket"), idleTimeoutMillis);
  }

  public WebSocketEnvironment(Environment environment, RequestLog requestLog, long idleTimeoutMillis) {
    DropwizardResourceConfig jerseyConfig = new DropwizardResourceConfig(environment.metrics());

    this.objectMapper           = environment.getObjectMapper();
    this.validator              = environment.getValidator();
    this.requestLog             = requestLog;
    this.jerseyServletContainer = new JerseyContainerHolder(new ServletContainer(jerseyConfig)  );
    this.jerseyEnvironment      = new JerseyEnvironment(jerseyServletContainer, jerseyConfig);
    this.messageFactory         = new ProtobufWebSocketMessageFactory();
    this.idleTimeoutMillis      = idleTimeoutMillis;
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

  public long getIdleTimeoutMillis() {
    return idleTimeoutMillis;
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

  public HttpServlet getJerseyServletContainer() {
    return (HttpServlet)jerseyServletContainer.getContainer();
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
