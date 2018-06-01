package com.github.liangyunfeng.retrofit.utils;

import com.github.liangyunfeng.retrofit.utils.call.BannerResp;
import com.github.liangyunfeng.retrofit.utils.factory.IGsonFactory2;
import com.github.liangyunfeng.retrofit.utils.request.Api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public class RetrofitHelper {
    /**
     * 基本路径
     */
    public static final String BASE_URL = "https://api.imeizan.cn";

    public static void tst(String json) {
        json = AesEncryptionUtil.encrypt(json); // 加密json,不必在意这些细节
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(IGsonFactory2.create(gson)).build();
        Api service = retrofit.create(Api.class);
        Call<BannerResp> call = service.doPost(json);
        call.enqueue(new Callback<BannerResp>() {

            @Override
            public void onResponse(Call<BannerResp> call,
                                   Response<BannerResp> resp) {
                BannerResp body = resp.body();
                System.out.println("异步返回:" + body.toString());
            }

            @Override
            public void onFailure(Call<BannerResp> msg, Throwable error) {
                System.out.println(msg.toString() + "|" + error.getMessage());
            }
        });
        final Call<BannerResp> clone = call.clone();
        new Thread() {
            public void run() {
                try {
                    Response<BannerResp> execute = clone.execute();
                    System.out.println("同步返回:" + execute.body().toString());
                } catch (Exception e) {
                    System.out.println("同步返回:" + e.getMessage());
                }
            }

            ;
        }.start();

    }

    public static OkHttpClient getOkHttpClient() {
        // 日志显示级别
        /*HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
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
