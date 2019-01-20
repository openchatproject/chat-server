package com.openchat.secureim.crypto;

import android.content.Context;
import android.support.annotation.NonNull;

import com.openchat.secureim.crypto.storage.TextSecureSessionStore;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.libim.openchatProtocolAddress;
import com.openchat.libim.state.SessionRecord;
import com.openchat.libim.state.SessionStore;
import com.openchat.imservice.api.push.openchatServiceAddress;

import java.util.List;

public class SessionUtil {

  public static boolean hasSession(Context context, MasterSecret masterSecret, Recipient recipient) {
    return hasSession(context, masterSecret, recipient.getAddress());
  }

  public static boolean hasSession(Context context, MasterSecret masterSecret, @NonNull Address address) {
    SessionStore          sessionStore   = new TextSecureSessionStore(context, masterSecret);
    openchatProtocolAddress axolotlAddress = new openchatProtocolAddress(address.serialize(), openchatServiceAddress.DEFAULT_DEVICE_ID);

    return sessionStore.containsSession(axolotlAddress);
  }

  public static void archiveSiblingSessions(Context context, openchatProtocolAddress address) {
    SessionStore  sessionStore = new TextSecureSessionStore(context);
    List<Integer> devices      = sessionStore.getSubDeviceSessions(address.getName());
    devices.add(1);

    for (int device : devices) {
      if (device != address.getDeviceId()) {
        openchatProtocolAddress sibling = new openchatProtocolAddress(address.getName(), device);

        if (sessionStore.containsSession(sibling)) {
          SessionRecord sessionRecord = sessionStore.loadSession(sibling);
          sessionRecord.archiveCurrentState();
          sessionStore.storeSession(sibling, sessionRecord);
        }
      }
    }
  }

  public static void archiveAllSessions(Context context) {
    new TextSecureSessionStore(context).archiveAllSessions();
  }
}
