package com.openchat.secureim.mms;

import com.openchat.secureim.attachments.Attachment;
import com.openchat.secureim.database.ThreadDatabase;
import com.openchat.secureim.recipients.Recipient;

import java.util.LinkedList;

public class OutgoingExpirationUpdateMessage extends OutgoingSecureMediaMessage {

  public OutgoingExpirationUpdateMessage(Recipient recipient, long sentTimeMillis, long expiresIn) {
    super(recipient, "", new LinkedList<Attachment>(), sentTimeMillis,
          ThreadDatabase.DistributionTypes.CONVERSATION, expiresIn);
  }

  @Override
  public boolean isExpirationUpdate() {
    return true;
  }

}
