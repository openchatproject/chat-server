package com.openchat.websocket.messages;


import com.google.common.base.Optional;

import java.util.Map;

public interface WebSocketRequestMessage {

  public String             getVerb();
  public String             getPath();
  public Map<String,String> getHeaders();
  public Optional<byte[]>   getBody();
  public long               getRequestId();
  public boolean            hasRequestId();

}
