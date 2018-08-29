package com.openchat.secureim.limits;


import net.spy.memcached.MemcachedClient;
import com.openchat.secureim.configuration.RateLimitsConfiguration;

public class RateLimiters {

  private final RateLimiter smsDestinationLimiter;
  private final RateLimiter voiceDestinationLimiter;
  private final RateLimiter verifyLimiter;

  private final RateLimiter attachmentLimiter;
  private final RateLimiter contactsLimiter;
  private final RateLimiter preKeysLimiter;
  private final RateLimiter messagesLimiter;

  public RateLimiters(RateLimitsConfiguration config, MemcachedClient memcachedClient) {
    this.smsDestinationLimiter = new RateLimiter(memcachedClient, "smsDestination",
                                                 config.getSmsDestination().getBucketSize(),
                                                 config.getSmsDestination().getLeakRatePerMinute());

    this.voiceDestinationLimiter = new RateLimiter(memcachedClient, "voxDestination",
                                                   config.getVoiceDestination().getBucketSize(),
                                                   config.getVoiceDestination().getLeakRatePerMinute());

    this.verifyLimiter = new RateLimiter(memcachedClient, "verify",
                                         config.getVerifyNumber().getBucketSize(),
                                         config.getVerifyNumber().getLeakRatePerMinute());

    this.attachmentLimiter = new RateLimiter(memcachedClient, "attachmentCreate",
                                             config.getAttachments().getBucketSize(),
                                             config.getAttachments().getLeakRatePerMinute());

    this.contactsLimiter = new RateLimiter(memcachedClient, "contactsQuery",
                                           config.getContactQueries().getBucketSize(),
                                           config.getContactQueries().getLeakRatePerMinute());

    this.preKeysLimiter = new RateLimiter(memcachedClient, "prekeys",
                                          config.getPreKeys().getBucketSize(),
                                          config.getPreKeys().getLeakRatePerMinute());

    this.messagesLimiter = new RateLimiter(memcachedClient, "messages",
                                           config.getMessages().getBucketSize(),
                                           config.getMessages().getLeakRatePerMinute());

  }

  public RateLimiter getMessagesLimiter() {
    return messagesLimiter;
  }

  public RateLimiter getPreKeysLimiter() {
    return preKeysLimiter;
  }

  public RateLimiter getContactsLimiter() {
    return contactsLimiter;
  }

  public RateLimiter getAttachmentLimiter() {
    return this.attachmentLimiter;
  }

  public RateLimiter getSmsDestinationLimiter() {
    return smsDestinationLimiter;
  }

  public RateLimiter getVoiceDestinationLimiter() {
    return voiceDestinationLimiter;
  }

  public RateLimiter getVerifyLimiter() {
    return verifyLimiter;
  }

}
