package com.aidangrabe.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by aidan on 30/04/15.
 *
 */
public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);

        SettingsFragment settingsFragment = new SettingsFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, settingsFragment, "fragment")
                .commit();

    }

}
