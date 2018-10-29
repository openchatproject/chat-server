package com.openchat.protocal.ratchet;

import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECKeyPair;
import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.kdf.HKDF;
import com.openchat.protocal.state.SessionState;
import com.openchat.protocal.util.ByteUtil;
import com.openchat.protocal.util.Pair;
import com.openchat.protocal.util.guava.Optional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class RatchetingSession {

  public static void initializeSession(SessionState sessionState,
                                       int sessionVersion,
                                       SymmetricOpenchatParameters parameters)
      throws InvalidKeyException
  {
    if (isAlice(parameters.getOurBaseKey().getPublicKey(), parameters.getTheirBaseKey())) {
      AliceOpenchatParameters.Builder aliceParameters = AliceOpenchatParameters.newBuilder();

      aliceParameters.setOurBaseKey(parameters.getOurBaseKey())
                     .setOurIdentityKey(parameters.getOurIdentityKey())
                     .setTheirRatchetKey(parameters.getTheirRatchetKey())
                     .setTheirIdentityKey(parameters.getTheirIdentityKey())
                     .setTheirSignedPreKey(parameters.getTheirBaseKey())
                     .setTheirOneTimePreKey(Optional.<ECPublicKey>absent());

      RatchetingSession.initializeSession(sessionState, sessionVersion, aliceParameters.create());
    } else {
      BobOpenchatParameters.Builder bobParameters = BobOpenchatParameters.newBuilder();

      bobParameters.setOurIdentityKey(parameters.getOurIdentityKey())
                   .setOurRatchetKey(parameters.getOurRatchetKey())
                   .setOurSignedPreKey(parameters.getOurBaseKey())
                   .setOurOneTimePreKey(Optional.<ECKeyPair>absent())
                   .setTheirBaseKey(parameters.getTheirBaseKey())
                   .setTheirIdentityKey(parameters.getTheirIdentityKey());

      RatchetingSession.initializeSession(sessionState, sessionVersion, bobParameters.create());
    }
  }

  public static void initializeSession(SessionState sessionState,
                                       int sessionVersion,
                                       AliceOpenchatParameters parameters)
      throws InvalidKeyException
  {
    try {
      sessionState.setSessionVersion(sessionVersion);
      sessionState.setRemoteIdentityKey(parameters.getTheirIdentityKey());
      sessionState.setLocalIdentityKey(parameters.getOurIdentityKey().getPublicKey());

      ECKeyPair             sendingRatchetKey = Curve.generateKeyPair();
      ByteArrayOutputStream secrets           = new ByteArrayOutputStream();

      if (sessionVersion >= 3) {
        secrets.write(getDiscontinuityBytes());
      }

      secrets.write(Curve.calculateAgreement(parameters.getTheirSignedPreKey(),
                                             parameters.getOurIdentityKey().getPrivateKey()));
      secrets.write(Curve.calculateAgreement(parameters.getTheirIdentityKey().getPublicKey(),
                                             parameters.getOurBaseKey().getPrivateKey()));
      secrets.write(Curve.calculateAgreement(parameters.getTheirSignedPreKey(),
                                             parameters.getOurBaseKey().getPrivateKey()));

      if (sessionVersion >= 3 && parameters.getTheirOneTimePreKey().isPresent()) {
        secrets.write(Curve.calculateAgreement(parameters.getTheirOneTimePreKey().get(),
                                               parameters.getOurBaseKey().getPrivateKey()));
      }

      DerivedKeys             derivedKeys  = calculateDerivedKeys(sessionVersion, secrets.toByteArray());
      Pair<RootKey, ChainKey> sendingChain = derivedKeys.getRootKey().createChain(parameters.getTheirRatchetKey(), sendingRatchetKey);

      sessionState.addReceiverChain(parameters.getTheirRatchetKey(), derivedKeys.getChainKey());
      sessionState.setSenderChain(sendingRatchetKey, sendingChain.second());
      sessionState.setRootKey(sendingChain.first());
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  public static void initializeSession(SessionState sessionState,
                                       int sessionVersion,
                                       BobOpenchatParameters parameters)
      throws InvalidKeyException
  {

    try {
      sessionState.setSessionVersion(sessionVersion);
      sessionState.setRemoteIdentityKey(parameters.getTheirIdentityKey());
      sessionState.setLocalIdentityKey(parameters.getOurIdentityKey().getPublicKey());

      ByteArrayOutputStream secrets = new ByteArrayOutputStream();

      if (sessionVersion >= 3) {
        secrets.write(getDiscontinuityBytes());
      }

      secrets.write(Curve.calculateAgreement(parameters.getTheirIdentityKey().getPublicKey(),
                                             parameters.getOurSignedPreKey().getPrivateKey()));
      secrets.write(Curve.calculateAgreement(parameters.getTheirBaseKey(),
                                             parameters.getOurIdentityKey().getPrivateKey()));
      secrets.write(Curve.calculateAgreement(parameters.getTheirBaseKey(),
                                             parameters.getOurSignedPreKey().getPrivateKey()));

      if (sessionVersion >= 3 && parameters.getOurOneTimePreKey().isPresent()) {
        secrets.write(Curve.calculateAgreement(parameters.getTheirBaseKey(),
                                               parameters.getOurOneTimePreKey().get().getPrivateKey()));
      }

      DerivedKeys derivedKeys = calculateDerivedKeys(sessionVersion, secrets.toByteArray());

      sessionState.setSenderChain(parameters.getOurRatchetKey(), derivedKeys.getChainKey());
      sessionState.setRootKey(derivedKeys.getRootKey());
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  private static byte[] getDiscontinuityBytes() {
    byte[] discontinuity = new byte[32];
    Arrays.fill(discontinuity, (byte) 0xFF);
    return discontinuity;
  }

  private static DerivedKeys calculateDerivedKeys(int sessionVersion, byte[] masterSecret) {
    HKDF     kdf                = HKDF.createFor(sessionVersion);
    byte[]   derivedSecretBytes = kdf.deriveSecrets(masterSecret, "OpenchatText".getBytes(), 64);
    byte[][] derivedSecrets     = ByteUtil.split(derivedSecretBytes, 32, 32);

    return new DerivedKeys(new RootKey(kdf, derivedSecrets[0]),
                           new ChainKey(kdf, derivedSecrets[1], 0));
  }

  private static boolean isAlice(ECPublicKey ourKey, ECPublicKey theirKey) {
    return ourKey.compareTo(theirKey) < 0;
  }

  private static class DerivedKeys {
    private final RootKey   rootKey;
    private final ChainKey  chainKey;

    private DerivedKeys(RootKey rootKey, ChainKey chainKey) {
      this.rootKey   = rootKey;
      this.chainKey  = chainKey;
    }

    public RootKey getRootKey() {
      return rootKey;
    }

    public ChainKey getChainKey() {
      return chainKey;
    }
  }
}
