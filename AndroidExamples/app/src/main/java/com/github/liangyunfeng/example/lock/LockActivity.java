package com.github.liangyunfeng.example.lock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.liangyunfeng.example.MainActivity;
import com.github.liangyunfeng.example.R;
import com.github.liangyunfeng.example.lock.view.LockView;

/**
 * Created by lyf on 2018/5/31.
 */

public class LockActivity extends AppCompatActivity {

    private static final String TAG = "LockActivity";

    LockView mLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        mLockView = (LockView) findViewById(R.id.lock_view);
        mLockView.setCallBack(new LockView.CallBack() {
            @Override
            public void onLocked() {
                Log.v(TAG, "onLocked()");
                Toast.makeText(LockActivity.this, "Locked", Toast.LENGTH_SHORT).show();
        }

            @Override
            public void onUnLocked() {
                Log.v(TAG, "onUnLocked()");
                Toast.makeText(LockActivity.this, "UnLocked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
