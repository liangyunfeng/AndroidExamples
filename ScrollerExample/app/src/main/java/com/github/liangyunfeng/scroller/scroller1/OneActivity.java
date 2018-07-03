package com.github.liangyunfeng.scroller.scroller1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.liangyunfeng.scroller.R;
import com.github.liangyunfeng.scroller.tradition.TestView;

public class OneActivity extends AppCompatActivity {
    TestView mTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
    }
}
