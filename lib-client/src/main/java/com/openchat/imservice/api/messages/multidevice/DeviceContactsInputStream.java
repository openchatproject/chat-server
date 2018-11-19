package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.logging.Log;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.imservice.internal.push.OpenchatServiceProtos;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.Verified;
import com.openchat.imservice.internal.util.Util;

import java.io.IOException;
import java.io.InputStream;

public class DeviceContactsInputStream extends ChunkedInputStream {

  private static final String TAG = DeviceContactsInputStream.class.getSimpleName();

  public DeviceContactsInputStream(InputStream in) {
    super(in);
  }

  public DeviceContact read() throws IOException {
    long   detailsLength     = readRawVarint32();
    byte[] detailsSerialized = new byte[(int)detailsLength];
    Util.readFully(in, detailsSerialized);

    OpenchatServiceProtos.ContactDetails      details  = OpenchatServiceProtos.ContactDetails.parseFrom(detailsSerialized);
    String                                  number   = details.getNumber();
    Optional<String>                        name     = Optional.fromNullable(details.getName());
    Optional<OpenchatServiceAttachmentStream> avatar   = Optional.absent();
    Optional<String>                        color    = details.hasColor() ? Optional.of(details.getColor()) : Optional.<String>absent();
    Optional<VerifiedMessage>               verified = Optional.absent();

    if (details.hasAvatar()) {
      long        avatarLength      = details.getAvatar().getLength();
      InputStream avatarStream      = new LimitedInputStream(in, avatarLength);
      String      avatarContentType = details.getAvatar().getContentType();

      avatar = Optional.of(new OpenchatServiceAttachmentStream(avatarStream, avatarContentType, avatarLength, Optional.<String>absent(), false, null));
    }

    if (details.hasVerified()) {
      try {
        String      destination = details.getVerified().getDestination();
        IdentityKey identityKey = new IdentityKey(details.getVerified().getIdentityKey().toByteArray(), 0);

        VerifiedMessage.VerifiedState state;

        switch (details.getVerified().getState()) {
          case VERIFIED:  state = VerifiedMessage.VerifiedState.VERIFIED;   break;
          case UNVERIFIED:state = VerifiedMessage.VerifiedState.UNVERIFIED; break;
          case DEFAULT:   state = VerifiedMessage.VerifiedState.DEFAULT;    break;
          default:        throw new InvalidMessageException("Unknown state: " + details.getVerified().getState());
        }

        verified = Optional.of(new VerifiedMessage(destination, identityKey, state, System.currentTimeMillis()));
      } catch (InvalidKeyException | InvalidMessageException e) {
        Log.w(TAG, e);
        verified = Optional.absent();
      }
    }

    return new DeviceContact(number, name, avatar, color, verified);
  }

}
