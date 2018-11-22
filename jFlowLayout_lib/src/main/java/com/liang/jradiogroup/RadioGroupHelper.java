package com.liang.jradiogroup;

import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RadioGroupHelper {
    private List<View> views = new ArrayList<>();

    private RadioGroupListener listener;

    private View selectedView;

    public void addRadioView(View view) {
        views.add(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(v);
            }
        });

        if (view.isSelected()) {
            selectedView = view;
        }
    }

    public void setListener(RadioGroupListener listener) {
        this.listener = listener;
    }

    public View getChildView(int position) {
        return views.size() > position ? views.get(position) : null;
    }

    public void check(View view) {
        if (views.contains(view)) {
            if (view.isSelected()) {
                dispatchReselected(view);
            } else {
                view.setSelected(true);
                dispatchSelected(view);

                if (selectedView != null) {
                    selectedView.setSelected(false);
                }

                selectedView = view;
            }
        }
    }
    private void dispatchSelected(View view) {
        if (listener != null) {
            listener.onSelected(view);
        }
    }

    private void dispatchReselected(View view) {
        if (listener != null) {
            listener.onReselected(view);
        }
    }

}
