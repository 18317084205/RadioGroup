package com.liang.jradiogroup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * android:orientation=""     vertical(竖直,默认)或者horizontal(水平)
 * 设置有多少行:android:rowCount=""
 * 设置有多少列:android:columnCount=""
 * android:layout_rowSpan = "X"    纵向横跨X行
 * android:layout_columnSpan = "X"     横向横跨X列
 */
public class JFlowLayout extends ViewGroup {

    public static final String TAG = JFlowLayout.class.getSimpleName();

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    public static final int TYPE_FLOW = 0;
    public static final int TYPE_COLUMN = 1;
    public static final int TYPE_ROW = 2;

    private int mOrientation = HORIZONTAL;
    private int mRowCount = 0;
    private int mColumnCount = 3;

    private int mType;


    public JFlowLayout(Context context) {
        this(context, null);
    }

    public JFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (mRowCount > 0 && mColumnCount == 0) {
            mType = TYPE_ROW;
        } else if (mRowCount == 0 && mColumnCount == 0) {
            mType = TYPE_FLOW;
        } else {
            mType = TYPE_COLUMN;
        }

    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        ChildLayout childLayout = new ChildLayout(getContext());
        childLayout.setBackgroundColor(Color.RED);
        childLayout.addView(child, params);
        LayoutParams layoutParams = new LayoutParams(params);
        super.addView(childLayout, index, layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childWidth = 0;
        int mCount = getChildCount();

        switch (mType) {
            case TYPE_FLOW:
                break;
            case TYPE_COLUMN:
                childWidth = (width - getPaddingLeft() - getPaddingRight()) / mColumnCount;
                if (mOrientation == VERTICAL) {
                    mRowCount = mCount % mColumnCount == 0 ? mCount / mColumnCount : (mCount / mColumnCount + 1);
                }
                break;
            case TYPE_ROW:
                mColumnCount = mCount % mRowCount == 0 ? mCount / mRowCount : (mCount / mRowCount + 1);
                childWidth = (width - getPaddingLeft() - getPaddingRight()) / mColumnCount;
                break;
        }

        int[] size;

        if (mOrientation == HORIZONTAL) {
            size = measureChildrenWithHorizontal(widthMeasureSpec, heightMeasureSpec, childWidth);
        } else {
            if (mType == TYPE_FLOW) {
                size = measureChildrenWithHorizontal(widthMeasureSpec, heightMeasureSpec, childWidth);
            } else {
                size = measureChildrenWithVertical(widthMeasureSpec, heightMeasureSpec, childWidth, mRowCount);
            }
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? width : size[0] + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? height : size[1] + getPaddingTop() + getPaddingBottom()
        );

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mCount = getChildCount();
        for (int i = 0; i < mCount; i++) {
            ChildLayout child = (ChildLayout) getChildAt(i);
            if (child == null) {
                continue;
            }
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            child.layout(child.left, child.top, child.right, child.bottom);
        }
    }

    private int[] measureChildrenWithHorizontal(int widthMeasureSpec, int heightMeasureSpec, int defChildWidth) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int maxWidth = 0;
        int maxHeight = 0;

        int maxChildWidth = 0;
        int maxChildHeight = 0;


        int mCount = getChildCount();
        for (int i = 0; i < mCount; i++) {
            final ChildLayout child = (ChildLayout) getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childWidth = 0;
            if (mType == TYPE_FLOW) {
                childWidth = child.getMeasuredWidth();
            } else {
                childWidth = defChildWidth * Math.min(child.rowSpan, mColumnCount);
            }
            Log.e(TAG, "measureChildrenWithHorizontal childWidth: " + childWidth);
            int childHeight = child.getMeasuredHeight();
            if (maxChildWidth + childWidth > width - getPaddingLeft() - getPaddingRight()) {
                maxWidth = Math.max(childWidth, maxChildWidth);
                maxChildWidth = childWidth;
                maxHeight += maxChildHeight;
                child.left = getPaddingLeft();
                maxChildHeight = childHeight;
            } else {
                child.left = maxChildWidth;
                maxChildWidth += childWidth;
                maxChildHeight = Math.max(maxChildHeight, childHeight);
            }
            child.top = maxHeight;
            if (i == mCount - 1) {
                maxWidth = Math.max(maxChildWidth, maxWidth);
                maxHeight += maxChildHeight;
            }

            child.right = child.left + childWidth;
            child.bottom = child.top + childHeight;
        }

        return new int[]{maxWidth, maxHeight};
    }

    private int[] measureChildrenWithVertical(int widthMeasureSpec, int heightMeasureSpec, int childWidth, int rowCount) {

        int maxWidth = 0;
        int maxHeight = 0;

        int maxChildWidth = 0;
        int maxChildHeight = 0;

        int row = 0;

        int mCount = getChildCount();

        for (int i = 0; i < mCount; i++) {

            final ChildLayout child = (ChildLayout) getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int childHeight = child.getMeasuredHeight();

            if (getParent() instanceof HorizontalScrollView) {
                childWidth = child.getMeasuredWidth();
            }

            if (row == rowCount) {
                maxHeight = Math.max(maxChildHeight, childHeight);
                maxChildHeight = childHeight;
                child.left = maxWidth += maxChildWidth;
                child.top = getPaddingTop();
                maxChildWidth = childWidth;
                row = 1;
            } else {
                maxChildWidth = Math.max(maxChildWidth, childWidth);
                child.left = maxWidth;
                child.top = maxChildHeight;
                maxChildHeight += childHeight;
                row++;
            }

            if (i == 0) {
                child.left = getPaddingLeft();
                child.top = getPaddingTop();
            }

            child.right = child.left + childWidth;
            child.bottom = child.top + childHeight;
            if (i == mCount - 1) {
                maxWidth = child.right;
                maxHeight = Math.max(maxChildHeight, maxHeight);
            }
        }

        return new int[]{maxWidth, maxHeight};
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new JLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new JLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new JLayoutParams(p);
    }
}
