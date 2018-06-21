package com.github.liangyunfeng.leakcanary.customizing;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by yunfeng.l on 2018/6/21.
 */

public class BaseLeakApplication extends Application {

    public static RefWatcher getRefWatcher(Context context) {
        BaseLeakApplication application = (BaseLeakApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = installLeakCanary();
    }

    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }
}
