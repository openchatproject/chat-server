package com.openchat.secureim.mms;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.util.Arrays;

import ws.com.google.android.mms.pdu.PduParser;
import ws.com.google.android.mms.pdu.RetrieveConf;

public class IncomingMmsConnection extends MmsConnection {
  private static final String TAG = IncomingMmsConnection.class.getSimpleName();

  public IncomingMmsConnection(Context context, Apn apn) {
    super(context, apn);
  }

  @Override
  protected HttpUriRequest constructRequest(boolean useProxy) throws IOException {
    HttpGetHC4 request = new HttpGetHC4(apn.getMmsc());
    for (Header header : getBaseHeaders()) {
      request.addHeader(header);
    }
    if (useProxy) {
      HttpHost proxy = new HttpHost(apn.getProxy(), apn.getPort());
      request.setConfig(RequestConfig.custom().setProxy(proxy).build());
    }
    return request;
  }

  public static boolean isConnectionPossible(Context context, String apn) {
    try {
      getApn(context, apn);
      return true;
    } catch (ApnUnavailableException e) {
      return false;
    }
  }

  public RetrieveConf retrieve(boolean usingMmsRadio, boolean useProxyIfAvailable)
      throws IOException, ApnUnavailableException
  {
    byte[] pdu = null;

    final boolean useProxy   = useProxyIfAvailable && apn.hasProxy();
    final String  targetHost = useProxy
                             ? apn.getProxy()
                             : Uri.parse(apn.getMmsc()).getHost();
    try {
      if (checkRouteToHost(context, targetHost, usingMmsRadio)) {
        Log.w(TAG, "got successful route to host " + targetHost);
        pdu = makeRequest(useProxy);
      }
    } catch (IOException ioe) {
      Log.w(TAG, ioe);
    }

    if (pdu == null) {
      throw new IOException("Connection manager could not obtain route to host.");
    }

    RetrieveConf retrieved = (RetrieveConf)new PduParser(pdu).parse();

    if (retrieved == null) {
      Log.w(TAG, "Couldn't parse PDU, byte response: " + Arrays.toString(pdu));
      Log.w(TAG, "Couldn't parse PDU, ASCII:         " + new String(pdu));
      throw new IOException("Bad retrieved PDU");
    }

    return retrieved;
  }
}
