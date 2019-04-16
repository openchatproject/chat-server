package com.openchat.secureim.mms;

import android.support.annotation.NonNull;

import com.openchat.secureim.transport.UndeliverableMessageException;

import ws.com.google.android.mms.pdu.SendConf;

public interface OutgoingMmsConnection {
  SendConf send(@NonNull byte[] pduBytes) throws UndeliverableMessageException;
}
