package com.openchat.websocket.messages;



import java.util.Map;
import java.util.Optional;

public interface WebSocketRequestMessage {

  public String             getVerb();
  public String             getPath();
  public Map<String,String> getHeaders();
  public Optional<byte[]> getBody();
  public long               getRequestId();
  public boolean            hasRequestId();

}
