package com.openchat.secureim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.openchat.secureim.crypto.IdentityKeyParcelable;
import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceSessionStore;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.protocal.OpenchatAddress;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.state.SessionRecord;
import com.openchat.protocal.state.SessionStore;
import com.openchat.imservice.api.push.OpenchatServiceAddress;

public class VerifyIdentityActivity extends KeyScanningActivity {

  private Recipient    recipient;
  private MasterSecret masterSecret;

  private TextView localIdentityFingerprint;
  private TextView remoteIdentityFingerprint;

  @Override
  protected void onCreate(Bundle state, @NonNull MasterSecret masterSecret) {
    this.masterSecret = masterSecret;
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(R.string.AndroidManifest__verify_identity);

    setContentView(R.layout.verify_identity_activity);

    this.localIdentityFingerprint  = (TextView)findViewById(R.id.you_read);
    this.remoteIdentityFingerprint = (TextView)findViewById(R.id.friend_reads);
  }

  @Override
  public void onResume() {
    super.onResume();

    this.recipient = RecipientFactory.getRecipientForId(this, this.getIntent().getLongExtra("recipient", -1), true);

    initializeFingerprints();
  }

  private void initializeFingerprints() {
    if (!IdentityKeyUtil.hasIdentityKey(this)) {
      localIdentityFingerprint.setText(R.string.VerifyIdentityActivity_you_do_not_have_an_identity_key);
      return;
    }

    localIdentityFingerprint.setText(IdentityKeyUtil.getIdentityKey(this).getFingerprint());

    IdentityKey identityKey = getRemoteIdentityKey(masterSecret, recipient);

    if (identityKey == null) {
      remoteIdentityFingerprint.setText(R.string.VerifyIdentityActivity_recipient_has_no_identity_key);
    } else {
      remoteIdentityFingerprint.setText(identityKey.getFingerprint());
    }
  }

  @Override
  protected void initiateDisplay() {
    if (!IdentityKeyUtil.hasIdentityKey(this)) {
      Toast.makeText(this,
                     R.string.VerifyIdentityActivity_you_don_t_have_an_identity_key_exclamation,
                     Toast.LENGTH_LONG).show();
      return;
    }

    super.initiateDisplay();
  }

  @Override
  protected void initiateScan() {
    IdentityKey identityKey = getRemoteIdentityKey(masterSecret, recipient);

    if (identityKey == null) {
      Toast.makeText(this, R.string.VerifyIdentityActivity_recipient_has_no_identity_key_exclamation,
                     Toast.LENGTH_LONG).show();
    } else {
      super.initiateScan();
    }
  }

  @Override
  protected String getScanString() {
    return getString(R.string.VerifyIdentityActivity_scan_their_key_to_compare);
  }

  @Override
  protected String getDisplayString() {
    return getString(R.string.VerifyIdentityActivity_get_my_key_scanned);
  }

  @Override
  protected IdentityKey getIdentityKeyToCompare() {
    return getRemoteIdentityKey(masterSecret, recipient);
  }

  @Override
  protected IdentityKey getIdentityKeyToDisplay() {
    return IdentityKeyUtil.getIdentityKey(this);
  }

  @Override
  protected String getNotVerifiedMessage() {
    return getString(R.string.VerifyIdentityActivity_warning_the_scanned_key_does_not_match_please_check_the_fingerprint_text_carefully);
  }

  @Override
  protected String getNotVerifiedTitle() {
    return getString(R.string.VerifyIdentityActivity_not_verified_exclamation);
  }

  @Override
  protected String getVerifiedMessage() {
    return getString(R.string.VerifyIdentityActivity_their_key_is_correct_it_is_also_necessary_to_verify_your_key_with_them_as_well);
  }

  @Override
  protected String getVerifiedTitle() {
    return getString(R.string.VerifyIdentityActivity_verified_exclamation);
  }

  private @Nullable IdentityKey getRemoteIdentityKey(MasterSecret masterSecret, Recipient recipient) {
    IdentityKeyParcelable identityKeyParcelable = getIntent().getParcelableExtra("remote_identity");

    if (identityKeyParcelable != null) {
      return identityKeyParcelable.get();
    }

    SessionStore   sessionStore   = new OpenchatServiceSessionStore(this, masterSecret);
    OpenchatAddress axolotlAddress = new OpenchatAddress(recipient.getNumber(), OpenchatServiceAddress.DEFAULT_DEVICE_ID);
    SessionRecord  record         = sessionStore.loadSession(axolotlAddress);

    if (record == null) {
      return null;
    }

    return record.getSessionState().getRemoteIdentityKey();
  }
}
