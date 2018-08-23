package com.openchat.secureim.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Util {

  public static byte[] getContactToken(String number) {
    try {
      MessageDigest digest    = MessageDigest.getInstance("SHA1");
      byte[]        result    = digest.digest(number.getBytes());
      byte[]        truncated = Util.truncate(result, 10);

      return truncated;
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  public static String getEncodedContactToken(String number) {
    return Base64.encodeBytesWithoutPadding(getContactToken(number));
  }

  public static boolean isValidNumber(String number) {
    return number.matches("^\\+[0-9]{10,}");
  }

  public static String encodeFormParams(Map<String, String> params) {
    try {
      StringBuffer buffer = new StringBuffer();

      for (String key : params.keySet()) {
        buffer.append(String.format("%s=%s",
                                    URLEncoder.encode(key, "UTF-8"),
                                    URLEncoder.encode(params.get(key), "UTF-8")));
        buffer.append("&");
      }

      buffer.deleteCharAt(buffer.length()-1);
      return buffer.toString();
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }

  public static boolean isEmpty(String param) {
    return param == null || param.length() == 0;
  }

  public static byte[] combine(byte[] one, byte[] two, byte[] three, byte[] four) {
    byte[] combined = new byte[one.length + two.length + three.length + four.length];
    System.arraycopy(one, 0, combined, 0, one.length);
    System.arraycopy(two, 0, combined, one.length, two.length);
    System.arraycopy(three, 0, combined, one.length + two.length, three.length);
    System.arraycopy(four, 0, combined, one.length + two.length + three.length, four.length);

    return combined;
  }

  public static byte[] truncate(byte[] element, int length) {
    byte[] result = new byte[length];
    System.arraycopy(element, 0, result, 0, result.length);

    return result;
  }

}
