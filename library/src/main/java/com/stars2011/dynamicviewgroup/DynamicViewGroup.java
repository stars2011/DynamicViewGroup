package com.stars2011.dynamicviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stars2011
 */

public class DynamicViewGroup extends ViewGroup {

    public static final String TAG = "DynamicViewGroup";

    @IntDef({ HORIZONTAL, VERTICAL })
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public static final int HORIZONTAL = 0; // 横向布局
    public static final int VERTICAL = 1; // 竖向布局

    @IntDef({ GRAVITY_LEFT, GRAVITY_RIGHT, GRAVITY_CENTER, GRAVITY_TOP, GRAVITY_BOTTOM, GRAVITY_BOTH })
    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityMode {
    }

    public static final int GRAVITY_LEFT = 10; // 布局左对齐  (用于 HORIZONTAL 模式)
    public static final int GRAVITY_RIGHT = 11; // 布局右对齐 (用于 HORIZONTAL 模式)
    public static final int GRAVITY_CENTER = 12; // 布局居中对齐 (用于 HORIZONTAL 和 VERTICAL模式)
    public static final int GRAVITY_TOP = 13; // 布局顶对齐 (用于 VERTICAL 模式)
    public static final int GRAVITY_BOTTOM = 14; // 布局底对齐 (用于 VERTICAL 模式)
    public static final int GRAVITY_BOTH = 15; // 双端对齐

    public static final int NUM_NOT_SET = -1;

    private int mOrientation = HORIZONTAL;
    private int mGravity = GRAVITY_LEFT;
    private int mMaxColumnNum = NUM_NOT_SET; // 最大列数，当每行子View个数超过则自动换行（用于 HORIZONTAL 模式）
    private int mMaxLineNum = NUM_NOT_SET; // 最大行数，当每列子View个数超过则自动换列（用于 VERTICAL 模式）
    private int mHorizontalSpacing = 0;
    private int mVerticalSpacing = 0;
    // true表示当不够位置的时候则不layout该子View，false表示无论是否够位置都layout（可能会出现View部分不显示的问题）
    private boolean mDoNotLayoutWhenHaveNoEnoughRoom = true;
    private List<View> mChildViewInThisLineOrColumn = new ArrayList<>();
    private List<View> mChildViewInThisLineOrColumnForMeasure = new ArrayList<>();

    public DynamicViewGroup(Context context) {
        this(context, null);
    }

