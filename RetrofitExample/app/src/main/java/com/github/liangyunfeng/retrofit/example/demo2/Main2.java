package com.github.liangyunfeng.retrofit.example.demo2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public class Main2 {

    /**
     * http://gank.io/api/data/福利/10/1
     */
    public static void request1() throws IOException {
        // 1、首先创建具体网络请求方法
        //public interface ApiService {
         //   @GET("api/data/{type}/{count}/{page}")
         //   Call<ResponseBody> getGirls(@Path("type") String type, @Path("count") int count, @Path("page") int page);
        //}

        // 2、然后创建一个Retrofit实例
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client )
                .build();

        // 3、获取定义的接口实例
        ApiService girlsService = retrofit.create(ApiService.class);

        // 4、调用请求方法，并得到 Call 实例
        int size = 10; int page = 1;
        Call<ResponseBody> girlsCall = girlsService.getGirls("福利", size, page);

        // 5、使用Call实例完成同步或异步请求
        // 同步请求
        ResponseBody response = girlsCall.execute().body();

        // 6、取消这个请求
        //Call 提供了cancel 方法可以取消请求，前提是该请求还没有执行；
        girlsCall.cancel();
    }
}

