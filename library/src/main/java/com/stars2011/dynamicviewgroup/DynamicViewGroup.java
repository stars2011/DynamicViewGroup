package com.stars2011.dynamicviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stars2011
 */

public class DynamicViewGroup extends ViewGroup {

    public static final String TAG = "DynamicViewGroup";

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public DynamicViewGroup(Context context) {
        this(context, null);
    }

    public DynamicViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureHorizontal(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 横向模式的测量方法
     */
    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) { // 实现wrap_content
            int resultWidth = 0;
            int resultHeight = 0;
            int calculateWidth = 0;
            int calculateHeight = 0;
            int childCount = getChildCount();
            // 遍历子View计算宽高
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int childViewWidth = childView.getMeasuredWidth();
                int childViewHeight = childView.getMeasuredHeight();
                if (calculateWidth + childViewWidth > widthSize) { // 超过了单行最大的宽度,需要换行
                    resultWidth = Math.max(resultWidth, calculateWidth);
                    resultHeight += calculateHeight;
                    calculateWidth = 0;
                    calculateHeight = 0;
                } else {
                    calculateWidth += childViewWidth;
                    calculateHeight = Math.max(calculateHeight, childViewHeight);
                }
            }
            setMeasuredDimension(resultWidth, resultHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) { // 无论heightMode是EXACTLY还是UNSPECIFIED，高度都是使用原本指定的heightSize

            int resultWidth = 0;
            int resultHeight = heightSize;
            int calculateWidth = 0;
            int calculateHeight = 0;
            int childCount = getChildCount();
            // 遍历子View计算宽高
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int childViewWidth = childView.getMeasuredWidth();
                int childViewHeight = childView.getMeasuredHeight();
                if (calculateWidth + childViewWidth > widthSize) { // 超过了单行最大的宽度,需要换行
                    resultWidth = Math.max(resultWidth, calculateWidth);
                    resultHeight += calculateHeight;
                    calculateWidth = 0;
                    calculateHeight = 0;
                } else {
                    calculateWidth += childViewWidth;
                    calculateHeight = Math.max(calculateHeight, childViewHeight);
                }
            }
        } else if (heightMode == MeasureSpec.AT_MOST) { // 无论widthMode是EXACTLY还是UNSPECIFIED，高度都是使用原本指定的widthSize

        }
    }

    private ResultSize getMeasureResultSizeForHorizontal(int maxWidth, int maxHeight) {
        int resultWidth = 0;
        int resultHeight = 0;
        int calculateWidth = 0;
        int calculateHeight = 0;
        int childCount = getChildCount();
        // 遍历子View计算宽高
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int childViewWidth = childView.getMeasuredWidth();
            int childViewHeight = childView.getMeasuredHeight();
            if (calculateWidth + childViewWidth > maxWidth) { // 超过了单行最大的宽度,需要换行
                resultWidth = Math.max(resultWidth, calculateWidth);
                resultHeight += calculateHeight;
                if (resultHeight > maxHeight) {
                    Log.e(TAG, "we have no room for view");
                    return new ResultSize(resultWidth, resultHeight);
                }
                calculateWidth = 0;
                calculateHeight = 0;
            } else {
                calculateWidth += childViewWidth;
                calculateHeight = Math.max(calculateHeight, childViewHeight);
            }
        }
        return new ResultSize(resultWidth, resultHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    static class ResultSize {
        int resultWidth = 0;
        int resultHeight = 0;

        public ResultSize(int resultWidth, int resultHeight) {
            this.resultWidth = resultWidth;
            this.resultHeight = resultHeight;
        }

        public int getResultWidth() {
            return resultWidth;
        }

        public void setResultWidth(int resultWidth) {
            this.resultWidth = resultWidth;
        }

        public int getResultHeight() {
            return resultHeight;
        }

        public void setResultHeight(int resultHeight) {
            this.resultHeight = resultHeight;
        }
    }
}
