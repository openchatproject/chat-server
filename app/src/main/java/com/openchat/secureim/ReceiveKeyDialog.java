package com.openchat.secureim;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import com.openchat.secureim.crypto.IdentityKeyParcelable;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.IdentityDatabase;
import com.openchat.secureim.database.PushDatabase;
import com.openchat.secureim.database.model.MessageRecord;
import com.openchat.secureim.jobs.PushDecryptJob;
import com.openchat.secureim.sms.IncomingPreKeyBundleMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.util.Base64;
import com.openchat.secureim.util.ProgressDialogAsyncTask;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.protocol.PreKeyOpenchatMessage;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;

import java.io.IOException;

public class ReceiveKeyDialog extends MaterialDialog {
  private static final String TAG = ReceiveKeyDialog.class.getSimpleName();

  private ReceiveKeyDialog(Builder builder, MessageRecord messageRecord, IdentityKey identityKey) {
    super(builder);
    initializeText(messageRecord, identityKey);
  }

  public static @NonNull ReceiveKeyDialog build(@NonNull Context context,
                                                @NonNull MasterSecret masterSecret,
                                                @NonNull MessageRecord messageRecord)
  {
    try {
      final IncomingPreKeyBundleMessage message = getMessage(messageRecord);
      final IdentityKey identityKey = getIdentityKey(message);
      Builder builder = new Builder(context).customView(R.layout.receive_key_dialog, true)
                                            .positiveText(R.string.receive_key_dialog__complete)
                                            .negativeText(android.R.string.cancel)
                                            .callback(new ReceiveKeyDialogCallback(context,
                                                                                   masterSecret,
                                                                                   messageRecord,
                                                                                   message,
                                                                                   identityKey));
      return new ReceiveKeyDialog(builder, messageRecord, identityKey);
    } catch (InvalidKeyException | InvalidVersionException | InvalidMessageException | LegacyMessageException e) {
      throw new AssertionError(e);
    }
  }

  private void initializeText(final MessageRecord messageRecord, final IdentityKey identityKey) {
    if (getCustomView() == null) {
      throw new AssertionError("CustomView should not be null in ReceiveKeyDialog.");
    }
    TextView        descriptionText = (TextView) getCustomView().findViewById(R.id.description_text);
    String          introText       = getContext().getString(R.string.ReceiveKeyActivity_the_signature_on_this_key_exchange_is_different);
    SpannableString spannableString = new SpannableString(introText + " " +
                                                          getContext().getString(R.string.ReceiveKeyActivity_you_may_wish_to_verify_this_contact));
    spannableString.setSpan(new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        Intent intent = new Intent(getContext(), VerifyIdentityActivity.class);
        intent.putExtra("recipient", messageRecord.getIndividualRecipient().getRecipientId());
        intent.putExtra("remote_identity", new IdentityKeyParcelable(identityKey));
        getContext().startActivity(intent);
      }
    }, introText.length() + 1,
       spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    descriptionText.setText(spannableString);
    descriptionText.setMovementMethod(LinkMovementMethod.getInstance());
  }

  private static IncomingPreKeyBundleMessage getMessage(MessageRecord messageRecord)
      throws InvalidKeyException, InvalidVersionException,
             InvalidMessageException, LegacyMessageException
  {
    IncomingTextMessage message = new IncomingTextMessage(messageRecord.getIndividualRecipient().getNumber(),
                                                          messageRecord.getRecipientDeviceId(),
                                                          System.currentTimeMillis(),
                                                          messageRecord.getBody().getBody(),
                                                          Optional.<OpenchatServiceGroup>absent());

    return new IncomingPreKeyBundleMessage(message, message.getMessageBody());
  }

  private static IdentityKey getIdentityKey(IncomingPreKeyBundleMessage message)
      throws InvalidKeyException, InvalidVersionException,
             InvalidMessageException, LegacyMessageException
  {
    try {
      return new PreKeyOpenchatMessage(Base64.decode(message.getMessageBody())).getIdentityKey();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  private static class ReceiveKeyDialogCallback extends ButtonCallback {
    private Context                     context;
    private MasterSecret                masterSecret;
    private MessageRecord               messageRecord;
    private IncomingPreKeyBundleMessage message;
    private IdentityKey                 identityKey;

    public ReceiveKeyDialogCallback(Context context,
                                    MasterSecret masterSecret,
                                    MessageRecord messageRecord,
                                    IncomingPreKeyBundleMessage message,
                                    IdentityKey identityKey)
    {
      this.context       = context;
      this.masterSecret  = masterSecret;
      this.messageRecord = messageRecord;
      this.message       = message;
      this.identityKey   = identityKey;
    }

    @Override public void onPositive(MaterialDialog dialog) {
      new VerifyAsyncTask(context, masterSecret, messageRecord, message, identityKey).execute();
    }
  }

  private static class VerifyAsyncTask extends ProgressDialogAsyncTask<Void,Void,Void> {

    private MasterSecret                masterSecret;
    private MessageRecord               messageRecord;
    private IncomingPreKeyBundleMessage message;
    private IdentityKey                 identityKey;

    public VerifyAsyncTask(Context context,
                           MasterSecret masterSecret,
                           MessageRecord messageRecord,
                           IncomingPreKeyBundleMessage message,
                           IdentityKey identityKey)
    {
      super(context, R.string.ReceiveKeyActivity_processing, R.string.ReceiveKeyActivity_processing_key_exchange);
      this.masterSecret  = masterSecret;
      this.messageRecord = messageRecord;
      this.message       = message;
      this.identityKey   = identityKey;
    }

    @Override
    protected Void doInBackground(Void... params) {
      if (getContext() == null) return null;

      IdentityDatabase identityDatabase = DatabaseFactory.getIdentityDatabase(getContext());
      PushDatabase pushDatabase = DatabaseFactory.getPushDatabase(getContext());

      identityDatabase.saveIdentity(masterSecret,
                                    messageRecord.getIndividualRecipient().getRecipientId(),
                                    identityKey);
      try {
        byte[] body = Base64.decode(message.getMessageBody());
        OpenchatServiceEnvelope envelope = new OpenchatServiceEnvelope(3, message.getSender(),
                                                             message.getSenderDeviceId(), "",
                                                             message.getSentTimestampMillis(),
                                                             body);

        long pushId = pushDatabase.insert(envelope);

        ApplicationContext.getInstance(getContext())
                          .getJobManager()
                          .add(new PushDecryptJob(getContext(), pushId, messageRecord.getId(), message.getSender()));
      } catch (IOException e) {
        throw new AssertionError(e);
      }

      return null;
    }
  }
}
