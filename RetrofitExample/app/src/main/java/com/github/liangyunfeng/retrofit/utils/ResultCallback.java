package com.github.liangyunfeng.retrofit.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.internal.$Gson$Types;

import retrofit2.Call;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public abstract class ResultCallback<T> {
    public Type mType;

    public ResultCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types
                .canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public abstract void onError(Call<T> request, Throwable e);

    public abstract void onResponse(T response);
}

