package com.github.liangyunfeng.annotation.demo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.liangyunfeng.annotation.R;
import com.github.liangyunfeng.annotation.demo1.annotation.BindId;
import com.github.liangyunfeng.annotation.demo1.annotation.OnClick;
import com.github.liangyunfeng.annotation.demo1.impl.BindIdApi;
import com.github.liangyunfeng.annotation.demo1.impl.BindOnClick;

@BindId(R.layout.activity_base_demo1)
public class BaseDemo1Activity extends AppCompatActivity {

    @BindId(R.id.tv)
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindIdApi.bindId(this);
        BindOnClick.bindOnClick(this);
    }

    @OnClick({R.id.btn1,R.id.btn2})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                tv.setText("This is a BindId test.");
                break;
            case R.id.btn2:
                tv.setText("This is a BindOnClick test.");
                break;
            default:
                break;
        }
    }
}
