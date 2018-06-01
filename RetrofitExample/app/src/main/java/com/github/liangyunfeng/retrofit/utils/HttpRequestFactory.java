package com.github.liangyunfeng.retrofit.utils;

import android.text.TextUtils;

import com.github.liangyunfeng.retrofit.utils.factory.IGsonFactory2;
import com.github.liangyunfeng.retrofit.utils.request.Api;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public class HttpRequestFactory {
    public static final Gson GSON = new Gson();
    public static final String TAG = HttpRequestFactory.class.getSimpleName();
    public static final String BASE_URL = "https://api.imeizan.cn";
    public static String reqParams(Object src) {
        String json = "";
        if (src != null && src instanceof String) {
            json = (String) src;
        } else {
            if (src == null) {
            } else {
                json = GSON.toJson(src);
            }
        }
        return json;
    }

    public static <T> void exec3(Object src, final ResultCallback<T> mCallback) {
        String json = reqParams(src);
        json = AesEncryptionUtil.encrypt(json);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(IGsonFactory2.create(GSON)).build();
        Api api = retrofit.create(Api.class);
        Call<String> doPost = api.doPost(json);
        doPost.enqueue(new retrofit2.Callback<String>() {

            @Override
            public void onFailure(Call<String> request, Throwable error) {
                System.out.println("#失败回调#");
                if (mCallback != null) {
                    mCallback.onError(null, error);
                }
            }

            @Override
            public void onResponse(Call<String> call, Response<String> resp) {
                System.out.println("#成功回调#");
                if (mCallback != null) {
                    if (resp.isSuccessful()) {
                        String finalStr = "";
                        String string = resp.body();
                        // finalStr = AesEncryptionUtil.decrypt(string); // 已经解密过了,注释掉
                        finalStr = string;
                        if (mCallback.mType != null) {
                            if (mCallback.mType == String.class) {
                                if (TextUtils.isEmpty(finalStr)) {      //if (Tools.isEmpty(finalStr)) {
                                    Exception e = new Exception(
                                            "AesEncryptionUtil.decrypt(string) return null!");
                                    exeFailedCallback(call, e, mCallback);
                                } else {
                                    exeSuccessCallback(finalStr, mCallback);
                                }
                            } else {
                                Object o = GSON.fromJson(finalStr,
                                        mCallback.mType);
                                if (o == null) {
                                    Exception e = new Exception(
                                            "mGson.fromJson(finalStr,callback.mType) return null!");
                                    exeFailedCallback(call, e, mCallback);
                                } else {
                                    exeSuccessCallback(o, mCallback);
                                }
                            }
                        } else {
                            Throwable e = new Throwable("mCallback.mType 为空!!!");
                            exeFailedCallback(call, e, mCallback);
                        }
                    } else {
                        Throwable e = new Throwable(resp.toString());
                        exeFailedCallback(call, e, mCallback);
                    }
                }
            }
        });
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void exeFailedCallback(final Call call, final Throwable e,
                                  final ResultCallback<?> callback) {
        if (callback != null) {
            callback.onError(call, e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void exeSuccessCallback(Object o, final ResultCallback callback) {
        if (callback != null) {
            callback.onResponse(o);
        }
    }

    public static OkHttpClient getOkHttpClient() {
        /*// 日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        // 新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        System.out.println(message);
                    }
                });
        loggingInterceptor.setLevel(level);*/
        // 定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        // OkHttp进行添加拦截器loggingInterceptor
        //httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }
}

