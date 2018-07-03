package com.github.liangyunfeng.drag.viewdraghelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.liangyunfeng.drag.R;

/**
 * Created by yunfeng.l on 2018/7/2.
 */

public class ViewDragHelperActivity extends AppCompatActivity {

    // 测试smoothSlideViewTo
    Button mBtnTest;
    DragHelperViewGroup mViewGroup;
    private boolean isReverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drag_helper);

        // 测试smoothSlideViewTo
        mViewGroup = (DragHelperViewGroup) findViewById(R.id.activity_viewgroup);
        mBtnTest = (Button) findViewById(R.id.btn_test);
        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewGroup.testSmoothSlide(isReverse);
                isReverse = !isReverse;
            }
        });
    }
}