package com.openchat.secureim.crypto;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECKeyPair;
import com.openchat.protocal.ecc.ECPrivateKey;
import com.openchat.imservice.crypto.MasterCipher;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.util.Base64;

import java.io.IOException;

public class IdentityKeyUtil {

  private static final String IDENTITY_PUBLIC_KEY_DJB_PREF  = "pref_identity_public_curve25519";
  private static final String IDENTITY_PRIVATE_KEY_DJB_PREF = "pref_identity_private_curve25519";
	
  public static boolean hasIdentityKey(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(MasterSecretUtil.PREFERENCES_NAME, 0);

    return
        preferences.contains(IDENTITY_PUBLIC_KEY_DJB_PREF) &&
        preferences.contains(IDENTITY_PRIVATE_KEY_DJB_PREF);
  }
	
  public static IdentityKey getIdentityKey(Context context) {
    if (!hasIdentityKey(context)) return null;
		
    try {
      byte[] publicKeyBytes = Base64.decode(retrieve(context, IDENTITY_PUBLIC_KEY_DJB_PREF));
      return new IdentityKey(publicKeyBytes, 0);
    } catch (IOException ioe) {
      Log.w("IdentityKeyUtil", ioe);
      return null;
    } catch (InvalidKeyException e) {
      Log.w("IdentityKeyUtil", e);
      return null;
    }
  }

  public static IdentityKeyPair getIdentityKeyPair(Context context,
                                                   MasterSecret masterSecret)
  {
    if (!hasIdentityKey(context))
      return null;

    try {
      MasterCipher masterCipher = new MasterCipher(masterSecret);
      IdentityKey  publicKey    = getIdentityKey(context);
      ECPrivateKey privateKey   = masterCipher.decryptKey(Base64.decode(retrieve(context, IDENTITY_PRIVATE_KEY_DJB_PREF)));

      return new IdentityKeyPair(publicKey, privateKey);
    } catch (IOException e) {
      throw new AssertionError(e);
    } catch (InvalidKeyException e) {
      throw new AssertionError(e);
    }
  }

  public static void generateIdentityKeys(Context context, MasterSecret masterSecret) {
    ECKeyPair    djbKeyPair     = Curve.generateKeyPair();

    MasterCipher masterCipher   = new MasterCipher(masterSecret);
    IdentityKey  djbIdentityKey = new IdentityKey(djbKeyPair.getPublicKey());
    byte[]       djbPrivateKey  = masterCipher.encryptKey(djbKeyPair.getPrivateKey());

    save(context, IDENTITY_PUBLIC_KEY_DJB_PREF, Base64.encodeBytes(djbIdentityKey.serialize()));
    save(context, IDENTITY_PRIVATE_KEY_DJB_PREF, Base64.encodeBytes(djbPrivateKey));
  }

  public static boolean hasCurve25519IdentityKeys(Context context) {
    return
        retrieve(context, IDENTITY_PUBLIC_KEY_DJB_PREF) != null &&
        retrieve(context, IDENTITY_PRIVATE_KEY_DJB_PREF) != null;
  }

  public static void generateCurve25519IdentityKeys(Context context, MasterSecret masterSecret) {
    MasterCipher masterCipher    = new MasterCipher(masterSecret);
    ECKeyPair    djbKeyPair      = Curve.generateKeyPair();
    IdentityKey  djbIdentityKey  = new IdentityKey(djbKeyPair.getPublicKey());
    byte[]       djbPrivateKey   = masterCipher.encryptKey(djbKeyPair.getPrivateKey());

    save(context, IDENTITY_PUBLIC_KEY_DJB_PREF, Base64.encodeBytes(djbIdentityKey.serialize()));
    save(context, IDENTITY_PRIVATE_KEY_DJB_PREF, Base64.encodeBytes(djbPrivateKey));
  }

  public static String retrieve(Context context, String key) {
    SharedPreferences preferences = context.getSharedPreferences(MasterSecretUtil.PREFERENCES_NAME, 0);
    return preferences.getString(key, null);
  }
	
  public static void save(Context context, String key, String value) {
    SharedPreferences preferences   = context.getSharedPreferences(MasterSecretUtil.PREFERENCES_NAME, 0);
    Editor preferencesEditor        = preferences.edit();
		
    preferencesEditor.putString(key, value);
    if (!preferencesEditor.commit()) throw new AssertionError("failed to save identity key/value to shared preferences");
  }
}
