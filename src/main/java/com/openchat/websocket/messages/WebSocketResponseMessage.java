package com.openchat.websocket.messages;


import java.util.Map;
import com.google.common.base.Optional;

public interface WebSocketResponseMessage {
  public long               getRequestId();
  public int                getStatus();
  public String             getMessage();
  public Map<String,String> getHeaders();
  public Optional<byte[]> getBody();
}
