package com.openchat.secureim.sms;


import com.google.common.base.Optional;
import com.openchat.secureim.configuration.NexmoConfiguration;
import com.openchat.secureim.configuration.TwilioConfiguration;

import java.io.IOException;

public class SenderFactory {

  private final TwilioSmsSender          twilioSender;
  private final Optional<NexmoSmsSender> nexmoSender;

  public SenderFactory(TwilioConfiguration twilioConfig, NexmoConfiguration nexmoConfig) {
    this.twilioSender = new TwilioSmsSender(twilioConfig);

    if (nexmoConfig != null) {
      this.nexmoSender = Optional.of(new NexmoSmsSender(nexmoConfig));
    } else {
      this.nexmoSender = Optional.absent();
    }
  }

  public SmsSender getSmsSender(String number) {
    if (nexmoSender.isPresent() && !isTwilioDestination(number)) {
      return nexmoSender.get();
    } else {
      return twilioSender;
    }
  }

  public VoxSender getVoxSender(String number) {
    if (nexmoSender.isPresent()) {
      return nexmoSender.get();
    }

    throw new AssertionError("FIX ME!");
  }

  private boolean isTwilioDestination(String number) {
    return number.length() == 12 && number.startsWith("+1");
  }

  public interface SmsSender {
    public static final String VERIFICATION_TEXT = "Your TextSecure verification code: ";
    public void deliverSmsVerification(String destination, String verificationCode) throws IOException;
  }

  public interface VoxSender {
    public static final String VERIFICATION_TEXT = "Your TextSecure verification code is: ";
    public void deliverVoxVerification(String destination, String verificationCode) throws IOException;
  }
}
