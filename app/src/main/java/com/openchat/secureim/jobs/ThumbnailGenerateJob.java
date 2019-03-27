package com.openchat.secureim.jobs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.R;
import com.openchat.secureim.crypto.AsymmetricMasterCipher;
import com.openchat.secureim.crypto.AsymmetricMasterSecret;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.crypto.SecurityEvent;
import com.openchat.secureim.crypto.SmsCipher;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.database.PartDatabase;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.sms.IncomingEncryptedMessage;
import com.openchat.secureim.sms.IncomingEndSessionMessage;
import com.openchat.secureim.sms.IncomingKeyExchangeMessage;
import com.openchat.secureim.sms.IncomingPreKeyBundleMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.sms.MessageSender;
import com.openchat.secureim.sms.OutgoingKeyExchangeMessage;
import com.openchat.secureim.util.BitmapDecodingException;
import com.openchat.secureim.util.BitmapUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.protocal.DuplicateMessageException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.NoSessionException;
import com.openchat.protocal.StaleKeyExchangeException;
import com.openchat.protocal.UntrustedIdentityException;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import ws.com.google.android.mms.ContentType;
import ws.com.google.android.mms.MmsException;
import ws.com.google.android.mms.pdu.PduPart;

public class ThumbnailGenerateJob extends MasterSecretJob {

  private static final String TAG = ThumbnailGenerateJob.class.getSimpleName();

  private final long partId;

  public ThumbnailGenerateJob(Context context, long partId) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new MasterSecretRequirement(context))
                                .create());

    this.partId = partId;
  }

  @Override
  public void onAdded() { }

  @Override
  public void onRun(MasterSecret masterSecret) throws MmsException {
    PartDatabase database = DatabaseFactory.getPartDatabase(context);
    PduPart part = database.getPart(partId);

    if (part.getThumbnailUri() != null) {
      return;
    }

    long startMillis = System.currentTimeMillis();
    Bitmap thumbnail = generateThumbnailForPart(masterSecret, part);

    if (thumbnail != null) {
      ByteArrayOutputStream thumbnailBytes = new ByteArrayOutputStream();
      thumbnail.compress(CompressFormat.JPEG, 85, thumbnailBytes);

      float aspectRatio = (float)thumbnail.getWidth() / (float)thumbnail.getHeight();
      Log.w(TAG, String.format("generated thumbnail for part #%d, %dx%d (%.3f:1) in %dms",
                               partId,
                               thumbnail.getWidth(),
                               thumbnail.getHeight(),
                               aspectRatio, System.currentTimeMillis() - startMillis));
      database.updatePartThumbnail(masterSecret, partId, part, new ByteArrayInputStream(thumbnailBytes.toByteArray()), aspectRatio);
    } else {
      Log.w(TAG, "thumbnail not generated");
    }
  }

  private Bitmap generateThumbnailForPart(MasterSecret masterSecret, PduPart part) {
    String contentType = new String(part.getContentType());

    if      (ContentType.isImageType(contentType)) return generateImageThumbnail(masterSecret, part);
    else                                           return null;
  }

  private Bitmap generateImageThumbnail(MasterSecret masterSecret, PduPart part) {
    try {
      int maxSize = context.getResources().getDimensionPixelSize(R.dimen.thumbnail_max_size);
      return BitmapUtil.createScaledBitmap(context, masterSecret, part.getDataUri(), maxSize, maxSize);
    } catch (FileNotFoundException | BitmapDecodingException e) {
      Log.w(TAG, e);
      return null;
    }
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    return false;
  }

  @Override
  public void onCanceled() { }
}
