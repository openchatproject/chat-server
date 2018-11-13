package com.openchat.imservice.api;

import com.openchat.protocal.InvalidVersionException;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.util.CredentialsProvider;
import com.openchat.imservice.internal.websocket.WebSocketConnection;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.openchat.imservice.internal.websocket.WebSocketProtos.WebSocketRequestMessage;
import static com.openchat.imservice.internal.websocket.WebSocketProtos.WebSocketResponseMessage;

public class OpenchatServiceMessagePipe {

  private final WebSocketConnection websocket;
  private final CredentialsProvider credentialsProvider;

  OpenchatServiceMessagePipe(WebSocketConnection websocket, CredentialsProvider credentialsProvider) {
    this.websocket           = websocket;
    this.credentialsProvider = credentialsProvider;

    this.websocket.connect();
  }

  
  public OpenchatServiceEnvelope read(long timeout, TimeUnit unit)
      throws InvalidVersionException, IOException, TimeoutException
  {
    return read(timeout, unit, new NullMessagePipeCallback());
  }

  
  public OpenchatServiceEnvelope read(long timeout, TimeUnit unit, MessagePipeCallback callback)
      throws TimeoutException, IOException, InvalidVersionException
  {
    while (true) {
      WebSocketRequestMessage  request  = websocket.readRequest(unit.toMillis(timeout));
      WebSocketResponseMessage response = createWebSocketResponse(request);

      try {
        if (isOpenchatServiceEnvelope(request)) {
          OpenchatServiceEnvelope envelope = new OpenchatServiceEnvelope(request.getBody().toByteArray(),
                                                                     credentialsProvider.getOpenchatingKey());

          callback.onMessage(envelope);
          return envelope;
        }
      } finally {
        websocket.sendResponse(response);
      }
    }
  }

  
  public void shutdown() {
    websocket.disconnect();
  }

  private boolean isOpenchatServiceEnvelope(WebSocketRequestMessage message) {
    return "PUT".equals(message.getVerb()) && "/api/v1/message".equals(message.getPath());
  }

  private WebSocketResponseMessage createWebSocketResponse(WebSocketRequestMessage request) {
    if (isOpenchatServiceEnvelope(request)) {
      return WebSocketResponseMessage.newBuilder()
                                     .setId(request.getId())
                                     .setStatus(200)
                                     .setMessage("OK")
                                     .build();
    } else {
      return WebSocketResponseMessage.newBuilder()
                                     .setId(request.getId())
                                     .setStatus(400)
                                     .setMessage("Unknown")
                                     .build();
    }
  }

  
  public static interface MessagePipeCallback {
    public void onMessage(OpenchatServiceEnvelope envelope);
  }

  private static class NullMessagePipeCallback implements MessagePipeCallback {
    @Override
    public void onMessage(OpenchatServiceEnvelope envelope) {}
  }

}
