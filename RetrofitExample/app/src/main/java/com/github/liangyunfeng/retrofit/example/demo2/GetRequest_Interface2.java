package com.github.liangyunfeng.retrofit.example.demo2;

import com.github.liangyunfeng.retrofit.example.User;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by yunfeng.l on 2018/5/28.
 */

public interface GetRequest_Interface2 {

}

//1、@Path：动态的url访问（这里只列举GET请求，其他请求一样处理）

/**
 * 网络请求地址： http://gank.io/api/data/Android/10/1
 *
 * 带参数就是在path路径中的type、count以及page是可以改变的，例如：http://gank.io/api/data/福利/10/1
 1. 其中“福利”就是type，而且这个值还有其他选择，如Android、视频等；
 2. 其中“10”就是count，表示每次请求的个数，可以随意替换；
 3. 其中“1”就是page，代表页码，根据请求次数依次叠加；
 */
interface GirlsService {
    @GET("api/data/Android/10/1")
    Call<ResponseBody> getGirls();

    @GET("api/data/{type}/{count}/{page}")
    Call<ResponseBody> getGirls(@Path("type") String type, @Path("count") int count, @Path("page") int page);
}


//2、@Query 和@QueryMap：查询参数的设置

/**
 *网络请求地址 ：https://api.github.com/users/whatever?client_id=xxxx&client_secret=yyyy
 */
interface UserService {

    @GET("users/whatever")
    Call<ResponseBody> getUser(@Query("client_id") String id, @Query("client_secret") String secret);
    /**
     * 访问代码：
     * Call<ResponseBody> call = retrofit.create(UserService.class).getUser("zhangsan", "12345@");
     */

    @GET("users/whatever")
    Call<ResponseBody> getUser(@QueryMap Map<String, String> info);
    /**
     * 访问代码：
     * Map<String, String> params = new HashMap<>()
     * params.put("client_id", "zhangsan");
     * params.put("client_secret", "12345@");
     * Call<ResponseBody> call = retrofit.create(UserService.class).getUser(params);
     */
}



/**
 * 网络请求地址 ：https://api.github.com/login
 */
interface LoginService{

    // 3、@Body：以POST请求体的方式向服务器上传 json 字符串

    @POST("login")
    Call<ResponseBody> login(@Body User user);
    /**
     * 访问代码：
     * Call<ResponseBody> call =  retrofit.create(LoginService.class).login(new User("zhangsan", "12345@"))
     */

    /**
     * 其实就是使用@Body这个注解标识我们的参数对象即可。
     * 但是我们平时并不会这样写，因为每次请求都要创建一个实体类，例如访问代码中的User，这样就会很麻烦，因为我们POST的是 json 字符串，所以我们可以这样写：
     * 我们将@Body原来声明的参数User 改成了OKhttp中的RequestBody，并且使用 @Headers声明了请求头的参数类型
     */
    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("login")
    Call<ResponseBody> login(@Body RequestBody body);
    /**
     *  访问代码：
     * JSONObject requestObject = new JSONObject();
     * try {
     *      requestObject .put("client_id", "zhangsan");
     *      requestObject .put("client_secret", "12345@");
     * } catch (Exception e) {
     *      e.printStackTrace();
     * }
     * RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), requestObject.toString());
     * Call<ResponseBody> call =  retrofit.create(LoginService.class).login(requestBody)；
     */



    // 4、@Field 和 @FieldMap：以表单的方式传递键值对 @FormUrlEncoded
    // 其实我们用 POST 的场景相对较多，绝大多数的服务端接口都需要做加密、鉴权和校验，GET 显然不能很好的满足这个需求，使用 POST 提交表单的场景就更是刚需了；
    // GET可以使用Path，Query，QueryMap； POST可以使用Field，FieldMap

    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> login(@Field("username") String username, @Field("password") String password);
    /**
     * 访问代码：
     * Call<ResponseBody> call = retrofit.create(LoginService.class).login("zhangsan", "12345@");
     *
     * 首先通过@POST指明Url，并且添加@FormUrlEncoded，然后通过@Field 声明了表单的项。
     */

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@FieldMap Map<String, String> map);
    /**
     * 访问代码：
     * Map<String, String> params = new ArrayMap<>()
     * params.put("username", "zhangsan");
     * params.put("password", "12345@");
     * Call<ResponseBody> call = retrofit.create(LoginService.class).login(params);
     *
     * 如果你不确定表单项的个数，还有能够携带多个Field的FieldMap：
     */
}


// 5、@Part & 和 PartMap：文件上传 @Multipart

