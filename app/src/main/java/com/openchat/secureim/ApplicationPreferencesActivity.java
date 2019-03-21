package com.openchat.secureim;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.preferences.AdvancedPreferenceFragment;
import com.openchat.secureim.preferences.AppProtectionPreferenceFragment;
import com.openchat.secureim.preferences.AppearancePreferenceFragment;
import com.openchat.secureim.preferences.NotificationsPreferenceFragment;
import com.openchat.secureim.preferences.SmsMmsPreferenceFragment;
import com.openchat.secureim.preferences.StoragePreferenceFragment;
import com.openchat.secureim.push.OpenchatServiceCommunicationFactory;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.secureim.util.Dialogs;
import com.openchat.secureim.util.DynamicLanguage;
import com.openchat.secureim.util.DynamicTheme;
import com.openchat.secureim.util.MemoryCleaner;
import com.openchat.secureim.util.ProgressDialogAsyncTask;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.OpenchatServiceAccountManager;
import com.openchat.imservice.api.push.exceptions.AuthorizationFailedException;

import java.io.IOException;

public class ApplicationPreferencesActivity extends PassphraseRequiredActionBarActivity
    implements SharedPreferences.OnSharedPreferenceChangeListener
{
  private static final String TAG = ApplicationPreferencesActivity.class.getSimpleName();

  private static final String PREFERENCE_CATEGORY_SMS_MMS        = "preference_category_sms_mms";
  private static final String PREFERENCE_CATEGORY_NOTIFICATIONS  = "preference_category_notifications";
  private static final String PREFERENCE_CATEGORY_APP_PROTECTION = "preference_category_app_protection";
  private static final String PREFERENCE_CATEGORY_APPEARANCE     = "preference_category_appearance";
  private static final String PREFERENCE_CATEGORY_STORAGE        = "preference_category_storage";
  private static final String PREFERENCE_CATEGORY_ADVANCED       = "preference_category_advanced";

  private static final String PUSH_MESSAGING_PREF = "pref_toggle_push_messaging";

  private final DynamicTheme    dynamicTheme    = new DynamicTheme();
  private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

  @Override
  protected void onCreate(Bundle icicle) {
    dynamicTheme.onCreate(this);
    dynamicLanguage.onCreate(this);
    super.onCreate(icicle);

    this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Fragment            fragment            = new ApplicationPreferenceFragment();
    FragmentManager     fragmentManager     = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(android.R.id.content, fragment);
    fragmentTransaction.commit();
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
    dynamicLanguage.onResume(this);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    Fragment fragment = getSupportFragmentManager().findFragmentById(android.R.id.content);
    fragment.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public boolean onSupportNavigateUp() {
    FragmentManager fragmentManager = getSupportFragmentManager();
    if (fragmentManager.getBackStackEntryCount() > 0) {
      fragmentManager.popBackStack();
    } else {
      Intent intent = new Intent(this, ConversationListActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      finish();
    }
    return true;
  }

  @Override
  public void onDestroy() {
    MemoryCleaner.clean((MasterSecret) getIntent().getParcelableExtra("master_secret"));
    super.onDestroy();
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals(OpenchatServicePreferences.THEME_PREF)) {
      dynamicTheme.onResume(this);
    } else if (key.equals(OpenchatServicePreferences.LANGUAGE_PREF)) {
      dynamicLanguage.onResume(this);

      Intent intent = new Intent(this, KeyCachingService.class);
      intent.setAction(KeyCachingService.LOCALE_CHANGE_EVENT);
      startService(intent);
    }
  }

  public static class ApplicationPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle icicle) {
      super.onCreate(icicle);

      addPreferencesFromResource(R.xml.preferences);

      initializePushMessagingToggle();

      this.findPreference(PREFERENCE_CATEGORY_SMS_MMS)
        .setOnPreferenceClickListener(new CategoryClickListener(PREFERENCE_CATEGORY_SMS_MMS));
      this.findPreference(PREFERENCE_CATEGORY_NOTIFICATIONS)
        .setOnPreferenceClickListener(new CategoryClickListener(PREFERENCE_CATEGORY_NOTIFICATIONS));
      this.findPreference(PREFERENCE_CATEGORY_APP_PROTECTION)
        .setOnPreferenceClickListener(new CategoryClickListener(PREFERENCE_CATEGORY_APP_PROTECTION));
      this.findPreference(PREFERENCE_CATEGORY_APPEARANCE)
        .setOnPreferenceClickListener(new CategoryClickListener(PREFERENCE_CATEGORY_APPEARANCE));
      this.findPreference(PREFERENCE_CATEGORY_STORAGE)
        .setOnPreferenceClickListener(new CategoryClickListener(PREFERENCE_CATEGORY_STORAGE));
      this.findPreference(PREFERENCE_CATEGORY_ADVANCED)
        .setOnPreferenceClickListener(new CategoryClickListener(PREFERENCE_CATEGORY_ADVANCED));
    }

    @Override
    public void onResume() {
      super.onResume();
      ((ApplicationPreferencesActivity) getActivity()).getSupportActionBar().setTitle(R.string.text_secure_normal__menu_settings);
      setCategorySummaries();
    }

    private void setCategorySummaries() {
      this.findPreference(PREFERENCE_CATEGORY_SMS_MMS)
          .setSummary(SmsMmsPreferenceFragment.getSummary(getActivity()));
      this.findPreference(PREFERENCE_CATEGORY_NOTIFICATIONS)
          .setSummary(NotificationsPreferenceFragment.getSummary(getActivity()));
      this.findPreference(PREFERENCE_CATEGORY_APP_PROTECTION)
          .setSummary(AppProtectionPreferenceFragment.getSummary(getActivity()));
      this.findPreference(PREFERENCE_CATEGORY_APPEARANCE)
          .setSummary(AppearancePreferenceFragment.getSummary(getActivity()));
      this.findPreference(PREFERENCE_CATEGORY_STORAGE)
          .setSummary(StoragePreferenceFragment.getSummary(getActivity()));
    }

    private class CategoryClickListener implements Preference.OnPreferenceClickListener {
      private String category;

      public CategoryClickListener(String category) {
        this.category = category;
      }

      @Override
      public boolean onPreferenceClick(Preference preference) {
        Fragment fragment;

        switch (category) {
        case PREFERENCE_CATEGORY_SMS_MMS:
          fragment = new SmsMmsPreferenceFragment();
          break;
        case PREFERENCE_CATEGORY_NOTIFICATIONS:
          fragment = new NotificationsPreferenceFragment();
          break;
        case PREFERENCE_CATEGORY_APP_PROTECTION:
          fragment = new AppProtectionPreferenceFragment();
          break;
        case PREFERENCE_CATEGORY_APPEARANCE:
          fragment = new AppearancePreferenceFragment();
          break;
        case PREFERENCE_CATEGORY_STORAGE:
          fragment = new StoragePreferenceFragment();
          break;
        case PREFERENCE_CATEGORY_ADVANCED:
          fragment = new AdvancedPreferenceFragment();
          break;
        default:
          throw new AssertionError();
        }

        FragmentManager     fragmentManager     = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        return true;
      }
    }

    private void initializePushMessagingToggle() {
      CheckBoxPreference preference = (CheckBoxPreference)this.findPreference(PUSH_MESSAGING_PREF);
      preference.setChecked(OpenchatServicePreferences.isPushRegistered(getActivity()));
      preference.setOnPreferenceChangeListener(new PushMessagingClickListener());
    }

    private class PushMessagingClickListener implements Preference.OnPreferenceChangeListener {
      private static final int SUCCESS       = 0;
      private static final int NETWORK_ERROR = 1;

      private class DisablePushMessagesTask extends ProgressDialogAsyncTask<Void, Void, Integer> {
        private final CheckBoxPreference checkBoxPreference;

        public DisablePushMessagesTask(final CheckBoxPreference checkBoxPreference) {
          super(getActivity(), R.string.ApplicationPreferencesActivity_unregistering, R.string.ApplicationPreferencesActivity_unregistering_for_data_based_communication);
          this.checkBoxPreference = checkBoxPreference;
        }

        @Override
        protected void onPostExecute(Integer result) {
          super.onPostExecute(result);
          switch (result) {
          case NETWORK_ERROR:
            Toast.makeText(getActivity(),
                           R.string.ApplicationPreferencesActivity_error_connecting_to_server,
                           Toast.LENGTH_LONG).show();
            break;
          case SUCCESS:
            checkBoxPreference.setChecked(false);
            OpenchatServicePreferences.setPushRegistered(getActivity(), false);
            break;
          }
        }

        @Override
        protected Integer doInBackground(Void... params) {
          try {
            Context                  context        = getActivity();
            OpenchatServiceAccountManager accountManager = OpenchatServiceCommunicationFactory.createManager(context);

            accountManager.setGcmId(Optional.<String>absent());
            GoogleCloudMessaging.getInstance(context).unregister();

            return SUCCESS;
          } catch (AuthorizationFailedException afe) {
            Log.w(TAG, afe);
            return SUCCESS;
          } catch (IOException ioe) {
            Log.w(TAG, ioe);
            return NETWORK_ERROR;
          }
        }
      }

      @Override
      public boolean onPreferenceChange(final Preference preference, Object newValue) {
        if (((CheckBoxPreference)preference).isChecked()) {
          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setIcon(Dialogs.resolveIcon(getActivity(), R.attr.dialog_info_icon));
          builder.setTitle(R.string.ApplicationPreferencesActivity_disable_push_messages);
          builder.setMessage(R.string.ApplicationPreferencesActivity_this_will_disable_push_messages);
          builder.setNegativeButton(android.R.string.cancel, null);
          builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              new DisablePushMessagesTask((CheckBoxPreference)preference).execute();
            }
          });
          builder.show();
        } else {
          Intent nextIntent = new Intent(getActivity(), ApplicationPreferencesActivity.class);
          nextIntent.putExtra("master_secret", getActivity().getIntent().getParcelableExtra("master_secret"));

          Intent intent = new Intent(getActivity(), RegistrationActivity.class);
          intent.putExtra("cancel_button", true);
          intent.putExtra("next_intent", nextIntent);
          intent.putExtra("master_secret", getActivity().getIntent().getParcelableExtra("master_secret"));
          startActivity(intent);
        }

        return false;
      }
    }

    
    @SuppressWarnings("deprecation")
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
    {
      super.onPreferenceTreeClick(preferenceScreen, preference);
      if (preference != null && preference instanceof PreferenceScreen && ((PreferenceScreen)preference).getDialog() != null)
        ((PreferenceScreen) preference).getDialog().getWindow().getDecorView().setBackgroundDrawable(getActivity().getWindow().getDecorView().getBackground().getConstantState().newDrawable());
      return false;
    }
  }
}
