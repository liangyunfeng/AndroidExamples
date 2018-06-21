package com.github.liangyunfeng.leakcanary;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "startAsyncTask");
                startAsyncTask();
            }
        });
    }

    void startAsyncTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(Void... params) {
                // Do some slow work in background
                Log.v(TAG, "sleep -- start");
                SystemClock.sleep(20000);
                Log.v(TAG, "sleep -- end");
                return null;
            }
        }.execute();
    }

    /**
     * 自己实现一个简易的LeakCanary，
     * 原理如下(更详细的请参考LeakCanary源码)：
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LeakCanary在调用install方法时会启动一个ActivityRefWatcher类，它用于自动监控Activity执行onDestroy方法之后是否发生内存泄露。
        // 如果想要监控Fragment，在Fragment中添加如上的onDestroy方法是有用的。在Activity的onDestroy中是不用显式添加的。
        //RefWatcher refWatcher = LeakApplication.getRefWatcher(this);
        //refWatcher.watch(this);


        /*
        // 开启一个后台线程，这个线程不要持有activity的应用，不然泄露又说怪这个线程
        DetectThread detectThread = new DetectThread("detectThread");
        //用一个弱引用指向这个activity，并且关联到弱引用序列，我的意思是关联。怎么关联法？这个是java的基础，你去搜下WeakReferenceQueue ，当WeakReference所指对象没有强引用时，WeakReference就是被放到这个WeakReferenceQueue 序列
        ReferenceQueue queue = new ReferenceQueue();
        WeakReference<Activity> weakref = new WeakReference(this, queue);
        detectThread.set(queue, weakref);
        detectThread.start();//开始检查
        */
        Log.v(TAG, "onDestroy");
    }
}