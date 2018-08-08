package com.github.liangyunfeng.coordinator.one;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.liangyunfeng.coordinator.R;

/**
 * Created by yunfeng.l on 2018/7/4.
 */

public class OneActivity  extends AppCompatActivity {

    Button mBtnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        mBtnTest = (Button) findViewById(R.id.btn_nested_scroll);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBtnTest.setNestedScrollingEnabled(true);
        }

        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBtnTest.startNestedScroll(View.SCROLL_AXIS_HORIZONTAL);
                }
            }
        });

        /*
        // 我们看上面的代码，当一个 View 只有在版本在 Lollipop 及以上时，它才能调用嵌套滑动相关的 api。如果是 5.0 版本以下呢？其实系统做了兼容。
        mBtnTest = (Button) findViewById(R.id.btn_nested_scroll);
        ViewCompat.setNestedScrollingEnabled(mBtnTest,true);
        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ViewCompat.startNestedScroll(mBtnTest,View.SCROLL_AXIS_HORIZONTAL);
                }
            }
        });*/
    }
}