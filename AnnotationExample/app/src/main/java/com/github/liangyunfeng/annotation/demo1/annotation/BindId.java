package com.github.liangyunfeng.annotation.demo1.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yunfeng.l on 2018/6/11.
 * @desc 绑定view的id或者layout的id，包含findViewById和setContentView两个功能
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface BindId {
    int value() default View.NO_ID;
}
