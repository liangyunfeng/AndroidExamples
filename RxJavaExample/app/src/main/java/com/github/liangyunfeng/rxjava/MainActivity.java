package com.github.liangyunfeng.rxjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.liangyunfeng.rxjava.demo1.BaseDemo1Activity;
import com.github.liangyunfeng.rxjava.demo2.BaseDemo2Activity;
import com.github.liangyunfeng.rxjava.demo3.BaseDemo3Activity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @OnClick({R.id.btn1,R.id.btn2,R.id.btn3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Intent intent1 = new Intent(MainActivity.this, BaseDemo1Activity.class);
                startActivity(intent1);
                break;
            case R.id.btn2:
                Intent intent2 = new Intent(MainActivity.this, BaseDemo2Activity.class);
                startActivity(intent2);
                break;
            case R.id.btn3:
                Intent intent3 = new Intent(MainActivity.this, BaseDemo3Activity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
