package com.openchat.secureim;

import android.app.Application;

import com.openchat.secureim.crypto.PRNGFixes;

public class ApplicationListener extends Application {

  @Override
  public void onCreate() {
    PRNGFixes.apply();
  }

}
