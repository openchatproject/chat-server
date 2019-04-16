package com.openchat.secureim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.crypto.IdentityKeyParcelable;
import com.openchat.secureim.crypto.MasterSecret;

public class ViewLocalIdentityActivity extends ViewIdentityActivity {

  @Override
  protected void onCreate(Bundle icicle, @NonNull MasterSecret masterSecret) {
    getIntent().putExtra(ViewIdentityActivity.IDENTITY_KEY,
                         new IdentityKeyParcelable(IdentityKeyUtil.getIdentityKey(this)));
    getIntent().putExtra(ViewIdentityActivity.TITLE,
                         getString(R.string.ViewIdentityActivity_my_identity_fingerprint));
    super.onCreate(icicle, masterSecret);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);

    MenuInflater inflater = this.getMenuInflater();
    inflater.inflate(R.menu.local_identity, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
        case android.R.id.home:finish(); return true;
    }

    return false;
  }
}
