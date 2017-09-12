package com.stars2011.dynamicviewgroup;

import android.content.Context;
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

    public DynamicRadioGroup(Context context) {
        this(context, null);
    }

    public DynamicRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCheckChangeTracker = new CheckChangeTracker();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) child;
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
        if (view != null && view instanceof RadioButton) {
            RadioButton radioButton = (RadioButton) view;
            radioButton.setChecked(true);
        }
        onAutoCheck = false;
        if (mOnCheckChangeListener != null) {
            mOnCheckChangeListener.onCheckedChanged(DynamicRadioGroup.this, viewId);
        }
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
        void onCheckedChanged(DynamicViewGroup dynamicViewGroup, int checkId);
    }
}