interface FileUploadService {
    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);

    @Multipart
    @POST("register")
    Call<ResponseBody> register(@PartMap Map<String, RequestBody> params,  @Part("password") RequestBody password);

    /**
     // 单文件上传访问代码：
     //先创建 service
     FileUploadService service = retrofit.create(FileUploadService.class);
     //构建要上传的文件
     File file = new File(filename);
     RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
     MultipartBody.Part body =MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);

     String descriptionString = "This is a description";
     RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

     Call<ResponseBody> call = service.upload(description, body);
     call.enqueue(new Callback<ResponseBody>() {
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
    System.out.println("success");
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
    t.printStackTrace();
    }
    });

     // 多文件上传访问代码：
     //多文件上传
     File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
     RequestBody photo = RequestBody.create(MediaType.parse("image/png", file);
     Map<String, RequestBody> photos = new HashMap<>();
     photos.put("photos", photo);
     photos.put("username",  RequestBody.create(null, "abc"));

     Call<User> call = service.register(photos, RequestBody.create(null, "12345@"));
     */



    /**
     * {@link Part} 后面支持三种类型，{@link RequestBody}、{@link okhttp3.MultipartBody.Part} 、任意类型
     * 除 {@link okhttp3.MultipartBody.Part} 以外，其它类型都必须带上表单字段({@link okhttp3.MultipartBody.Part} 中已经包含了表单字段的信息)，
     */
    @POST("/form")
    @Multipart
    Call<ResponseBody> testFileUpload1(@Part("name") RequestBody name, @Part("age") RequestBody age, @Part MultipartBody.Part file);

    /**
     * PartMap 注解支持一个Map作为参数，支持 {@link RequestBody } 类型，
     * 如果有其它的类型，会被{@link retrofit2.Converter}转换，如后面会介绍的 使用{@link com.google.gson.Gson} 的 {@link retrofit2.converter.gson.GsonRequestBodyConverter}
     * 所以{@link MultipartBody.Part} 就不适用了,所以文件只能用<b> @Part MultipartBody.Part </b>
     */
    @POST("/form")
    @Multipart
    Call<ResponseBody> testFileUpload2(@PartMap Map<String, RequestBody> args, @Part MultipartBody.Part file);

    @POST("/form")
    @Multipart
    Call<ResponseBody> testFileUpload3(@PartMap Map<String, RequestBody> args);

    /**
     // 具体使用
     MediaType textType = MediaType.parse("text/plain");
     RequestBody name = RequestBody.create(textType, "Carson");
     RequestBody age = RequestBody.create(textType, "24");
     RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), "这里是模拟文件的内容");

     // @Part
     MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
     Call<ResponseBody> call3 = service.testFileUpload1(name, age, filePart);
     ResponseBodyPrinter.printResponseBody(call3);

     // @PartMap
     // 实现和上面同样的效果
     Map<String, RequestBody> fileUpload2Args = new HashMap<>();
     fileUpload2Args.put("name", name);
     fileUpload2Args.put("age", age);
     //这里并不会被当成文件，因为没有文件名(包含在Content-Disposition请求头中)，但上面的 filePart 有
     //fileUpload2Args.put("file", file);
     Call<ResponseBody> call4 = service.testFileUpload2(fileUpload2Args, filePart); //单独处理文件
     ResponseBodyPrinter.printResponseBody(call4);
     */
}


// 6、@Url ：指定请求路径

/**
 *网络请求地址： http://gank.io/api/data/Android/10/1
 */
interface ApiService {

    @GET
    Call<ResponseBody> getGirls(@Url String url);
    /**
     * 访问代码：
     *  Call<ResponseBody> call = retrofit.create(ApiService .class).getGirls("api/data/Android/10/1");
     */

    @GET("api/data/{type}/{count}/{page}")
    Call<ResponseBody> getGirls(@Path("type") String type, @Path("count") int count, @Path("page") int page);
}


// 7、@Header 和@HeaderMap以及@Headers ：添加请求头

interface UserService1 {
    @GET("users/whatever")
    Call<ResponseBody> getUser(@Header("Token") String Token, @Url String url);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST
    Call<ResponseBody> executePostJson(@Header("Token") String Token, @Url String url, @Body RequestBody body);



    // @Header
    @GET("user")
    Call<User> getUser(@Header("Authorization") String authorization);

    // @Headers
    @Headers("Authorization: authorization")
    @GET("user")
    Call<User> getUser();

    // 以上的效果是一致的。
    // 区别在于使用场景和使用方式
    // 1. 使用场景：@Header用于添加不固定的请求头，@Headers用于添加固定的请求头
    // 2. 使用方式：@Header作用于方法的参数；@Headers作用于方法
}



