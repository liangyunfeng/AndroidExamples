package com.github.liangyunfeng.annotation.demo1.impl;

import android.app.Activity;
import android.view.View;

import com.github.liangyunfeng.annotation.demo1.annotation.BindId;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yunfeng.l on 2018/6/11.
 */

public class BindIdApi {
    public static void bindId(Activity activity) {
        Class<?> cls = activity.getClass();
        // 使用反射调用setContentView
        if(cls.isAnnotationPresent(BindId.class)) {
            // 得到这个类的BindId注解
            BindId mId = (BindId) cls.getAnnotation(BindId.class);
            // 得到这个注解的值
            int id = mId.value();
            try {
                Method method = cls.getMethod("setContentView", int.class);
                method.setAccessible(true);
                method.invoke(activity, id);
            } catch(NoSuchMethodException e) {
                e.printStackTrace();
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            } catch(InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        // 使用反射调用findViewById
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(BindId.class)) {
                BindId mId = field.getAnnotation(BindId.class);
                int id = mId.value();
                // 使用反射调用findViewById，并为字段设置值
                try {
                    Method method = cls.getMethod("findViewById", int.class);
                    method.setAccessible(true);
                    Object view = method.invoke(activity, id);
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 这种看起来是不是更加简洁方便呢，既然传入了activity，我们直接调用Activity的方法就可以了，何必再去反射方法呢，哈哈～
     * @param activity
     */
    public static void bindId2(Activity activity) {
        Class<?> cls = activity.getClass();
        if(cls.isAnnotationPresent(BindId.class)) {
            // 得到这个类的BindId注解
            BindId mId = (BindId) cls.getAnnotation(BindId.class);
            // 得到注解的值
            int id = mId.value();
            activity.setContentView(id);
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(BindId.class)) {
                BindId mId = field.getAnnotation(BindId.class);
                int id = mId.value();
                View view = activity.findViewById(id);
                try {
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
