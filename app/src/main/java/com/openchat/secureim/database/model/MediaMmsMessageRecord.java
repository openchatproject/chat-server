package com.openchat.secureim.database.model;

import android.content.Context;
import android.text.SpannableString;
import android.util.Log;

import com.openchat.secureim.R;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.mms.MediaNotFoundException;
import com.openchat.secureim.mms.Slide;
import com.openchat.secureim.mms.SlideDeck;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.imservice.push.exceptions.NotFoundException;
import com.openchat.imservice.util.FutureTaskListener;
import com.openchat.imservice.util.ListenableFutureTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaMmsMessageRecord extends MessageRecord {
  private final static String TAG = MediaMmsMessageRecord.class.getSimpleName();

  private final Context context;
  private final int partCount;
  private final ListenableFutureTask<SlideDeck> slideDeckFutureTask;

  public MediaMmsMessageRecord(Context context, long id, Recipients recipients,
                               Recipient individualRecipient, int recipientDeviceId,
                               long dateSent, long dateReceived, int deliveredCount,
                               long threadId, Body body,
                               ListenableFutureTask<SlideDeck> slideDeck,
                               int partCount, long mailbox)
  {
    super(context, id, body, recipients, individualRecipient, recipientDeviceId,
          dateSent, dateReceived, threadId, deliveredCount, DELIVERY_STATUS_NONE, mailbox);

    this.context             = context.getApplicationContext();
    this.partCount           = partCount;
    this.slideDeckFutureTask = slideDeck;
  }

  public ListenableFutureTask<SlideDeck> getSlideDeckFuture() {
    return slideDeckFutureTask;
  }

  private SlideDeck getSlideDeckSync() {
    try {
      return slideDeckFutureTask.get();
    } catch (InterruptedException e) {
      Log.w(TAG, e);
      return null;
    } catch (ExecutionException e) {
      Log.w(TAG, e);
      return null;
    }
  }

  public boolean containsMediaSlide() {
    SlideDeck deck = getSlideDeckSync();
    return deck != null && deck.containsMediaSlide();
  }

  public void fetchMediaSlide(final FutureTaskListener<Slide> listener) {
    slideDeckFutureTask.addListener(new FutureTaskListener<SlideDeck>() {
      @Override
      public void onSuccess(SlideDeck deck) {
        for (Slide slide : deck.getSlides()) {
          if (slide.hasImage() || slide.hasVideo() || slide.hasAudio()) {
            listener.onSuccess(slide);
            return;
          }
        }
        listener.onFailure(new MediaNotFoundException("no media slide found"));
      }

      @Override
      public void onFailure(Throwable error) {
        listener.onFailure(error);
      }
    });
  }

  public int getPartCount() {
    return partCount;
  }

  @Override
  public boolean isMms() {
    return true;
  }

  @Override
  public boolean isMmsNotification() {
    return false;
  }

  @Override
  public SpannableString getDisplayBody() {
    if (MmsDatabase.Types.isDecryptInProgressType(type)) {
      return emphasisAdded(context.getString(R.string.MmsMessageRecord_decrypting_mms_please_wait));
    } else if (MmsDatabase.Types.isFailedDecryptType(type)) {
      return emphasisAdded(context.getString(R.string.MmsMessageRecord_bad_encrypted_mms_message));
    } else if (MmsDatabase.Types.isDuplicateMessageType(type)) {
      return emphasisAdded(context.getString(R.string.SmsMessageRecord_duplicate_message));
    } else if (MmsDatabase.Types.isNoRemoteSessionType(type)) {
      return emphasisAdded(context.getString(R.string.MmsMessageRecord_mms_message_encrypted_for_non_existing_session));
    } else if (isLegacyMessage()) {
      return emphasisAdded(context.getString(R.string.MessageRecord_message_encrypted_with_a_legacy_protocol_version_that_is_no_longer_supported));
    } else if (!getBody().isPlaintext()) {
      return emphasisAdded(context.getString(R.string.MessageNotifier_encrypted_message));
    }

    return super.getDisplayBody();
  }
}
