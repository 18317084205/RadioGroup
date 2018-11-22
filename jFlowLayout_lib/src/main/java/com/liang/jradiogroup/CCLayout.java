package com.liang.jradiogroup;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.liang.jradiogroup.manager.LayoutManager;

public class CCLayout extends AdapterView<Adapter> {

    private Adapter mAdapter;

    private LayoutManager mLayoutManager;

    public CCLayout(Context context) {
        this(context, null);
    }

    public CCLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CCLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        layoutManager.setLayout(this);
        mLayoutManager = layoutManager;
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        removeAllViewsInLayout();
        requestLayout();
    }


    @Override
    public View getSelectedView() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setSelection(int position) {
        throw new UnsupportedOperationException("Not supported");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mLayoutManager == null) {
            return;
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {

        if (mAdapter == null|| mLayoutManager == null) {
            return;
        }

        mLayoutManager.layoutChildren();

    }

    public boolean addViewInLayout(View child, int index, LayoutParams params, boolean preventRequestLayout) {
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getChildCount() == 0) {
            return false;
        }

        return mLayoutManager.onTouchEvent(event);
    }
}
