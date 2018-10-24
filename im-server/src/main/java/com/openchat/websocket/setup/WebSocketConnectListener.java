package com.openchat.websocket.setup;

import com.openchat.websocket.session.WebSocketSessionContext;

public interface WebSocketConnectListener {
  public void onWebSocketConnect(WebSocketSessionContext context);
}
