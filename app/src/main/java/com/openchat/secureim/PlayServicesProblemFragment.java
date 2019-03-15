package com.openchat.secureim;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.google.android.gms.common.GooglePlayServicesUtil;

public class PlayServicesProblemFragment extends DialogFragment {

  @Override
  public Dialog onCreateDialog(@NonNull Bundle bundle) {
    int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
    return GooglePlayServicesUtil.getErrorDialog(code, getActivity(), 9111);
  }

}
