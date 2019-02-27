package com.openchat.secureim.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.GroupDatabase;
import com.openchat.secureim.database.PartDatabase;
import com.openchat.secureim.push.PushServiceSocketFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.util.BitmapDecodingException;
import com.openchat.secureim.util.BitmapUtil;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.imservice.crypto.AttachmentCipherInputStream;
import com.openchat.imservice.crypto.InvalidMessageException;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.push.PushServiceSocket;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AvatarDownloader {

  private final Context context;

  public AvatarDownloader(Context context) {
    this.context = context.getApplicationContext();
  }

  public void process(MasterSecret masterSecret, Intent intent) {
    try {
      if (!SendReceiveService.DOWNLOAD_AVATAR_ACTION.equals(intent.getAction()))
        return;

      byte[]                    groupId  = intent.getByteArrayExtra("group_id");
      GroupDatabase             database = DatabaseFactory.getGroupDatabase(context);
      GroupDatabase.GroupRecord record   = database.getGroup(groupId);

      if (record != null) {
        long        avatarId           = record.getAvatarId();
        byte[]      key                = record.getAvatarKey();
        String      relay              = record.getRelay();

        if (avatarId == -1 || key == null) {
          return;
        }

        File        attachment         = downloadAttachment(relay, avatarId);
        InputStream scaleInputStream   = new AttachmentCipherInputStream(attachment, key);
        InputStream measureInputStream = new AttachmentCipherInputStream(attachment, key);
        Bitmap      avatar             = BitmapUtil.createScaledBitmap(measureInputStream, scaleInputStream, 500, 500);

        database.updateAvatar(groupId, avatar);

        try {
          Recipient groupRecipient = RecipientFactory.getRecipientsFromString(context, GroupUtil.getEncodedId(groupId), true)
                                                     .getPrimaryRecipient();
          groupRecipient.setContactPhoto(avatar);
        } catch (RecipientFormattingException e) {
          Log.w("AvatarDownloader", e);
        }

        attachment.delete();
      }
    } catch (IOException e) {
      Log.w("AvatarDownloader", e);
    } catch (InvalidMessageException e) {
      Log.w("AvatarDownloader", e);
    } catch (BitmapDecodingException e) {
      Log.w("AvatarDownloader", e);
    }
  }

  private File downloadAttachment(String relay, long contentLocation) throws IOException {
    PushServiceSocket socket = PushServiceSocketFactory.create(context);
    return socket.retrieveAttachment(relay, contentLocation);
  }

}
