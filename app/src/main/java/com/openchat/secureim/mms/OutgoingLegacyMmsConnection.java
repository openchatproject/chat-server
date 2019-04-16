package com.openchat.secureim.mms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntityHC4;
import com.openchat.secureim.transport.UndeliverableMessageException;

import java.io.IOException;

import ws.com.google.android.mms.pdu.PduParser;
import ws.com.google.android.mms.pdu.SendConf;

@SuppressWarnings("deprecation")
public class OutgoingLegacyMmsConnection extends LegacyMmsConnection implements OutgoingMmsConnection {
  private final static String TAG = OutgoingLegacyMmsConnection.class.getSimpleName();

  public OutgoingLegacyMmsConnection(Context context) throws ApnUnavailableException {
    super(context);
  }

  private HttpUriRequest constructRequest(byte[] pduBytes, boolean useProxy)
      throws IOException
  {
    try {
      HttpPostHC4 request = new HttpPostHC4(apn.getMmsc());
      for (Header header : getBaseHeaders()) {
        request.addHeader(header);
      }

      request.setEntity(new ByteArrayEntityHC4(pduBytes));
      if (useProxy) {
        HttpHost proxy = new HttpHost(apn.getProxy(), apn.getPort());
        request.setConfig(RequestConfig.custom().setProxy(proxy).build());
      }
      return request;
    } catch (IllegalArgumentException iae) {
      throw new IOException(iae);
    }
  }

  public void sendNotificationReceived(byte[] pduBytes, boolean usingMmsRadio, boolean useProxyIfAvailable)
      throws IOException
  {
    sendBytes(pduBytes, usingMmsRadio, useProxyIfAvailable);
  }

  @Override
  public SendConf send(@NonNull byte[] pduBytes) throws UndeliverableMessageException {
    try {
      MmsRadio radio = MmsRadio.getInstance(context);

      if (isCdmaDevice()) {
        Log.w(TAG, "Sending MMS directly without radio change...");
        try {
          return send(pduBytes, false, false);
        } catch (IOException e) {
          Log.w(TAG, e);
        }
      }

      Log.w(TAG, "Sending MMS with radio change and proxy...");
      radio.connect();

      try {
        try {
          return send(pduBytes, true, true);
        } catch (IOException e) {
          Log.w(TAG, e);
        }

        Log.w(TAG, "Sending MMS with radio change and without proxy...");

        try {
          return send(pduBytes, true, false);
        } catch (IOException ioe) {
          Log.w(TAG, ioe);
          throw new UndeliverableMessageException(ioe);
        }
      } finally {
        radio.disconnect();
      }

    } catch (MmsRadioException e) {
      Log.w(TAG, e);
      throw new UndeliverableMessageException(e);
    }

  }

  private SendConf send(byte[] pduBytes, boolean useMmsRadio, boolean useProxyIfAvailable)  throws IOException {
    byte[] response = sendBytes(pduBytes, useMmsRadio, useProxyIfAvailable);
    return (SendConf) new PduParser(response).parse();
  }

  private byte[] sendBytes(byte[] pduBytes, boolean useMmsRadio, boolean useProxyIfAvailable) throws IOException {
    final boolean useProxy   = useProxyIfAvailable && apn.hasProxy();
    final String  targetHost = useProxy
                             ? apn.getProxy()
                             : Uri.parse(apn.getMmsc()).getHost();

    Log.w(TAG, "Sending MMS of length: " + pduBytes.length
               + (useMmsRadio ? ", using mms radio" : "")
               + (useProxy ? ", using proxy" : ""));

    try {
      if (checkRouteToHost(context, targetHost, useMmsRadio)) {
        Log.w(TAG, "got successful route to host " + targetHost);
        byte[] response = execute(constructRequest(pduBytes, useProxy));
        if (response != null) return response;
      }
    } catch (IOException ioe) {
      Log.w(TAG, ioe);
    }
    throw new IOException("Connection manager could not obtain route to host.");
  }

  public static boolean isConnectionPossible(Context context) {
    try {
      ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo         networkInfo         = connectivityManager.getNetworkInfo(MmsRadio.TYPE_MOBILE_MMS);
      if (networkInfo == null) {
        Log.w(TAG, "MMS network info was null, unsupported by this device");
        return false;
      }

      getApn(context);
      return true;
    } catch (ApnUnavailableException e) {
      Log.w(TAG, e);
      return false;
    }
  }
}
