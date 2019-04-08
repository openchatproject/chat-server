package com.openchat.secureim.mms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.NoConnectionReuseStrategyHC4;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import com.openchat.secureim.database.ApnDatabase;
import com.openchat.secureim.util.TelephonyUtil;
import com.openchat.secureim.util.Conversions;
import com.openchat.secureim.util.Util;
import com.openchat.protocal.util.guava.Optional;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;

public abstract class MmsConnection {
  private static final String TAG = "MmsCommunication";

  protected final Context context;
  protected final Apn     apn;

  protected MmsConnection(Context context, Apn apn) {
    this.context = context;
    this.apn     = apn;
  }

  public static Apn getApn(Context context, String apnName) throws ApnUnavailableException {
    Log.w(TAG, "Getting MMSC params for apn " + apnName);

    try {
      Optional<Apn> params = ApnDatabase.getInstance(context)
                                        .getMmsConnectionParameters(TelephonyUtil.getMccMnc(context),
                                                                    TelephonyUtil.getApn(context));

      if (!params.isPresent()) {
        throw new ApnUnavailableException("No parameters available from ApnDefaults.");
      }

      return params.get();
    } catch (IOException ioe) {
      throw new ApnUnavailableException("ApnDatabase threw an IOException", ioe);
    }
  }

  protected static boolean checkRouteToHost(Context context, String host, boolean usingMmsRadio)
      throws IOException
  {
    InetAddress inetAddress = InetAddress.getByName(host);
    if (!usingMmsRadio) {
      if (inetAddress.isSiteLocalAddress()) {
        throw new IOException("RFC1918 address in non-MMS radio situation!");
      }
      Log.w(TAG, "returning vacuous success since MMS radio is not in use");
      return true;
    }

    if (inetAddress == null) {
      throw new IOException("Unable to lookup host: InetAddress.getByName() returned null.");
    }

    byte[] ipAddressBytes = inetAddress.getAddress();
    if (ipAddressBytes == null || ipAddressBytes.length != 4) {
      Log.w(TAG, "returning vacuous success since android.net package doesn't support IPv6");
      return true;
    }

    Log.w(TAG, "Checking route to address: " + host + ", " + inetAddress.getHostAddress());
    ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    int     ipAddress           = Conversions.byteArrayToIntLittleEndian(ipAddressBytes, 0);
    boolean routeToHostObtained = manager.requestRouteToHost(MmsRadio.TYPE_MOBILE_MMS, ipAddress);
    Log.w(TAG, "requestRouteToHost result: " + routeToHostObtained);
    return routeToHostObtained;
  }

  protected static byte[] parseResponse(InputStream is) throws IOException {
    InputStream           in   = new BufferedInputStream(is);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Util.copy(in, baos);

    Log.w(TAG, "Received full server response, " + baos.size() + " bytes");

    return baos.toByteArray();
  }

  protected CloseableHttpClient constructHttpClient()
      throws IOException {
    RequestConfig config = RequestConfig.custom()
                                        .setConnectTimeout(20 * 1000)
                                        .setConnectionRequestTimeout(20 * 1000)
                                        .setSocketTimeout(20 * 1000)
                                        .setMaxRedirects(20)
                                        .build();

    URL mmsc = new URL(apn.getMmsc());
    CredentialsProvider credsProvider = new BasicCredentialsProvider();

    if (apn.hasAuthentication()) {
      credsProvider.setCredentials(new AuthScope(mmsc.getHost(), mmsc.getPort() > -1 ? mmsc.getPort() : mmsc.getDefaultPort()),
                                   new UsernamePasswordCredentials(apn.getUsername(), apn.getPassword()));
    }

    return HttpClients.custom()
                      .setConnectionReuseStrategy(new NoConnectionReuseStrategyHC4())
                      .setRedirectStrategy(new LaxRedirectStrategy())
                      .setUserAgent("Android-Mms/2.0")
                      .setConnectionManager(new BasicHttpClientConnectionManager())
                      .setDefaultRequestConfig(config)
                      .setDefaultCredentialsProvider(credsProvider)
                      .build();
  }

  protected byte[] makeRequest(boolean useProxy) throws IOException {
    Log.w(TAG, "connecting to " + apn.getMmsc() + (useProxy ? " using proxy" : ""));

    HttpUriRequest request;
    CloseableHttpClient   client   = null;
    CloseableHttpResponse response = null;
    try {
      request  = constructRequest(useProxy);
      client   = constructHttpClient();
      response = client.execute(request);

      Log.w(TAG, "* response code: " + response.getStatusLine());

      if (response.getStatusLine().getStatusCode() == 200) {
        return parseResponse(response.getEntity().getContent());
      }
    } finally {
      if (response != null) response.close();
      if (client != null)   client.close();
    }

    throw new IOException("unhandled response code");
  }

  protected abstract HttpUriRequest constructRequest(boolean useProxy) throws IOException;

  public static class Apn {

    public static Apn EMPTY = new Apn("", "", "", "", "");

    private final String mmsc;
    private final String proxy;
    private final String port;
    private final String username;
    private final String password;

    public Apn(String mmsc, String proxy, String port, String username, String password) {
      this.mmsc     = mmsc;
      this.proxy    = proxy;
      this.port     = port;
      this.username = username;
      this.password = password;
    }

    public Apn(Apn customApn, Apn defaultApn,
               boolean useCustomMmsc,
               boolean useCustomProxy,
               boolean useCustomProxyPort,
               boolean useCustomUsername,
               boolean useCustomPassword)
    {
      this.mmsc     = useCustomMmsc ? customApn.mmsc : defaultApn.mmsc;
      this.proxy    = useCustomProxy ? customApn.proxy : defaultApn.proxy;
      this.port     = useCustomProxyPort ? customApn.port : defaultApn.port;
      this.username = useCustomUsername ? customApn.username : defaultApn.username;
      this.password = useCustomPassword ? customApn.password : defaultApn.password;
    }

    public boolean hasProxy() {
      return !TextUtils.isEmpty(proxy);
    }

    public String getMmsc() {
      return mmsc;
    }

    public String getProxy() {
      return hasProxy() ? proxy : null;
    }

    public int getPort() {
      return TextUtils.isEmpty(port) ? 80 : Integer.parseInt(port);
    }

    public boolean hasAuthentication() {
      return !TextUtils.isEmpty(username) || !TextUtils.isEmpty(password);
    }

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }

    @Override
    public String toString() {
      return Apn.class.getSimpleName() +
          "{ mmsc: \"" + mmsc + "\"" +
          ", proxy: " + (proxy == null ? "none" : '"' + proxy + '"') +
          ", port: " + (port == null ? "(none)" : port) +
          ", user: " + (username == null ? "none" : '"' + username + '"') +
          ", pass: " + (password == null ? "none" : '"' + password + '"') + " }";
    }
  }
}
