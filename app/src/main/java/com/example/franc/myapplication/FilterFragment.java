package com.example.franc.myapplication;

/**
 * Created by franc on 11/12/2017.
 */

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
public class FilterFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);
    }
}