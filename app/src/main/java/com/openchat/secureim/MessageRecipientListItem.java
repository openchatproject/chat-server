package com.openchat.secureim;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openchat.secureim.components.AvatarImageView;
import com.openchat.secureim.components.FromTextView;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.documents.IdentityKeyMismatch;
import com.openchat.secureim.database.documents.NetworkFailure;
import com.openchat.secureim.database.model.MessageRecord;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.sms.MessageSender;

public class MessageRecipientListItem extends RelativeLayout
    implements Recipient.RecipientModifiedListener
{
  private final static String TAG = MessageRecipientListItem.class.getSimpleName();

  private Recipient       recipient;
  private FromTextView    fromView;
  private TextView        errorDescription;
  private Button          conflictButton;
  private Button          resendButton;
  private AvatarImageView contactPhotoImage;

  private final Handler handler = new Handler();

  public MessageRecipientListItem(Context context) {
    super(context);
  }

  public MessageRecipientListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    this.fromView          = (FromTextView)    findViewById(R.id.from);
    this.errorDescription  = (TextView)        findViewById(R.id.error_description);
    this.contactPhotoImage = (AvatarImageView) findViewById(R.id.contact_photo_image);
    this.conflictButton    = (Button)          findViewById(R.id.conflict_button);
    this.resendButton      = (Button)          findViewById(R.id.resend_button);
  }

  public void set(final MasterSecret masterSecret,
                  final MessageRecord record,
                  final Recipient recipient,
                  final boolean isPushGroup)
  {
    this.recipient = recipient;

    recipient.addListener(this);
    fromView.setText(recipient);
    contactPhotoImage.setAvatar(recipient, false);
    setIssueIndicators(masterSecret, record, isPushGroup);
  }

  private void setIssueIndicators(final MasterSecret masterSecret,
                                  final MessageRecord record,
                                  final boolean isPushGroup)
  {
    final NetworkFailure      networkFailure = getNetworkFailure(record);
    final IdentityKeyMismatch keyMismatch    = networkFailure == null ? getKeyMismatch(record) : null;

    String errorText = "";

    if (keyMismatch != null) {
      resendButton.setVisibility(View.GONE);
      conflictButton.setVisibility(View.VISIBLE);

      errorText = getContext().getString(R.string.MessageDetailsRecipient_new_identity);
      conflictButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          new ConfirmIdentityDialog(getContext(), masterSecret, record, keyMismatch).show();
        }
      });
    } else if (networkFailure != null || (!isPushGroup && record.isFailed())) {
      resendButton.setVisibility(View.VISIBLE);
      resendButton.setEnabled(true);
      conflictButton.setVisibility(View.GONE);

      errorText = getContext().getString(R.string.MessageDetailsRecipient_failed_to_send);
      resendButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          resendButton.setEnabled(false);
          new ResendAsyncTask(masterSecret, record, networkFailure).execute();
        }
      });
    } else {
      resendButton.setVisibility(View.GONE);
      conflictButton.setVisibility(View.GONE);
    }

    errorDescription.setText(errorText);
    errorDescription.setVisibility(TextUtils.isEmpty(errorText) ? View.GONE : View.VISIBLE);
  }

  private NetworkFailure getNetworkFailure(final MessageRecord record) {
    if (record.hasNetworkFailures()) {
      for (final NetworkFailure failure : record.getNetworkFailures()) {
        if (failure.getRecipientId() == recipient.getRecipientId()) {
          return failure;
        }
      }
    }
    return null;
  }

  private IdentityKeyMismatch getKeyMismatch(final MessageRecord record) {
    if (record.isIdentityMismatchFailure()) {
      for (final IdentityKeyMismatch mismatch : record.getIdentityKeyMismatches()) {
        if (mismatch.getRecipientId() == recipient.getRecipientId()) {
          return mismatch;
        }
      }
    }
    return null;
  }

  public void unbind() {
    if (this.recipient != null) this.recipient.removeListener(this);
  }

  @Override
  public void onModified(final Recipient recipient) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        fromView.setText(recipient);
        contactPhotoImage.setAvatar(recipient, false);
      }
    });
  }

  private class ResendAsyncTask extends AsyncTask<Void,Void,Void> {
    private final MasterSecret   masterSecret;
    private final MessageRecord  record;
    private final NetworkFailure failure;

    public ResendAsyncTask(MasterSecret masterSecret, MessageRecord record, NetworkFailure failure) {
      this.masterSecret = masterSecret;
      this.record       = record;
      this.failure      = failure;
    }

    @Override
    protected Void doInBackground(Void... params) {
      MmsDatabase mmsDatabase = DatabaseFactory.getMmsDatabase(getContext());
      mmsDatabase.removeFailure(record.getId(), failure);

      if (record.getRecipients().isGroupRecipient()) {
        MessageSender.resendGroupMessage(getContext(), masterSecret, record, failure.getRecipientId());
      } else {
        MessageSender.resend(getContext(), masterSecret, record);
      }
      return null;
    }
  }

}
