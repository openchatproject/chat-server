package com.openchat.secureim.mms;

import com.openchat.secureim.transport.UndeliverableMessageException;

import ws.com.google.android.mms.pdu.SendConf;

public interface OutgoingMmsConnection {
  SendConf send(byte[] pduBytes) throws UndeliverableMessageException;
}
