package com.openchat.secureim.database;

public interface MmsSmsColumns {

  public static final String ID                       = "_id";
  public static final String NORMALIZED_DATE_SENT     = "date_sent";
  public static final String NORMALIZED_DATE_RECEIVED = "date_received";
  public static final String THREAD_ID                = "thread_id";
  public static final String READ                     = "read";
  public static final String BODY                     = "body";
  public static final String ADDRESS                  = "address";
  public static final String ADDRESS_DEVICE_ID        = "address_device_id";
  public static final String RECEIPT_COUNT            = "delivery_receipt_count";

  public static class Types {
    protected static final long TOTAL_MASK = 0xFFFFFFFF;

    protected static final long BASE_TYPE_MASK                     = 0x1F;

    protected static final long BASE_INBOX_TYPE                    = 20;
    protected static final long BASE_OUTBOX_TYPE                   = 21;
    protected static final long BASE_SENDING_TYPE                  = 22;
    protected static final long BASE_SENT_TYPE                     = 23;
    protected static final long BASE_SENT_FAILED_TYPE              = 24;
    protected static final long BASE_PENDING_SECURE_SMS_FALLBACK   = 25;
    protected static final long BASE_PENDING_INSECURE_SMS_FALLBACK = 26;
    public    static final long BASE_DRAFT_TYPE                    = 27;

    protected static final long[] OUTGOING_MESSAGE_TYPES = {BASE_OUTBOX_TYPE, BASE_SENT_TYPE,
                                                            BASE_SENDING_TYPE, BASE_SENT_FAILED_TYPE,
                                                            BASE_PENDING_SECURE_SMS_FALLBACK,
                                                            BASE_PENDING_INSECURE_SMS_FALLBACK};

    protected static final long MESSAGE_ATTRIBUTE_MASK = 0xE0;
    protected static final long MESSAGE_FORCE_SMS_BIT  = 0x40;

    protected static final long KEY_EXCHANGE_BIT                 = 0x8000;
    protected static final long KEY_EXCHANGE_STALE_BIT           = 0x4000;
    protected static final long KEY_EXCHANGE_PROCESSED_BIT       = 0x2000;
    protected static final long KEY_EXCHANGE_CORRUPTED_BIT       = 0x1000;
    protected static final long KEY_EXCHANGE_INVALID_VERSION_BIT =  0x800;
    protected static final long KEY_EXCHANGE_BUNDLE_BIT          =  0x400;
    protected static final long KEY_EXCHANGE_IDENTITY_UPDATE_BIT =  0x200;

    protected static final long SECURE_MESSAGE_BIT = 0x800000;
    protected static final long END_SESSION_BIT    = 0x400000;
    protected static final long PUSH_MESSAGE_BIT   = 0x200000;

    protected static final long GROUP_UPDATE_BIT = 0x10000;
    protected static final long GROUP_QUIT_BIT   = 0x20000;

    protected static final long ENCRYPTION_MASK                  = 0xFF000000;
    protected static final long ENCRYPTION_SYMMETRIC_BIT         = 0x80000000;
    protected static final long ENCRYPTION_ASYMMETRIC_BIT        = 0x40000000;
    protected static final long ENCRYPTION_REMOTE_BIT            = 0x20000000;
    protected static final long ENCRYPTION_REMOTE_FAILED_BIT     = 0x10000000;
    protected static final long ENCRYPTION_REMOTE_NO_SESSION_BIT = 0x08000000;
    protected static final long ENCRYPTION_REMOTE_DUPLICATE_BIT  = 0x04000000;
    protected static final long ENCRYPTION_REMOTE_LEGACY_BIT     = 0x02000000;

    public static boolean isDraftMessageType(long type) {
      return (type & BASE_TYPE_MASK) == BASE_DRAFT_TYPE;
    }

    public static boolean isFailedMessageType(long type) {
      return (type & BASE_TYPE_MASK) == BASE_SENT_FAILED_TYPE;
    }

    public static boolean isOutgoingMessageType(long type) {
      for (long outgoingType : OUTGOING_MESSAGE_TYPES) {
        if ((type & BASE_TYPE_MASK) == outgoingType)
          return true;
      }

      return false;
    }

    public static boolean isForcedSms(long type) {
      return (type & MESSAGE_FORCE_SMS_BIT) != 0;
    }

