package com.openchat.secureim.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.annotations.VisibleForTesting;

public class VerificationCode {

  @JsonProperty
  private String verificationCode;
  @JsonIgnore
  private String verificationCodeDisplay;
  @JsonIgnore
  private String verificationCodeSpeech;

  @VisibleForTesting VerificationCode() {}

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

  @VisibleForTesting public boolean equals(Object o) {
    return o instanceof VerificationCode && verificationCode.equals(((VerificationCode) o).verificationCode);
  }
}
