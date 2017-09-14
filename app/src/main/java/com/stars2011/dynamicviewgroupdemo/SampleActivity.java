package com.stars2011.dynamicviewgroupdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.stars2011.dynamicviewgroup.DynamicRadioGroup;
import com.stars2011.dynamicviewgroup.DynamicViewGroup;

public class SampleActivity extends AppCompatActivity {

    public static final String TAG = "SampleActivity";
    private ScrollView mScrollView;
    private LinearLayout mLimitLinearLayout;
    private LinearLayout.LayoutParams mLimitLayoutParams;
    private FrameLayout.LayoutParams mScrollViewLayoutParams;
    private DynamicViewGroup mDynamicViewGroup;
    private int mCurrentOrientation = DynamicViewGroup.HORIZONTAL;
    private int mCurrentGravity = DynamicViewGroup.GRAVITY_LEFT;
    private boolean mHeightLimit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mDynamicViewGroup = (DynamicViewGroup) findViewById(R.id.dynamic_view_group);
        mLimitLinearLayout = (LinearLayout) findViewById(R.id.ll_limit);
        mLimitLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mScrollViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        //mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_CENTER);
        //mDynamicViewGroup.setOrientation(DynamicViewGroup.HORIZONTAL);
        //mDynamicViewGroup.setMaxColumnNum(2);
        //mDynamicViewGroup.setHorizontalSpacing(6);
        //mDynamicViewGroup.setVerticalSpacing(6);

        final DynamicRadioGroup dynamicRadioGroupOrientation = (DynamicRadioGroup) findViewById(R.id.drg_orientation);
        dynamicRadioGroupOrientation.setOnCheckChangeListener(new DynamicRadioGroup.OnCheckChangeListener() {
            @Override
            public void onCheckedChanged(DynamicRadioGroup dynamicRadioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb_horizontal:
                        Log.i(TAG, "Orientation rb_horizontal");
                        mDynamicViewGroup.setOrientation(DynamicViewGroup.HORIZONTAL);
                        mCurrentOrientation = DynamicViewGroup.HORIZONTAL;
                        limitHeight(false);
                        break;

                    case R.id.rb_vertical:
                        Log.i(TAG, "Orientation rb_vertical");
                        mDynamicViewGroup.setOrientation(DynamicViewGroup.VERTICAL);
                        mCurrentOrientation = DynamicViewGroup.VERTICAL;
                        limitHeight(mHeightLimit);
                        break;
                }
            }
        });

        final DynamicRadioGroup dynamicRadioGroupGravity = (DynamicRadioGroup) findViewById(R.id.drg_gravity);
        dynamicRadioGroupGravity.setOnCheckChangeListener(new DynamicRadioGroup.OnCheckChangeListener() {
            @Override
            public void onCheckedChanged(DynamicRadioGroup dynamicRadioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb_gravity_left:
                        Log.i(TAG, "rb_gravity_left");
                        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_LEFT);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_LEFT;
                        break;

                    case R.id.rb_gravity_top:
                        Log.i(TAG, "rb_gravity_top");
                        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_TOP);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_TOP;
                        break;

                    case R.id.rb_gravity_right:
                        Log.i(TAG, "rb_gravity_right");
                        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_RIGHT);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_RIGHT;
                        break;

                    case R.id.rb_gravity_bottom:
                        Log.i(TAG, "rb_gravity_bottom");
                        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_BOTTOM);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_BOTTOM;
                        break;

                    case R.id.rb_gravity_center:
                        Log.i(TAG, "rb_gravity_center");
                        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_CENTER);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_CENTER;
                        break;

                    case R.id.rb_gravity_both:
                        Log.i(TAG, "rb_gravity_both");
                        mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_BOTH);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_BOTH;
                        break;
                }
            }
        });

        // 横向间距
        ((EditText) findViewById(R.id.et_horizontal_spacing)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    return;
                }
                mDynamicViewGroup.setHorizontalSpacing(Integer.valueOf(s.toString()));
            }
        });

        // 竖向间距
        ((EditText) findViewById(R.id.et_vertical_spacing)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    return;
                }
                mDynamicViewGroup.setVerticalSpacing(Integer.valueOf(s.toString()));
            }
        });

        // 行列限制
        ((EditText) findViewById(R.id.et_max_column_or_line_num)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    return;
                }
                switch (mCurrentOrientation) {
                    case DynamicViewGroup.HORIZONTAL:
                        mDynamicViewGroup.setMaxColumnNum(Integer.valueOf(s.toString()));
                        break;

                    case DynamicViewGroup.VERTICAL:
                        mDynamicViewGroup.setMaxLineNum(Integer.valueOf(s.toString()));
                        break;
                }
            }
        });

        // 限高
        ((CheckBox) findViewById(R.id.cb_limit)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHeightLimit = isChecked;
                if (mCurrentOrientation == DynamicViewGroup.VERTICAL) {
                    limitHeight(mHeightLimit);
                }
            }
        });
    }

    private void limitHeight(boolean limit) {
        if (limit) {
            mScrollView.removeView(mDynamicViewGroup);
            mLimitLinearLayout.addView(mDynamicViewGroup, mLimitLayoutParams);
            mScrollView.setVisibility(View.GONE);
            mLimitLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mLimitLinearLayout.removeView(mDynamicViewGroup);
            if (mScrollView.getChildCount() == 0) {
                mScrollView.addView(mDynamicViewGroup, mScrollViewLayoutParams);
            }
            mScrollView.setVisibility(View.VISIBLE);
            mLimitLinearLayout.setVisibility(View.GONE);
        }
    }
}
