package com.liang.jradiogroup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

public class ChildLayout extends FrameLayout{
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
}
