package com.openchat.secureim.crypto;

import android.content.Context;

import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.SessionCipher;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.protocol.CiphertextMessage;
import com.openchat.protocal.protocol.PreKeyOpenchatMessage;
import com.openchat.protocal.protocol.OpenchatMessage;
import com.openchat.protocal.state.IdentityKeyStore;
import com.openchat.protocal.state.PreKeyStore;
import com.openchat.protocal.state.SessionStore;
import com.openchat.protocal.state.SignedPreKeyStore;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.crypto.TransportDetails;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.storage.OpenchatServicePreKeyStore;
import com.openchat.imservice.storage.OpenchatServiceSessionStore;

public class OpenchatServiceCipher {

  private final SessionCipher    sessionCipher;
  private final TransportDetails transportDetails;

  public OpenchatServiceCipher(Context context, MasterSecret masterSecret,
                          RecipientDevice recipient, TransportDetails transportDetails)
  {
    SessionStore      sessionStore      = new OpenchatServiceSessionStore(context, masterSecret);
    PreKeyStore       preKeyStore       = new OpenchatServicePreKeyStore(context, masterSecret);
    SignedPreKeyStore signedPreKeyStore = new OpenchatServicePreKeyStore(context, masterSecret);
    IdentityKeyStore  identityKeyStore  = new OpenchatServiceIdentityKeyStore(context, masterSecret);

    this.transportDetails = transportDetails;
    this.sessionCipher    = new SessionCipher(sessionStore, preKeyStore, signedPreKeyStore, identityKeyStore,
                                              recipient.getRecipientId(), recipient.getDeviceId());
  }

  public CiphertextMessage encrypt(byte[] unpaddedMessage) {
    return sessionCipher.encrypt(transportDetails.getPaddedMessageBody(unpaddedMessage));
  }

  public byte[] decrypt(OpenchatMessage message)
      throws DuplicateMessageException, LegacyMessageException, InvalidMessageException, NoSessionException
  {
    byte[] paddedMessage = sessionCipher.decrypt(message);
    return transportDetails.getStrippedPaddingMessageBody(paddedMessage);
  }

  public byte[] decrypt(PreKeyOpenchatMessage message)
      throws InvalidKeyException, LegacyMessageException, InvalidMessageException,
             DuplicateMessageException, InvalidKeyIdException, UntrustedIdentityException, NoSessionException
  {
    byte[] paddedMessage = sessionCipher.decrypt(message);
    return transportDetails.getStrippedPaddingMessageBody(paddedMessage);
  }

  public int getRemoteRegistrationId() {
    return sessionCipher.getRemoteRegistrationId();
  }

}
