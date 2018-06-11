package com.github.liangyunfeng.rxjava.demo1;

/**
 * Created by yunfeng.l on 2018/6/8.
 */

public interface Callback<T> {
    public void success(T t);

    public void failure(Exception error);
}