    public DynamicViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicViewGroup, defStyleAttr, 0);
        mOrientation = typedArray.getInt(R.styleable.DynamicViewGroup_orientation, HORIZONTAL);
        mGravity = typedArray.getInt(R.styleable.DynamicViewGroup_gravity, GRAVITY_LEFT);
        mMaxColumnNum = typedArray.getInt(R.styleable.DynamicViewGroup_max_column_num, NUM_NOT_SET);
        mMaxLineNum = typedArray.getInt(R.styleable.DynamicViewGroup_max_line_num, NUM_NOT_SET);
        mHorizontalSpacing = (int) (typedArray.getDimension(R.styleable.DynamicViewGroup_horizontal_spacing, 0.0f));
        mVerticalSpacing = (int) (typedArray.getDimension(R.styleable.DynamicViewGroup_vertical_spacing, 0.0f));
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(@OrientationMode int orientation) {
        this.mOrientation = orientation;
        refresh();
    }

    public int getGravity() {
        return mGravity;
    }

    public void setGravity(@GravityMode int gravity) {
        this.mGravity = gravity;
        refresh();
    }

    public int getMaxColumnNum() {
        return mMaxColumnNum;
    }

    public void setMaxColumnNum(int maxColumnNum) {
        this.mMaxColumnNum = maxColumnNum;
        refresh();
    }

    public int getMaxLineNum() {
        return mMaxLineNum;
    }

    public void setMaxLineNum(int maxLineNum) {
        this.mMaxLineNum = maxLineNum;
        refresh();
    }

    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.mHorizontalSpacing = horizontalSpacing;
        refresh();
    }

    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.mVerticalSpacing = verticalSpacing;
        refresh();
    }

    private void refresh() {
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureDependOnMode(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 根据当前模式选择对应的测量模式并设置
     */
    private void measureDependOnMode(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) { // 宽高都是wrap_content
            calculateSizeAndSetMeasuredDimensionDependOnMode(widthSize, false, heightSize, false);
        } else if (widthMode == MeasureSpec.AT_MOST) { // 无论高是EXACTLY还是UNSPECIFIED，都直接使用heightSize
            boolean heightBeMax = true;
            if (heightMode == MeasureSpec.UNSPECIFIED && heightSize == 0) {
                heightSize = Integer.MAX_VALUE;
                heightBeMax = false;
            }
            calculateSizeAndSetMeasuredDimensionDependOnMode(widthSize, false, heightSize, heightBeMax);
        } else if (heightMode == MeasureSpec.AT_MOST) { // 无论宽是EXACTLY还是UNSPECIFIED，都直接使用widthSize
            boolean widthBeMax = true;
            if (widthMode == MeasureSpec.UNSPECIFIED && widthSize == 0) {
                widthSize = Integer.MAX_VALUE;
                widthBeMax = false;
            }
            calculateSizeAndSetMeasuredDimensionDependOnMode(widthSize, widthBeMax, heightSize, false);
        } else if (widthMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.UNSPECIFIED) {
            // 来到这里有以下三种情况
            // 宽EXACTLY，高UNSPECIFIED
            // 宽UNSPECIFIED，高EXACTLY
            // 宽UNSPECIFIED，UNSPECIFIED
            boolean widthBeMax = true;
            boolean heightBeMax = true;
            if (widthSize == 0) {
                widthSize = Integer.MAX_VALUE;
                widthBeMax = false;
            }
            if (heightSize == 0) {
                heightSize = Integer.MAX_VALUE;
                heightBeMax = false;
            }
            calculateSizeAndSetMeasuredDimensionDependOnMode(widthSize, widthBeMax, heightSize, heightBeMax);
        }
    }

    /**
     * 根据模式计算ViewGroup的尺寸，并设置
     *
     * @param maxWidth 最大宽度
     * @param widthBeMax 最后尺寸是否直接使用最大宽度（当此选项为真则表示宽度是已经固定的，不需要计算，传入参数为了给计算高度的时候提供一个边界）
     * @param maxHeight 最大高度
     * @param heightBeMax 最后尺寸是否直接使用最大高度（当此选项为真则表示高度是已经固定的，不需要计算，传入参数为了给计算宽度的时候提供一个边界）
     */
    private void calculateSizeAndSetMeasuredDimensionDependOnMode(int maxWidth, boolean widthBeMax, int maxHeight, boolean heightBeMax) {
        ResultSize resultSize = null;
        resultSize = getMeasureResultSize(maxWidth, widthBeMax, maxHeight, heightBeMax);
        if (resultSize == null) {
            Log.e(TAG, "resultSize null when calculateSize");
            return;
        }
        setMeasuredDimension(resultSize.getResultWidth(), resultSize.getResultHeight());
    }

    private ResultSize getMeasureResultSize(int maxWidth, boolean widthBeMax, int maxHeight, boolean heightBeMax) {
        CalculateSize calculateSize = new CalculateSize(
            0,
            0,
            0,
            0,
            getChildCount(),
            maxWidth - getPaddingLeft() - getPaddingRight(),
            maxHeight - getPaddingTop() - getPaddingBottom()
        );
        // 遍历子View计算宽高
        for (int i = 0; i < calculateSize.getChildCount(); i++) {
            View childView = getChildAt(i);
            // 获取margin信息
            ChildViewMarginSize marginSize = getChildViewMargin(childView);
            int leftMargin = marginSize.getLeftMargin();
            int rightMargin = marginSize.getRightMargin();
            int topMargin = marginSize.getTopMargin();
            int bottomMargin = marginSize.getBottomMargin();
            // 记录子View的测量后的宽高
            calculateSize.setChildViewWidth(childView.getMeasuredWidth() + leftMargin + rightMargin + mHorizontalSpacing);
            calculateSize.setChildViewHeight(childView.getMeasuredHeight() + topMargin + bottomMargin + mVerticalSpacing);

            switch (mOrientation) {
                case HORIZONTAL:
                    calculateSize =
                        calculateForHorizontal(calculateSize, i, isNewLineOrNewColumnByChildViewIndex(mChildViewInThisLineOrColumnForMeasure.size()));
                    break;

                case VERTICAL:
                    calculateSize =
                        calculateForVertical(calculateSize, i, isNewLineOrNewColumnByChildViewIndex(mChildViewInThisLineOrColumnForMeasure.size()));
                    break;
            }
        }
        // 处理额外多添加的横向间距和竖向
        calculateSize.setResultWidth(calculateSize.getResultWidth() - mHorizontalSpacing);
        calculateSize.setResultHeight(calculateSize.getResultHeight() - mVerticalSpacing);
        // 添加padding的计算
        int[] resultWidthAndHeightAfterAddPadding =
            addPaddingToWidthAndHeight(calculateSize.getResultWidth(), maxWidth, calculateSize.getResultHeight(), maxHeight);
        calculateSize.setResultWidth(resultWidthAndHeightAfterAddPadding[0]);
        calculateSize.setResultHeight(resultWidthAndHeightAfterAddPadding[1]);

        if (widthBeMax) {
            calculateSize.setResultWidth(maxWidth);
        }
        if (heightBeMax) {
            calculateSize.setResultHeight(maxHeight);
        }
        return new ResultSize(calculateSize.getResultWidth(), calculateSize.getResultHeight());
    }

    /**
     * 横向模式计算尺寸
     */
    private CalculateSize calculateForHorizontal(CalculateSize calculateSize, int index, boolean isForceNewLine) {
        if (calculateSize.getCalculateWidth() + (calculateSize.getChildViewWidth() - mHorizontalSpacing) > calculateSize.getMaxWidth()
            || isForceNewLine) { // 超过了单行最大的宽度,需要换行
            // 换行的时候就清理上一行的数据
            mChildViewInThisLineOrColumnForMeasure.clear();
            // 换行的时候更新left和top
            calculateSize.setResultWidth(Math.max(calculateSize.getResultWidth(), calculateSize.getCalculateWidth()));
            calculateSize.setResultHeight(calculateSize.getResultHeight() + calculateSize.getCalculateHeight());
            if (calculateSize.getResultHeight() > calculateSize.getMaxHeight()) {
                Log.e(TAG, "we have no room for view");
                return calculateSize;
            }
            calculateSize.setCalculateWidth(0);
            calculateSize.setCalculateHeight(0);
            // 继续做插入测量
            calculateSize.setCalculateWidth(calculateSize.getCalculateWidth() + calculateSize.getChildViewWidth());
            calculateSize.setCalculateHeight(Math.max(calculateSize.getCalculateHeight(), calculateSize.getChildViewHeight()));
            // 到了最后一个View了,即将返回，对Result赋值
            if (index == (calculateSize.getChildCount() - 1)) {
                calculateSize.setResultWidth(Math.max(calculateSize.getResultWidth(), calculateSize.getCalculateWidth()));
                calculateSize.setResultHeight(calculateSize.getResultHeight() + calculateSize.getCalculateHeight());
            }
        } else {
            calculateSize.setCalculateWidth(calculateSize.getCalculateWidth() + calculateSize.getChildViewWidth());
            calculateSize.setCalculateHeight(Math.max(calculateSize.getCalculateHeight(), calculateSize.getChildViewHeight()));
            // 到了最后一个View了,即将返回，对Result赋值
            if (index == (calculateSize.getChildCount() - 1)) {
                calculateSize.setResultWidth(Math.max(calculateSize.getResultWidth(), calculateSize.getCalculateWidth()));
                calculateSize.setResultHeight(calculateSize.getResultHeight() + calculateSize.getCalculateHeight());
            }
        }
        mChildViewInThisLineOrColumnForMeasure.add(getChildAt(index));
        if (index == (getChildCount() - 1)) {
            mChildViewInThisLineOrColumnForMeasure.clear();
        }
        return calculateSize;
    }

    /**
     * 竖向模式计算尺寸
     */
    private CalculateSize calculateForVertical(CalculateSize calculateSize, int index, boolean isForceNewColumn) {
        if (calculateSize.getCalculateHeight() + (calculateSize.getChildViewHeight() - mVerticalSpacing) > calculateSize.getMaxHeight()
            || isForceNewColumn) { // 超过了单列最大的高度,需要换列
            // 换行的时候就清理上一行的数据
            mChildViewInThisLineOrColumnForMeasure.clear();
            // 换列的时候更新left和top
            calculateSize.setResultHeight(Math.max(calculateSize.getResultHeight(), calculateSize.getCalculateHeight()));
            calculateSize.setResultWidth(calculateSize.getResultWidth() + calculateSize.getCalculateWidth());
            if (calculateSize.getResultWidth() > calculateSize.getMaxWidth()) {
                Log.e(TAG, "we have no room for view");
                return calculateSize;
            }
            calculateSize.setCalculateWidth(0);
            calculateSize.setCalculateHeight(0);
            // 继续做插入测量
            calculateSize.setCalculateHeight(calculateSize.getCalculateHeight() + calculateSize.getChildViewHeight());
            calculateSize.setCalculateWidth(Math.max(calculateSize.getCalculateWidth(), calculateSize.getChildViewWidth()));
            if (index == (calculateSize.getChildCount() - 1)) { // 到了最后一个View了,即将返回，对Result赋值
                calculateSize.setResultHeight(Math.max(calculateSize.getResultHeight(), calculateSize.getCalculateHeight()));
                calculateSize.setResultWidth(calculateSize.getResultWidth() + calculateSize.getCalculateWidth());
            }
        } else {
            calculateSize.setCalculateHeight(calculateSize.getCalculateHeight() + calculateSize.getChildViewHeight());
            calculateSize.setCalculateWidth(Math.max(calculateSize.getCalculateWidth(), calculateSize.getChildViewWidth()));
            if (index == (calculateSize.getChildCount() - 1)) { // 到了最后一个View了,即将返回，对Result赋值
                calculateSize.setResultHeight(Math.max(calculateSize.getResultHeight(), calculateSize.getCalculateHeight()));
                calculateSize.setResultWidth(calculateSize.getResultWidth() + calculateSize.getCalculateWidth());
            }
        }
        mChildViewInThisLineOrColumnForMeasure.add(getChildAt(index));
        if (index == (getChildCount() - 1)) {
            mChildViewInThisLineOrColumnForMeasure.clear();
        }
        return calculateSize;
    }

    private boolean isNewLineOrNewColumnByChildViewIndex(int childIndex) {
        switch (mOrientation) {
            case HORIZONTAL:
                if (mMaxColumnNum == NUM_NOT_SET) {
                    return false;
                }
                if (childIndex == 0) {
                    return false;
                }
                return (childIndex) % mMaxColumnNum == 0;

            case VERTICAL:
                if (mMaxLineNum == NUM_NOT_SET) {
                    return false;
                }
                if (childIndex == 0) {
                    return false;
                }
                return (childIndex) % mMaxLineNum == 0;

            default:
                return false;
        }
    }

    /**
     * 计算ViewGroup宽高的时候为宽和高添加padding
     *
     * @param resultWidth 根据子View计算出的ViewGroup宽度
     * @param maxWidth ViewGroup的最大宽度
     * @param resultHeight 根据子View计算出的ViewGroup高度
     * @param maxHeight ViewGroup的最大高度
     * @return 包含两个元素的int数组，宽度的索引为0，高度的索引为1
     */
    private int[] addPaddingToWidthAndHeight(int resultWidth, int maxWidth, int resultHeight, int maxHeight) {
        int addPaddingResultWidth = resultWidth + getPaddingLeft() + getPaddingRight();
        int addPaddingResultHeight = resultHeight + getPaddingTop() + getPaddingBottom();
        if (addPaddingResultWidth > maxWidth) {
            addPaddingResultWidth = maxWidth;
        }
        if (addPaddingResultHeight > maxHeight) {
            addPaddingResultHeight = maxHeight;
        }
        return new int[] { addPaddingResultWidth, addPaddingResultHeight };
    }

    /**
     * 获取子View左上右下边距
     */
    private ChildViewMarginSize getChildViewMargin(View childView) {
        ViewGroup.LayoutParams childViewLayoutParams = childView.getLayoutParams();
        int leftMargin = 0;
        int rightMargin = 0;
        int topMargin = 0;
        int bottomMargin = 0;
        if (childViewLayoutParams instanceof MarginLayoutParams) {
            leftMargin = ((MarginLayoutParams) childViewLayoutParams).leftMargin;
            rightMargin = ((MarginLayoutParams) childViewLayoutParams).rightMargin;
            topMargin = ((MarginLayoutParams) childViewLayoutParams).topMargin;
            bottomMargin = ((MarginLayoutParams) childViewLayoutParams).bottomMargin;
            if (leftMargin < 0) {
                leftMargin = 0;
            }
            if (rightMargin < 0) {
                rightMargin = 0;
            }
            if (topMargin < 0) {
                topMargin = 0;
            }
            if (bottomMargin < 0) {
                bottomMargin = 0;
            }
        }
        return new ChildViewMarginSize(leftMargin, topMargin, rightMargin, bottomMargin);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutDependOnMode();
    }

    /**
     * 根据当前模式来layout
     */
    private void layoutDependOnMode() {
        LayoutSize layoutSize = new LayoutSize(
            0 + getPaddingLeft(),
            0 + getPaddingTop(),
            getMeasuredWidth() - getPaddingRight(),
            getMeasuredHeight() - getPaddingBottom(),
            getChildCount(),
            0,
            0
        );
        // layout每一个子View
        for (int i = 0; i < layoutSize.getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            ChildViewMarginSize marginSize = getChildViewMargin(childView);
            switch (mOrientation) {
                case HORIZONTAL:
                    layoutForHorizontal(
                        childView,
                        layoutSize,
                        marginSize,
                        i,
                        isNewLineOrNewColumnByChildViewIndex(mChildViewInThisLineOrColumn.size())
                    );
                    break;

                case VERTICAL:
                    layoutForVertical(
                        childView,
                        layoutSize,
                        marginSize,
                        i,
                        isNewLineOrNewColumnByChildViewIndex(mChildViewInThisLineOrColumn.size())
                    );
                    break;
            }
        }
    }

    /**
     * 根据横向布局模式layout子View
     */
    private void layoutForHorizontal(View childView,
                                     LayoutSize layoutSize,
                                     ChildViewMarginSize marginSize,
                                     int childViewIndex,
                                     boolean isForceNewLine) {
        int leftMargin = marginSize.getLeftMargin();
        int rightMargin = marginSize.getRightMargin();
        int topMargin = marginSize.getTopMargin();
        int bottomMargin = marginSize.getBottomMargin();
        int right = layoutSize.getLeft() + childView.getMeasuredWidth() + leftMargin;
        int bottom = layoutSize.getTop() + childView.getMeasuredHeight() + topMargin;

        if (right > layoutSize.getViewGroupWidth() || isForceNewLine) {
            // 换行的时候先根据gravity来调整当前行子View的位置
            adjustChildViewPositionDependOnGravityInHorizontalMode(mChildViewInThisLineOrColumn);
            // 不够位置，需要换行
            layoutSize.setTop(layoutSize.getTop() + layoutSize.getMaxHeightInThisLine());
            layoutSize.setLeft(getPaddingLeft());
            layoutSize.setMaxHeightInThisLine(0);
            // 换行后继续layout
            right = layoutSize.getLeft() + childView.getMeasuredWidth() + leftMargin;
            bottom = layoutSize.getTop() + childView.getMeasuredHeight() + topMargin;
            childView.layout(layoutSize.getLeft() + leftMargin, layoutSize.getTop() + topMargin, right, bottom);
            layoutSize.setLeft(right + rightMargin + mHorizontalSpacing);
            layoutSize.setMaxHeightInThisLine(Math.max(
                layoutSize.getMaxHeightInThisLine(),
                childView.getMeasuredHeight() + topMargin + bottomMargin + mVerticalSpacing
            ));
        } else { // 从左到右排列
            childView.layout(layoutSize.getLeft() + leftMargin, layoutSize.getTop() + topMargin, right, bottom);
            layoutSize.setLeft(right + rightMargin + mHorizontalSpacing);
            layoutSize.setMaxHeightInThisLine(Math.max(
                layoutSize.getMaxHeightInThisLine(),
                childView.getMeasuredHeight() + topMargin + bottomMargin + mVerticalSpacing
            ));
        }
        mChildViewInThisLineOrColumn.add(childView);
        if (childViewIndex == getChildCount() - 1) {
            adjustChildViewPositionDependOnGravityInHorizontalMode(mChildViewInThisLineOrColumn);
        }
    }

    /**
     * 根据竖向布局模式layout子View
     */
    private void layoutForVertical(View childView,
                                   LayoutSize layoutSize,
                                   ChildViewMarginSize marginSize,
                                   int childViewIndex,
                                   boolean isForceNewColumn) {
        int leftMargin = marginSize.getLeftMargin();
        int rightMargin = marginSize.getRightMargin();
        int topMargin = marginSize.getTopMargin();
        int bottomMargin = marginSize.getBottomMargin();
        int right = layoutSize.getLeft() + childView.getMeasuredWidth() + leftMargin;
        int bottom = layoutSize.getTop() + childView.getMeasuredHeight() + topMargin;

        if (bottom > layoutSize.getViewGroupHeight() || isForceNewColumn) {
            // 换行的时候先根据gravity来调整当前行子View的位置
            adjustChildViewPositionDependOnGravityInVerticalMode(mChildViewInThisLineOrColumn);
            // 不够位置，需要换列
            layoutSize.setLeft(layoutSize.getLeft() + layoutSize.getMaxWidthInThisColumn());
            layoutSize.setTop(getPaddingTop());
            layoutSize.setMaxWidthInThisColumn(0);
            // 换列后继续layout
            right = layoutSize.getLeft() + childView.getMeasuredWidth() + leftMargin;
            bottom = layoutSize.getTop() + childView.getMeasuredHeight() + topMargin;
            // 检查是否有足够的位置layout，够位置才layout
            if (right <= layoutSize.getViewGroupWidth() || !mDoNotLayoutWhenHaveNoEnoughRoom) {
                childView.layout(layoutSize.getLeft() + leftMargin, layoutSize.getTop() + topMargin, right, bottom);
            } else { // 不够位置则layout到一个不可见的位置
                childView.layout(-childView.getMeasuredWidth(), -childView.getMeasuredHeight(), 0, 0);
            }
            layoutSize.setTop(bottom + bottomMargin + mVerticalSpacing);
            layoutSize.setMaxWidthInThisColumn(Math.max(
                layoutSize.getMaxWidthInThisColumn(),
                childView.getMeasuredWidth() + leftMargin + rightMargin + mHorizontalSpacing
            ));
        } else { // 从上到下排列
            // 检查是否有足够的位置layout，够位置才layout
            if (right <= layoutSize.getViewGroupWidth() || !mDoNotLayoutWhenHaveNoEnoughRoom) {
                childView.layout(layoutSize.getLeft() + leftMargin, layoutSize.getTop() + topMargin, right, bottom);
            } else { // 不够位置则layout到一个不可见的位置
                childView.layout(-childView.getMeasuredWidth(), -childView.getMeasuredHeight(), 0, 0);
            }
            layoutSize.setTop(bottom + bottomMargin + mVerticalSpacing);
            layoutSize.setMaxWidthInThisColumn(Math.max(
                layoutSize.getMaxWidthInThisColumn(),
                childView.getMeasuredWidth() + leftMargin + rightMargin + mHorizontalSpacing
            ));
        }
        mChildViewInThisLineOrColumn.add(childView);
        if (childViewIndex == getChildCount() - 1) {
            adjustChildViewPositionDependOnGravityInVerticalMode(mChildViewInThisLineOrColumn);
        }
    }

    private void adjustChildViewPositionDependOnGravityInHorizontalMode(List<View> childViewInThisLineOrColumn) {
        switch (mGravity) {
            case GRAVITY_LEFT:
                // do nothing
                break;

            case GRAVITY_CENTER:
                adjustChildViewForGravityInHorizontalMode(childViewInThisLineOrColumn);
                break;

            case GRAVITY_RIGHT:
                adjustChildViewForGravityInHorizontalMode(childViewInThisLineOrColumn);
                break;

            case GRAVITY_BOTH:
                adjustChildViewForGravityBothInHorizontalMode(childViewInThisLineOrColumn);
                break;
        }
        childViewInThisLineOrColumn.clear();
    }

    private void adjustChildViewForGravityInHorizontalMode(List<View> childViewInThisLineOrColumn) {
        if (childViewInThisLineOrColumn == null || childViewInThisLineOrColumn.size() == 0) {
            return;
        }
        int totalWidth = 0;
        if (childViewInThisLineOrColumn.size() == 1) {
            int originalWidth = childViewInThisLineOrColumn.get(0).getRight() - childViewInThisLineOrColumn.get(0).getLeft();
            ChildViewMarginSize marginSize = getChildViewMargin(childViewInThisLineOrColumn.get(0));
            int rightMargin = marginSize.getRightMargin();
            int leftMargin = marginSize.getLeftMargin();
            totalWidth = originalWidth + rightMargin + leftMargin;
        } else {
            int originalWidth =
                childViewInThisLineOrColumn.get(childViewInThisLineOrColumn.size() - 1).getRight() - childViewInThisLineOrColumn.get(0).getLeft();
            int firstChildViewLeftMargin = getChildViewMargin(childViewInThisLineOrColumn.get(0)).getLeftMargin();
            int lastChildViewRightMargin =
                getChildViewMargin(childViewInThisLineOrColumn.get(childViewInThisLineOrColumn.size() - 1)).getRightMargin();
            totalWidth = originalWidth + firstChildViewLeftMargin + lastChildViewRightMargin;
        }
        // 最长的那行会触及边界所以要减去padding，其他的行是不需要管padding的
        int viewGroupSpace = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        // 如果viewGroupSpace大于totalWidth表明这行不是最长的，所以不需要处理padding
        //if (viewGroupSpace > totalWidth) {
        //    viewGroupSpace = getMeasuredWidth();
        //}

        int leftOffset = 0;
        switch (mGravity) {
            case GRAVITY_LEFT:
                // do nothing
                break;

            case GRAVITY_CENTER:
                leftOffset = (viewGroupSpace - totalWidth) / 2;
                break;

            case GRAVITY_RIGHT:
                leftOffset = (viewGroupSpace - totalWidth);
                break;
        }
        for (int i = 0; i < childViewInThisLineOrColumn.size(); i++) {
            childViewInThisLineOrColumn.get(i).offsetLeftAndRight(leftOffset);
        }
    }

    private void adjustChildViewForGravityBothInHorizontalMode(List<View> childViewInThisLineOrColumn) {
        if (childViewInThisLineOrColumn == null || childViewInThisLineOrColumn.size() <= 1) {
            return;
        }
        int viewGroupSpace = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int totalViewWidth = 0;
        for (int i = 0; i < childViewInThisLineOrColumn.size(); i++) {
            totalViewWidth += childViewInThisLineOrColumn.get(i).getMeasuredWidth();
        }
        int space = (viewGroupSpace - totalViewWidth) / (childViewInThisLineOrColumn.size() - 1);
        for (int i = 1; i < childViewInThisLineOrColumn.size(); i++) {
            int lastViewRight = childViewInThisLineOrColumn.get(i - 1).getRight();
            View childView = childViewInThisLineOrColumn.get(i);
            int width = childView.getMeasuredWidth();
            int newLeft = lastViewRight + space;
            int newRight = newLeft + width;
            childViewInThisLineOrColumn.get(i).layout(newLeft, childView.getTop(), newRight, childView.getBottom());
        }
    }

    private void adjustChildViewPositionDependOnGravityInVerticalMode(List<View> childViewInThisLineOrColumn) {
        switch (mGravity) {
            case GRAVITY_TOP:
                // do nothing
                break;

            case GRAVITY_CENTER:
                adjustChildViewForGravityInVerticalMode(childViewInThisLineOrColumn);
                break;

            case GRAVITY_BOTTOM:
                adjustChildViewForGravityInVerticalMode(childViewInThisLineOrColumn);
                break;

            case GRAVITY_BOTH:
                adjustChildViewForGravityBothInVerticalMode(childViewInThisLineOrColumn);
                break;
        }
        childViewInThisLineOrColumn.clear();
    }

    private void adjustChildViewForGravityInVerticalMode(List<View> childViewInThisLineOrColumn) {
        if (childViewInThisLineOrColumn == null || childViewInThisLineOrColumn.size() == 0) {
            return;
        }
        int totalHeight = 0;
        if (childViewInThisLineOrColumn.size() == 1) {
            int originalHeight = childViewInThisLineOrColumn.get(0).getBottom() - childViewInThisLineOrColumn.get(0).getTop();
            ChildViewMarginSize marginSize = getChildViewMargin(childViewInThisLineOrColumn.get(0));
            int topMargin = marginSize.getTopMargin();
            int bottomMargin = marginSize.getBottomMargin();
            totalHeight = originalHeight + topMargin + bottomMargin;
        } else {
            int originalHeight =
                childViewInThisLineOrColumn.get(childViewInThisLineOrColumn.size() - 1).getBottom() - childViewInThisLineOrColumn.get(0).getTop();
            int firstChildViewTopMargin = getChildViewMargin(childViewInThisLineOrColumn.get(0)).getTopMargin();
            int lastChildViewBottomMargin =
                getChildViewMargin(childViewInThisLineOrColumn.get(childViewInThisLineOrColumn.size() - 1)).getBottomMargin();
            totalHeight = originalHeight + firstChildViewTopMargin + lastChildViewBottomMargin;
        }
        // 最长的那行会触及边界所以要减去padding，其他的行是不需要管padding的
        int viewGroupSpace = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        // 如果viewGroupSpace大于totalWidth表明这行不是最长的，所以不需要处理padding
        //if (viewGroupSpace > totalHeight) {
        //    viewGroupSpace = getMeasuredHeight();
        //}

        int topOffset = 0;
        switch (mGravity) {
            case GRAVITY_CENTER:
                topOffset = (viewGroupSpace - totalHeight) / 2;
                break;

            case GRAVITY_BOTTOM:
                topOffset = (viewGroupSpace - totalHeight);
                break;
        }
        for (int i = 0; i < childViewInThisLineOrColumn.size(); i++) {
            childViewInThisLineOrColumn.get(i).offsetTopAndBottom(topOffset);
        }
    }

    private void adjustChildViewForGravityBothInVerticalMode(List<View> childViewInThisLineOrColumn) {
        if (childViewInThisLineOrColumn == null || childViewInThisLineOrColumn.size() <= 1) {
            return;
        }
        int viewGroupSpace = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int totalViewHeight = 0;
        for (int i = 0; i < childViewInThisLineOrColumn.size(); i++) {
            totalViewHeight += childViewInThisLineOrColumn.get(i).getMeasuredHeight();
        }
        int space = (viewGroupSpace - totalViewHeight) / (childViewInThisLineOrColumn.size() - 1);
        for (int i = 1; i < childViewInThisLineOrColumn.size(); i++) {
            int lastViewBottom = childViewInThisLineOrColumn.get(i - 1).getBottom();
            View childView = childViewInThisLineOrColumn.get(i);
            int height = childView.getMeasuredHeight();
            int newTop = lastViewBottom + space;
            int newBottom = newTop + height;
            childViewInThisLineOrColumn.get(i).layout(childView.getLeft(), newTop, childView.getRight(), newBottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    static class ChildViewMarginSize {
        int leftMargin = 0;
        int topMargin = 0;
        int rightMargin = 0;
        int bottomMargin = 0;

        public ChildViewMarginSize(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
            this.leftMargin = leftMargin;
            this.topMargin = topMargin;
            this.rightMargin = rightMargin;
            this.bottomMargin = bottomMargin;
        }

        public int getLeftMargin() {
            return leftMargin;
        }

        public void setLeftMargin(int leftMargin) {
            this.leftMargin = leftMargin;
        }

        public int getRightMargin() {
            return rightMargin;
        }

        public void setRightMargin(int rightMargin) {
            this.rightMargin = rightMargin;
        }

        public int getTopMargin() {
            return topMargin;
        }

        public void setTopMargin(int topMargin) {
            this.topMargin = topMargin;
        }

        public int getBottomMargin() {
            return bottomMargin;
        }

        public void setBottomMargin(int bottomMargin) {
            this.bottomMargin = bottomMargin;
        }
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

    static class CalculateSize {
        int resultWidth = 0;
        int resultHeight = 0;
        int calculateWidth = 0;
        int calculateHeight = 0;
        int childCount = 0;
        int childViewWidth = 0;
        int childViewHeight = 0;
        int maxWidth = 0;
        int maxHeight = 0;

        public CalculateSize(int resultWidth,
                             int resultHeight,
                             int calculateWidth,
                             int calculateHeight,
                             int childCount,
                             int maxWidth,
                             int maxHeight) {
            this.resultWidth = resultWidth;
            this.resultHeight = resultHeight;
            this.calculateWidth = calculateWidth;
            this.calculateHeight = calculateHeight;
            this.childCount = childCount;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
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

        public int getCalculateWidth() {
            return calculateWidth;
        }

        public void setCalculateWidth(int calculateWidth) {
            this.calculateWidth = calculateWidth;
        }

        public int getCalculateHeight() {
            return calculateHeight;
        }

        public void setCalculateHeight(int calculateHeight) {
            this.calculateHeight = calculateHeight;
        }

        public int getChildCount() {
            return childCount;
        }

        public void setChildCount(int childCount) {
            this.childCount = childCount;
        }

        public int getChildViewWidth() {
            return childViewWidth;
        }

        public void setChildViewWidth(int childViewWidth) {
            this.childViewWidth = childViewWidth;
        }

        public int getChildViewHeight() {
            return childViewHeight;
        }

        public void setChildViewHeight(int childViewHeight) {
            this.childViewHeight = childViewHeight;
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public void setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public void setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
        }
    }

    static class LayoutSize {
        int left;
        int top;
        int viewGroupWidth;
        int viewGroupHeight;
        int childCount;
        int maxWidthInThisColumn;
        int maxHeightInThisLine;

        public LayoutSize(int left,
                          int top,
                          int viewGroupWidth,
                          int viewGroupHeight,
                          int childCount,
                          int maxWidthInThisColumn,
                          int maxHeightInThisLine) {
            this.left = left;
            this.top = top;
            this.viewGroupWidth = viewGroupWidth;
            this.viewGroupHeight = viewGroupHeight;
            this.childCount = childCount;
            this.maxWidthInThisColumn = maxWidthInThisColumn;
            this.maxHeightInThisLine = maxHeightInThisLine;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getViewGroupWidth() {
            return viewGroupWidth;
        }

        public void setViewGroupWidth(int viewGroupWidth) {
            this.viewGroupWidth = viewGroupWidth;
        }

        public int getViewGroupHeight() {
            return viewGroupHeight;
        }

        public void setViewGroupHeight(int viewGroupHeight) {
            this.viewGroupHeight = viewGroupHeight;
        }

        public int getChildCount() {
            return childCount;
        }

        public void setChildCount(int childCount) {
            this.childCount = childCount;
        }

        public int getMaxWidthInThisColumn() {
            return maxWidthInThisColumn;
        }

        public void setMaxWidthInThisColumn(int maxWidthInThisColumn) {
            this.maxWidthInThisColumn = maxWidthInThisColumn;
        }

        public int getMaxHeightInThisLine() {
            return maxHeightInThisLine;
        }

        public void setMaxHeightInThisLine(int maxHeightInThisLine) {
            this.maxHeightInThisLine = maxHeightInThisLine;
        }
    }
}
