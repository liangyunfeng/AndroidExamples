package com.github.liangyunfeng.module_api;

/**
 * Created by yunfeng.l on 2018/6/12.
 */

public interface TA<T> {
    void inject(T t, Object o);
}
