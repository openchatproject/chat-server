package com.openchat.secureim;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.openchat.secureim.crypto.InvalidPassphraseException;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.util.DynamicLanguage;
import com.openchat.secureim.util.MemoryCleaner;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.Util;

public class PassphrasePromptActivity extends PassphraseActivity {

  private DynamicLanguage dynamicLanguage = new DynamicLanguage();

  private EditText passphraseText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    dynamicLanguage.onCreate(this);
    super.onCreate(savedInstanceState);

    setContentView(R.layout.prompt_passphrase_activity);
    initializeResources();
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicLanguage.onResume(this);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.log_submit, menu);
    super.onPrepareOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
    switch (item.getItemId()) {
    case R.id.menu_submit_debug_logs: handleLogSubmit(); return true;
    }

    return false;
  }

  private void handleLogSubmit() {
    Intent intent = new Intent(this, LogSubmitActivity.class);
    startActivity(intent);
  }

  private void handlePassphrase() {
    try {
      Editable text             = passphraseText.getText();
      String passphrase         = (text == null ? "" : text.toString());
      MasterSecret masterSecret = MasterSecretUtil.getMasterSecret(PassphrasePromptActivity.this, passphrase);

      MemoryCleaner.clean(passphrase);
      setMasterSecret(masterSecret);
    } catch (InvalidPassphraseException ipe) {
      passphraseText.setText("");
      Toast.makeText(this, R.string.PassphrasePromptActivity_invalid_passphrase_exclamation,
                     Toast.LENGTH_SHORT).show();
    }
  }

  private void initializeResources() {
    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    getSupportActionBar().setCustomView(R.layout.light_centered_app_title);
    mitigateAndroidTilingBug();

    ImageButton okButton = (ImageButton) findViewById(R.id.ok_button);
    passphraseText       = (EditText)    findViewById(R.id.passphrase_edit);
    SpannableString hint = new SpannableString(getString(R.string.PassphrasePromptActivity_enter_passphrase));

    hint.setSpan(new RelativeSizeSpan(0.8f), 0, hint.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    hint.setSpan(new TypefaceSpan("sans-serif"), 0, hint.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    hint.setSpan(new ForegroundColorSpan(0x66000000), 0, hint.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    passphraseText.setHint(hint);
    okButton.setOnClickListener(new OkButtonClickListener());
    passphraseText.setOnEditorActionListener(new PassphraseActionListener());
    passphraseText.setImeActionLabel(getString(R.string.prompt_passphrase_activity__unlock),
                                     EditorInfo.IME_ACTION_DONE);
  }

  private class PassphraseActionListener implements TextView.OnEditorActionListener {
    @Override
    public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent keyEvent) {
      if ((keyEvent == null && actionId == EditorInfo.IME_ACTION_DONE) ||
          (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
              (actionId == EditorInfo.IME_NULL)))
      {
        handlePassphrase();
        return true;
      } else if (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_UP &&
                 actionId == EditorInfo.IME_NULL)
      {
        return true;
      }

      return false;
    }
  }

  private void mitigateAndroidTilingBug() {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      Drawable actionBarBackground = getResources().getDrawable(R.drawable.background_pattern_repeat);
      Util.fixBackgroundRepeat(actionBarBackground);
      getSupportActionBar().setBackgroundDrawable(actionBarBackground);
      Util.fixBackgroundRepeat(findViewById(R.id.scroll_parent).getBackground());
    }
  }

  private class OkButtonClickListener implements OnClickListener {
    @Override
    public void onClick(View v) {
      handlePassphrase();
    }
  }

  @Override
  protected void cleanup() {
    this.passphraseText.setText("");
    System.gc();
  }
}
