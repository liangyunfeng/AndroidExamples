package com.github.liangyunfeng.leakcanary;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * Created by yunfeng.l on 2018/6/21.
 */

public class ActivityLifecycleCallbackTestApplication extends Application{
    private final String TAG = "TestApplication";

    // Starts as true in order to be notified on first launch
    private boolean isBackground = true;

    @Override
    public void onCreate() {
        super.onCreate();

        listenForForeground();
        listenForScreenTurningOff();
    }

    /**
     * 这个方法包含了一个等级叫TRIM_MEMORY_UI_HIDDEN，用于记录应用即将进入后台运行。
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isBackground = true;
            notifyBackground();
        }
    }

    private void listenForForeground() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (isBackground) {
                    isBackground = false;
                    notifyForeground();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                /*if (activity instanceof ThirdPartyActivity) {
                    return;
                }
                refWatcher.watch(activity);*/
            }
        });
    }

    /**
     * Application.onTrimMemory(int level)在手机熄屏时不回调怎么办？用Intent.ACTION_SCREEN_OFF注册BroadcastReceiver
     */
    private void listenForScreenTurningOff() {
        IntentFilter screenStateFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isBackground = true;
                notifyBackground();
            }
        }, screenStateFilter);
    }

    private void notifyForeground() {
        // This is where you can notify listeners, handle session tracking, etc
    }

    private void notifyBackground() {
        // This is where you can notify listeners, handle session tracking, etc
    }

    public boolean isBackground() {
        return isBackground;
    }
}
