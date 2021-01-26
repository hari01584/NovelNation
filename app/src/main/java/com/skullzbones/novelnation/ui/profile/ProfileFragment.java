package com.skullzbones.novelnation.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.skullzbones.novelnation.R;
import com.skullzbones.novelnation.Utility.Utility;

public class ProfileFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
       // setPreferencesFromResource(R.xml.preference_settings, rootKey);
        addPreferencesFromResource(R.xml.preference_settings);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_gallery_name)));

        // feedback preference click listener
        Preference myPref = findPreference(getString(R.string.key_send_feedback));
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                sendFeedback(getActivity());
                return true;
            }
        });
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                if(index>=0) {
                    String value = listPreference.getEntries()[index].toString();
                    if (listPreference.getKey().equals("key_gallery_name")) {
                        preference.setSummary(value);
                    }
                    else if (listPreference.getKey().equals("key_upload_quality")) {
                        preference.setSummary(value);
                    }
                }
            }
            /*else if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals("key_gallery_name")) {
                    // update the changed gallery name to summary filed
                    preference.setSummary(stringValue);
                }
            }*/ else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hari01584@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }


}