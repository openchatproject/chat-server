package com.openchat.secureim.push;


import android.content.Context;
import android.support.annotation.Nullable;

import com.openchat.secureim.BuildConfig;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.imservice.api.push.TrustStore;
import com.openchat.imservice.internal.configuration.openchatCdnUrl;
import com.openchat.imservice.internal.configuration.openchatServiceConfiguration;
import com.openchat.imservice.internal.configuration.openchatServiceUrl;

import java.util.HashMap;
import java.util.Map;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.TlsVersion;

public class openchatServiceNetworkAccess {

  private static final String TAG = openchatServiceNetworkAccess.class.getName();

  private static final String APPSPOT_SERVICE_REFLECTOR_HOST = "openchat-reflector-meek.appspot.com";
  private static final String APPSPOT_CDN_REFLECTOR_HOST     = "openchat-cdn-reflector.appspot.com";

  private static final ConnectionSpec GMAPS_CONNECTION_SPEC = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
      .tlsVersions(TlsVersion.TLS_1_2)
      .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA)
      .supportsTlsExtensions(true)
      .build();

  private static final ConnectionSpec GMAIL_CONNECTION_SPEC = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
      .tlsVersions(TlsVersion.TLS_1_2)
      .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV)
      .supportsTlsExtensions(true)
      .build();

  private static final ConnectionSpec PLAY_CONNECTION_SPEC = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
      .tlsVersions(TlsVersion.TLS_1_2)
      .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV)
      .supportsTlsExtensions(true)
      .build();


  private final Map<String, openchatServiceConfiguration> censorshipConfiguration;
  private final String[]                                censoredCountries;
  private final openchatServiceConfiguration              uncensoredConfiguration;

  public openchatServiceNetworkAccess(Context context) {
    final TrustStore       googleTrustStore      = new GoogleFrontingTrustStore(context);
    final openchatServiceUrl baseGoogleService     = new openchatServiceUrl("https://www.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, GMAIL_CONNECTION_SPEC);
    final openchatServiceUrl baseAndroidService    = new openchatServiceUrl("https://android.clients.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, PLAY_CONNECTION_SPEC);
    final openchatServiceUrl mapsOneAndroidService = new openchatServiceUrl("https://clients3.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, GMAPS_CONNECTION_SPEC);
    final openchatServiceUrl mapsTwoAndroidService = new openchatServiceUrl("https://clients4.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, GMAPS_CONNECTION_SPEC);
    final openchatServiceUrl mailAndroidService    = new openchatServiceUrl("https://mail.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, GMAIL_CONNECTION_SPEC);

    final openchatCdnUrl     baseGoogleCdn         = new openchatCdnUrl("https://www.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, GMAIL_CONNECTION_SPEC);
    final openchatCdnUrl     baseAndroidCdn        = new openchatCdnUrl("https://android.clients.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, PLAY_CONNECTION_SPEC);
    final openchatCdnUrl     mapsOneAndroidCdn     = new openchatCdnUrl("https://clients3.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, GMAPS_CONNECTION_SPEC);
    final openchatCdnUrl     mapsTwoAndroidCdn     = new openchatCdnUrl("https://clients4.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, GMAPS_CONNECTION_SPEC);
    final openchatCdnUrl     mailAndroidCdn        = new openchatCdnUrl("https://mail.google.com", APPSPOT_SERVICE_REFLECTOR_HOST, googleTrustStore, GMAIL_CONNECTION_SPEC);

    this.censorshipConfiguration = new HashMap<String, openchatServiceConfiguration>() {{
      put("+20", new openchatServiceConfiguration(new openchatServiceUrl[] {new openchatServiceUrl("https://www.google.com.eg",
                                                                                             APPSPOT_SERVICE_REFLECTOR_HOST,
                                                                                             googleTrustStore, GMAIL_CONNECTION_SPEC),
                                                                        baseAndroidService, mapsOneAndroidService, mapsTwoAndroidService, mailAndroidService},
                                                new openchatCdnUrl[] {new openchatCdnUrl("https://www.google.com.eg",
                                                                                     APPSPOT_CDN_REFLECTOR_HOST,
                                                                                     googleTrustStore, GMAIL_CONNECTION_SPEC),
                                                                    baseAndroidCdn, mapsOneAndroidCdn, mapsTwoAndroidCdn, mailAndroidCdn, mailAndroidCdn}));

      put("+971", new openchatServiceConfiguration(new openchatServiceUrl[] {new openchatServiceUrl("https://www.google.ae",
                                                                                              APPSPOT_SERVICE_REFLECTOR_HOST,
                                                                                              googleTrustStore, GMAIL_CONNECTION_SPEC),
                                                                         baseAndroidService, baseGoogleService, mapsOneAndroidService, mapsTwoAndroidService, mailAndroidService},
                                                 new openchatCdnUrl[] {new openchatCdnUrl("https://www.google.ae",
                                                                                      APPSPOT_CDN_REFLECTOR_HOST,
                                                                                      googleTrustStore, GMAIL_CONNECTION_SPEC),
                                                                     baseAndroidCdn, baseGoogleCdn, mapsOneAndroidCdn, mapsTwoAndroidCdn, mailAndroidCdn}));

      put("+968", new openchatServiceConfiguration(new openchatServiceUrl[] {new openchatServiceUrl("https://www.google.com.om",
                                                                                              APPSPOT_SERVICE_REFLECTOR_HOST,
                                                                                              googleTrustStore, GMAIL_CONNECTION_SPEC),
                                                                         baseAndroidService, baseGoogleService, mapsOneAndroidService, mapsTwoAndroidService, mailAndroidService},
                                                 new openchatCdnUrl[] {new openchatCdnUrl("https://www.google.com.om",
                                                                                      APPSPOT_CDN_REFLECTOR_HOST,
                                                                                      googleTrustStore, GMAIL_CONNECTION_SPEC),
                                                                     baseAndroidCdn, baseGoogleCdn, mapsOneAndroidCdn, mapsTwoAndroidCdn, mailAndroidCdn}));

      put("+974", new openchatServiceConfiguration(new openchatServiceUrl[] {new openchatServiceUrl("https://www.google.com.qa",
                                                                                              APPSPOT_SERVICE_REFLECTOR_HOST,
                                                                                              googleTrustStore, GMAIL_CONNECTION_SPEC),
                                                                         baseAndroidService, baseGoogleService, mapsOneAndroidService, mapsTwoAndroidService, mailAndroidService},
                                                 new openchatCdnUrl[] {new openchatCdnUrl("https://www.google.com.qa",
                                                                                      APPSPOT_CDN_REFLECTOR_HOST,
                                                                                      googleTrustStore, GMAIL_CONNECTION_SPEC),
                                                                     baseAndroidCdn, baseGoogleCdn, mapsOneAndroidCdn, mapsTwoAndroidCdn, mailAndroidCdn}));
    }};

    this.uncensoredConfiguration = new openchatServiceConfiguration(new openchatServiceUrl[] {new openchatServiceUrl(BuildConfig.openchat_URL, new openchatServiceTrustStore(context))},
                                                                  new openchatCdnUrl[] {new openchatCdnUrl(BuildConfig.openchat_CDN_URL, new openchatServiceTrustStore(context))});

    this.censoredCountries = this.censorshipConfiguration.keySet().toArray(new String[0]);
  }

  public openchatServiceConfiguration getConfiguration(Context context) {
    String localNumber = TextSecurePreferences.getLocalNumber(context);
    return getConfiguration(localNumber);
  }

  public openchatServiceConfiguration getConfiguration(@Nullable String localNumber) {
    if (localNumber == null) return this.uncensoredConfiguration;

    for (String censoredRegion : this.censoredCountries) {
      if (localNumber.startsWith(censoredRegion)) {
        return this.censorshipConfiguration.get(censoredRegion);
      }
    }

    return this.uncensoredConfiguration;
  }

  public boolean isCensored(Context context) {
    return getConfiguration(context) != this.uncensoredConfiguration;
  }

  public boolean isCensored(String number) {
    return getConfiguration(number) != this.uncensoredConfiguration;
  }

}
