package com.openchat.secureim.recipients;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.openchat.secureim.contacts.avatars.ContactPhotoFactory;
import com.openchat.secureim.database.CanonicalAddressDatabase;
import com.openchat.secureim.util.Util;
import com.openchat.protocal.util.guava.Optional;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class RecipientFactory {

  private static final RecipientProvider provider = new RecipientProvider();

  public static Recipients getRecipientsForIds(Context context, String recipientIds, boolean asynchronous) {
    if (TextUtils.isEmpty(recipientIds))
      return new Recipients();

    return getRecipientsForIds(context, Util.split(recipientIds, " "), asynchronous);
  }

  public static Recipients getRecipientsFor(Context context, List<Recipient> recipients, boolean asynchronous) {
    long[] ids = new long[recipients.size()];
    int    i   = 0;

    for (Recipient recipient : recipients) {
      ids[i++] = recipient.getRecipientId();
    }

    return provider.getRecipients(context, ids, asynchronous);
  }

  public static Recipients getRecipientsFor(Context context, Recipient recipient, boolean asynchronous) {
    long[] ids = new long[1];
    ids[0] = recipient.getRecipientId();

    return provider.getRecipients(context, ids, asynchronous);
  }

  public static Recipient getRecipientForId(Context context, long recipientId, boolean asynchronous) {
    return provider.getRecipient(context, recipientId, asynchronous);
  }

  public static Recipients getRecipientsForIds(Context context, long[] recipientIds, boolean asynchronous) {
    return provider.getRecipients(context, recipientIds, asynchronous);
  }

  public static Recipients getRecipientsFromString(Context context, @NonNull String rawText, boolean asynchronous) {
    StringTokenizer tokenizer = new StringTokenizer(rawText, ",");
    List<String>    ids       = new LinkedList<>();

    while (tokenizer.hasMoreTokens()) {
      Optional<Long> id = getRecipientIdFromNumber(context, tokenizer.nextToken());

      if (id.isPresent()) {
        ids.add(String.valueOf(id.get()));
      }
    }

    return getRecipientsForIds(context, ids, asynchronous);
  }

  private static Recipients getRecipientsForIds(Context context, List<String> idStrings, boolean asynchronous) {
    long[]       ids      = new long[idStrings.size()];
    int          i        = 0;

    for (String id : idStrings) {
      ids[i++] = Long.parseLong(id);
    }

    return provider.getRecipients(context, ids, asynchronous);
  }

  private static Optional<Long> getRecipientIdFromNumber(Context context, String number) {
    number = number.trim();

    if (number.isEmpty()) return Optional.absent();

    if (hasBracketedNumber(number)) {
      number = parseBracketedNumber(number);
    }

    return Optional.of(CanonicalAddressDatabase.getInstance(context).getCanonicalAddressId(number));
  }

  private static boolean hasBracketedNumber(String recipient) {
    int openBracketIndex = recipient.indexOf('<');

    return (openBracketIndex != -1) &&
           (recipient.indexOf('>', openBracketIndex) != -1);
  }

  private static String parseBracketedNumber(String recipient) {
    int begin    = recipient.indexOf('<');
    int end      = recipient.indexOf('>', begin);
    String value = recipient.substring(begin + 1, end);

    return value;
  }

  public static void clearCache() {
    provider.clearCache();
  }

}
