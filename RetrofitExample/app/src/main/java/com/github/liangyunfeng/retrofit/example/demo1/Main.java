package com.github.liangyunfeng.retrofit.example.demo1;

import com.github.liangyunfeng.retrofit.example.Translation;
import com.github.liangyunfeng.retrofit.example.User;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public class Main {

    public static void request1() {
        // 第1部分：在网络请求接口的注解设置
        //@GET("openapi.do?keyfrom=Yanzhikai&key=2032414398&type=data&doctype=json&version=1.1&q=car")
        //Call<Translation> getCall();

        // 第2部分：在创建Retrofit实例时通过.baseUrl()设置
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        // 从上面看出：一个请求的URL可以通过 替换块 和 请求方法的参数 来进行动态的URL更新。
        // 替换块是由 被{}包裹起来的字符串构成
        // 即：Retrofit支持动态改变网络请求根目录

        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

        Call<Translation> call = request.getCall();

        call.enqueue(new Callback<Translation>() {
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {

            }

            @Override
            public void onFailure(Call<Translation> call, Throwable t) {

            }
        });
    }

    public static void request2() {
        MediaType MEDIA_TYPE_TEXT= MediaType.parse("text/plain; charset=utf-8");
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        // 具体使用
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        GetRequest_Interface service = retrofit.create(GetRequest_Interface.class);

        // @FormUrlEncoded
        Call<ResponseBody> call1 = service.testFormUrlEncoded1("Carson", 24);

        // @Multipart
        RequestBody name = RequestBody.create(MEDIA_TYPE_TEXT, "Carson");
        RequestBody age = RequestBody.create(MEDIA_TYPE_TEXT, "24");

        RequestBody file = RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png"));

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        Call<ResponseBody> call3 = service.testFileUpload1(name, age, filePart);
    }

    public static void request3() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        //作用：以 Post方式 传递 自定义数据类型 给服务器
        //特别注意：如果提交的是一个Map，那么作用相当于 @Field
        //不过Map要经过 FormBody.Builder 类处理成为符合 Okhttp 格式的表单，如：
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("key","value");

        Call<ResponseBody> call = retrofit.create(GetRequest_Interface.class).login(new User("zhangsan", "12345@"));
    }

    public static void request4() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        GetRequest_Interface service = retrofit.create(GetRequest_Interface.class);

        // 具体使用
        // @Field
        Call<ResponseBody> call1 = service.testFormUrlEncoded1("Carson", 24);

        // @FieldMap
        // 实现的效果与上面相同，但要传入Map
        Map<String, Object> map = new HashMap<>();
        map.put("username", "Carson");
        map.put("age", 24);
        Call<ResponseBody> call2 = service.testFormUrlEncoded2(map);
    }

    public static void request5() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        GetRequest_Interface service = retrofit.create(GetRequest_Interface.class);

        // 具体使用
        MediaType textType = MediaType.parse("text/plain");
        RequestBody name = RequestBody.create(textType, "Carson");
        RequestBody age = RequestBody.create(textType, "24");
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), "这里是模拟文件的内容");

        // @Part
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        Call<ResponseBody> call3 = service.testFileUpload1(name, age, filePart);
        //ResponseBodyPrinter.printResponseBody(call3);

        // @PartMap
        // 实现和上面同样的效果
        Map<String, RequestBody> fileUpload2Args = new HashMap<>();
        fileUpload2Args.put("name", name);
        fileUpload2Args.put("age", age);
        //这里并不会被当成文件，因为没有文件名(包含在Content-Disposition请求头中)，但上面的 filePart 有
        //fileUpload2Args.put("file", file);
        Call<ResponseBody> call4 = service.testFileUpload2(fileUpload2Args, filePart); //单独处理文件
        //ResponseBodyPrinter.printResponseBody(call4);
    }




}
