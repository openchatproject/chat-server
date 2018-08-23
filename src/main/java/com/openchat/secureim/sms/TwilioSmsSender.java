package com.openchat.secureim.sms;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.openchat.secureim.configuration.TwilioConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TwilioSmsSender implements SenderFactory.SmsSender, SenderFactory.VoxSender {

  public static final String SAY_TWIML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                         "<Response>\n" +
                                         "    <Say voice=\"woman\" language=\"en\">%s</Say>\n" +
                                         "</Response>";

  private final Meter smsMeter = Metrics.newMeter(TwilioSmsSender.class, "sms", "delivered", TimeUnit.MINUTES);
  private final Meter voxMeter = Metrics.newMeter(TwilioSmsSender.class, "vox", "delivered", TimeUnit.MINUTES);

  private final String accountId;
  private final String accountToken;
  private final String number;
  private final String localDomain;

  public TwilioSmsSender(TwilioConfiguration config) {
    this.accountId    = config.getAccountId();
    this.accountToken = config.getAccountToken();
    this.number       = config.getNumber();
    this.localDomain  = config.getLocalDomain();
  }

  @Override
  public void deliverSmsVerification(String destination, String verificationCode)
      throws IOException
  {
    try {
      TwilioRestClient    client         = new TwilioRestClient(accountId, accountToken);
      MessageFactory      messageFactory = client.getAccount().getMessageFactory();
      List<NameValuePair> messageParams  = new LinkedList<>();
      messageParams.add(new BasicNameValuePair("To", destination));
      messageParams.add(new BasicNameValuePair("From", number));
      messageParams.add(new BasicNameValuePair("Body", SenderFactory.SmsSender.VERIFICATION_TEXT + verificationCode));
      messageFactory.create(messageParams);
    } catch (TwilioRestException e) {
      throw new IOException(e);
    }

    smsMeter.mark();
  }

  @Override
  public void deliverVoxVerification(String destination, String verificationCode) throws IOException {
    try {
      TwilioRestClient    client      = new TwilioRestClient(accountId, accountToken);
      CallFactory         callFactory = client.getAccount().getCallFactory();
      Map<String, String> callParams  = new HashMap<>();
      callParams.put("To", destination);
      callParams.put("From", number);
      callParams.put("Url", "https://" + localDomain + "/v1/accounts/voice/twiml/" + verificationCode);
      callFactory.create(callParams);
    } catch (TwilioRestException e) {
      throw new IOException(e);
    }

    voxMeter.mark();
  }
}
