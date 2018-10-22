package com.openchat.websocket.messages;

import com.google.common.base.Optional;

import java.util.List;

public interface WebSocketMessageFactory {

  public WebSocketMessage parseMessage(byte[] serialized, int offset, int len)
      throws InvalidMessageException;

  public WebSocketMessage createRequest(Optional<Long> requestId,
                                        String verb, String path,
                                        List<String> headers,
                                        Optional<byte[]> body);

  public WebSocketMessage createResponse(long requestId, int status, String message,
                                         List<String> headers,
                                         Optional<byte[]> body);

}
