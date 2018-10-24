package com.openchat.secureim.storage;

import org.bouncycastle.openssl.PEMReader;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import com.openchat.secureim.configuration.DirectoryServerConfiguration;
import com.openchat.secureim.entities.DirectoryReconciliationRequest;
import com.openchat.secureim.entities.DirectoryReconciliationResponse;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class DirectoryReconciliationClient {

  private final String replicationUrl;
  private final Client client;

  public DirectoryReconciliationClient(DirectoryServerConfiguration directoryServerConfiguration)
      throws CertificateException
  {
    this.replicationUrl = directoryServerConfiguration.getReplicationUrl();
    this.client         = initializeClient(directoryServerConfiguration);
  }

  public DirectoryReconciliationResponse sendChunk(DirectoryReconciliationRequest request) {
    return client.target(replicationUrl)
                 .path("/v1/directory/reconcile")
                 .request(MediaType.APPLICATION_JSON_TYPE)
                 .put(Entity.json(request), DirectoryReconciliationResponse.class);
  }

  private static Client initializeClient(DirectoryServerConfiguration directoryServerConfiguration)
      throws CertificateException
  {
    KeyStore   trustStore = initializeKeyStore(directoryServerConfiguration.getReplicationCaCertificate());
    SSLContext sslContext = SslConfigurator.newInstance()
                                           .securityProtocol("TLSv1.2")
                                           .trustStore(trustStore)
                                           .createSSLContext();
    return ClientBuilder.newBuilder()
                        .register(HttpAuthenticationFeature.basic("signal", directoryServerConfiguration.getReplicationPassword().getBytes()))
                        .sslContext(sslContext)
                        .build();
  }

  private static KeyStore initializeKeyStore(String caCertificatePem)
      throws CertificateException
  {
    try {
      PEMReader       reader      = new PEMReader(new InputStreamReader(new ByteArrayInputStream(caCertificatePem.getBytes())));
      X509Certificate certificate = (X509Certificate) reader.readObject();

      if (certificate == null) {
        throw new CertificateException("No certificate found in parsing!");
      }

      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null);
      keyStore.setCertificateEntry("ca", certificate);
      return keyStore;
    } catch (IOException | KeyStoreException ex) {
      throw new CertificateException(ex);
    } catch (NoSuchAlgorithmException ex) {
      throw new AssertionError(ex);
    }
  }

}
