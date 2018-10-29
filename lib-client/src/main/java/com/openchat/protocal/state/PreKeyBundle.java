package com.openchat.protocal.state;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.ecc.ECPublicKey;


public class PreKeyBundle {

  private int         registrationId;

  private int         deviceId;

  private int         preKeyId;
  private ECPublicKey preKeyPublic;

  private int         signedPreKeyId;
  private ECPublicKey signedPreKeyPublic;
  private byte[]      signedPreKeySignature;

  private IdentityKey identityKey;

  public PreKeyBundle(int registrationId, int deviceId, int preKeyId, ECPublicKey preKeyPublic,
                      int signedPreKeyId, ECPublicKey signedPreKeyPublic, byte[] signedPreKeySignature,
                      IdentityKey identityKey)
  {
    this.registrationId        = registrationId;
    this.deviceId              = deviceId;
    this.preKeyId              = preKeyId;
    this.preKeyPublic          = preKeyPublic;
    this.signedPreKeyId        = signedPreKeyId;
    this.signedPreKeyPublic    = signedPreKeyPublic;
    this.signedPreKeySignature = signedPreKeySignature;
    this.identityKey           = identityKey;
  }

  
  public int getDeviceId() {
    return deviceId;
  }

  
  public int getPreKeyId() {
    return preKeyId;
  }

  
  public ECPublicKey getPreKey() {
    return preKeyPublic;
  }

  
  public int getSignedPreKeyId() {
    return signedPreKeyId;
  }

  
  public ECPublicKey getSignedPreKey() {
    return signedPreKeyPublic;
  }

  
  public byte[] getSignedPreKeySignature() {
    return signedPreKeySignature;
  }

  
  public IdentityKey getIdentityKey() {
    return identityKey;
  }

  
  public int getRegistrationId() {
    return registrationId;
  }
}
