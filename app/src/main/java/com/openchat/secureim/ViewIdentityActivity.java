package com.openchat.secureim;

import android.os.Bundle;
import android.widget.TextView;

import com.openchat.protocal.IdentityKey;
import com.openchat.imservice.crypto.IdentityKeyParcelable;

public class ViewIdentityActivity extends KeyScanningActivity {

  public static final String IDENTITY_KEY = "identity_key";
  public static final String TITLE        = "title";

  private TextView    identityFingerprint;
  private IdentityKey identityKey;

  @Override
  public void onCreate(Bundle state) {
    super.onCreate(state);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setContentView(R.layout.view_identity_activity);

    initialize();
  }

  protected void initialize() {
    initializeResources();
    initializeFingerprint();
  }

  private void initializeFingerprint() {
    if (identityKey == null) {
      identityFingerprint.setText(R.string.ViewIdentityActivity_you_do_not_have_an_identity_key);
    } else {
      identityFingerprint.setText(identityKey.getFingerprint());
    }
  }

  private void initializeResources() {
    IdentityKeyParcelable identityKeyParcelable = getIntent().getParcelableExtra(IDENTITY_KEY);

    if (identityKeyParcelable == null) {
      throw new AssertionError("No identity key!");
    }

    this.identityKey         = identityKeyParcelable.get();
    this.identityFingerprint = (TextView)findViewById(R.id.identity_fingerprint);
    String title             = getIntent().getStringExtra(TITLE);

    if (title != null) {
      getSupportActionBar().setTitle(getIntent().getStringExtra(TITLE));
    }
  }

  @Override
  protected String getScanString() {
    return getString(R.string.ViewIdentityActivity_scan_to_compare);
  }

  @Override
  protected String getDisplayString() {
    return getString(R.string.ViewIdentityActivity_get_scanned_to_compare);
  }

  @Override
  protected IdentityKey getIdentityKeyToCompare() {
    return identityKey;
  }

  @Override
  protected IdentityKey getIdentityKeyToDisplay() {
    return identityKey;
  }

  @Override
  protected String getNotVerifiedMessage() {
    return  getString(R.string.ViewIdentityActivity_warning_the_scanned_key_does_not_match_exclamation);
  }

  @Override
  protected String getNotVerifiedTitle() {
    return getString(R.string.ViewIdentityActivity_not_verified_exclamation);
  }

  @Override
  protected String getVerifiedMessage() {
    return getString(R.string.ViewIdentityActivity_the_scanned_key_matches_exclamation);
  }

  @Override
  protected String getVerifiedTitle() {
    return getString(R.string.ViewIdentityActivity_verified_exclamation);
  }
}
