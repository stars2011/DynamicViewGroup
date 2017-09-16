package com.stars2011.dynamicviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

/**
 * Created by stars2011
 */

public class DynamicRadioGroup extends DynamicViewGroup {

    public static final String TAG = "DynamicRadioGroup";

    private CheckChangeTracker mCheckChangeTracker;
    private OnCheckChangeListener mOnCheckChangeListener;
    private boolean onAutoCheck = false;
    private int mCheckId;

    public DynamicRadioGroup(Context context) {
        this(context, null);
    }

    public DynamicRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mCheckChangeTracker = new CheckChangeTracker();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicRadioGroup, defStyleAttr, 0);
        mCheckId = typedArray.getResourceId(R.styleable.DynamicRadioGroup_check_button, View.NO_ID);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) child;
            if (mCheckId != View.NO_ID && radioButton.getId() == mCheckId) {
                radioButton.setChecked(true);
            }
            radioButton.setOnCheckedChangeListener(mCheckChangeTracker);
        }
        super.addView(child, index, params);
    }

    public OnCheckChangeListener getOnCheckChangeListener() {
        return mOnCheckChangeListener;
    }

    public void setOnCheckChangeListener(OnCheckChangeListener onCheckChangeListener) {
        this.mOnCheckChangeListener = onCheckChangeListener;
    }

    public void check(int viewId) {
        onAutoCheck = true;
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) getChildAt(i);
                radioButton.setChecked(false);
            }
        }
        View view = findViewById(viewId);
        boolean isNotNullAndRadioButton = (view != null && view instanceof RadioButton);
        if (isNotNullAndRadioButton) {
            RadioButton radioButton = (RadioButton) view;
            radioButton.setChecked(true);
            mCheckId = viewId;
        }
        onAutoCheck = false;
        if (mOnCheckChangeListener != null && isNotNullAndRadioButton) {
            mOnCheckChangeListener.onCheckedChanged(DynamicRadioGroup.this, viewId);
        }
    }

    public void setRadioButtonEnable(int[] buttonId, boolean[] enable) {
        if (buttonId == null || enable == null) {
            return;
        }
        if (buttonId.length != enable.length) {
            return;
        }
        for (int i = 0; i < buttonId.length; i++) {
            View view = findViewById(buttonId[i]);
            if (view == null || !(view instanceof RadioButton)) {
                continue;
            }
            RadioButton radioButton = ((RadioButton) view);
            radioButton.setEnabled(enable[i]);
        }
    }

    public void setAllRadioButtonEnable(boolean enable) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (!(view instanceof RadioButton)) {
                continue;
            }
            RadioButton radioButton = ((RadioButton) view);
            radioButton.setEnabled(enable);
        }
    }

    public int getCheckId() {
        return mCheckId;
    }

    private class CheckChangeTracker implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (onAutoCheck) {
                return;
            }
            if (buttonView.getId() == View.NO_ID) {
                Log.e(TAG, "button id not set");
            }
            check(buttonView.getId());
        }
    }

    public interface OnCheckChangeListener {
        void onCheckedChanged(DynamicRadioGroup dynamicRadioGroup, int checkId);
    }
}
