package com.openchat.websocket.messages;


import java.util.List;
import com.google.common.base.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
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
