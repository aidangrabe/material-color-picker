package com.aidangrabe.demo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;

import com.aidangrabe.materialcolorpicker.ColorPickerPreference;

/**
 * Created by aidan on 30/04/15.
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        ActionBarActivity activity = (ActionBarActivity) getActivity();

        ColorPickerPreference colorPref = (ColorPickerPreference) findPreference("bg_color");
        colorPref.setFragmentManager(activity.getSupportFragmentManager());

    }

}
