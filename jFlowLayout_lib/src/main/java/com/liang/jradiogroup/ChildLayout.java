package com.liang.jradiogroup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

public class ChildLayout extends FrameLayout {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public int position;
    public int row;
    public int column;

    public ChildLayout(@NonNull Context context) {
        super(context);
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

}
