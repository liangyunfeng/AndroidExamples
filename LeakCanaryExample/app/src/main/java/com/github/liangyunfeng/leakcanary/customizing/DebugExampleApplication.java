package com.github.liangyunfeng.leakcanary.customizing;

import android.app.Activity;
import android.os.Bundle;

//import com.squareup.leakcanary.AndroidExcludedRefs;
//import com.squareup.leakcanary.LeakCanary;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;

/**
 * Created by yunfeng.l on 2018/6/21.
 */

public class DebugExampleApplication extends BaseLeakApplication{
    @Override protected RefWatcher installLeakCanary() {
        // 4. Ignoring specific references
        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
                .instanceField("com.example.ExampleClass", "exampleField")
                .build();

        // 5. Not watching specific activity classes
        LeakCanary.enableDisplayLeakActivity(this);

        // Build a customized RefWatcher
        final RefWatcher refWatcher = LeakCanary.refWatcher(this)
                .watchDelay(10, TimeUnit.SECONDS)           // 1. watchDelay.
                .maxStoredHeapDumps(42)                     // 2. LeakCanary saves up to 7 heap dumps. To change that.
                .listenerServiceClass(LeakUploadService.class)  // 3. Uploading to a server.
                .excludedRefs(excludedRefs)                 // 4. Ignoring specific references
                .buildAndInstall();

        // 5. Not watching specific activity classes
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            public void onActivityDestroyed(Activity activity) {
                if (activity instanceof ThirdPartyActivity) {
                    return;
                }
                refWatcher.watch(activity);
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

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
        });

        return refWatcher;
    }
}