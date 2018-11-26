package com.openchat.imservice.internal.contacts.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoteAttestationResponse {

  @JsonProperty
  private byte[] serverEphemeralPublic;

  @JsonProperty
  private byte[] serverStaticPublic;

  @JsonProperty
  private byte[] quote;

  @JsonProperty
  private byte[] iv;

  @JsonProperty
  private byte[] ciphertext;

  @JsonProperty
  private byte[] tag;

  @JsonProperty
  private String signature;

  @JsonProperty
  private String certificates;

  @JsonProperty
  private String signatureBody;

  public RemoteAttestationResponse(byte[] serverEphemeralPublic, byte[] serverStaticPublic,
                                   byte[] iv, byte[] ciphertext, byte[] tag,
                                   byte[] quote,  String signature, String certificates, String signatureBody)
  {
    this.serverEphemeralPublic = serverEphemeralPublic;
    this.serverStaticPublic    = serverStaticPublic;
    this.iv                    = iv;
    this.ciphertext            = ciphertext;
    this.tag                   = tag;
    this.quote                 = quote;
    this.signature             = signature;
    this.certificates          = certificates;
    this.signatureBody         = signatureBody;
  }

  public RemoteAttestationResponse() {}

  public byte[] getServerEphemeralPublic() {
    return serverEphemeralPublic;
  }

  public byte[] getServerStaticPublic() {
    return serverStaticPublic;
  }

  public byte[] getQuote() {
    return quote;
  }

  public byte[] getIv() {
    return iv;
  }

  public byte[] getCiphertext() {
    return ciphertext;
  }

  public byte[] getTag() {
    return tag;
  }

  public String getSignature() {
    return signature;
  }

  public String getCertificates() {
    return certificates;
  }

  public String getSignatureBody() {
    return signatureBody;
  }

}
