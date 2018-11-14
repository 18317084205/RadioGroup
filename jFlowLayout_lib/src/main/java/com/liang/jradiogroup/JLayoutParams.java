package com.liang.jradiogroup;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class JLayoutParams extends ViewGroup.MarginLayoutParams {

    public int rowSpan;
    public int columnSpan;

    public JLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);

        TypedArray type = c.obtainStyledAttributes(attrs, R.styleable.JFlowLayout);
        rowSpan = (int) type.getDimension(R.styleable.JFlowLayout_rowSpan, 1);
        columnSpan = (int) type.getDimension(R.styleable.JFlowLayout_columnSpan, 1);
        type.recycle();
    }

    public JLayoutParams(int width, int height) {
        super(width, height);
    }

    public JLayoutParams(ViewGroup.LayoutParams source) {
        super(source);
    }
}
