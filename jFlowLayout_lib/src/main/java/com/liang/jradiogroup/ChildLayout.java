package com.liang.jradiogroup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ChildLayout extends FrameLayout {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public int leftMargin;
    public int topMargin;
    public int rightMargin;
    public int bottomMargin;

    public int position;
    public int row;
    public int column;

    public int rowSpan;
    public int columnSpan;

    public ChildLayout(@NonNull Context context) {
        super(context);
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        JLayoutParams lp = (JLayoutParams) params;
        rowSpan = lp.rowSpan;
        columnSpan = lp.columnSpan;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int maxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }
        }
        super.onMeasure(maxWidth, maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int childLeft = getPaddingLeft() + lp.leftMargin;
                int childTop = getPaddingTop() + lp.topMargin;
                int childRight = getWidth() - getPaddingRight() - lp.rightMargin;
                int childBottom = getHeight() - getPaddingBottom() - lp.bottomMargin;

                child.layout(childLeft, childTop, childRight, childBottom);
            }
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new JLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new JLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

}
