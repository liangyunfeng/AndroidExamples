package com.github.liangyunfeng.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.liangyunfeng.retrofit.iciba.GetRequest_Interface;
import com.github.liangyunfeng.retrofit.iciba.Translation;
import com.github.liangyunfeng.retrofit.utils.RetrofitHelper;
import com.github.liangyunfeng.retrofit.utils.call.BannerReq;
import com.github.liangyunfeng.retrofit.youdao.PostRequest_Interface;
import com.github.liangyunfeng.retrofit.youdao.Translation1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button mBtnIciba, mBtnYoudao, mBtnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnIciba = (Button)findViewById(R.id.iciba);
        mBtnYoudao = (Button)findViewById(R.id.youdao);
        mBtnTest = (Button)findViewById(R.id.test);
        mBtnIciba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestIciba();
            }
        });

        mBtnYoudao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestYoudao();
            }
        });

        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestTest();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ProtoConverterFactory.create())        // 支持Prototocobuff解析
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())   // 支持RxJava
                .build();


    }

    private void requestIciba() {
        // 步骤4：创建Retrofit对象
        Retrofit retorfit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")    // 设置 网络请求 url
                .addConverterFactory(GsonConverterFactory.create()) // 设置使用Gson解析（记得加入依赖）
                .build();

        // 步骤5：创建 网络请求接口 的实例
        GetRequest_Interface request = retorfit.create(GetRequest_Interface.class);

        // 对 发送请求 进行封装
        Call<Translation> call = request.getCall();

        // 步骤6：发送网络请求（异步）
        call.enqueue(new Callback<Translation>() {
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                // 步骤7：处理返回的数据结果
                response.body().show();
            }

            @Override
            public void onFailure(Call<Translation> call, Throwable t) {
                System.out.println("连接失败");
            }
        });
    }

    private void requestYoudao() {
        // 步骤4：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/")    // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        // 步骤5：创建 网络请求接口 的实例
        PostRequest_Interface request = retrofit.create(PostRequest_Interface.class);

        Call<Translation1> call = request.getCall("I love you");

        // 步骤6：发送网络请求（异步）
        call.enqueue(new Callback<Translation1>() {
            @Override
            public void onResponse(Call<Translation1> call, Response<Translation1> response) {
                // 步骤7：处理返回的数据结果：输出翻译的内容
                System.out.println(response.body().getTranslateResult().get(0).get(0).getTgt());
            }

            @Override
            public void onFailure(Call<Translation1> call, Throwable t) {
                System.out.println("连接失败");
                System.out.println(t.getMessage());
            }
        });
    }

    private void requestTest() {
        BannerReq req = new BannerReq(1); // 接口号1是banner
        Gson gson = new Gson();
        String json = gson.toJson(req);
        RetrofitHelper.tst(json);

    }
}
