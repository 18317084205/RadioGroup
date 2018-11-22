package com.liang.jradiogroup;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class JLayoutParams extends FrameLayout.LayoutParams {

    public int rowSpan;
    public int columnSpan;

    public JLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
        TypedArray type = c.obtainStyledAttributes(attrs, R.styleable.JFlowLayout);
        type.recycle();
    }

    public JLayoutParams(int width, int height) {
        super(width, height);
    }

    public JLayoutParams(ViewGroup.LayoutParams source) {
        super(source);
    }
}
