package com.openchat.imservice.internal.push;

import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;

import java.util.List;

public class OpenchatServiceEnvelopeEntityList {

  private List<OpenchatServiceEnvelopeEntity> messages;

  public OpenchatServiceEnvelopeEntityList() {}

  public List<OpenchatServiceEnvelopeEntity> getMessages() {
    return messages;
  }
}
