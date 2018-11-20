package com.openchat.imservice.api.messages.multidevice;

import com.google.protobuf.ByteString;

import com.openchat.imservice.internal.push.OpenchatServiceProtos;

import java.io.IOException;
import java.io.OutputStream;

public class DeviceContactsOutputStream extends ChunkedOutputStream {

  public DeviceContactsOutputStream(OutputStream out) {
    super(out);
  }

  public void write(DeviceContact contact) throws IOException {
    writeContactDetails(contact);
    writeAvatarImage(contact);
  }

  public void close() throws IOException {
    out.close();
  }

  private void writeAvatarImage(DeviceContact contact) throws IOException {
    if (contact.getAvatar().isPresent()) {
      writeStream(contact.getAvatar().get().getInputStream());
    }
  }

  private void writeContactDetails(DeviceContact contact) throws IOException {
    OpenchatServiceProtos.ContactDetails.Builder contactDetails = OpenchatServiceProtos.ContactDetails.newBuilder();
    contactDetails.setNumber(contact.getNumber());

    if (contact.getName().isPresent()) {
      contactDetails.setName(contact.getName().get());
    }

    if (contact.getAvatar().isPresent()) {
      OpenchatServiceProtos.ContactDetails.Avatar.Builder avatarBuilder = OpenchatServiceProtos.ContactDetails.Avatar.newBuilder();
      avatarBuilder.setContentType(contact.getAvatar().get().getContentType());
      avatarBuilder.setLength((int)contact.getAvatar().get().getLength());
      contactDetails.setAvatar(avatarBuilder);
    }

    if (contact.getColor().isPresent()) {
      contactDetails.setColor(contact.getColor().get());
    }

    if (contact.getVerified().isPresent()) {
      OpenchatServiceProtos.Verified.State state;

      switch (contact.getVerified().get().getVerified()) {
        case VERIFIED:   state = OpenchatServiceProtos.Verified.State.VERIFIED;   break;
        case UNVERIFIED: state = OpenchatServiceProtos.Verified.State.UNVERIFIED; break;
        default:         state = OpenchatServiceProtos.Verified.State.DEFAULT;    break;
      }

      contactDetails.setVerified(OpenchatServiceProtos.Verified.newBuilder()
                                                             .setDestination(contact.getVerified().get().getDestination())
                                                             .setIdentityKey(ByteString.copyFrom(contact.getVerified().get().getIdentityKey().serialize()))
                                                             .setState(state));
    }

    if (contact.getProfileKey().isPresent()) {
      contactDetails.setProfileKey(ByteString.copyFrom(contact.getProfileKey().get()));
    }

    byte[] serializedContactDetails = contactDetails.build().toByteArray();

    writeVarint32(serializedContactDetails.length);
    out.write(serializedContactDetails);
  }

}
