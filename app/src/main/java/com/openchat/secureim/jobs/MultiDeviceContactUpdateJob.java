package com.openchat.secureim.jobs;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import com.openchat.secureim.contacts.ContactAccessor;
import com.openchat.secureim.contacts.ContactAccessor.ContactData;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.dependencies.OpenchatServiceCommunicationModule.OpenchatServiceMessageSenderFactory;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.util.Util;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.OpenchatServiceMessageSender;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;
import com.openchat.imservice.api.messages.multidevice.DeviceContact;
import com.openchat.imservice.api.messages.multidevice.DeviceContactsOutputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.inject.Inject;

public class MultiDeviceContactUpdateJob extends MasterSecretJob implements InjectableType {

  private static final String TAG = MultiDeviceContactUpdateJob.class.getSimpleName();

  @Inject transient OpenchatServiceMessageSenderFactory messageSenderFactory;

  public MultiDeviceContactUpdateJob(Context context) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withRequirement(new MasterSecretRequirement(context))
                                .withGroupId(MultiDeviceContactUpdateJob.class.getSimpleName())
                                .withPersistence()
                                .create());
  }

  @Override
  public void onRun(MasterSecret masterSecret)
      throws IOException, UntrustedIdentityException, NetworkException
  {
    OpenchatServiceMessageSender messageSender   = messageSenderFactory.create(masterSecret);
    File                    contactDataFile = createTempFile("multidevice-contact-update");

    try {
      DeviceContactsOutputStream out      = new DeviceContactsOutputStream(new FileOutputStream(contactDataFile));
      Collection<ContactData>    contacts = ContactAccessor.getInstance().getContactsWithPush(context);

      for (ContactData contactData : contacts) {
        Uri              contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactData.id));
        String           number     = contactData.numbers.get(0).number;
        Optional<String> name       = Optional.fromNullable(contactData.name);

        out.write(new DeviceContact(number, name, getAvatar(contactUri)));
      }

      out.close();
      sendUpdate(messageSender, contactDataFile);

    } finally {
      if (contactDataFile != null) contactDataFile.delete();
    }
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    if (exception instanceof NetworkException) return true;
    return false;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onCanceled() {

  }

  private void sendUpdate(OpenchatServiceMessageSender messageSender, File contactsFile)
      throws IOException, UntrustedIdentityException, NetworkException
  {
    FileInputStream            contactsFileStream = new FileInputStream(contactsFile);
    OpenchatServiceAttachmentStream attachmentStream   = new OpenchatServiceAttachmentStream(contactsFileStream,
                                                                                   "application/octet-stream",
                                                                                   contactsFile.length());

    try {
      messageSender.sendMultiDeviceContactsUpdate(attachmentStream);
    } catch (IOException ioe) {
      throw new NetworkException(ioe);
    }
  }

  private Optional<OpenchatServiceAttachmentStream> getAvatar(Uri uri) throws IOException {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      try {
        Uri                 displayPhotoUri = Uri.withAppendedPath(uri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        AssetFileDescriptor fd              = context.getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");
        return Optional.of(new OpenchatServiceAttachmentStream(fd.createInputStream(), "image/*", fd.getLength()));
      } catch (IOException e) {
        Log.w(TAG, e);
      }
    }

    Uri photoUri = Uri.withAppendedPath(uri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

    if (photoUri == null) {
      return Optional.absent();
    }

    Cursor cursor = context.getContentResolver().query(photoUri,
                                                       new String[] {
                                                           ContactsContract.CommonDataKinds.Photo.PHOTO,
                                                           ContactsContract.CommonDataKinds.Phone.MIMETYPE
                                                       }, null, null, null);

    try {
      if (cursor != null && cursor.moveToNext()) {
        byte[] data = cursor.getBlob(0);

        if (data != null) {
          return Optional.of(new OpenchatServiceAttachmentStream(new ByteArrayInputStream(data), "image/*", data.length));
        }
      }

      return Optional.absent();
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  private File createTempFile(String prefix) throws IOException {
    File file = File.createTempFile(prefix, "tmp", context.getCacheDir());
    file.deleteOnExit();

    return file;
  }

  private static class NetworkException extends Exception {

    public NetworkException(Exception ioe) {
      super(ioe);
    }
  }

}
