package com.openchat.curve25519;

public class JavaCurve25519Provider extends BaseJavaCurve25519Provider {

  protected JavaCurve25519Provider() {
    super(new JCESha512Provider(), new JCESecureRandomProvider());
  }

  @Override
  public boolean isNative() {
    return false;
  }

}
