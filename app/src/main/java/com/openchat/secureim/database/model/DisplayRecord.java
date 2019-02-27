package com.openchat.secureim.database.model;

import android.content.Context;
import android.text.SpannableString;

import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.recipients.Recipients;

public abstract class DisplayRecord {

  protected final Context context;
  protected final long type;

  private final Recipients recipients;
  private final long       dateSent;
  private final long       dateReceived;
  private final long       threadId;
  private final Body       body;

  public DisplayRecord(Context context, Body body, Recipients recipients, long dateSent,
                       long dateReceived, long threadId, long type)
  {
    this.context              = context.getApplicationContext();
    this.threadId             = threadId;
    this.recipients           = recipients;
    this.dateSent             = dateSent;
    this.dateReceived         = dateReceived;
    this.type                 = type;
    this.body                 = body;
  }

  public Body getBody() {
    return body;
  }

  public abstract SpannableString getDisplayBody();

  public Recipients getRecipients() {
    return recipients;
  }

  public long getDateSent() {
    return dateSent;
  }

  public long getDateReceived() {
    return dateReceived;
  }

  public long getThreadId() {
    return threadId;
  }

  public boolean isKeyExchange() {
    return SmsDatabase.Types.isKeyExchangeType(type);
  }

  public boolean isEndSession() {
    return SmsDatabase.Types.isEndSessionType(type);
  }

  public boolean isGroupUpdate() {
    return SmsDatabase.Types.isGroupUpdate(type);
  }

  public boolean isGroupQuit() {
    return SmsDatabase.Types.isGroupQuit(type);
  }

  public boolean isGroupAction() {
    return isGroupUpdate() || isGroupQuit();
  }

  public static class Body {
    private final String body;
    private final boolean plaintext;

    public Body(String body, boolean plaintext) {
      this.body      = body;
      this.plaintext = plaintext;
    }

    public boolean isPlaintext() {
      return plaintext;
    }

    public String getBody() {
      return body == null ? "" : body;
    }
  }
}
