package com.stars2011.dynamicviewgroupdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import com.stars2011.dynamicviewgroup.DynamicRadioGroup;
import com.stars2011.dynamicviewgroup.DynamicViewGroup;

public class SampleActivity extends AppCompatActivity {

    public static final String TAG = "SampleActivity";
    private int mCurrentOrientation = DynamicViewGroup.HORIZONTAL;
    private int mCurrentGravity = DynamicViewGroup.GRAVITY_LEFT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        final DynamicViewGroup dynamicViewGroup = (DynamicViewGroup) findViewById(R.id.dynamic_view_group);
        //dynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_CENTER);
        //dynamicViewGroup.setOrientation(DynamicViewGroup.HORIZONTAL);
        //dynamicViewGroup.setMaxColumnNum(2);
        //dynamicViewGroup.setHorizontalSpacing(6);
        //dynamicViewGroup.setVerticalSpacing(6);

        final DynamicRadioGroup dynamicRadioGroupOrientation = (DynamicRadioGroup) findViewById(R.id.drg_orientation);
        dynamicRadioGroupOrientation.setOnCheckChangeListener(new DynamicRadioGroup.OnCheckChangeListener() {
            @Override
            public void onCheckedChanged(DynamicRadioGroup dynamicRadioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb_horizontal:
                        Log.i(TAG, "Orientation rb_horizontal");
                        dynamicViewGroup.setOrientation(DynamicViewGroup.HORIZONTAL);
                        mCurrentOrientation = DynamicViewGroup.HORIZONTAL;
                        break;

                    case R.id.rb_vertical:
                        Log.i(TAG, "Orientation rb_vertical");
                        dynamicViewGroup.setOrientation(DynamicViewGroup.VERTICAL);
                        mCurrentOrientation = DynamicViewGroup.VERTICAL;
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
                        dynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_LEFT);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_LEFT;
                        break;

                    case R.id.rb_gravity_top:
                        Log.i(TAG, "rb_gravity_top");
                        dynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_TOP);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_TOP;
                        break;

                    case R.id.rb_gravity_right:
                        Log.i(TAG, "rb_gravity_right");
                        dynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_RIGHT);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_RIGHT;
                        break;

                    case R.id.rb_gravity_bottom:
                        Log.i(TAG, "rb_gravity_bottom");
                        dynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_BOTTOM);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_BOTTOM;
                        break;

                    case R.id.rb_gravity_center:
                        Log.i(TAG, "rb_gravity_center");
                        dynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_CENTER);
                        mCurrentGravity = DynamicViewGroup.GRAVITY_CENTER;
                        break;

                    case R.id.rb_gravity_both:
                        Log.i(TAG, "rb_gravity_both");
                        dynamicViewGroup.setGravity(DynamicViewGroup.GRAVITY_BOTH);
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
                dynamicViewGroup.setHorizontalSpacing(Integer.valueOf(s.toString()));
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
                dynamicViewGroup.setVerticalSpacing(Integer.valueOf(s.toString()));
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
                        dynamicViewGroup.setMaxColumnNum(Integer.valueOf(s.toString()));
                        break;

                    case DynamicViewGroup.VERTICAL:
                        dynamicViewGroup.setMaxLineNum(Integer.valueOf(s.toString()));
                        break;
                }
            }
        });
    }
}
