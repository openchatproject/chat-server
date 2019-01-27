package com.openchat.secureim.mms;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.openchat.secureim.R;
import com.openchat.secureim.attachments.Attachment;
import com.openchat.secureim.attachments.UriAttachment;
import com.openchat.secureim.database.AttachmentDatabase;
import com.openchat.secureim.util.MediaUtil;
import com.openchat.secureim.util.ResUtil;


public class AudioSlide extends Slide {

  public AudioSlide(Context context, Uri uri, long dataSize, boolean voiceNote) {
    super(context, constructAttachmentFromUri(context, uri, MediaUtil.AUDIO_UNSPECIFIED, dataSize, false, null, voiceNote));
  }

  public AudioSlide(Context context, Uri uri, long dataSize, String contentType, boolean voiceNote) {
    super(context,  new UriAttachment(uri, null, contentType, AttachmentDatabase.TRANSFER_PROGRESS_STARTED, dataSize, null, null, voiceNote));
  }

  public AudioSlide(Context context, Attachment attachment) {
    super(context, attachment);
  }

  @Override
  @Nullable
  public Uri getThumbnailUri() {
    return null;
  }

  @Override
  public boolean hasPlaceholder() {
    return true;
  }

  @Override
  public boolean hasImage() {
    return true;
  }

  @Override
  public boolean hasAudio() {
    return true;
  }

  @NonNull
  @Override
  public String getContentDescription() {
    return context.getString(R.string.Slide_audio);
  }

  @Override
  public @DrawableRes int getPlaceholderRes(Theme theme) {
    return ResUtil.getDrawableRes(theme, R.attr.conversation_icon_attach_audio);
  }
}
