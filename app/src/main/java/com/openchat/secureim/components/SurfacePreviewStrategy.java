package com.openchat.secureim.components;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.commonsware.cwac.camera.PreviewStrategy;

import java.io.IOException;

class SurfacePreviewStrategy implements PreviewStrategy,
    SurfaceHolder.Callback {
  private final CameraView cameraView;
  private SurfaceView preview=null;
  private SurfaceHolder previewHolder=null;

  @SuppressWarnings("deprecation")
  SurfacePreviewStrategy(CameraView cameraView) {
    this.cameraView=cameraView;
    preview=new SurfaceView(cameraView.getContext());
    previewHolder=preview.getHolder();
    previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    previewHolder.addCallback(this);
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    cameraView.previewCreated();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format,
                             int width, int height) {
    cameraView.initPreview(width, height);
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    cameraView.previewDestroyed();
  }

  @Override
  public void attach(Camera camera) throws IOException {
    camera.setPreviewDisplay(previewHolder);
  }

  @Override
  public void attach(MediaRecorder recorder) {
    recorder.setPreviewDisplay(previewHolder.getSurface());
  }

  @Override
  public View getWidget() {
    return(preview);
  }
}
