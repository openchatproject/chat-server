package com.openchat.secureim.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.openchat.secureim.ApplicationPreferencesActivity;
import com.openchat.secureim.R;
import com.openchat.secureim.util.OpenchatServicePreferences;

public class PassphraseTimeoutPreference extends DialogPreference {

  private Spinner scaleSpinner;
  private SeekBar seekBar;
  private TextView timeoutText;

  public PassphraseTimeoutPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.setDialogLayoutResource(R.layout.passphrase_timeout_dialog);
    this.setPositiveButtonText(android.R.string.ok);
    this.setNegativeButtonText(android.R.string.cancel);
  }

  @Override
  protected View onCreateDialogView() {
    View dialog       = super.onCreateDialogView();
    this.scaleSpinner = (Spinner)dialog.findViewById(R.id.scale);
    this.seekBar      = (SeekBar)dialog.findViewById(R.id.seekbar);
    this.timeoutText  = (TextView)dialog.findViewById(R.id.timeout_text);

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
      this.timeoutText.setTextColor(Color.parseColor("#cccccc"));
    }

    initializeDefaults();
    initializeListeners();

    return dialog;
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    if (which == DialogInterface.BUTTON_POSITIVE) {
      int interval;

      if (scaleSpinner.getSelectedItemPosition() == 0) {
        interval = Math.max(seekBar.getProgress(), 1);
      } else {
        interval = Math.max(seekBar.getProgress(), 1) * 60;
      }

      OpenchatServicePreferences.setPassphraseTimeoutInterval(getContext(), interval);
    }

    super.onClick(dialog, which);
  }

  private void initializeDefaults() {
    int timeout = OpenchatServicePreferences.getPassphraseTimeoutInterval(getContext());

    if (timeout > 60) {
      scaleSpinner.setSelection(1);
      seekBar.setMax(24);
      seekBar.setProgress(timeout / 60);
      timeoutText.setText((timeout / 60) + "");
    } else {
      scaleSpinner.setSelection(0);
      seekBar.setMax(60);
      seekBar.setProgress(timeout);
      timeoutText.setText(timeout + "");
    }
  }

  private void initializeListeners() {
    this.seekBar.setOnSeekBarChangeListener(new SeekbarChangedListener());
    this.scaleSpinner.setOnItemSelectedListener(new ScaleSelectedListener());
  }

  private class ScaleSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long selected) {
      if (selected == 0) {
        seekBar.setMax(60);
      } else {
        seekBar.setMax(24);
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
  }

  private class SeekbarChangedListener implements SeekBar.OnSeekBarChangeListener {

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      if (progress < 1)
        progress = 1;

      timeoutText.setText(progress +"");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

  }

}
