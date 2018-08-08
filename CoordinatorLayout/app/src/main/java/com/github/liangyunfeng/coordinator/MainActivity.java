package com.github.liangyunfeng.coordinator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.liangyunfeng.coordinator.four.FourActivity;
import com.github.liangyunfeng.coordinator.one.OneActivity;
import com.github.liangyunfeng.coordinator.three.ThreeActivity;
import com.github.liangyunfeng.coordinator.two.TwoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Intent intent1 = new Intent(MainActivity.this, OneActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn2:
                Intent intent2 = new Intent(MainActivity.this, TwoActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn3:
                Intent intent3 = new Intent(MainActivity.this, ThreeActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn4:
                Intent intent4 = new Intent(MainActivity.this, FourActivity.class);
                startActivity(intent4);
                break;
            case R.id.btn5:

                break;
            default:
                break;
        }
    }
}
