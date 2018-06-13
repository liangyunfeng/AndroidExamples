package com.github.liangyunfeng.module_api;

import android.app.Activity;

import com.github.liangyunfeng.module_compiler.two.ProxyInfo2;

/**
 * Created by yunfeng.l on 2018/6/12.
 * @desc 实现帮助注入的类
 */

public class TAHelper {
    public static void inject(Activity o) {
        inject(o, o);
    }

    public static void inject(Activity host, Object root) {
        String classFullName = host.getClass().getName() + ProxyInfo2.ClassSuffix;
        try {
            Class proxy = Class.forName(classFullName);
            TA injector = (TA) proxy.newInstance();
            injector.inject(host, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
