package com.openchat.secureim.sms;

import com.sun.jersey.core.util.Base64;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import com.openchat.secureim.configuration.TwilioConfiguration;
import com.openchat.secureim.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TwilioSmsSender implements SenderFactory.SmsSender {

  private final Meter smsMeter = Metrics.newMeter(TwilioSmsSender.class, "sms", "delivered", TimeUnit.MINUTES);

  private static final String TWILIO_URL = "https://api.twilio.com/2010-04-01/Accounts/%s/SMS/Messages";

  private final String accountId;
  private final String accountToken;
  private final String number;

  public TwilioSmsSender(TwilioConfiguration config) {
    this.accountId    = config.getAccountId();
    this.accountToken = config.getAccountToken();
    this.number       = config.getNumber();
  }

  @Override
  public void deliverSmsVerification(String destination, String verificationCode) throws IOException {
    URL url                      = new URL(String.format(TWILIO_URL, accountId));
    URLConnection connection     = url.openConnection();
    connection.setDoOutput(true);
    connection.setRequestProperty("Authorization", getTwilioAuthorizationHeader());
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

    Map<String, String> formData = new HashMap<>();
    formData.put("From", number);
    formData.put("To", destination);
    formData.put("Body", VERIFICATION_TEXT + verificationCode);

    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
    writer.write(Util.encodeFormParams(formData));
    writer.flush();

    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    while (reader.readLine() != null) {}
    writer.close();
    reader.close();

    smsMeter.mark();
  }

  private String getTwilioAuthorizationHeader() {
    String encoded = new String(Base64.encode(String.format("%s:%s", accountId, accountToken)));
    return "Basic " + encoded.replace("\n", "");
  }

}
