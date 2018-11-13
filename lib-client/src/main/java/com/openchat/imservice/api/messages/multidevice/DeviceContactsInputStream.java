package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.imservice.internal.push.OpenchatServiceProtos;
import com.openchat.imservice.internal.util.Util;

import java.io.IOException;
import java.io.InputStream;

public class DeviceContactsInputStream extends ChunkedInputStream {

  public DeviceContactsInputStream(InputStream in) {
    super(in);
  }

  public DeviceContact read() throws IOException {
    long   detailsLength     = readRawVarint32();
    byte[] detailsSerialized = new byte[(int)detailsLength];
    Util.readFully(in, detailsSerialized);

    OpenchatServiceProtos.ContactDetails      details = OpenchatServiceProtos.ContactDetails.parseFrom(detailsSerialized);
    String                                  number  = details.getNumber();
    Optional<String>                        name    = Optional.fromNullable(details.getName());
    Optional<OpenchatServiceAttachmentStream> avatar  = Optional.absent();

    if (details.hasAvatar()) {
      long        avatarLength      = details.getAvatar().getLength();
      InputStream avatarStream      = new LimitedInputStream(in, avatarLength);
      String      avatarContentType = details.getAvatar().getContentType();

      avatar = Optional.of(new OpenchatServiceAttachmentStream(avatarStream, avatarContentType, avatarLength, null));
    }

    return new DeviceContact(number, name, avatar);
  }

}
