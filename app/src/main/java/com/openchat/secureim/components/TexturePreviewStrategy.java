package com.openchat.secureim.components;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.view.TextureView;
import android.view.View;

import com.commonsware.cwac.camera.PreviewStrategy;

import java.io.IOException;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class TexturePreviewStrategy implements PreviewStrategy,
    TextureView.SurfaceTextureListener {
  private final CameraView cameraView;
  private TextureView widget=null;
  private SurfaceTexture surface=null;

  TexturePreviewStrategy(CameraView cameraView) {
    this.cameraView=cameraView;
    widget=new TextureView(cameraView.getContext());
    widget.setSurfaceTextureListener(this);
  }

  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surface,
                                        int width, int height) {
    this.surface=surface;

    cameraView.previewCreated();
    cameraView.initPreview(width, height);
  }

  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
                                          int width, int height) {
    cameraView.previewReset(width, height);
  }

  @Override
  public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    cameraView.previewDestroyed();

    return(true);
  }

  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) {
  }

  @Override
  public void attach(Camera camera) throws IOException {
    camera.setPreviewTexture(surface);
  }

  @Override
  public void attach(MediaRecorder recorder) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    }
    else {
      throw new IllegalStateException(
          "Cannot use TextureView with MediaRecorder");
    }
  }

  @Override
  public View getWidget() {
    return(widget);
  }
}
