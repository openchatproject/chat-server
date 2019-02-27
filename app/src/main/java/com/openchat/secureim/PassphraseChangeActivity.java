package com.openchat.secureim;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.openchat.secureim.crypto.InvalidPassphraseException;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.util.MemoryCleaner;
import com.openchat.secureim.util.OpenchatServicePreferences;

public class PassphraseChangeActivity extends PassphraseActivity {

  private EditText originalPassphrase;
  private EditText newPassphrase;
  private EditText repeatPassphrase;
  private TextView originalPassphraseLabel;
  private Button   okButton;
  private Button   cancelButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.change_passphrase_activity);

    initializeResources();
  }

  private void initializeResources() {
    this.originalPassphraseLabel = (TextView) findViewById(R.id.old_passphrase_label);
    this.originalPassphrase      = (EditText) findViewById(R.id.old_passphrase      );
    this.newPassphrase           = (EditText) findViewById(R.id.new_passphrase      );
    this.repeatPassphrase        = (EditText) findViewById(R.id.repeat_passphrase   );

    this.okButton                = (Button  ) findViewById(R.id.ok_button           );
    this.cancelButton            = (Button  ) findViewById(R.id.cancel_button       );

    this.okButton.setOnClickListener(new OkButtonClickListener());
    this.cancelButton.setOnClickListener(new CancelButtonClickListener());

    if (OpenchatServicePreferences.isPasswordDisabled(this)) {
      this.originalPassphrase.setVisibility(View.GONE);
      this.originalPassphraseLabel.setVisibility(View.GONE);
    } else {
      this.originalPassphrase.setVisibility(View.VISIBLE);
      this.originalPassphraseLabel.setVisibility(View.VISIBLE);
    }
  }

  private void verifyAndSavePassphrases() {
    Editable originalText = this.originalPassphrase.getText();
    Editable newText      = this.newPassphrase.getText();
    Editable repeatText   = this.repeatPassphrase.getText();

    String original         = (originalText == null ? "" : originalText.toString());
    String passphrase       = (newText == null ? "" : newText.toString());
    String passphraseRepeat = (repeatText == null ? "" : repeatText.toString());

    if (OpenchatServicePreferences.isPasswordDisabled(this)) {
      original = MasterSecretUtil.UNENCRYPTED_PASSPHRASE;
    }

    try {
      if (!passphrase.equals(passphraseRepeat)) {
        Toast.makeText(getApplicationContext(),
                       R.string.PassphraseChangeActivity_passphrases_dont_match_exclamation,
                       Toast.LENGTH_SHORT).show();
        this.newPassphrase.setText("");
        this.repeatPassphrase.setText("");
      } else {
        MasterSecret masterSecret = MasterSecretUtil.changeMasterSecretPassphrase(this, original, passphrase);
        OpenchatServicePreferences.setPasswordDisabled(this, false);

        MemoryCleaner.clean(original);
        MemoryCleaner.clean(passphrase);
        MemoryCleaner.clean(passphraseRepeat);

        setMasterSecret(masterSecret);
      }
    } catch (InvalidPassphraseException e) {
      Toast.makeText(this, R.string.PassphraseChangeActivity_incorrect_old_passphrase_exclamation,
                     Toast.LENGTH_LONG).show();
      this.originalPassphrase.setText("");
    }
  }

  private class CancelButtonClickListener implements OnClickListener {
    public void onClick(View v) {
      finish();
    }
  }

  private class OkButtonClickListener implements OnClickListener {
    public void onClick(View v) {
      verifyAndSavePassphrases();
    }
  }

  @Override
  protected void cleanup() {
    this.originalPassphrase = null;
    this.newPassphrase      = null;
    this.repeatPassphrase   = null;

    System.gc();
  }
}
