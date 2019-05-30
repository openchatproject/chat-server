package com.openchat.secureim.mms;

import android.content.Context;
import android.text.TextUtils;

import com.openchat.secureim.crypto.MasterSecretUnion;
import com.openchat.secureim.crypto.MediaKey;
import com.openchat.secureim.database.ThreadDatabase;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.Util;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;

import java.util.List;

import ws.com.google.android.mms.pdu.PduBody;
import ws.com.google.android.mms.pdu.PduPart;

public class OutgoingMediaMessage {

  private   final Recipients recipients;
  protected final PduBody    body;
  private   final int        distributionType;

  public OutgoingMediaMessage(Context context, Recipients recipients, PduBody body,
                              String message, int distributionType)
  {
    this.recipients       = recipients;
    this.body             = body;
    this.distributionType = distributionType;

    if (!TextUtils.isEmpty(message)) {
      this.body.addPart(new TextSlide(context, message).getPart());
    }
  }

  public OutgoingMediaMessage(Context context, Recipients recipients, SlideDeck slideDeck,
                              String message, int distributionType)
  {
    this(context, recipients, slideDeck.toPduBody(), message, distributionType);
  }

  public OutgoingMediaMessage(Context context, MasterSecretUnion masterSecret,
                              Recipients recipients, List<OpenchatServiceAttachment> attachments,
                              String message)
  {
    this(context, recipients, pduBodyFor(masterSecret, attachments), message,
         ThreadDatabase.DistributionTypes.CONVERSATION);
  }

  public OutgoingMediaMessage(OutgoingMediaMessage that) {
    this.recipients       = that.getRecipients();
    this.body             = that.body;
    this.distributionType = that.distributionType;
  }

  public Recipients getRecipients() {
    return recipients;
  }

  public PduBody getPduBody() {
    return body;
  }

  public int getDistributionType() {
    return distributionType;
  }

  public boolean isSecure() {
    return false;
  }

  public boolean isGroup() {
    return false;
  }

  private static PduBody pduBodyFor(MasterSecretUnion masterSecret, List<OpenchatServiceAttachment> attachments) {
    PduBody body = new PduBody();

    for (OpenchatServiceAttachment attachment : attachments) {
      if (attachment.isPointer()) {
        PduPart media        = new PduPart();
        String  encryptedKey = MediaKey.getEncrypted(masterSecret, attachment.asPointer().getKey());

        media.setContentType(Util.toIsoBytes(attachment.getContentType()));
        media.setContentLocation(Util.toIsoBytes(String.valueOf(attachment.asPointer().getId())));
        media.setContentDisposition(Util.toIsoBytes(encryptedKey));

        body.addPart(media);
      }
    }

    return body;
  }

}
