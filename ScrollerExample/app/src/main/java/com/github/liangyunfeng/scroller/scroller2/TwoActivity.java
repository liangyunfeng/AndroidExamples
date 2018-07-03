package com.github.liangyunfeng.scroller.scroller2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.liangyunfeng.scroller.R;
import com.github.liangyunfeng.scroller.tradition.TestView;

public class TwoActivity extends AppCompatActivity {
    TestView mTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }
}
