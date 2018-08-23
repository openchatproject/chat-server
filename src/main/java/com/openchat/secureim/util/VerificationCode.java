package com.openchat.secureim.util;

public class VerificationCode {

  private String verificationCode;
  private String verificationCodeDisplay;
  private String verificationCodeSpeech;

  public VerificationCode(int verificationCode) {
    this.verificationCode        = verificationCode + "";
    this.verificationCodeDisplay = this.verificationCode.substring(0, 3) + "-" +
                                   this.verificationCode.substring(3, 6);
    this.verificationCodeSpeech  = delimit(verificationCode + "");
  }

  public String getVerificationCode() {
    return verificationCode;
  }

  public String getVerificationCodeDisplay() {
    return verificationCodeDisplay;
  }

  public String getVerificationCodeSpeech() {
    return verificationCodeSpeech;
  }

  private String delimit(String code) {
    String delimited = "";

    for (int i=0;i<code.length();i++) {
      delimited += code.charAt(i);

      if (i != code.length() - 1)
        delimited += ',';
    }

    return delimited;
  }

}
