package com.stars2011.dynamicviewgroupdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import com.stars2011.dynamicviewgroup.DynamicRadioGroup;
import com.stars2011.dynamicviewgroup.DynamicViewGroup;

public class SampleActivity extends AppCompatActivity {

    public static final String TAG = "SampleActivity";
    private ScrollView mScrollView;
    private HorizontalScrollView mHorizontalScrollView;
    private DynamicRadioGroup mDynamicRadioGroupOrientation;
    private DynamicRadioGroup mDynamicRadioGroupGravity;
    private FrameLayout.LayoutParams mLimitLayoutParams;
    private FrameLayout.LayoutParams mScrollViewLayoutParams;
    private int[] mDisableButtonIdForHorizontalMode;
    private int[] mDisableButtonIdForVerticalMode;
    private boolean[] disableButtonBooleanArray;
    private DynamicViewGroup mDynamicViewGroup;
    private int mCurrentOrientation = DynamicViewGroup.HORIZONTAL;
    private int mCurrentGravity = DynamicViewGroup.GRAVITY_LEFT;
    private boolean mHeightLimit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_limit);
        mDynamicRadioGroupOrientation = (DynamicRadioGroup) findViewById(R.id.drg_orientation);
        mDynamicRadioGroupGravity = (DynamicRadioGroup) findViewById(R.id.drg_gravity);

        mLimitLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mScrollViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        mDisableButtonIdForHorizontalMode = new int[] { R.id.rb_gravity_top, R.id.rb_gravity_bottom };
        mDisableButtonIdForVerticalMode = new int[] { R.id.rb_gravity_left, R.id.rb_gravity_right };
        disableButtonBooleanArray = new boolean[] { false, false };

        mDynamicViewGroup = (DynamicViewGroup) findViewById(R.id.dynamic_view_group);
        //mDynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_CENTER);
        //mDynamicViewGroup.setOrientation(DynamicViewGroup.HORIZONTAL);
        //mDynamicViewGroup.setMaxColumnNum(3);
        //mDynamicViewGroup.setHorizontalSpacing(20);
        //mDynamicViewGroup.setVerticalSpacing(20);
        setRadioButtonForOrientation(mDynamicViewGroup.getOrientation());

        // 排列方向
        mDynamicRadioGroupOrientation.setOnCheckChangeListener(new DynamicRadioGroup.OnCheckChangeListener() {
            @Override
            public void onCheckedChanged(DynamicRadioGroup dynamicRadioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb_horizontal:
                        Log.i(TAG, "Orientation rb_horizontal");
                        mDynamicViewGroup.setOrientation(DynamicViewGroup.HORIZONTAL);
                        mCurrentOrientation = DynamicViewGroup.HORIZONTAL;
                        limitHeight(false);
                        setRadioButtonForOrientation(mDynamicViewGroup.getOrientation());
                        break;

                    case R.id.rb_vertical:
                        Log.i(TAG, "Orientation rb_vertical");
                        mDynamicViewGroup.setOrientation(DynamicViewGroup.VERTICAL);
                        mCurrentOrientation = DynamicViewGroup.VERTICAL;
                        limitHeight(mHeightLimit);
                        setRadioButtonForOrientation(mDynamicViewGroup.getOrientation());
                        break;
                }
            }
        });

        mDynamicRadioGroupGravity.setOnCheckChangeListener(new DynamicRadioGroup.OnCheckChangeListener() {
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
                int max = 0;
                if (TextUtils.isEmpty(s.toString())) {
                    max = DynamicViewGroup.NUM_NOT_SET;
                } else {
                    max = Integer.valueOf(s.toString());
                }
                switch (mCurrentOrientation) {
                    case DynamicViewGroup.HORIZONTAL:
                        mDynamicViewGroup.setMaxColumnNum(max);
                        break;

                    case DynamicViewGroup.VERTICAL:
                        mDynamicViewGroup.setMaxLineNum(max);
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

    private void setRadioButtonForOrientation(int orientation) {
        switch (orientation) {
            case DynamicViewGroup.HORIZONTAL:
                mDynamicRadioGroupGravity.setAllRadioButtonEnable(true);
                mDynamicRadioGroupGravity.setRadioButtonEnable(mDisableButtonIdForHorizontalMode, disableButtonBooleanArray);
                int checkIdInHorizontal = mDynamicRadioGroupGravity.getCheckId();
                if (checkIdInHorizontal == R.id.rb_gravity_top || checkIdInHorizontal == R.id.rb_gravity_bottom) {
                    mDynamicRadioGroupGravity.check(R.id.rb_gravity_left);
                }
                break;

            case DynamicViewGroup.VERTICAL:
                mDynamicRadioGroupGravity.setAllRadioButtonEnable(true);
                mDynamicRadioGroupGravity.setRadioButtonEnable(mDisableButtonIdForVerticalMode, disableButtonBooleanArray);
                int checkIdInVertical = mDynamicRadioGroupGravity.getCheckId();
                if (checkIdInVertical == R.id.rb_gravity_left || checkIdInVertical == R.id.rb_gravity_right) {
                    mDynamicRadioGroupGravity.check(R.id.rb_gravity_top);
                }
                break;
        }
    }

    private void limitHeight(boolean limit) {
        if (limit) {
            mScrollView.removeView(mDynamicViewGroup);
            if (mHorizontalScrollView.getChildCount() == 0) {
                mHorizontalScrollView.addView(mDynamicViewGroup, mLimitLayoutParams);
            }
            mScrollView.setVisibility(View.GONE);
            mHorizontalScrollView.setVisibility(View.VISIBLE);
        } else {
            mHorizontalScrollView.removeView(mDynamicViewGroup);
            if (mScrollView.getChildCount() == 0) {
                mScrollView.addView(mDynamicViewGroup, mScrollViewLayoutParams);
            }
            mScrollView.setVisibility(View.VISIBLE);
            mHorizontalScrollView.setVisibility(View.GONE);
        }
    }
}
