package com.openchat.secureim.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.openchat.secureim.ApplicationPreferencesActivity;
import com.openchat.secureim.R;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.secureim.util.Dialogs;

public class LedBlinkPatternListPreference extends ListPreference implements OnSeekBarChangeListener {

  private Context context;
  private SeekBar seekBarOn;
  private SeekBar seekBarOff;

  private TextView seekBarOnLabel;
  private TextView seekBarOffLabel;

  private boolean dialogInProgress;

  public LedBlinkPatternListPreference(Context context) {
    super(context);
    this.context = context;
  }

  public LedBlinkPatternListPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  @Override
  protected void onDialogClosed(boolean positiveResult) {
    super.onDialogClosed(positiveResult);

    if (positiveResult) {
      String blinkPattern = OpenchatServicePreferences.getNotificationLedPattern(context);
      if (blinkPattern.equals("custom")) showDialog();
    }
  }

  private void initializeSeekBarValues() {
    String patternString  = OpenchatServicePreferences.getNotificationLedPatternCustom(context);
    String[] patternArray = patternString.split(",");
    seekBarOn.setProgress(Integer.parseInt(patternArray[0]));
    seekBarOff.setProgress(Integer.parseInt(patternArray[1]));
  }

  private void initializeDialog(View view) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setIcon(Dialogs.resolveIcon(context, R.attr.dialog_info_icon));
    builder.setTitle(R.string.preferences__pref_led_blink_custom_pattern_title);
    builder.setView(view);
    builder.setOnCancelListener(new CustomDialogCancelListener());
    builder.setNegativeButton(android.R.string.cancel, new CustomDialogCancelListener());
    builder.setPositiveButton(android.R.string.ok, new CustomDialogClickListener());
    builder.setInverseBackgroundForced(true);
    builder.show();
  }

  private void showDialog() {
    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view               = inflater.inflate(R.layout.led_pattern_dialog, null);

    this.seekBarOn       = (SeekBar)view.findViewById(R.id.SeekBarOn);
    this.seekBarOff      = (SeekBar)view.findViewById(R.id.SeekBarOff);
    this.seekBarOnLabel  = (TextView)view.findViewById(R.id.SeekBarOnMsLabel);
    this.seekBarOffLabel = (TextView)view.findViewById(R.id.SeekBarOffMsLabel);

    this.seekBarOn.setOnSeekBarChangeListener(this);
    this.seekBarOff.setOnSeekBarChangeListener(this);

    initializeSeekBarValues();
    initializeDialog(view);
    
    dialogInProgress = true;
  }

  @Override
  protected void onRestoreInstanceState(Parcelable state) {
    super.onRestoreInstanceState(state);
    if (dialogInProgress) {
      showDialog();
    }
  }

  @Override
  protected View onCreateDialogView() {
    dialogInProgress = false;
    return super.onCreateDialogView();
  }

  public void onProgressChanged(SeekBar seekbar, int progress, boolean fromTouch) {
    if (seekbar.equals(seekBarOn)) {
      seekBarOnLabel.setText(Integer.toString(progress));
    } else if (seekbar.equals(seekBarOff)) {
      seekBarOffLabel.setText(Integer.toString(progress));
    }
  }

  public void onStartTrackingTouch(SeekBar seekBar) {
  }

  public void onStopTrackingTouch(SeekBar seekBar) {
  }

  private class CustomDialogCancelListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    public void onClick(DialogInterface dialog, int which) {
      dialogInProgress = false;
    }

    public void onCancel(DialogInterface dialog) {
      dialogInProgress = false;
    }
  }

  private class CustomDialogClickListener implements DialogInterface.OnClickListener {

    public void onClick(DialogInterface dialog, int which) {
      String pattern   = seekBarOnLabel.getText() + "," + seekBarOffLabel.getText();
      dialogInProgress = false;

      OpenchatServicePreferences.setNotificationLedPatternCustom(context, pattern);
      Toast.makeText(context, R.string.preferences__pref_led_blink_custom_pattern_set, Toast.LENGTH_LONG).show();
    }

  }

}
