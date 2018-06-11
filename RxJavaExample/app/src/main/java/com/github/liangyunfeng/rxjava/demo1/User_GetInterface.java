package com.github.liangyunfeng.rxjava.demo1;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yunfeng.l on 2018/6/8.
 */

public interface User_GetInterface {

    @GET("/user")
    public Call<ResponseBody> getUser(@Query("userId") String userId, Callback<User> callback);

    @GET("/user")
    public Observable<User> getUser(@Query("userId") String userId);


    @GET("/token")
    public void getToken(Callback<String> callback);

    @GET("/user")
    public void getUser(@Query("token") String token, @Query("userId") String userId, Callback<User> callback);


    @GET("/token")
    public Observable<String> getToken();

    @GET("/user")
    public Observable<User> getUser(@Query("token") String token, @Query("userId") String userId);
}
