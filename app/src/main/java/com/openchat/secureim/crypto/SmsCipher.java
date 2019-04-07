package com.openchat.secureim.crypto;

import android.content.Context;

import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.sms.IncomingEncryptedMessage;
import com.openchat.secureim.sms.IncomingKeyExchangeMessage;
import com.openchat.secureim.sms.IncomingPreKeyBundleMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.sms.OutgoingKeyExchangeMessage;
import com.openchat.secureim.sms.OutgoingPrekeyBundleMessage;
import com.openchat.secureim.sms.OutgoingTextMessage;
import com.openchat.secureim.sms.SmsTransportDetails;
import com.openchat.protocal.OpenchatAddress;
import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.SessionBuilder;
import com.openchat.protocal.SessionCipher;
import com.openchat.protocal.StaleKeyExchangeException;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.protocol.CiphertextMessage;
import com.openchat.protocal.protocol.KeyExchangeMessage;
import com.openchat.protocal.protocol.PreKeyOpenchatMessage;
import com.openchat.protocal.protocol.OpenchatMessage;
import com.openchat.protocal.state.OpenchatStore;
import com.openchat.imservice.api.push.OpenchatServiceAddress;

import java.io.IOException;

public class SmsCipher {

  private final SmsTransportDetails transportDetails = new SmsTransportDetails();

  private final OpenchatStore axolotlStore;

  public SmsCipher(OpenchatStore axolotlStore) {
    this.axolotlStore = axolotlStore;
  }

  public IncomingTextMessage decrypt(Context context, IncomingTextMessage message)
      throws LegacyMessageException, InvalidMessageException,
             DuplicateMessageException, NoSessionException
  {
    try {
      byte[]         decoded        = transportDetails.getDecodedMessage(message.getMessageBody().getBytes());
      OpenchatMessage openchatMessage = new OpenchatMessage(decoded);
      SessionCipher  sessionCipher  = new SessionCipher(axolotlStore, new OpenchatAddress(message.getSender(), OpenchatServiceAddress.DEFAULT_DEVICE_ID));
      byte[]         padded         = sessionCipher.decrypt(openchatMessage);
      byte[]         plaintext      = transportDetails.getStrippedPaddingMessageBody(padded);

      if (message.isEndSession() && "TERMINATE".equals(new String(plaintext))) {
        axolotlStore.deleteSession(new OpenchatAddress(message.getSender(), OpenchatServiceAddress.DEFAULT_DEVICE_ID));
      }

      return message.withMessageBody(new String(plaintext));
    } catch (IOException e) {
      throw new InvalidMessageException(e);
    }
  }

  public IncomingEncryptedMessage decrypt(Context context, IncomingPreKeyBundleMessage message)
      throws InvalidVersionException, InvalidMessageException, DuplicateMessageException,
             UntrustedIdentityException, LegacyMessageException
  {
    try {
      byte[]               decoded       = transportDetails.getDecodedMessage(message.getMessageBody().getBytes());
      PreKeyOpenchatMessage preKeyMessage = new PreKeyOpenchatMessage(decoded);
      SessionCipher        sessionCipher = new SessionCipher(axolotlStore, new OpenchatAddress(message.getSender(), OpenchatServiceAddress.DEFAULT_DEVICE_ID));
      byte[]               padded        = sessionCipher.decrypt(preKeyMessage);
      byte[]               plaintext     = transportDetails.getStrippedPaddingMessageBody(padded);

      return new IncomingEncryptedMessage(message, new String(plaintext));
    } catch (IOException | InvalidKeyException | InvalidKeyIdException e) {
      throw new InvalidMessageException(e);
    }
  }

  public OutgoingTextMessage encrypt(OutgoingTextMessage message) throws NoSessionException {
    byte[] paddedBody      = transportDetails.getPaddedMessageBody(message.getMessageBody().getBytes());
    String recipientNumber = message.getRecipients().getPrimaryRecipient().getNumber();

    if (!axolotlStore.containsSession(new OpenchatAddress(recipientNumber, OpenchatServiceAddress.DEFAULT_DEVICE_ID))) {
      throw new NoSessionException("No session for: " + recipientNumber);
    }

    SessionCipher     cipher            = new SessionCipher(axolotlStore, new OpenchatAddress(recipientNumber, OpenchatServiceAddress.DEFAULT_DEVICE_ID));
    CiphertextMessage ciphertextMessage = cipher.encrypt(paddedBody);
    String            encodedCiphertext = new String(transportDetails.getEncodedMessage(ciphertextMessage.serialize()));

    if (ciphertextMessage.getType() == CiphertextMessage.PREKEY_TYPE) {
      return new OutgoingPrekeyBundleMessage(message, encodedCiphertext);
    } else {
      return message.withBody(encodedCiphertext);
    }
  }

  public OutgoingKeyExchangeMessage process(Context context, IncomingKeyExchangeMessage message)
      throws UntrustedIdentityException, StaleKeyExchangeException,
             InvalidVersionException, LegacyMessageException, InvalidMessageException
  {
    try {
      Recipient          recipient       = RecipientFactory.getRecipientsFromString(context, message.getSender(), false).getPrimaryRecipient();
      OpenchatAddress     axolotlAddress  = new OpenchatAddress(message.getSender(), OpenchatServiceAddress.DEFAULT_DEVICE_ID);
      KeyExchangeMessage exchangeMessage = new KeyExchangeMessage(transportDetails.getDecodedMessage(message.getMessageBody().getBytes()));
      SessionBuilder     sessionBuilder  = new SessionBuilder(axolotlStore, axolotlAddress);

      KeyExchangeMessage response        = sessionBuilder.process(exchangeMessage);

      if (response != null) {
        byte[] serializedResponse = transportDetails.getEncodedMessage(response.serialize());
        return new OutgoingKeyExchangeMessage(recipient, new String(serializedResponse));
      } else {
        return null;
      }
    } catch (IOException | InvalidKeyException e) {
      throw new InvalidMessageException(e);
    }
  }

}
