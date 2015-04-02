package com.iit_mandi.android.hassle;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by pamu on 2/4/15.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.notification_time_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.notifications_key)));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference instanceof CheckBoxPreference) {
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
            checkBoxPreference.setSummary(((CheckBoxPreference) preference).isChecked() ? "off" : "on");
        } else {
            String value = newValue.toString();
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            editTextPreference.setSummary(Integer.valueOf(value) + " seconds");
        }

        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        if (preference instanceof EditTextPreference)
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        else
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), false));
    }
}