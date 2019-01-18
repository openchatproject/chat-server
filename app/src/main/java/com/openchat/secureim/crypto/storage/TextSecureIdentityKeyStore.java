package com.openchat.secureim.crypto.storage;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.crypto.SessionUtil;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.IdentityDatabase;
import com.openchat.secureim.database.IdentityDatabase.IdentityRecord;
import com.openchat.secureim.database.IdentityDatabase.VerifiedStatus;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.util.IdentityUtil;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.libim.IdentityKey;
import com.openchat.libim.IdentityKeyPair;
import com.openchat.libim.openchatProtocolAddress;
import com.openchat.libim.state.IdentityKeyStore;
import com.openchat.libim.util.guava.Optional;

import java.util.concurrent.TimeUnit;

public class TextSecureIdentityKeyStore implements IdentityKeyStore {

  private static final int TIMESTAMP_THRESHOLD_SECONDS = 5;

  private static final String TAG = TextSecureIdentityKeyStore.class.getSimpleName();
  private static final Object LOCK = new Object();

  private final Context context;

  public TextSecureIdentityKeyStore(Context context) {
    this.context = context;
  }

  @Override
  public IdentityKeyPair getIdentityKeyPair() {
    return IdentityKeyUtil.getIdentityKeyPair(context);
  }

  @Override
  public int getLocalRegistrationId() {
    return TextSecurePreferences.getLocalRegistrationId(context);
  }

  public boolean saveIdentity(openchatProtocolAddress address, IdentityKey identityKey, boolean nonBlockingApproval) {
    synchronized (LOCK) {
      IdentityDatabase         identityDatabase = DatabaseFactory.getIdentityDatabase(context);
      Address                  openchatAddress    = Address.fromExternal(context, address.getName());
      Optional<IdentityRecord> identityRecord   = identityDatabase.getIdentity(openchatAddress);

      if (!identityRecord.isPresent()) {
        Log.w(TAG, "Saving new identity...");
        identityDatabase.saveIdentity(openchatAddress, identityKey, VerifiedStatus.DEFAULT, true, System.currentTimeMillis(), nonBlockingApproval);
        return false;
      }

      if (!identityRecord.get().getIdentityKey().equals(identityKey)) {
        Log.w(TAG, "Replacing existing identity...");
        VerifiedStatus verifiedStatus;

        if (identityRecord.get().getVerifiedStatus() == VerifiedStatus.VERIFIED ||
            identityRecord.get().getVerifiedStatus() == VerifiedStatus.UNVERIFIED)
        {
          verifiedStatus = VerifiedStatus.UNVERIFIED;
        } else {
          verifiedStatus = VerifiedStatus.DEFAULT;
        }

        identityDatabase.saveIdentity(openchatAddress, identityKey, verifiedStatus, false, System.currentTimeMillis(), nonBlockingApproval);
        IdentityUtil.markIdentityUpdate(context, Recipient.from(context, openchatAddress, true));
        SessionUtil.archiveSiblingSessions(context, address);
        return true;
      }

      if (isNonBlockingApprovalRequired(identityRecord.get())) {
        Log.w(TAG, "Setting approval status...");
        identityDatabase.setApproval(openchatAddress, nonBlockingApproval);
        return false;
      }

      return false;
    }
  }

  @Override
  public boolean saveIdentity(openchatProtocolAddress address, IdentityKey identityKey) {
    return saveIdentity(address, identityKey, false);
  }

  @Override
  public boolean isTrustedIdentity(openchatProtocolAddress address, IdentityKey identityKey, Direction direction) {
    synchronized (LOCK) {
      IdentityDatabase identityDatabase = DatabaseFactory.getIdentityDatabase(context);
      String           ourNumber        = TextSecurePreferences.getLocalNumber(context);
      Address          theirAddress     = Address.fromExternal(context, address.getName());

      if (ourNumber.equals(address.getName()) || Address.fromSerialized(ourNumber).equals(theirAddress)) {
        return identityKey.equals(IdentityKeyUtil.getIdentityKey(context));
      }

      switch (direction) {
        case SENDING:   return isTrustedForSending(identityKey, identityDatabase.getIdentity(theirAddress));
        case RECEIVING: return true;
        default:        throw new AssertionError("Unknown direction: " + direction);
      }
    }
  }

  private boolean isTrustedForSending(IdentityKey identityKey, Optional<IdentityRecord> identityRecord) {
    if (!identityRecord.isPresent()) {
      Log.w(TAG, "Nothing here, returning true...");
      return true;
    }

    if (!identityKey.equals(identityRecord.get().getIdentityKey())) {
      Log.w(TAG, "Identity keys don't match...");
      return false;
    }

    if (identityRecord.get().getVerifiedStatus() == VerifiedStatus.UNVERIFIED) {
      Log.w(TAG, "Needs unverified approval!");
      return false;
    }

    if (isNonBlockingApprovalRequired(identityRecord.get())) {
      Log.w(TAG, "Needs non-blocking approval!");
      return false;
    }

    return true;
  }

  private boolean isNonBlockingApprovalRequired(IdentityRecord identityRecord) {
    return !identityRecord.isFirstUse() &&
           System.currentTimeMillis() - identityRecord.getTimestamp() < TimeUnit.SECONDS.toMillis(TIMESTAMP_THRESHOLD_SECONDS) &&
           !identityRecord.isApprovedNonBlocking();
  }
}
