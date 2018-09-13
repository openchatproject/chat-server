package com.openchat.secureim.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.MessagesManager;
import com.openchat.secureim.storage.PubSubManager;
import com.openchat.secureim.storage.PubSubProtos;
import com.openchat.secureim.util.Util;
import com.openchat.websocket.session.WebSocketSessionContext;
import com.openchat.websocket.setup.WebSocketConnectListener;

public class AuthenticatedConnectListener implements WebSocketConnectListener {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketConnection.class);

  private final AccountsManager accountsManager;
  private final PushSender      pushSender;
  private final MessagesManager messagesManager;
  private final PubSubManager   pubSubManager;

  public AuthenticatedConnectListener(AccountsManager accountsManager, PushSender pushSender,
                                      MessagesManager messagesManager, PubSubManager pubSubManager)
  {
    this.accountsManager = accountsManager;
    this.pushSender      = pushSender;
    this.messagesManager = messagesManager;
    this.pubSubManager   = pubSubManager;
  }

  @Override
  public void onWebSocketConnect(WebSocketSessionContext context) {
    Account account = context.getAuthenticated(Account.class).get();
    Device  device  = account.getAuthenticatedDevice().get();

    updateLastSeen(account, device);
    closeExistingDeviceConnection(account, device);

    final WebSocketConnection connection = new WebSocketConnection(accountsManager, pushSender,
                                                                   messagesManager, pubSubManager,
                                                                   account, device,
                                                                   context.getClient());

    connection.onConnected();

    context.addListener(new WebSocketSessionContext.WebSocketEventListener() {
      @Override
      public void onWebSocketClose(WebSocketSessionContext context, int statusCode, String reason) {
        connection.onConnectionLost();
      }
    });
  }

  private void updateLastSeen(Account account, Device device) {
    if (device.getLastSeen() != Util.todayInMillis()) {
      device.setLastSeen(Util.todayInMillis());
      accountsManager.update(account);
    }
  }

  private void closeExistingDeviceConnection(Account account, Device device) {
    pubSubManager.publish(new WebsocketAddress(account.getNumber(), device.getId()),
                          PubSubProtos.PubSubMessage.newBuilder()
                                                    .setType(PubSubProtos.PubSubMessage.Type.CLOSE)
                                                    .build());
  }
}

