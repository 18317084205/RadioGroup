package com.liang.jradiogroup.manager;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.liang.jradiogroup.CCLayout;
import com.liang.jradiogroup.JFlowLayout;

public abstract class LayoutManager {
    public static final String TAG = LayoutManager.class.getSimpleName();
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    protected int mOrientation;
    protected CCLayout mLayout;

    public abstract void measure(int widthMeasureSpec, int heightMeasureSpec);

    public abstract void layoutChildren();

    public abstract boolean onTouchEvent(MotionEvent event);

    public void setLayout(CCLayout layout) {
        this.mLayout = layout;
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation:" + orientation);
        }

        if (orientation != mOrientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    private void requestLayout() {
        if (mLayout != null) {
            mLayout.requestLayout();
        }
    }


}
