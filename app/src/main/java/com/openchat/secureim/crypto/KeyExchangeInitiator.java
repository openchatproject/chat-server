package com.openchat.secureim.crypto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.openchat.secureim.R;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingKeyExchangeMessage;
import com.openchat.secureim.util.Dialogs;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECKeyPair;
import com.openchat.protocal.protocol.KeyExchangeMessage;
import com.openchat.protocal.state.SessionRecord;
import com.openchat.protocal.state.SessionStore;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.storage.OpenchatServiceSessionStore;
import com.openchat.imservice.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyExchangeInitiator {

  public static void initiate(final Context context, final MasterSecret masterSecret, final Recipient recipient, boolean promptOnExisting) {
    if (promptOnExisting && hasInitiatedSession(context, masterSecret, recipient)) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(context);
      dialog.setTitle(R.string.KeyExchangeInitiator_initiate_despite_existing_request_question);
      dialog.setMessage(R.string.KeyExchangeInitiator_youve_already_sent_a_session_initiation_request_to_this_recipient_are_you_sure);
      dialog.setIcon(Dialogs.resolveIcon(context, R.attr.dialog_alert_icon));
      dialog.setCancelable(true);
      dialog.setPositiveButton(R.string.KeyExchangeInitiator_send, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          initiateKeyExchange(context, masterSecret, recipient);
        }
      });
      dialog.setNegativeButton(android.R.string.cancel, null);
      dialog.show();
    } else {
      initiateKeyExchange(context, masterSecret, recipient);
    }
  }

  private static void initiateKeyExchange(Context context, MasterSecret masterSecret, Recipient recipient) {
    int             sequence     = getRandomSequence();
    int             flags        = KeyExchangeMessage.INITIATE_FLAG;
    ECKeyPair       baseKey      = Curve.generateKeyPair(true);
    ECKeyPair       ephemeralKey = Curve.generateKeyPair(true);
    IdentityKeyPair identityKey  = IdentityKeyUtil.getIdentityKeyPair(context, masterSecret);

    KeyExchangeMessage message = new KeyExchangeMessage(sequence, flags,
                                                        baseKey.getPublicKey(),
                                                        ephemeralKey.getPublicKey(),
                                                        identityKey.getPublicKey());

    OutgoingKeyExchangeMessage textMessage     = new OutgoingKeyExchangeMessage(recipient, Base64.encodeBytesWithoutPadding(message.serialize()));
    SessionStore               sessionStore    = new OpenchatServiceSessionStore(context, masterSecret);
    SessionRecord              sessionRecord   = sessionStore.get(recipient.getRecipientId(), RecipientDevice.DEFAULT_DEVICE_ID);

    sessionRecord.getSessionState().setPendingKeyExchange(sequence, baseKey, ephemeralKey, identityKey);
    sessionStore.put(recipient.getRecipientId(), RecipientDevice.DEFAULT_DEVICE_ID, sessionRecord);

    MessageSender.send(context, masterSecret, textMessage, -1, false);
  }

  private static boolean hasInitiatedSession(Context context, MasterSecret masterSecret,
                                             Recipient recipient)
  {
    SessionStore  sessionStore  = new OpenchatServiceSessionStore(context, masterSecret);
    SessionRecord sessionRecord = sessionStore.get(recipient.getRecipientId(), RecipientDevice.DEFAULT_DEVICE_ID);

    return sessionRecord.getSessionState().hasPendingPreKey();
  }

  private static int getRandomSequence() {
    try {
      SecureRandom random    = SecureRandom.getInstance("SHA1PRNG");
      int          candidate = Math.abs(random.nextInt());

      return candidate % 65535;
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }
}
