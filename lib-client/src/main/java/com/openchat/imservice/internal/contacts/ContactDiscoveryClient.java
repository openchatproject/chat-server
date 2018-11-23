package com.openchat.imservice.internal.contacts;

import org.spongycastle.crypto.InvalidCipherTextException;
import com.openchat.curve25519.Curve25519;
import com.openchat.curve25519.Curve25519KeyPair;
import com.openchat.imservice.internal.contacts.crypto.ContactDiscoveryCipher;
import com.openchat.imservice.internal.contacts.crypto.Quote;
import com.openchat.imservice.internal.contacts.crypto.RemoteAttestation;
import com.openchat.imservice.internal.contacts.crypto.RemoteAttestationKeys;
import com.openchat.imservice.internal.contacts.crypto.UnauthenticatedQuoteException;
import com.openchat.imservice.internal.contacts.crypto.UnauthenticatedResponseException;
import com.openchat.imservice.internal.contacts.entities.DiscoveryRequest;
import com.openchat.imservice.internal.contacts.entities.DiscoveryResponse;
import com.openchat.imservice.internal.contacts.entities.RemoteAttestationRequest;
import com.openchat.imservice.internal.contacts.entities.RemoteAttestationResponse;
import com.openchat.imservice.internal.push.PushServiceSocket;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SignatureException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ContactDiscoveryClient {

  private final PushServiceSocket socket;

  public ContactDiscoveryClient(PushServiceSocket socket) {
    this.socket = socket;
  }

  public RemoteAttestation getRemoteAttestation(KeyStore iasKeyStore, String mrenclave)
      throws UnauthenticatedQuoteException, SignatureException, KeyStoreException, Quote.InvalidQuoteFormatException, UnauthenticatedResponseException, IOException
  {
    try {
      Curve25519        curve   = Curve25519.getInstance(Curve25519.BEST);
      Curve25519KeyPair keyPair = curve.generateKeyPair();

      ContactDiscoveryCipher    cipher   = new ContactDiscoveryCipher();
      RemoteAttestationRequest  request  = new RemoteAttestationRequest(keyPair.getPublicKey());
      RemoteAttestationResponse response = socket.getContactDiscoveryRemoteAttestation(request, mrenclave);

      RemoteAttestationKeys keys      = new RemoteAttestationKeys(keyPair, response.getServerEphemeralPublic(), response.getServerStaticPublic());
      Quote                 quote     = new Quote(response.getQuote());
      byte[]                requestId = cipher.getRequestId(keys, response);

      cipher.verifyServerQuote(quote, response.getServerStaticPublic(), mrenclave);
      cipher.verifyIasSignature(iasKeyStore, response.getCertificates(), response.getSignatureBody(), response.getSignature(), quote);

      return new RemoteAttestation(requestId, keys);
    } catch (InvalidCipherTextException e) {
      throw new UnauthenticatedResponseException(e);
    }
  }

  public List<String> getRegisteredUsers(List<String> addressBook, RemoteAttestation remoteAttestation, String mrenclave)
      throws IOException
  {
    try {
      ContactDiscoveryCipher cipher   = new ContactDiscoveryCipher();
      DiscoveryRequest       request  = cipher.createDiscoveryRequest(addressBook, remoteAttestation);
      DiscoveryResponse      response = socket.getContactDiscoveryRegisteredUsers(request, mrenclave);
      byte[]                 data     = cipher.getDiscoveryResponseData(response, remoteAttestation);

      Iterator<String> addressBookIterator = addressBook.iterator();
      List<String>     results             = new LinkedList<>();

      for (byte aData : data) {
        String candidate = addressBookIterator.next();

        if (aData != 0) results.add(candidate);
      }

      return results;
    } catch (InvalidCipherTextException e) {
      throw new IOException(e);
    }
  }

}
