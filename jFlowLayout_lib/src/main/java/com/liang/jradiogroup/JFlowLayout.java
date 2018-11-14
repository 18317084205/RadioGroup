package com.liang.jradiogroup;

import android.content.Context;
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

    private Map<Integer, ChildLayout> mChildParams = new LinkedHashMap<>();

    private int mOrientation = VERTICAL;
    private int mRowCount = 3;
    private int mColumnCount = 0;
    private int mRowSpan;
    private int mColumnSpan;

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
        JLayoutParams lp = (JLayoutParams) params;
        ChildLayout childLayout = new ChildLayout(getContext());
        childLayout.rowSpan = lp.rowSpan;
        childLayout.columnSpan = lp.columnSpan;
        childLayout.leftMargin = lp.leftMargin;
        childLayout.topMargin = lp.topMargin;
        childLayout.rightMargin = lp.rightMargin;
        childLayout.bottomMargin = lp.bottomMargin;
        childLayout.addView(child);
        super.addView(childLayout, index, lp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int[] size;
        if (mOrientation == HORIZONTAL) {
            size = measureChildrenWithHorizontal(widthMeasureSpec, heightMeasureSpec);
        } else {
            size = measureChildrenWithVertical(widthMeasureSpec, heightMeasureSpec);
        }

        Log.e(TAG, "Measure mGroupWidth:" + size[0]);
        Log.e(TAG, "Measure mGroupHeight:" + size[1]);
        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? mWidth : size[0] + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? mHeight : size[1] + getPaddingTop() + getPaddingBottom()
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

    private int[] measureChildrenWithHorizontal(int widthMeasureSpec, int heightMeasureSpec) {

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);

        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();

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

            int childWidth = 0;
            switch (mType) {
                case TYPE_FLOW:
                    childWidth = child.getMeasuredWidth();
                    break;
                case TYPE_COLUMN:
                    childWidth = (mWidth - getPaddingLeft() - getPaddingRight() -
                            lp.leftMargin * mColumnCount - lp.rightMargin * mColumnCount) / mColumnCount;
                    break;
                case TYPE_ROW:
                    int columnCount = mCount % mRowCount == 0 ? mCount / mRowCount : (mCount / mRowCount + 1);
                    childWidth = (mWidth - getPaddingLeft() - getPaddingRight() -
                            lp.leftMargin * columnCount - lp.rightMargin * columnCount) / columnCount;
                    break;

            }

            int childHeight = child.getMeasuredHeight();
            if (childLeft + childWidth + lp.rightMargin > mWidth - getPaddingLeft() - getPaddingRight()) {
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
//            ChildParams childParams = createParams(childLeft, childTop, childWidth, childHeight, lp);
//            mChildParams.put(child.getId(), childParams);
            childLeft += childWidth + lp.leftMargin + lp.rightMargin;
        }

        return new int[]{mGroupWidth, mGroupHeight};
    }

    private int[] measureChildrenWithVertical(int widthMeasureSpec, int heightMeasureSpec) {

        if (mType == TYPE_FLOW) {
            return measureChildrenWithHorizontal(widthMeasureSpec, heightMeasureSpec);
        }

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);

        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();

        int mGroupWidth = 0;
        int mGroupHeight = 0;

        int mChildWidth = 0;
        int mChildHeight = 0;

        int rowCount = mRowCount;
        int row = 0;

        int mCount = getChildCount();
        for (int i = 0; i < mCount; i++) {

            final ChildLayout child = (ChildLayout) getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int childWidth = 0;
            switch (mType) {
                case TYPE_FLOW:
                    break;
                case TYPE_COLUMN:
                    childWidth = (mWidth - getPaddingLeft() - getPaddingRight() -
                            child.leftMargin * mColumnCount - child.rightMargin * mColumnCount) / mColumnCount;
                    rowCount = mCount % mColumnCount == 0 ? mCount / mColumnCount : (mCount / mColumnCount + 1);
                    break;
                case TYPE_ROW:
                    if (getParent() instanceof HorizontalScrollView) {
                        childWidth = child.getMeasuredWidth();
                    } else {
                        int columnCount = mCount % mRowCount == 0 ? mCount / mRowCount : (mCount / mRowCount + 1);
                        childWidth = (mWidth - getPaddingLeft() - getPaddingRight() -
                                child.leftMargin * columnCount - child.rightMargin * columnCount) / columnCount;
                    }
                    break;
            }
            int childHeight = child.getMeasuredHeight();
            if (row == rowCount) {
                mGroupHeight = Math.max(mChildHeight, childHeight + child.topMargin + child.bottomMargin);
                mChildHeight = childHeight + child.topMargin + child.bottomMargin;
                childLeft += mChildWidth;
                childTop = getPaddingTop();
                mChildWidth = childWidth + child.leftMargin + child.rightMargin;
                row = 1;
            } else {
                mChildWidth = Math.max(mChildWidth, childWidth + child.leftMargin + child.rightMargin);
                mChildHeight += childHeight + child.topMargin + child.bottomMargin;
                row++;
            }

            restParams(child, childLeft, childTop, childWidth, childHeight);
            childTop += childHeight + child.topMargin + child.bottomMargin;

            if (i == mCount - 1) {
                mGroupWidth = child.right + child.rightMargin + getPaddingRight();
                mGroupHeight = Math.max(mChildHeight, mGroupHeight);
            }
        }

        return new int[]{mGroupWidth, mGroupHeight};
    }


    private void restParams(ChildLayout child, int childLeft, int childTop, int childWidth, int childHeight) {
        child.left = childLeft + child.leftMargin;
        child.top = childTop + child.topMargin;
        child.right = child.left + childWidth;
        child.bottom = child.top + childHeight;
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
