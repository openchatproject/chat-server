package com.openchat.secureim;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.openchat.secureim.components.AnimatingToggle;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.DirectoryHelper;
import com.openchat.secureim.util.DynamicLanguage;
import com.openchat.secureim.util.DynamicNoActionBarTheme;
import com.openchat.secureim.util.DynamicTheme;

import java.io.IOException;
import java.lang.ref.WeakReference;

public abstract class ContactSelectionActivity extends PassphraseRequiredActionBarActivity
                                               implements SwipeRefreshLayout.OnRefreshListener,
                                                          ContactSelectionListFragment.OnContactSelectedListener
{
  private static final String TAG             = ContactSelectionActivity.class.getSimpleName();
  public  final static String PUSH_ONLY_EXTRA = "push_only";

  private final DynamicTheme    dynamicTheme    = new DynamicNoActionBarTheme();
  private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

  protected ContactSelectionListFragment contactsFragment;

  private   Toolbar         toolbar;
  private   EditText        searchText;
  private   AnimatingToggle toggle;
  protected ImageView       action;
  private   ImageView       keyboardToggle;
  private   ImageView       dialpadToggle;
  private   ImageView       clearToggle;

  @Override
  protected void onPreCreate() {
    dynamicTheme.onCreate(this);
    dynamicLanguage.onCreate(this);
  }

  @Override
  protected void onCreate(Bundle icicle, @NonNull MasterSecret masterSecret) {
    setContentView(R.layout.contact_selection_activity);

    initializeToolbar();
    initializeResources();
    initializeSearch();
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
    dynamicLanguage.onResume(this);
  }

  private void initializeToolbar() {
    this.toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  private void initializeResources() {
    this.action         = (ImageView) findViewById(R.id.action_icon);
    this.searchText     = (EditText) findViewById(R.id.search_view);
    this.toggle         = (AnimatingToggle) findViewById(R.id.button_toggle);
    this.keyboardToggle = (ImageView) findViewById(R.id.search_keyboard);
    this.dialpadToggle  = (ImageView) findViewById(R.id.search_dialpad);
    this.clearToggle    = (ImageView) findViewById(R.id.search_clear);

    contactsFragment = (ContactSelectionListFragment) getSupportFragmentManager().findFragmentById(R.id.contact_selection_list_fragment);
    contactsFragment.setOnContactSelectedListener(this);
    contactsFragment.setOnRefreshListener(this);

    this.keyboardToggle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        searchText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        toggle.display(dialpadToggle);
      }
    });

    this.dialpadToggle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        searchText.setInputType(InputType.TYPE_CLASS_PHONE);
        toggle.display(keyboardToggle);
      }
    });

    this.clearToggle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        searchText.setText("");

        if (SearchUtil.isTextInput(searchText)) toggle.display(dialpadToggle);
        else                                    toggle.display(keyboardToggle);
      }
    });
  }

  private void initializeSearch() {
    this.searchText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if      (!SearchUtil.isEmpty(searchText))     toggle.display(clearToggle);
        else if (SearchUtil.isTextInput(searchText))  toggle.display(dialpadToggle);
        else if (SearchUtil.isPhoneInput(searchText)) toggle.display(keyboardToggle);

        contactsFragment.setQueryFilter(searchText.getText().toString());
      }
    });
  }

  @Override
  public void onRefresh() {
    new RefreshDirectoryTask(this).execute(getApplicationContext());
  }

  @Override
  public void onContactSelected(String number) {}

  private static class SearchUtil {

    public static boolean isTextInput(EditText editText) {
      return (editText.getInputType() & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT;
    }

    public static boolean isPhoneInput(EditText editText) {
      return (editText.getInputType() & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_PHONE;
    }

    public static boolean isEmpty(EditText editText) {
      return editText.getText().length() <= 0;
    }
  }

  private static class RefreshDirectoryTask extends AsyncTask<Context, Void, Void> {

    private final WeakReference<ContactSelectionActivity> activity;

    private RefreshDirectoryTask(ContactSelectionActivity activity) {
      this.activity = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(Context... params) {
      try {
        DirectoryHelper.refreshDirectory(params[0]);
      } catch (IOException e) {
        Log.w(TAG, e);
      }

      return null;
    }

    @Override
    protected void onPostExecute(Void result) {
      ContactSelectionActivity activity = this.activity.get();

      if (activity != null && !activity.isFinishing()) {
        activity.searchText.setText("");
        activity.contactsFragment.resetQueryFilter();
      }
    }
  }
}
