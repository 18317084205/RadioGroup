package com.liang.radiogroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.liang.jradiogroup.RadioGroupHelper;
import com.liang.jradiogroup.RadioGroupListener;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;

    RadioGroupHelper radioGroupHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);

        radioGroupHelper = new RadioGroupHelper();

        radioGroupHelper.addRadioView(textView);
        radioGroupHelper.addRadioView(textView2);
        radioGroupHelper.addRadioView(textView3);
        radioGroupHelper.addRadioView(textView4);
        radioGroupHelper.addRadioView(textView5);
        radioGroupHelper.addRadioView(textView6);

        radioGroupHelper.setListener(new RadioGroupListener() {
            @Override
            public void onSelected(View view) {
                Log.e("radioGroupHelper", "onSelected" + view.getId());
            }

            @Override
            public void onReselected(View view) {
                Log.e("radioGroupHelper", "onReselected" + view.getId());
            }
        });
    }
}
