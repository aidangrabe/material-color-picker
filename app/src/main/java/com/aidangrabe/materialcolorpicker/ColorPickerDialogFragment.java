package com.aidangrabe.materialcolorpicker;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aidangrabe.materialcolorpicker.views.ColorCircleView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aidan on 23/04/15.
 * ColorPicker Dialog that allows the user to select a colour from the Material
 * Design Colour Palette: http://www.google.com/design/spec/style/color.html#color-color-palette
 */
public class ColorPickerDialogFragment extends DialogFragment {

    private static final int CIRCLES_PER_ROW = 4;
    private static final int NUM_ROWS = 5;
    private List<ColorCircleView> mColorViews;
    private Map<Integer, Integer> mColorMap;
    private ColorSelectedListener mListener;

    public interface ColorSelectedListener {
        void onColorSelected(int color);
    }

    private final View.OnTouchListener mColorTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            ColorCircleView colorView = (ColorCircleView) view;
            final int arrayId = mColorMap.get(colorView.getColor());

            if (arrayId == 0) {
                dismiss();

                if (mListener != null) {
                    mListener.onColorSelected(colorView.getColor());
                }

            } else {

                // TODO: animate
                setColorArray(arrayId);

            }

            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.color_palette, container, false);

        mColorViews = new ArrayList<>();
        mColorMap = new HashMap<>();

        int size =  (int) dp2px(48);
        LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(size, size);
        colorParams.setMargins(5, 5, 5, 5);

        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < NUM_ROWS; i++) {
            LinearLayout rowLayout = new LinearLayout(getActivity());
            rowLayout.setLayoutParams(rowParams);
            rowLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < CIRCLES_PER_ROW; j++) {
                ColorCircleView colorView = new ColorCircleView(getActivity());
                colorView.setLayoutParams(colorParams);
                colorView.setOnTouchListener(mColorTouchListener);
                rowLayout.addView(colorView);
                mColorViews.add(colorView);
            }
            view.addView(rowLayout);
        }

        setColorArray(R.array.primary_colors);

        return view;

    }

    public void setColorArray(@ArrayRes int colorArray) {
        Log.d("D", "Getting array: " + colorArray);
        String[] hexColors = getResources().getStringArray(colorArray);
        int[] colors = new int[hexColors.length];
        mColorMap.clear();

        for (int i = 0; i < hexColors.length; i++) {
            String[] parts = hexColors[i].split("\\|", 2);
            String hexValue = parts[0];
            int arrayId = 0;
            colors[i] = Color.parseColor(hexValue);
            if (parts.length > 1) {
                arrayId = getResId(parts[1], R.array.class);
                Log.d("D", "ArrayId = " + arrayId);
            }
            mColorMap.put(colors[i], arrayId);
        }

        setColors(colors);
    }

    public void setColors(int... colors) {

        int i = 0;
        for (int color : colors) {
            mColorViews.get(i).setColor(color);
            mColorViews.get(i).setVisibility(View.VISIBLE);
            i++;
        }

        for (int j = i; j < mColorViews.size(); j++) {
            mColorViews.get(j).setVisibility(View.GONE);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Select a color");

        View view = dialog.getWindow().getDecorView();

        if (view != null) {
            addRevealAnimationToView(view);
        }

        return dialog;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void addRevealAnimationToView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    Animator reveal = ViewAnimationUtils.createCircularReveal(v, 0, 0, 0, v.getWidth());
                    reveal.setDuration(300);
                    reveal.start();
                }
            });
        }
    }

    private float dp2px(float dp) {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);

    }
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void setOnColorSelectedListener(ColorSelectedListener listener) {
        mListener = listener;
    }

}
