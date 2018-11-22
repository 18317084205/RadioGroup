package com.liang.radiogroup;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.liang.jradiogroup.CCLayout;
import com.liang.jradiogroup.manager.LineLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CCLayout listView = findViewById(R.id.cc);
        CustomAdapter customAdapter = new CustomAdapter(this, createTestData());
        listView.setLayoutManager(new LineLayoutManager(this));
        listView.setAdapter(customAdapter);

        ListView listView1 = findViewById(R.id.lv);
        listView1.setAdapter(customAdapter);
    }

    /**
     * ListView数据创建
     */
    private List<String> createTestData() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            data.add("Love World " + i);
        }
        return data;
    }


    private class CustomAdapter extends BaseAdapter {

        private List<String> mData;
        private LayoutInflater mInflater;

        public CustomAdapter(Context context, List<String> data) {
            mData = data;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (mData == null || mData.size() <= 0) {
                return 0;
            }
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            if (mData == null || mData.size() <= 0
                    || position < 0 || position >= mData.size()) {
                return null;
            }
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.layout_item, null);
            }

            TextView name = convertView.findViewById(R.id.tv_name);
            name.setText( (CharSequence) getItem(position) );

            return convertView;
        }

    }

}
