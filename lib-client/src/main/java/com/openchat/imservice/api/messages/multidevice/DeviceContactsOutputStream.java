package com.openchat.imservice.api.messages.multidevice;

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

    byte[] serializedContactDetails = contactDetails.build().toByteArray();

    writeVarint32(serializedContactDetails.length);
    out.write(serializedContactDetails);
  }

}
