package com.github.liangyunfeng.module_api;

import android.app.Activity;
import android.view.View;

import com.github.liangyunfeng.module_compiler.two.ProxyInfo2;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yunfeng.l on 2018/6/12.
 *
 * @desc 实现帮助注入的类
 */

public class TAHelper2 {
    /**
     * 用来缓存反射出来的类，节省每次都去反射引起的性能问题
     */
    static final Map<Class<?>, Constructor<?>> BINDINGS = new LinkedHashMap<>();

    public static void inject(Activity o) {
        inject(o, o.getWindow().getDecorView());
    }

    public static void inject(Activity host, View root) {
        String classFullName = host.getClass().getName() + ProxyInfo2.ClassSuffix;
        try {
            Constructor constructor = BINDINGS.get(host.getClass());
            if (constructor == null) {
                Class proxy = Class.forName(classFullName);
                constructor = proxy.getDeclaredConstructor(host.getClass(), View.class);
                BINDINGS.put(host.getClass(), constructor);
            }
            constructor.setAccessible(true);
            constructor.newInstance(host, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
