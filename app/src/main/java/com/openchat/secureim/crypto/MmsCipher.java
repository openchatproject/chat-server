package com.openchat.secureim.crypto;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.mms.TextTransport;
import com.openchat.secureim.protocol.WirePrefix;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.transport.UndeliverableMessageException;
import com.openchat.secureim.util.Util;
import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.SessionCipher;
import com.openchat.protocal.protocol.CiphertextMessage;
import com.openchat.protocal.protocol.OpenchatMessage;
import com.openchat.protocal.state.OpenchatStore;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.push.PushAddress;

import java.io.IOException;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.pdu.EncodedStringValue;
import ws.com.google.android.mms.pdu.MultimediaMessagePdu;
import ws.com.google.android.mms.pdu.PduBody;
import ws.com.google.android.mms.pdu.PduComposer;
import ws.com.google.android.mms.pdu.PduParser;
import ws.com.google.android.mms.pdu.PduPart;
import ws.com.google.android.mms.pdu.RetrieveConf;
import ws.com.google.android.mms.pdu.SendReq;

public class MmsCipher {

  private static final String TAG = MmsCipher.class.getSimpleName();

  private final TextTransport textTransport = new TextTransport();
  private final OpenchatStore axolotlStore;

  public MmsCipher(OpenchatStore axolotlStore) {
    this.axolotlStore = axolotlStore;
  }

  public MultimediaMessagePdu decrypt(Context context, MultimediaMessagePdu pdu)
      throws InvalidMessageException, LegacyMessageException, DuplicateMessageException,
             NoSessionException
  {
    try {
      Recipients    recipients    = RecipientFactory.getRecipientsFromString(context, pdu.getFrom().getString(), false);
      long          recipientId   = recipients.getPrimaryRecipient().getRecipientId();
      SessionCipher sessionCipher = new SessionCipher(axolotlStore, recipientId, 1);
      Optional<byte[]> ciphertext = getEncryptedData(pdu);

      if (!ciphertext.isPresent()) {
        throw new InvalidMessageException("No ciphertext present!");
      }

      byte[] decodedCiphertext = textTransport.getDecodedMessage(ciphertext.get());
      byte[] plaintext;

      if (decodedCiphertext == null) {
        throw new InvalidMessageException("failed to decode ciphertext");
      }

      try {
        plaintext = sessionCipher.decrypt(new OpenchatMessage(decodedCiphertext));
      } catch (InvalidMessageException e) {
        if (ciphertext.get().length > 2) {
          Log.w(TAG, "Attempting truncated decrypt...");
          byte[] truncated = Util.trim(ciphertext.get(), ciphertext.get().length - 1);
          decodedCiphertext = textTransport.getDecodedMessage(truncated);
          plaintext = sessionCipher.decrypt(new OpenchatMessage(decodedCiphertext));
        } else {
          throw e;
        }
      }

      MultimediaMessagePdu plaintextGenericPdu = (MultimediaMessagePdu) new PduParser(plaintext).parse();
      return new RetrieveConf(plaintextGenericPdu.getPduHeaders(), plaintextGenericPdu.getBody());
    } catch (RecipientFormattingException | IOException e) {
      throw new InvalidMessageException(e);
    }
  }

  public SendReq encrypt(Context context, SendReq message)
      throws NoSessionException, RecipientFormattingException, UndeliverableMessageException
  {
    EncodedStringValue[] encodedRecipient = message.getTo();
    String               recipientString  = encodedRecipient[0].getString();
    Recipients           recipients       = RecipientFactory.getRecipientsFromString(context, recipientString, false);
    long                 recipientId      = recipients.getPrimaryRecipient().getRecipientId();
    byte[]               pduBytes         = new PduComposer(context, message).make();

    if (pduBytes == null) {
      throw new UndeliverableMessageException("PDU composition failed, null payload");
    }

    if (!axolotlStore.containsSession(recipientId, PushAddress.DEFAULT_DEVICE_ID)) {
      throw new NoSessionException("No session for: " + recipientId);
    }

    SessionCipher     cipher            = new SessionCipher(axolotlStore, recipientId, PushAddress.DEFAULT_DEVICE_ID);
    CiphertextMessage ciphertextMessage = cipher.encrypt(pduBytes);
    byte[]            encryptedPduBytes = textTransport.getEncodedMessage(ciphertextMessage.serialize());

    PduBody body         = new PduBody();
    PduPart part         = new PduPart();
    SendReq encryptedPdu = new SendReq(message.getPduHeaders(), body);

    part.setContentId((System.currentTimeMillis()+"").getBytes());
    part.setContentType(ContentType.TEXT_PLAIN.getBytes());
    part.setName((System.currentTimeMillis()+"").getBytes());
    part.setData(encryptedPduBytes);
    body.addPart(part);
    encryptedPdu.setSubject(new EncodedStringValue(WirePrefix.calculateEncryptedMmsSubject()));
    encryptedPdu.setBody(body);

    return encryptedPdu;
  }

  private Optional<byte[]> getEncryptedData(MultimediaMessagePdu pdu) {
    for (int i=0;i<pdu.getBody().getPartsNum();i++) {
      if (new String(pdu.getBody().getPart(i).getContentType()).equals(ContentType.TEXT_PLAIN)) {
        return Optional.of(pdu.getBody().getPart(i).getData());
      }
    }

    return Optional.absent();
  }

}
