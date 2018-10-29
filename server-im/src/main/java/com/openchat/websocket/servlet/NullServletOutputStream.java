package com.openchat.websocket.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class NullServletOutputStream extends ServletOutputStream {
  @Override
  public void write(int b) throws IOException {}

  @Override
  public void write(byte[] buf) {}

  @Override
  public void write(byte[] buf, int offset, int len) {}

  @Override
  public boolean isReady() {
    return false;
  }

  @Override
  public void setWriteListener(WriteListener writeListener) {

  }
}
