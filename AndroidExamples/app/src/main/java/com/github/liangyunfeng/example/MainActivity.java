package com.github.liangyunfeng.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.liangyunfeng.example.lock.LockActivity;

public class MainActivity extends AppCompatActivity {

    Button mBtn1, mBtn2, mBtn3, mBtn4, mBtn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn3 = (Button) findViewById(R.id.btn2);
        mBtn4 = (Button) findViewById(R.id.btn3);
        mBtn5 = (Button) findViewById(R.id.btn4);




    }

    public void handleClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Intent intent = new Intent(MainActivity.this, LockActivity.class);
                startActivity(intent);
                break;
            case R.id.btn2:

                break;
            case R.id.btn3:

                break;
            case R.id.btn4:

                break;
            case R.id.btn5:

                break;
            default:
                break;
        }
    }

}
