package com.github.liangyunfeng.eventbus;

import android.app.Application;

import com.github.liangyunfeng.eventbus.demo1.MyEventBusIndex;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yunfeng.l on 2018/6/14.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 配置EventBus
        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
    }
}
