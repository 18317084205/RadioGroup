package com.liang.jradiogroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * android:orientation=""     vertical(竖直,默认)或者horizontal(水平)
 * 设置有多少行:android:rowCount=""
 * 设置有多少列:android:columnCount=""
 * android:layout_rowSpan = "X"    纵向横跨X行
 * android:layout_columnSpan = "X"     横向横跨X列
 */
public class JRadioGroup extends ViewGroup {

    public static final String TAG = JRadioGroup.class.getSimpleName();
    /**
     * The horizontal orientation.
     */
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;

    /**
     * The vertical orientation.
     */
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private Map<Integer, ChildParams> mChildParams = new LinkedHashMap<>();

    private int mOrientation = HORIZONTAL;
    private int mRowCount = 0;
    private int mColumnCount = 0;
    private int mRowSpan;
    private int mColumnSpan;

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

        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();

        mChildParams.clear();
        if (mRowCount > 0 && mColumnCount > 0) {
            if (mOrientation == HORIZONTAL) {
                int mChildWidth = 0;
                int mChildHeight = 0;

                int mCount = getChildCount();

                for (int i = 0; i < mCount; i++) {
                    final View child = getChildAt(i);
                    if (child.getVisibility() == View.GONE) {
                        continue;
                    }
                    ChildParams childParams = new ChildParams();
                    measureChild(child, widthMeasureSpec, heightMeasureSpec);
                    MarginLayoutParams lp = (MarginLayoutParams) child
                            .getLayoutParams();
                    Log.e(TAG, "measure mColumnCount:" + mWidth / mColumnCount);
                    int childWidth = mWidth / mColumnCount - lp.leftMargin
                            - lp.rightMargin;
                    int childHeight = child.getMeasuredHeight() + lp.topMargin
                            + lp.bottomMargin;
                    Log.e(TAG, "measure childWidth:" + childWidth);
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
            } else {

            }
        } else {
            int mChildWidth = 0;
            int mChildHeight = 0;

            int mCount = getChildCount();
            for (int i = 0; i < mCount; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                ChildParams childParams = new ChildParams();
                measureChild(child, widthMeasureSpec, heightMeasureSpec);

                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                Log.e(TAG, "onLayout childWidth:" + childWidth);
                if (childLeft + childWidth + lp.rightMargin > getWidth() - getPaddingLeft() - getPaddingRight()) {
                    mGroupWidth = Math.max(childWidth + lp.leftMargin + lp.rightMargin, mChildWidth);
                    mChildWidth = childWidth + lp.leftMargin + lp.rightMargin;
                    mGroupHeight += mChildHeight;
                    mChildHeight = childHeight + lp.topMargin + lp.bottomMargin;
                    childLeft = getPaddingLeft();
                    childTop += childHeight + lp.topMargin + lp.bottomMargin;
                } else {
                    mChildWidth += childWidth + lp.leftMargin + lp.rightMargin;
                    mChildHeight = Math.max(mChildHeight, childHeight + lp.topMargin + lp.bottomMargin);
                }

                if (i == mCount - 1) {
                    mGroupWidth = Math.max(mChildWidth, mGroupWidth);
                    mGroupHeight += mChildHeight;
                }

                childParams.left = childLeft + lp.leftMargin;
                childParams.top = childTop + lp.topMargin;
                childParams.right = childParams.left + child.getMeasuredWidth();
                childParams.bottom = childParams.top + child.getMeasuredHeight();
                mChildParams.put(child.getId(), childParams);
                childLeft += childWidth + lp.leftMargin + lp.rightMargin;
            }
        }


        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? mWidth : mGroupWidth + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? mHeight : mGroupHeight + getPaddingTop() + getPaddingBottom()
        );

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int mCount = getChildCount();
        for (int i = 0; i < mCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            ChildParams params = mChildParams.get(child.getId());
            if (params == null) {
                continue;
            }
            child.layout(params.left, params.top, params.right, params.bottom);
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
