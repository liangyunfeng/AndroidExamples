package com.github.liangyunfeng.annotation.demo1.impl;

import android.app.Activity;
import android.view.View;

import com.github.liangyunfeng.annotation.demo1.annotation.OnClick;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yunfeng.l on 2018/6/11.
 */

public class BindOnClick {
    public static void bindOnClick(final Activity activity) {
        Class<?> cls = activity.getClass();
        // 获取当前Activity的所有方法，包括私有
        Method methods[] = cls.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            if(method.isAnnotationPresent(OnClick.class)) {
                // 得到这个类的OnClick注解
                OnClick mOnClick = (OnClick) method.getAnnotation(OnClick.class);
                // 得到注解的值
                int[] ids = mOnClick.value();
                for (int j = 0; j < ids.length; j++) {
                    final View view = activity.findViewById(ids[j]);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 反射指定的点击方法
                            try {
                                // 私有方法需要设置true才能访问
                                method.setAccessible(true);
                                method.invoke(activity, view);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }
}
