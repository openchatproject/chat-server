package com.openchat.secureim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.openchat.protocal.IdentityKey;
import com.openchat.imservice.util.Base64;
import com.openchat.secureim.util.Dialogs;
import com.openchat.secureim.util.DynamicTheme;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.openchat.imservice.zxing.integration.IntentIntegrator;
import com.openchat.imservice.zxing.integration.IntentResult;

public abstract class KeyScanningActivity extends PassphraseRequiredSherlockActivity {

  private final DynamicTheme dynamicTheme = new DynamicTheme();

  @Override
  protected void onCreate(Bundle bundle) {
    dynamicTheme.onCreate(this);
    super.onCreate(bundle);
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);

    MenuInflater inflater = this.getSupportMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.key_scanning, menu);

    menu.findItem(R.id.menu_scan).setTitle(getScanString());
    menu.findItem(R.id.menu_get_scanned).setTitle(getDisplayString());

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
    case R.id.menu_scan:        initiateScan();    return true;
    case R.id.menu_get_scanned: initiateDisplay(); return true;
    case android.R.id.home:     finish();          return true;
    }

    return false;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

    if ((scanResult != null) && (scanResult.getContents() != null)) {
      String data = scanResult.getContents();

      if (data.equals(Base64.encodeBytes(getIdentityKeyToCompare().serialize()))) {
        Dialogs.showInfoDialog(this, getVerifiedTitle(), getVerifiedMessage());
      } else {
        Dialogs.showAlertDialog(this, getNotVerifiedTitle(), getNotVerifiedMessage());
      }
    } else {
      Toast.makeText(this, R.string.KeyScanningActivity_no_scanned_key_found_exclamation,
                     Toast.LENGTH_LONG).show();
    }
  }

  protected void initiateScan() {
    IntentIntegrator.initiateScan(this);
  }

  protected void initiateDisplay() {
    IntentIntegrator.shareText(this, Base64.encodeBytes(getIdentityKeyToDisplay().serialize()));
  }

  protected abstract String getScanString();
  protected abstract String getDisplayString();

  protected abstract String getNotVerifiedTitle();
  protected abstract String getNotVerifiedMessage();

  protected abstract IdentityKey getIdentityKeyToCompare();
  protected abstract IdentityKey getIdentityKeyToDisplay();

  protected abstract String getVerifiedTitle();
  protected abstract String getVerifiedMessage();

}
