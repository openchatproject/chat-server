package com.openchat.imservice.api;

import com.google.protobuf.ByteString;

import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.util.Pair;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.api.profiles.OpenchatServiceProfile;
import com.openchat.imservice.api.util.CredentialsProvider;
import com.openchat.imservice.internal.push.OutgoingPushMessageList;
import com.openchat.imservice.internal.push.SendMessageResponse;
import com.openchat.imservice.internal.util.JsonUtil;
import com.openchat.imservice.internal.util.Util;
import com.openchat.imservice.internal.websocket.WebSocketConnection;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.openchat.imservice.internal.websocket.WebSocketProtos.WebSocketRequestMessage;
import static com.openchat.imservice.internal.websocket.WebSocketProtos.WebSocketResponseMessage;

public class OpenchatServiceMessagePipe {

  private static final String TAG = OpenchatServiceMessagePipe.class.getName();

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

  public SendMessageResponse send(OutgoingPushMessageList list) throws IOException {
    try {
      WebSocketRequestMessage requestMessage = WebSocketRequestMessage.newBuilder()
                                                                      .setId(SecureRandom.getInstance("SHA1PRNG").nextLong())
                                                                      .setVerb("PUT")
                                                                      .setPath(String.format("/v1/messages/%s", list.getDestination()))
                                                                      .addHeaders("content-type:application/json")
                                                                      .setBody(ByteString.copyFrom(JsonUtil.toJson(list).getBytes()))
                                                                      .build();

      Pair<Integer, String> response = websocket.sendRequest(requestMessage).get(10, TimeUnit.SECONDS);

      if (response.first() < 200 || response.first() >= 300) {
        throw new IOException("Non-successful response: " + response.first());
      }

      if (Util.isEmpty(response.second())) return new SendMessageResponse(false);
      else                                 return JsonUtil.fromJson(response.second(), SendMessageResponse.class);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new IOException(e);
    }
  }

  public OpenchatServiceProfile getProfile(OpenchatServiceAddress address) throws IOException {
    try {
      WebSocketRequestMessage requestMessage = WebSocketRequestMessage.newBuilder()
                                                                      .setId(SecureRandom.getInstance("SHA1PRNG").nextLong())
                                                                      .setVerb("GET")
                                                                      .setPath(String.format("/v1/profile/%s", address.getNumber()))
                                                                      .build();

      Pair<Integer, String> response = websocket.sendRequest(requestMessage).get(10, TimeUnit.SECONDS);

      if (response.first() < 200 || response.first() >= 300) {
        throw new IOException("Non-successful response: " + response.first());
      }

      return JsonUtil.fromJson(response.second(), OpenchatServiceProfile.class);
    } catch (NoSuchAlgorithmException nsae) {
      throw new AssertionError(nsae);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new IOException(e);
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