    public static boolean isPendingMessageType(long type) {
      return
          (type & BASE_TYPE_MASK) == BASE_OUTBOX_TYPE ||
              (type & BASE_TYPE_MASK) == BASE_SENDING_TYPE;
    }

    public static boolean isPendingSmsFallbackType(long type) {
      return (type & BASE_TYPE_MASK) == BASE_PENDING_INSECURE_SMS_FALLBACK ||
             (type & BASE_TYPE_MASK) == BASE_PENDING_SECURE_SMS_FALLBACK;
    }

    public static boolean isPendingSecureSmsFallbackType(long type) {
      return (type & BASE_TYPE_MASK) == BASE_PENDING_SECURE_SMS_FALLBACK;
    }

    public static boolean isPendingInsecureSmsFallbackType(long type) {
      return (type & BASE_TYPE_MASK) == BASE_PENDING_INSECURE_SMS_FALLBACK;
    }

    public static boolean isInboxType(long type) {
      return (type & BASE_TYPE_MASK) == BASE_INBOX_TYPE;
    }

    public static boolean isSecureType(long type) {
      return (type & SECURE_MESSAGE_BIT) != 0;
    }

    public static boolean isPushType(long type) {
      return (type & PUSH_MESSAGE_BIT) != 0;
    }

    public static boolean isEndSessionType(long type) {
      return (type & END_SESSION_BIT) != 0;
    }

    public static boolean isKeyExchangeType(long type) {
      return (type & KEY_EXCHANGE_BIT) != 0;
    }

    public static boolean isStaleKeyExchange(long type) {
      return (type & KEY_EXCHANGE_STALE_BIT) != 0;
    }

    public static boolean isProcessedKeyExchange(long type) {
      return (type & KEY_EXCHANGE_PROCESSED_BIT) != 0;
    }

    public static boolean isCorruptedKeyExchange(long type) {
      return (type & KEY_EXCHANGE_CORRUPTED_BIT) != 0;
    }

    public static boolean isInvalidVersionKeyExchange(long type) {
      return (type & KEY_EXCHANGE_INVALID_VERSION_BIT) != 0;
    }

    public static boolean isBundleKeyExchange(long type) {
      return (type & KEY_EXCHANGE_BUNDLE_BIT) != 0;
    }

    public static boolean isIdentityUpdate(long type) {
      return (type & KEY_EXCHANGE_IDENTITY_UPDATE_BIT) != 0;
    }

    public static boolean isGroupUpdate(long type) {
      return (type & GROUP_UPDATE_BIT) != 0;
    }

    public static boolean isGroupQuit(long type) {
      return (type & GROUP_QUIT_BIT) != 0;
    }

    public static boolean isSymmetricEncryption(long type) {
      return (type & ENCRYPTION_SYMMETRIC_BIT) != 0;
    }

    public static boolean isAsymmetricEncryption(long type) {
      return (type & ENCRYPTION_ASYMMETRIC_BIT) != 0;
    }

    public static boolean isFailedDecryptType(long type) {
      return (type & ENCRYPTION_REMOTE_FAILED_BIT) != 0;
    }

    public static boolean isDuplicateMessageType(long type) {
      return (type & ENCRYPTION_REMOTE_DUPLICATE_BIT) != 0;
    }

    public static boolean isDecryptInProgressType(long type) {
      return
          (type & ENCRYPTION_REMOTE_BIT)     != 0 ||
          (type & ENCRYPTION_ASYMMETRIC_BIT) != 0;
    }

    public static boolean isNoRemoteSessionType(long type) {
      return (type & ENCRYPTION_REMOTE_NO_SESSION_BIT) != 0;
    }

    public static boolean isLegacyType(long type) {
      return (type & ENCRYPTION_REMOTE_LEGACY_BIT) != 0;
    }

    public static long translateFromSystemBaseType(long theirType) {

      switch ((int)theirType) {
        case 1: return BASE_INBOX_TYPE;
        case 2: return BASE_SENT_TYPE;
        case 4: return BASE_OUTBOX_TYPE;
        case 5: return BASE_SENT_FAILED_TYPE;
      }

      return BASE_INBOX_TYPE;
    }

    public static int translateToSystemBaseType(long type) {
      if      (isInboxType(type))           return 1;
      else if (isOutgoingMessageType(type)) return 2;
      else if (isFailedMessageType(type))   return 5;

      return 1;
    }

  }

}
