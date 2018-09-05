package com.openchat.secureim.websocket;

import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.auth.AccountAuthenticator;
import com.openchat.secureim.controllers.WebsocketController;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.PubSubManager;
import com.openchat.secureim.storage.StoredMessages;


public class WebsocketControllerFactory extends WebSocketServlet implements WebSocketCreator {

  private final Logger logger = LoggerFactory.getLogger(WebsocketControllerFactory.class);

  private final PushSender           pushSender;
  private final StoredMessages       storedMessages;
  private final PubSubManager        pubSubManager;
  private final AccountAuthenticator accountAuthenticator;
  private final AccountsManager      accounts;

  public WebsocketControllerFactory(AccountAuthenticator accountAuthenticator,
                                    AccountsManager      accounts,
                                    PushSender           pushSender,
                                    StoredMessages       storedMessages,
                                    PubSubManager        pubSubManager)
  {
    this.accountAuthenticator = accountAuthenticator;
    this.accounts             = accounts;
    this.pushSender           = pushSender;
    this.storedMessages       = storedMessages;
    this.pubSubManager        = pubSubManager;
  }

  @Override
  public void configure(WebSocketServletFactory factory) {
    factory.setCreator(this);
  }

  @Override
  public Object createWebSocket(UpgradeRequest upgradeRequest, UpgradeResponse upgradeResponse) {
    return new WebsocketController(accountAuthenticator, accounts,  pushSender, pubSubManager, storedMessages);
  }
}
