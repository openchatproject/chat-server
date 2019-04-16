package com.openchat.secureim.mms;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

import com.openchat.secureim.providers.MmsBodyProvider;
import com.openchat.secureim.transport.UndeliverableMessageException;
import com.openchat.secureim.util.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import ws.com.google.android.mms.pdu.PduParser;
import ws.com.google.android.mms.pdu.SendConf;

public class OutgoingLollipopMmsConnection extends LollipopMmsConnection implements OutgoingMmsConnection {
  private static final String TAG    = OutgoingLollipopMmsConnection.class.getSimpleName();
  private static final String ACTION = OutgoingLollipopMmsConnection.class.getCanonicalName() + "MMS_SENT_ACTION";

  private byte[] response;

  public OutgoingLollipopMmsConnection(Context context) {
    super(context, ACTION);
  }

  @TargetApi(VERSION_CODES.LOLLIPOP_MR1)
  @Override
  public synchronized void onResult(Context context, Intent intent) {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP_MR1) {
      Log.w(TAG, "HTTP status: " + intent.getIntExtra(SmsManager.EXTRA_MMS_HTTP_STATUS, -1));
    }

    response = intent.getByteArrayExtra(SmsManager.EXTRA_MMS_DATA);
  }

  @Override
  @TargetApi(VERSION_CODES.LOLLIPOP)
  public @Nullable synchronized SendConf send(@NonNull byte[] pduBytes) throws UndeliverableMessageException {
    beginTransaction();
    try {
      MmsBodyProvider.Pointer pointer = MmsBodyProvider.makeTemporaryPointer(getContext());
      Util.copy(new ByteArrayInputStream(pduBytes), pointer.getOutputStream());

      SmsManager.getDefault().sendMultimediaMessage(getContext(),
                                                    pointer.getUri(),
                                                    null,
                                                    null,
                                                    getPendingIntent());

      waitForResult();

      Log.w(TAG, "MMS broadcast received and processed.");
      pointer.close();

      if (response == null) {
        throw new UndeliverableMessageException("Null response.");
      }

      return (SendConf) new PduParser(response).parse();
    } catch (IOException | TimeoutException e) {
      throw new UndeliverableMessageException(e);
    } finally {
      endTransaction();
    }
  }
}

