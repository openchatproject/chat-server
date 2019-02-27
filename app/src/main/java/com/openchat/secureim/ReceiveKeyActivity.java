package com.openchat.secureim;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.openchat.secureim.crypto.DecryptingQueue;
import com.openchat.secureim.crypto.KeyExchangeProcessor;
import com.openchat.secureim.crypto.KeyExchangeProcessorV2;
import com.openchat.secureim.crypto.protocol.KeyExchangeMessage;
import com.openchat.imservice.crypto.LegacyMessageException;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.service.SendReceiveService;
import com.openchat.secureim.sms.SmsTransportDetails;
import com.openchat.secureim.util.MemoryCleaner;
import com.openchat.secureim.util.Util;
import com.openchat.imservice.crypto.IdentityKey;
import com.openchat.imservice.crypto.InvalidKeyException;
import com.openchat.imservice.crypto.InvalidMessageException;
import com.openchat.imservice.crypto.InvalidVersionException;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.crypto.protocol.CiphertextMessage;
import com.openchat.imservice.crypto.protocol.PreKeyOpenchatMessage;
import com.openchat.imservice.push.IncomingPushMessage;
import com.openchat.imservice.storage.InvalidKeyIdException;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.util.Base64;
import com.openchat.imservice.util.InvalidNumberException;

import java.io.IOException;

import static com.openchat.imservice.push.PushMessageProtos.IncomingPushMessageOpenchat.Type;

public class ReceiveKeyActivity extends Activity {

  private TextView descriptionText;

  private Button confirmButton;
  private Button cancelButton;

  private Recipient recipient;
  private int       recipientDeviceId;
  private long      threadId;
  private long      messageId;

