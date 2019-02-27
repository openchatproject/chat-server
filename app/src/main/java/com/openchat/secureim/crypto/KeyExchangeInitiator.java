package com.openchat.secureim.crypto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.openchat.secureim.R;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingKeyExchangeMessage;
import com.openchat.secureim.util.Dialogs;
import com.openchat.protocal.SessionBuilder;
import com.openchat.protocal.protocol.KeyExchangeMessage;
import com.openchat.protocal.state.IdentityKeyStore;
import com.openchat.protocal.state.PreKeyStore;
import com.openchat.protocal.state.SessionRecord;
import com.openchat.protocal.state.SessionStore;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.storage.OpenchatServicePreKeyStore;
import com.openchat.imservice.storage.OpenchatServiceSessionStore;
import com.openchat.imservice.util.Base64;

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
    SessionStore     sessionStore     = new OpenchatServiceSessionStore(context, masterSecret);
    PreKeyStore      preKeyStore      = new OpenchatServicePreKeyStore(context, masterSecret);
    IdentityKeyStore identityKeyStore = new OpenchatServiceIdentityKeyStore(context, masterSecret);

    SessionBuilder   sessionBuilder   = new SessionBuilder(sessionStore, preKeyStore, identityKeyStore,
                                                           recipient.getRecipientId(),
                                                           RecipientDevice.DEFAULT_DEVICE_ID);

    KeyExchangeMessage         keyExchangeMessage = sessionBuilder.process();
    String                     serializedMessage  = Base64.encodeBytesWithoutPadding(keyExchangeMessage.serialize());
    OutgoingKeyExchangeMessage textMessage        = new OutgoingKeyExchangeMessage(recipient, serializedMessage);

    MessageSender.send(context, masterSecret, textMessage, -1, false);
  }

  private static boolean hasInitiatedSession(Context context, MasterSecret masterSecret,
                                             Recipient recipient)
  {
    SessionStore  sessionStore  = new OpenchatServiceSessionStore(context, masterSecret);
    SessionRecord sessionRecord = sessionStore.get(recipient.getRecipientId(), RecipientDevice.DEFAULT_DEVICE_ID);

    return sessionRecord.getSessionState().hasPendingPreKey();
  }
}
