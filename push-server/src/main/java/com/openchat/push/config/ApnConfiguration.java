package com.openchat.push.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;


public class ApnConfiguration {

  @NotEmpty
  @JsonProperty
  private String pushCertificate;

  @NotEmpty
  @JsonProperty
  private String pushKey;

  @NotEmpty
  @JsonProperty
  private String voipCertificate;

  @NotEmpty
  @JsonProperty
  private String voipKey;

  @JsonProperty
  private boolean feedback = true;

  public String getPushCertificate() {
    return pushCertificate;
  }

  public String getPushKey() {
    return pushKey;
  }

  public String getVoipCertificate() {
    return voipCertificate;
  }

  public String getVoipKey() {
    return voipKey;
  }

  public boolean isFeedbackEnabled() {
    return feedback;
  }
}
