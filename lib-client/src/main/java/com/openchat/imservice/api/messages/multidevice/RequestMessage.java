package com.openchat.imservice.api.messages.multidevice;

import com.openchat.imservice.internal.push.OpenchatServiceProtos.SyncMessage.Request;

public class RequestMessage {

  private final Request request;

  public RequestMessage(Request request) {
    this.request = request;
  }

  public boolean isContactsRequest() {
    return request.getType() == Request.Type.CONTACTS;
  }
}
