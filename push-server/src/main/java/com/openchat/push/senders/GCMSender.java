package com.openchat.push.senders;

import com.openchat.push.entities.GcmMessage;

import io.dropwizard.lifecycle.Managed;

public interface GCMSender extends Managed {
  public void sendMessage(GcmMessage message);
}