  private MasterSecret         masterSecret;
  private PreKeyOpenchatMessage keyExchangeMessageBundle;
  private KeyExchangeMessage   keyExchangeMessage;
  private IdentityKey          identityUpdateMessage;

  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);
    setContentView(R.layout.receive_key_activity);

    initializeResources();

    try {
      initializeKey();
      initializeText();
    } catch (InvalidKeyException ike) {
      Log.w("ReceiveKeyActivity", ike);
    } catch (InvalidVersionException ive) {
      Log.w("ReceiveKeyActivity", ive);
    } catch (InvalidMessageException e) {
      Log.w("ReceiveKeyActivity", e);
    } catch (LegacyMessageException e) {
      Log.w("ReceiveKeyActivity", e);
    }
    initializeListeners();
  }

  @Override
  protected void onDestroy() {
    MemoryCleaner.clean(masterSecret);
    super.onDestroy();
  }

  private void initializeText() {
    if (isTrusted(keyExchangeMessage, keyExchangeMessageBundle, identityUpdateMessage)) {
      initializeTrustedText();
    } else {
      initializeUntrustedText();
    }
  }

  private void initializeTrustedText() {
    descriptionText.setText(getString(R.string.ReceiveKeyActivity_the_signature_on_this_key_exchange_is_trusted_but));
  }

  private void initializeUntrustedText() {
    SpannableString spannableString = new SpannableString(getString(R.string.ReceiveKeyActivity_the_signature_on_this_key_exchange_is_different) + " " +
                                                          getString(R.string.ReceiveKeyActivity_you_may_wish_to_verify_this_contact));
    spannableString.setSpan(new ClickableSpan() {
      @Override
      public void onClick(View widget) {
        IdentityKey remoteIdentity;

        if      (identityUpdateMessage != null)    remoteIdentity = identityUpdateMessage;
        else if (keyExchangeMessageBundle != null) remoteIdentity = keyExchangeMessageBundle.getIdentityKey();
        else                                       remoteIdentity = keyExchangeMessage.getIdentityKey();

        Intent intent = new Intent(ReceiveKeyActivity.this, VerifyIdentityActivity.class);
        intent.putExtra("recipient", recipient);
        intent.putExtra("master_secret", masterSecret);
        intent.putExtra("remote_identity", remoteIdentity);
        startActivity(intent);
      }
    }, getString(R.string.ReceiveKeyActivity_the_signature_on_this_key_exchange_is_different).length() +1,
       spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    descriptionText.setText(spannableString);
    descriptionText.setMovementMethod(LinkMovementMethod.getInstance());
  }

  private boolean isTrusted(KeyExchangeMessage message, PreKeyOpenchatMessage messageBundle, IdentityKey identityUpdateMessage) {
    RecipientDevice recipientDevice = new RecipientDevice(recipient.getRecipientId(), recipientDeviceId);

    if (message != null) {
      KeyExchangeProcessor processor = KeyExchangeProcessor.createFor(this, masterSecret,
                                                                      recipientDevice, message);
      return processor.isTrusted(message);
    } else if (messageBundle != null) {
      KeyExchangeProcessorV2 processor = new KeyExchangeProcessorV2(this, masterSecret, recipientDevice);
      return processor.isTrusted(messageBundle);
    } else if (identityUpdateMessage != null) {
      KeyExchangeProcessorV2 processor = new KeyExchangeProcessorV2(this, masterSecret, recipientDevice);
      return processor.isTrusted(identityUpdateMessage);
    }

    return false;
  }

  private void initializeKey()
      throws InvalidKeyException, InvalidVersionException,
      InvalidMessageException, LegacyMessageException
  {
    try {
      String messageBody = getIntent().getStringExtra("body");

      if (getIntent().getBooleanExtra("is_bundle", false)) {
        boolean isPush = getIntent().getBooleanExtra("is_push", false);
        byte[] body;

        if (isPush) {
          body = Base64.decode(messageBody.getBytes());
        } else {
          body = new SmsTransportDetails().getDecodedMessage(messageBody.getBytes());
        }

        this.keyExchangeMessageBundle = new PreKeyOpenchatMessage(body);
      } else if (getIntent().getBooleanExtra("is_identity_update", false)) {
        this.identityUpdateMessage = new IdentityKey(Base64.decodeWithoutPadding(messageBody), 0);
      } else {
        this.keyExchangeMessage = KeyExchangeMessage.createFor(messageBody);
      }
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  private void initializeResources() {
    this.descriptionText      = (TextView) findViewById(R.id.description_text);
    this.confirmButton        = (Button)   findViewById(R.id.ok_button);
    this.cancelButton         = (Button)   findViewById(R.id.cancel_button);
    this.recipient            = getIntent().getParcelableExtra("recipient");
    this.recipientDeviceId    = getIntent().getIntExtra("recipient_device_id", -1);
    this.threadId             = getIntent().getLongExtra("thread_id", -1);
    this.messageId            = getIntent().getLongExtra("message_id", -1);
    this.masterSecret         = getIntent().getParcelableExtra("master_secret");
  }

  private void initializeListeners() {
    this.confirmButton.setOnClickListener(new OkListener());
    this.cancelButton.setOnClickListener(new CancelListener());
  }

  private class OkListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      new AsyncTask<Void, Void, Void> () {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
          dialog = ProgressDialog.show(ReceiveKeyActivity.this,
                                       getString(R.string.ReceiveKeyActivity_processing),
                                       getString(R.string.ReceiveKeyActivity_processing_key_exchange),
                                       true);
        }

        @Override
        protected Void doInBackground(Void... params) {
          if (keyExchangeMessage != null) {
            try {
              RecipientDevice recipientDevice = new RecipientDevice(recipient.getRecipientId(), recipientDeviceId);
              KeyExchangeProcessor processor = KeyExchangeProcessor.createFor(ReceiveKeyActivity.this, masterSecret, recipientDevice, keyExchangeMessage);
              processor.processKeyExchangeMessage(keyExchangeMessage, threadId);
              DatabaseFactory.getEncryptingSmsDatabase(ReceiveKeyActivity.this)
                             .markAsProcessedKeyExchange(messageId);
            } catch (InvalidMessageException e) {
              Log.w("ReceiveKeyActivity", e);
              DatabaseFactory.getEncryptingSmsDatabase(ReceiveKeyActivity.this)
                             .markAsCorruptKeyExchange(messageId);
            }
          } else if (keyExchangeMessageBundle != null) {
            try {
              RecipientDevice recipientDevice = new RecipientDevice(recipient.getRecipientId(), recipientDeviceId);
              KeyExchangeProcessorV2 processor = new KeyExchangeProcessorV2(ReceiveKeyActivity.this,
                                                                            masterSecret, recipientDevice);
              processor.processKeyExchangeMessage(keyExchangeMessageBundle);

              CiphertextMessage bundledMessage = keyExchangeMessageBundle.getOpenchatMessage();

              if (getIntent().getBooleanExtra("is_push", false)) {
                String source = Util.canonicalizeNumber(ReceiveKeyActivity.this, recipient.getNumber());
                IncomingPushMessage incoming = new IncomingPushMessage(Type.CIPHERTEXT_VALUE, source, recipientDeviceId, bundledMessage.serialize(), System.currentTimeMillis());

                DatabaseFactory.getEncryptingSmsDatabase(ReceiveKeyActivity.this)
                               .markAsProcessedKeyExchange(messageId);

                Intent intent = new Intent(ReceiveKeyActivity.this, SendReceiveService.class);
                intent.setAction(SendReceiveService.RECEIVE_PUSH_ACTION);
                intent.putExtra("message", incoming);
                startService(intent);
              } else {
                SmsTransportDetails transportDetails = new SmsTransportDetails();
                String              messageBody      = new String(transportDetails.getEncodedMessage(bundledMessage.serialize()));

                DatabaseFactory.getEncryptingSmsDatabase(ReceiveKeyActivity.this)
                               .updateBundleMessageBody(masterSecret, messageId, messageBody);

                DecryptingQueue.scheduleDecryption(ReceiveKeyActivity.this, masterSecret, messageId,
                                                   threadId, recipient.getNumber(), recipientDeviceId,
                                                   messageBody, true, false, false);
              }
            } catch (InvalidKeyIdException e) {
              Log.w("ReceiveKeyActivity", e);
              DatabaseFactory.getEncryptingSmsDatabase(ReceiveKeyActivity.this)
                             .markAsCorruptKeyExchange(messageId);
            } catch (InvalidKeyException e) {
              Log.w("ReceiveKeyActivity", e);
              DatabaseFactory.getEncryptingSmsDatabase(ReceiveKeyActivity.this)
                             .markAsCorruptKeyExchange(messageId);
            } catch (InvalidNumberException e) {
              Log.w("ReceiveKeyActivity", e);
              DatabaseFactory.getEncryptingSmsDatabase(ReceiveKeyActivity.this)
                             .markAsCorruptKeyExchange(messageId);
            }
          } else if (identityUpdateMessage != null) {
            DatabaseFactory.getIdentityDatabase(ReceiveKeyActivity.this)
                           .saveIdentity(masterSecret, recipient.getRecipientId(), identityUpdateMessage);

            DatabaseFactory.getSmsDatabase(ReceiveKeyActivity.this).markAsProcessedKeyExchange(messageId);
          }

          return null;
        }

        @Override
        protected void onPostExecute(Void result) {
          dialog.dismiss();
          finish();
        }
      }.execute();
    }
  }

  private class CancelListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
      ReceiveKeyActivity.this.finish();
    }
  }
}
