package com.openchat.websocket;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.websocket.messages.WebSocketMessage;
import com.openchat.websocket.messages.WebSocketMessageFactory;
import com.openchat.websocket.messages.WebSocketResponseMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class WebSocketClient {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketClient.class);

  private final Session                                             session;
  private final RemoteEndpoint                                      remoteEndpoint;
  private final WebSocketMessageFactory                             messageFactory;
  private final Map<Long, SettableFuture<WebSocketResponseMessage>> pendingRequestMapper;

  public WebSocketClient(Session session, RemoteEndpoint remoteEndpoint,
                         WebSocketMessageFactory messageFactory,
                         Map<Long, SettableFuture<WebSocketResponseMessage>> pendingRequestMapper)
  {
    this.session              = session;
    this.remoteEndpoint       = remoteEndpoint;
    this.messageFactory       = messageFactory;
    this.pendingRequestMapper = pendingRequestMapper;
  }

  public ListenableFuture<WebSocketResponseMessage> sendRequest(String verb, String path,
                                                                List<String> headers,
                                                                Optional<byte[]> body)
  {
    final long                                     requestId = generateRequestId();
    final SettableFuture<WebSocketResponseMessage> future    = SettableFuture.create();

    pendingRequestMapper.put(requestId, future);

    WebSocketMessage requestMessage = messageFactory.createRequest(Optional.of(requestId), verb, path, headers, body);

    try {
      remoteEndpoint.sendBytes(ByteBuffer.wrap(requestMessage.toByteArray()), new WriteCallback() {
        @Override
        public void writeFailed(Throwable x) {
          logger.debug("Write failed", x);
          pendingRequestMapper.remove(requestId);
          future.setException(x);
        }

        @Override
        public void writeSuccess() {}
      });
    } catch (WebSocketException e) {
      logger.debug("Write", e);
      pendingRequestMapper.remove(requestId);
      future.setException(e);
    }

    return future;
  }

  public void close(int code, String message) {
    session.close(code, message);
  }

  public void hardDisconnectQuietly() {
    try {
      session.disconnect();
    } catch (IOException e) {
      // quietly we said
    }
  }

  private long generateRequestId() {
    return Math.abs(new SecureRandom().nextLong());
  }

}
