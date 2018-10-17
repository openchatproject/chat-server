package com.openchat.secureim.sqs;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.MessageAttributeValue;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.configuration.DirectoryConfiguration;
import com.openchat.secureim.configuration.SqsConfiguration;
import com.openchat.secureim.util.Constants;

import java.util.HashMap;
import java.util.Map;

import static com.codahale.metrics.MetricRegistry.name;

public class ContactDiscoveryQueueSender {

  private static final Logger  logger = LoggerFactory.getLogger(ContactDiscoveryQueueSender.class);

  private final MetricRegistry metricRegistry    = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);
  private final Meter          serviceErrorMeter = metricRegistry.meter(name(ContactDiscoveryQueueSender.class, "serviceError"));
  private final Meter          clientErrorMeter  = metricRegistry.meter(name(ContactDiscoveryQueueSender.class, "clientError"));

  private final String         queueUrl;
  private final AmazonSQS      sqs;

  public ContactDiscoveryQueueSender(SqsConfiguration sqsConfig) {
    final AWSCredentials               credentials         = new BasicAWSCredentials(sqsConfig.getAccessKey(), sqsConfig.getAccessSecret());
    final AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
    
    this.queueUrl = sqsConfig.getQueueUrl();
    this.sqs      = AmazonSQSClientBuilder.standard().withCredentials(credentialsProvider).build();
  }

  public void addRegisteredUser(String user) {
    sendMessage("add", user);
  }

  public void deleteRegisteredUser(String user) {
    sendMessage("delete", user);
  }

  private void sendMessage(String action, String user) {
    final Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
    messageAttributes.put("id", new MessageAttributeValue().withDataType("String").withStringValue(user));
    messageAttributes.put("action", new MessageAttributeValue().withDataType("String").withStringValue(action));
    SendMessageRequest sendMessageRequest = new SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody("-")
            .withMessageDeduplicationId(user + action)
            .withMessageGroupId(user)
            .withMessageAttributes(messageAttributes);
    try {
      sqs.sendMessage(sendMessageRequest);
    } catch (AmazonServiceException ex) {
      serviceErrorMeter.mark();
      logger.warn("sqs service error: ", ex);
    } catch (AmazonClientException ex) {
      clientErrorMeter.mark();
      logger.warn("sqs client error: ", ex);
    } catch (Throwable t) {
      logger.warn("sqs unexpected error: ", t);
    }
  }

}
