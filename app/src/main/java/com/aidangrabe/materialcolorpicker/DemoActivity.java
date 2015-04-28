package com.aidangrabe.materialcolorpicker;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by aidan on 23/04/15.
 *
 */
public class DemoActivity extends ActionBarActivity implements ColorPickerDialogFragment.ColorSelectedListener {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mButton = new Button(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogFragment dialog = new ColorPickerDialogFragment();
                dialog.setOnColorSelectedListener(DemoActivity.this);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        setContentView(mButton);

    }

    @Override
    public void onColorSelected(int color) {
        mButton.setBackgroundColor(color);
    }

}
