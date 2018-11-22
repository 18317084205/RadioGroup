package com.liang.jradiogroup.manager;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Adapter;


public class LineLayoutManager extends LayoutManager {

    private final Context mContext;
    // 当前显示最后一个Item在Adapter中位置
    private int mLastItemPosition = -1;
    // 当前显示第一个Item在Adapter中位置
    private int mFirstItemPosition;

    private int mListTop;
    private int mListTopOffset;

    private int mListLeft;

    // 初始模式，用户还未接触ListView
    private static final int TOUCH_MODE_REST = -1;
    // 触摸Down事件模式
    private static final int TOUCH_MODE_DOWN = 0;
    // 滚动模式
    private static final int TOUCH_MODE_SCROLL = 1;

    // 触摸Down事件时进行记录
    private int mListTopStart;

    // 记录ListView当前处于哪种模式
    private int mTouchMode = TOUCH_MODE_REST;

    // 记录上一次触摸X轴
    private int mTouchStartX;
    // 记录上一次触摸Y轴
    private int mTouchStartY;
    // 仅记录Down事件时Y轴值
    private int mMotionY;

    // 触发滚动的最小移动距离
    private int mTouchSlop;

    public LineLayoutManager(Context context) {
        this(context, VERTICAL);
    }

    public LineLayoutManager(Context context, int orientation) {
        mContext = context;
        mOrientation = orientation;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }


    @Override
    public void measure(int widthMeasureSpec, int heightMeasureSpec) {

    }

    private boolean isVertical() {
        return mOrientation == VERTICAL;
    }


    @Override
    public void layoutChildren() {
        if (mLayout.getChildCount() == 0) {
            mListTop = 0;
            mListLeft = 0;
            mLastItemPosition = -1;
            if (isVertical()) {
                fillVerticalDown(mListTop, 0);
            } else {
                fillHorizontal(mListLeft, 0);
            }
        } else {
            if (isVertical()) {
                final int offset = mListTop + mListTopOffset - mLayout.getChildAt(0).getTop();
                fillVertical(offset);
            } else {
                fillHorizontal(mListLeft, 0);
            }
        }
        childLayout();
    }

    private void fillVertical(int offset) {
// 最后一个item的下边界值就是当前ListView的下边界值
        final int bottomEdge = mLayout.getChildAt(mLayout.getChildCount() - 1).getBottom();
        fillVerticalDown(bottomEdge, offset);

        // 第一个Item的上边界值就是ListVie的上边界值
        final int topEdge = mLayout.getChildAt(0).getTop();
        fillVerticalUp(topEdge, offset);
    }


    private void fillHorizontal(int rightEdge, int offset) {
        Adapter adapter = mLayout.getAdapter();
        int count = adapter.getCount();
        while (rightEdge + offset < mLayout.getWidth() && mLastItemPosition < count - 1) {
            mLastItemPosition++;
            View newRightChild = adapter.getView(mLastItemPosition, null, mLayout);
            addAndMeasureChild(newRightChild);
            rightEdge += newRightChild.getMeasuredWidth();
        }
    }

    private void fillVerticalUp(int topEdge, int offset) {
        Adapter adapter = mLayout.getAdapter();
        while (topEdge + offset > 0 && mFirstItemPosition > 0) {
            // 现在添加的视图时当前子视图后面，所以位置+1
            mLastItemPosition--;

            View newTopChild = adapter.getView(mFirstItemPosition, null, mLayout);
            addAndMeasureChild(newTopChild);
            int childHeight = newTopChild.getMeasuredHeight();
            topEdge -= childHeight;
            // 在顶部添加视图后，更新顶部偏移
            mListTopOffset -= childHeight;
        }
    }

    private void fillVerticalDown(int bottomEdge, int offset) {
        Adapter adapter = mLayout.getAdapter();
        int count = adapter.getCount();
        while (bottomEdge + offset < mLayout.getHeight() && mLastItemPosition < count - 1) {
            mLastItemPosition++;
            View newBottomChild = adapter.getView(mLastItemPosition, null, mLayout);
            addAndMeasureChild(newBottomChild);
            bottomEdge += newBottomChild.getMeasuredHeight();
        }
    }

    private void addAndMeasureChild(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (isVertical()) {
            params.width = mLayout.getWidth() - mLayout.getPaddingLeft() - mLayout.getPaddingRight();
        }

        mLayout.addViewInLayout(child, -1, params, true);
        child.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    private void childLayout() {
        int top = mListTop;
        int left = mListLeft;
        int LayoutWidth = mLayout.getWidth() - mLayout.getPaddingLeft() - mLayout.getPaddingRight();
        for (int i = 0; i < mLayout.getChildCount(); i++) {
            final View child = mLayout.getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            if (isVertical()) {
                left = (LayoutWidth - width) / 2;
            }
            Log.e(TAG, "childLayout: " + left);
            child.layout(left, top, left + width, top + height);
            if (isVertical()) {
                top += height;
            } else {
                left += width;
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(event);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mTouchMode == TOUCH_MODE_DOWN) {
                    startScrollIfNeeded(y);
                } else if (mTouchMode == TOUCH_MODE_SCROLL) {
                    scrollList(y);
                }
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
//                endTouch();
                break;
        }

        return true;
    }


    /**
     * 初始化用于之后触摸事件判断处理的参数
     *
     * @param event
     */
    private void startTouch(MotionEvent event) {
        mTouchStartX = (int) event.getX();
        mMotionY = mTouchStartY = (int) event.getY();

        mListTopStart = mLayout.getChildAt(0).getTop() - mListTopOffset;

        mTouchMode = TOUCH_MODE_DOWN;
    }

    /**
     * 控制ListView进行滚动
     *
     * @param y 当前触摸点Y轴的值
     */
    private void scrollList(int y) { // scrollIfNeeded
        // 当前手指坐在位置与刚触摸到屏幕之间的距离
        // 也就是当前手指在屏幕上Y轴总移动位置
        int scrolledDistance = y - mTouchStartY;
        // 改变当前记录的ListView顶部位置
        mListTop = mListTopStart + scrolledDistance;

        // 关键，要想使相面的计算生效必须重新请求布局
        // 会触发当前onLayout方法，指定Item位置与绘制先关还是在onLayout中
        mLayout.requestLayout();
    }

    /**
     * ListView初始化
     */
    private void initListView(Context context) {


    }


    /**
     * 是否满足滚动条件
     *
     * @param y 当前触摸点Y轴的值
     * @return true 可以滚动
     */
    private boolean startScrollIfNeeded(int y) {
        // 不同，此处模拟AbsListView实现

        final int deltaY = y - mMotionY;
        final int distance = Math.abs(deltaY);

        // 只有移动一定距离之后才认为目的是想让ListView滚动
        if (distance > mTouchSlop) {


            // 记录当前处于滚动状态
            mTouchMode = TOUCH_MODE_SCROLL;
            return true;
        }

        return false;
    }
}
