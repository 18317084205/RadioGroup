package com.liang.jradiogroup;

public class Listener {
    public interface OnSelectedListener {
        void onSelected(int position, int row, int column);
    }
    public interface OnClickListener {
        void onSelected(int position, int row, int column);
    }
    public interface OnLongClickListener {
        void onSelected(int position, int row, int column);
    }
}
