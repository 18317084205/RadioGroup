package com.liang.jradiogroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

/**
 * android:orientation=""     vertical(竖直,默认)或者horizontal(水平)
 * 设置有多少行:android:rowCount=""
 * 设置有多少列:android:columnCount=""
 * android:layout_rowSpan = "X"    纵向横跨X行
 * android:layout_columnSpan = "X"     横向横跨X列
 */
public class JRadioGroup extends ViewGroup {
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;

    private int orientation = VERTICAL;
    private int rowCount;
    private int columnCount;
    private int rowSpan;
    private int columnSpan;

    public JRadioGroup(Context context) {
        this(context, null);
    }

    public JRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int mGroupWidth = 0;
        int mGroupHeight = 0;

        int mChildWidth = 0;
        int mChildHeight = 0;

        int mCount = getChildCount();
        for (int i = 0; i < mCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;

            if (childWidth + mChildWidth > mWidth - getPaddingLeft() - getPaddingRight()) {
                mGroupWidth = Math.max(childWidth, mChildWidth);
                mChildWidth = childWidth;
                mGroupHeight += mChildHeight;
                mChildHeight = childHeight;
            } else {
                mChildWidth += childWidth;
                mChildHeight = Math.max(mChildHeight, childHeight);
            }

            if (i == mCount - 1) {
                mGroupWidth = Math.max(mChildWidth, mGroupWidth);
                mGroupHeight += mChildHeight;
            }
        }

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? mWidth : mGroupWidth + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? mHeight : mGroupHeight + getPaddingTop() + getPaddingBottom()
        );

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();
        int mCount = getChildCount();
        for (int i = 0; i < mCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (childLeft + childWidth + lp.rightMargin > getWidth() - getPaddingLeft() - getPaddingRight()) {
                childLeft = getPaddingLeft();
                childTop += childHeight + lp.topMargin + lp.bottomMargin;
            } else {

            }
            int left = childLeft + lp.leftMargin;
            int top = childTop + lp.topMargin;
            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);
            childLeft += childWidth + lp.leftMargin + lp.rightMargin;

        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
