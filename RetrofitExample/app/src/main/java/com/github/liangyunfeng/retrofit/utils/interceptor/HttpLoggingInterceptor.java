package com.github.liangyunfeng.retrofit.utils.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public class HttpLoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.v("HttpLoggingInterceptor", "");
        Response response = chain.proceed(chain.request());
        Log.v("HttpLoggingInterceptor", "");
        return response;
    }
}
