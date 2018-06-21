package com.github.liangyunfeng.leakcanary;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * Created by yunfeng.l on 2018/6/21.
 */

public class DetectThread extends Thread {
    private final String TAG = "DetectThread";
    private ReferenceQueue queue;
    private WeakReference<Activity> weakref;

    public DetectThread(String name) {
        setName(name);
    }

    public void set(ReferenceQueue queue, WeakReference<Activity> weakref) {
        this.queue = queue;
        this.weakref = weakref;
    }

    @Override
    public void run() {
        super.run();
        try {
            Log.v(TAG, "waiting...");
            Thread.sleep(5000);//睡个5秒去检测
            Log.v(TAG, "gc - start");
            System.gc();   //回收下垃圾先
            Log.v(TAG, "gc - end");
            Thread.sleep(100);
            Log.v(TAG, "begin to detect...");
            if (weakref.get() == null) {
                Log.i(TAG, "没有泄露，做的很好");
            } else {
                Log.i(TAG, "泄露了傻逼！！！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
