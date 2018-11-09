package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.imservice.internal.push.OpenchatServiceProtos;
import com.openchat.imservice.internal.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DeviceGroupsInputStream extends ChunkedInputStream{

  public DeviceGroupsInputStream(InputStream in) {
    super(in);
  }

  public DeviceGroup read() throws IOException {
    long   detailsLength     = readRawVarint32();
    byte[] detailsSerialized = new byte[(int)detailsLength];
    Util.readFully(in, detailsSerialized);

    OpenchatServiceProtos.GroupDetails details = OpenchatServiceProtos.GroupDetails.parseFrom(detailsSerialized);

    if (!details.hasId()) {
      throw new IOException("ID missing on group record!");
    }

    byte[]                               id      = details.getId().toByteArray();
    Optional<String>                     name    = Optional.fromNullable(details.getName());
    List<String>                         members = details.getMembersList();
    Optional<OpenchatServiceAttachmentStream> avatar  = Optional.absent();

    if (details.hasAvatar()) {
      long        avatarLength      = details.getAvatar().getLength();
      InputStream avatarStream      = new ChunkedInputStream.LimitedInputStream(in, avatarLength);
      String      avatarContentType = details.getAvatar().getContentType();

      avatar = Optional.of(new OpenchatServiceAttachmentStream(avatarStream, avatarContentType, avatarLength));
    }

    return new DeviceGroup(id, name, members, avatar);
  }

}
