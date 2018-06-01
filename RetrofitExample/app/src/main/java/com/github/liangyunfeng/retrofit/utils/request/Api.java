package com.github.liangyunfeng.retrofit.utils.request;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

/**
 * 利用泛型来简化类似的接口定义
  */
public interface Api<T> {
    public final String KEY = "data";

    @FormUrlEncoded
    @POST("/")
    public Call<T> doPost(@Field(KEY) String json);
}

/*
//1.来个获取banner的
public interface IBannerService {
    @FormUrlEncoded
    @POST("/")
    public Call<BannerResp> getBanner(@Field("data") String json);
}

//2.来个登陆的
public interface ILoginService {
    @FormUrlEncoded
    @POST("/")
    public Call<LoginResp> getBanner(@Field("data") String json);
}

//3.来个注册的
public interface IRegisterService {
    @FormUrlEncoded
    @POST("/")
    public Call<RegisterResp> getBanner(@Field("data") String json);
}

//4.来个评论的
public interface ICommentService {
    @FormUrlEncoded
    @POST("/")
    public Call<CommentResp> getBanner(@Field("data") String json);
}*/

