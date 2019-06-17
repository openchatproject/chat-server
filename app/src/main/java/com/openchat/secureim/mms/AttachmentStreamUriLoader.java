package com.openchat.secureim.mms;

import android.content.Context;
import android.net.Uri;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.mms.AttachmentStreamUriLoader.AttachmentModel;
import com.openchat.secureim.mms.DecryptableStreamUriLoader.DecryptableUri;
import com.openchat.secureim.util.SaveAttachmentTask.Attachment;

import java.io.File;
import java.io.InputStream;

public class AttachmentStreamUriLoader implements StreamModelLoader<AttachmentModel> {
  private final Context context;

  
  public static class Factory implements ModelLoaderFactory<AttachmentModel, InputStream> {

    @Override
    public StreamModelLoader<AttachmentModel> build(Context context, GenericLoaderFactory factories) {
      return new AttachmentStreamUriLoader(context);
    }

    @Override
    public void teardown() {
    }
  }

  public AttachmentStreamUriLoader(Context context) {
    this.context = context;
  }

  @Override
  public DataFetcher<InputStream> getResourceFetcher(AttachmentModel model, int width, int height) {
    return new AttachmentStreamLocalUriFetcher(model.attachment, model.key);
  }

  public static class AttachmentModel {
    public File   attachment;
    public byte[] key;

    public AttachmentModel(File attachment, byte[] key) {
      this.attachment = attachment;
      this.key        = key;
    }
  }
}

