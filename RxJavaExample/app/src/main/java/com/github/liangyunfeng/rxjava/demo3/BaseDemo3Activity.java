package com.github.liangyunfeng.rxjava.demo3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.liangyunfeng.rxjava.R;
import com.github.liangyunfeng.rxjava.demo3.model.FoodDetail;
import com.github.liangyunfeng.rxjava.demo3.model.FoodList;
import com.github.liangyunfeng.rxjava.demo3.model.MobileAddress;
import com.github.liangyunfeng.rxjava.demo3.utils.CacheManager;
import com.google.gson.Gson;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BaseDemo3Activity extends AppCompatActivity {

    private final String TAG = "BaseDemo3Activity";
    private StringBuilder mRxOperatorsText = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_demo3);
        ButterKnife.bind(this);
    }

    /**
     * 简单的网络请求
     * 想必大家都知道，很多时候我们在使用 RxJava 的时候总是和 Retrofit 进行结合使用，而为了方便演示，这里我们就暂且采用 OkHttp3 进行演示，配合 map，doOnNext ，线程切换进行简单的网络请求：
     * 1）通过 Observable.create() 方法，调用 OkHttp 网络请求；
     * 2）通过 map 操作符集合 gson，将 Response 转换为 bean 类；
     * 3）通过 doOnNext() 方法，解析 bean 中的数据，并进行数据库存储等操作；
     * 4）调度线程，在子线程中进行耗时操作任务，在主线程中更新 UI ；
     * 5）通过 subscribe()，根据请求成功或者失败来更新 UI 。
     */
    @OnClick(R.id.btn1)
    public void testRxJavaWithOkhttp() {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> emitter) throws Exception {
                Request.Builder builder = new Request.Builder()
                        .url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, MobileAddress>() {
            @Override
            public MobileAddress apply(@NonNull Response response) throws Exception {
                Log.e(TAG, "map 线程:" + Thread.currentThread().getName() + "\n");
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if(responseBody != null) {
                        Log.e(TAG, "map:转换前:" + response.body());
                        return new Gson().fromJson(responseBody.string(), MobileAddress.class);
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(MobileAddress mobileAddress) throws Exception {
                        Log.e(TAG, "doOnNext 线程:" + Thread.currentThread().getName() + "\n");
                        mRxOperatorsText.append("\ndoOnNext 线程:" + Thread.currentThread().getName() + "\n");
                        Log.e(TAG, "doOnNext: 保存成功：" + mobileAddress.toString() + "\n");
                        mRxOperatorsText.append("doOnNext: 保存成功：" + mobileAddress.toString() + "\n");
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(MobileAddress data) throws Exception {
                        Log.e(TAG, "subscribe 线程:" + Thread.currentThread().getName() + "\n");
                        mRxOperatorsText.append("\nsubscribe 线程:" + Thread.currentThread().getName() + "\n");
                        Log.e(TAG, "成功:" + data.toString() + "\n");
                        mRxOperatorsText.append("成功:" + data.toString() + "\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "subscribe 线程:" + Thread.currentThread().getName() + "\n");
                        mRxOperatorsText.append("\nsubscribe 线程:" + Thread.currentThread().getName() + "\n");
                        Log.e(TAG, "失败：" + throwable.getMessage() + "\n");
                        mRxOperatorsText.append("失败：" + throwable.getMessage() + "\n");
                    }
                });
    }

    /**
     * 通过使用doOnNext和map可以进行数据的多次转换
     */
    @OnClick(R.id.btn2)
    public void testRxJavaWithOkhttp2() {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> emitter) throws Exception {
                Request.Builder builder = new Request.Builder()
                        .url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, MobileAddress>() {
            @Override
            public MobileAddress apply(@NonNull Response response) throws Exception {
                Log.e(TAG, "map 线程:" + Thread.currentThread().getName() + "\n");
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if(responseBody != null) {
                        Log.e(TAG, "map:转换前:" + response.body());
                        return new Gson().fromJson(responseBody.string(), MobileAddress.class);
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(MobileAddress mobileAddress) throws Exception {
                        Log.e(TAG, "doOnNext 线程:" + Thread.currentThread().getName() + "\n");
                        mRxOperatorsText.append("\ndoOnNext 线程:" + Thread.currentThread().getName() + "\n");
                        Log.e(TAG, "doOnNext: 保存成功：" + mobileAddress.toString() + "\n");
                        mRxOperatorsText.append("doOnNext: 保存成功：" + mobileAddress.toString() + "\n");
                    }
                })
                .map(new Function<MobileAddress, MobileAddress.ResultBean>() {
                    @Override
                    public MobileAddress.ResultBean apply(@NonNull MobileAddress mobileAddress) throws Exception {
                        Log.e(TAG, "\n");
                        mRxOperatorsText.append("\n");
                        Log.e(TAG, "map:" + Thread.currentThread().getName() + "\n");
                        mRxOperatorsText.append("map:" + Thread.currentThread().getName() + "\n");
                        return mobileAddress.getResult();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MobileAddress.ResultBean>() {
                    @Override
                    public void accept(MobileAddress.ResultBean data) throws Exception {
                        Log.e(TAG, "subscribe 成功:" + Thread.currentThread().getName() + "\n");
                        mRxOperatorsText.append("\nsubscribe 成功:" + Thread.currentThread().getName() + "\n");
                        Log.e(TAG, "成功:" + data.toString() + "\n");
                        mRxOperatorsText.append("成功:" + data.toString() + "\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "subscribe 失败:" + Thread.currentThread().getName() + "\n");
                        mRxOperatorsText.append("\nsubscribe 失败:" + Thread.currentThread().getName() + "\n");
                        Log.e(TAG, "失败：" + throwable.getMessage() + "\n");
                        mRxOperatorsText.append("失败：" + throwable.getMessage() + "\n");
                    }
                });
    }

    boolean isFromNet = false;

    /**
     * 先读取缓存，如果缓存没数据再通过网络请求获取数据后更新UI
     * 想必在实际应用中，很多时候（对数据操作不敏感时）都需要我们先读取缓存的数据，如果缓存没有数据，再通过网络请求获取，随后在主线程更新我们的 UI。
     * concat 操作符简直就是为我们这种需求量身定做。
     * concat 可以做到不交错的发射两个甚至多个 Observable 的发射事件，并且只有前一个 Observable 终止( onComplete() ) 后才会定义下一个 Observable。
     * 利用这个特性，我们就可以先读取缓存数据，倘若获取到的缓存数据不是我们想要的，再调用 onComplete() 以执行获取网络数据的 Observable，
     * 如果缓存数据能应我们所需，则直接调用 onNext() ，防止过度的网络请求，浪费用户的流量。
     */
    @OnClick(R.id.btn3)
    public void testRxJavaConcat() {
        Observable<FoodList> cache = Observable.create(new ObservableOnSubscribe<FoodList>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<FoodList> emitter) throws Exception {
                Log.e(TAG, "create当前线程:"+Thread.currentThread().getName() );
                FoodList data = CacheManager.getInstance().getFoodListData();

                // 在操作符 concat 中，只有调用 onComplete 之后才会执行下一个 Observable
                if (data != null) { // 如果缓存数据不为空，则直接读取缓存数据，而不读取网络数据
                    isFromNet = false;
                    Log.e(TAG, "\nsubscribe: 读取缓存数据:");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRxOperatorsText.append("\nsubscribe: 读取缓存数据:\n");
                        }
                    });
                    emitter.onNext(data);
                } else {
                    isFromNet = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRxOperatorsText.append("\nsubscribe: 读取网络数据:\n");
                        }
                    });
                    Log.e(TAG, "\nsubscribe: 读取网络数据:");
                    emitter.onComplete();
                }
            }
        });

        Observable<FoodList> network = Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> emitter) throws Exception {
                /*URL httpUrl = new HttpUrl.Builder()
                        .scheme("http")
                        .host("www.tngou.net")
                        //.port(4567)
                        .addPathSegments("api/food/list")
                        .addQueryParameter("rows", "10")
                        .build().url();
                Request.Builder builder = new Request.Builder()
                        .url(httpUrl)
                        .get();*/

                Request.Builder builder = new Request.Builder()
                        .url("http://www.tngou.net/api/food/list?rows=10")
                        .get();     // 默认是Get，如需使用Post方式请求，可以通过post(RequestBody body)来设置
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, FoodList>() {
            @Override
            public FoodList apply(@NonNull Response response) throws Exception {
                Log.e(TAG, "map 线程:" + Thread.currentThread().getName() + "\n");
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if(responseBody != null) {
                        Log.e(TAG, "map:转换前:" + response.body());
                        return new Gson().fromJson(responseBody.string(), FoodList.class);
                    }
                }
                return null;
            }
        });

        // 两个 Observable 的泛型应当保持一致
        Observable.concat(cache,network)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FoodList>() {
                    @Override
                    public void accept(FoodList tngouBeen) throws Exception {
                        Log.e(TAG, "subscribe 成功:" + Thread.currentThread().getName());
                        if (isFromNet) {    // concat无论怎样都会去请求，应该使用Disposable来禁止通过网络请求？
                            mRxOperatorsText.append("accept : 网络获取数据设置缓存: \n");
                            Log.e(TAG, "accept : 网络获取数据设置缓存: \n" + tngouBeen.toString());
                            CacheManager.getInstance().setFoodListData(tngouBeen);
                        }
                        mRxOperatorsText.append("accept: 读取数据成功:" + tngouBeen.toString() + "\n");
                        Log.e(TAG, "accept: 读取数据成功:" + tngouBeen.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "subscribe 失败:" + Thread.currentThread().getName());
                        Log.e(TAG, "accept: 读取数据失败：" + throwable.getMessage());
                        mRxOperatorsText.append("accept: 读取数据失败：" + throwable.getMessage() + "\n");
                    }
                });
    }

    @OnClick(R.id.btn4)
    public void testRxJavaConcatBase() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                Log.e(TAG, "create当前线程:"+Thread.currentThread().getName() );
                isFromNet = true;
                emitter.onNext(1);
                //isFromNet = true;
                emitter.onComplete();
            }
        });

        Observable.concat(observable1,Observable.just(3,4,5))
                .subscribe(new Observer<Integer>() {
                    Disposable mDisposable;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe: d = " + d);
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.e(TAG, "onNext: integer = " + integer);
                        if (isFromNet)
                            mDisposable.dispose();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: e = " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onNext: onComplete");
                    }
                });
    }

    /**
     * 多个网络请求依次依赖
     * 想必这种情况也在实际情况中比比皆是，例如用户注册成功后需要自动登录，我们只需要先通过注册接口注册用户信息，注册成功后马上调用登录接口进行自动登录即可。
     * 我们的 flatMap 恰好解决了这种应用场景，flatMap 操作符可以将一个发射数据的 Observable 变换为多个 Observables ，
     * 然后将它们发射的数据合并后放到一个单独的 Observable，利用这个特性，我们很轻松地达到了我们的需求。
     */
    @OnClick(R.id.btn5)
    public void testRxJavaFlatmap() {
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> emitter) throws Exception {
                /*URL httpUrl = new HttpUrl.Builder()
                        .scheme("http")
                        .host("www.tngou.net")
                        //.port(4567)
                        .addPathSegments("api/food/list")
                        .addQueryParameter("rows", "1")
                        .build().url();
                Request.Builder builder = new Request.Builder()
                        .url(httpUrl)
                        .get();*/

                Request.Builder builder = new Request.Builder()
                        .url("http://www.tngou.net/api/food/list?rows=1")
                        .get();     // 默认是Get，如需使用Post方式请求，可以通过post(RequestBody body)来设置
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                emitter.onNext(response);
            }
        }).map(new Function<Response, FoodList>() {
            @Override
            public FoodList apply(@NonNull Response response) throws Exception {
                Log.e(TAG, "map 线程:" + Thread.currentThread().getName() + "\n");
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        Log.e(TAG, "map:转换前:" + response.body());
                        return new Gson().fromJson(responseBody.string(), FoodList.class);
                    }
                }
                return null;
            }
        }).subscribeOn(Schedulers.io()) // 在io线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理获取食品列表的请求结果
                .doOnNext(new Consumer<FoodList>() {
                    @Override
                    public void accept(@NonNull FoodList foodList) throws Exception { // 先根据获取食品列表的响应结果做一些操作
                        Log.e(TAG, "accept: doOnNext :" + foodList.toString());
                        mRxOperatorsText.append("accept: doOnNext :" + foodList.toString() + "\n");
                    }
                }).observeOn(Schedulers.io()) // 回到 io 线程去处理获取食品详情的请求
                .flatMap(new Function<FoodList, ObservableSource<FoodDetail>>() {
                    @Override
                    public ObservableSource<FoodDetail> apply(@NonNull final FoodList foodList) throws Exception {
                        if (foodList != null && foodList.getTngou() != null && foodList.getTngou().size() > 0) {
                            return Observable.create(new ObservableOnSubscribe<Response>() {
                                @Override
                                public void subscribe(@NonNull ObservableEmitter<Response> emitter) throws Exception {
                                    Request.Builder builder = new Request.Builder()
                                            .url("http://www.tngou.net/api/food/show?id="+foodList.getTngou().get(0).getId())
                                            .get();     // 默认是Get，如需使用Post方式请求，可以通过post(RequestBody body)来设置
                                    Request request = builder.build();
                                    Call call = new OkHttpClient().newCall(request);
                                    Response response = call.execute();
                                    emitter.onNext(response);
                                }
                            }).map(new Function<Response, FoodDetail>() {
                                @Override
                                public FoodDetail apply(@NonNull Response response) throws Exception {
                                    Log.e(TAG, "map 线程:" + Thread.currentThread().getName() + "\n");
                                    if (response.isSuccessful()) {
                                        ResponseBody responseBody = response.body();
                                        if (responseBody != null) {
                                            Log.e(TAG, "map:转换前:" + response.body());
                                            return new Gson().fromJson(responseBody.string(), FoodDetail.class);
                                        }
                                    }
                                    return null;
                                }
                            });
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FoodDetail>() {
                    @Override
                    public void accept(@NonNull FoodDetail foodDetail) throws Exception {
                        Log.e(TAG, "accept: success ：" + foodDetail.toString());
                        mRxOperatorsText.append("accept: success ：" + foodDetail.toString() + "\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: error :" + throwable.getMessage());
                        mRxOperatorsText.append("accept: error :" + throwable.getMessage() + "\n");
                    }
                });
    }

    /**
     * 结合多个接口的数据更新 UI
     * 在实际应用中，我们极有可能会在一个页面显示的数据来源于多个接口，这时候我们的 zip 操作符为我们排忧解难。
     * zip 操作符可以将多个 Observable 的数据结合为一个数据源再发射出去。
     */
    @OnClick(R.id.btn6)
    public void testRxJavaZip() {
        /*Observable<MobileAddress> observable1 = Rx2AndroidNetworking.get("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512").build().getObjectObservable(MobileAddress.class);
        Observable<CategoryResult> observable2 = Network.getGankApi().getCategoryData("Android", 1, 1);
        Observable.zip(observable1, observable2, new BiFunction<MobileAddress, CategoryResult, String>() {
            @Override
            public String apply(@NonNull MobileAddress mobileAddress, @NonNull CategoryResult categoryResult) throws Exception {
                return "合并后的数据为：手机归属地：" + mobileAddress.getResult().getMobilearea() + "人名：" + categoryResult.results.get(0).who;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e(TAG, "accept: 成功：" + s + "\n");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Log.e(TAG, "accept: 失败：" + throwable + "\n");
            }
        });*/
        Observable<String> observable1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("I am A : ");
                emitter.onNext("I am B : ");
                emitter.onNext("I am C : ");
                emitter.onNext("I am D : ");
                emitter.onComplete();
            }
        });
        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onComplete();
            }
        });

        Observable.zip(observable1, observable2, new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                return s + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.e(TAG, "合并后： " + s);
            }
        });
    }

    private Disposable mDisposable;

    /**
     * 间隔任务实现心跳
     * 想必即时通讯等需要轮训的任务在如今的 APP 中已是很常见，而 RxJava 2.x 的 interval 操作符可谓完美地解决了我们的疑惑。
     * 这里就简单的意思一下轮询。
     */
    @OnClick(R.id.btn7)
    public void testRxJavaInterval() {
        Log.e(TAG, "testRxJavaInterval : 当前线程:"+Thread.currentThread().getName() );
        mDisposable = Flowable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "accept: doOnNext : 当前线程:"+Thread.currentThread().getName() );
                        Log.e(TAG, "accept: doOnNext : "+aLong );
                    }
                }).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "accept: subscribe: 当前线程:"+Thread.currentThread().getName() );
                        Log.e(TAG, "accept: 设置文本 ："+aLong );
                        mRxOperatorsText.append("accept: 设置文本 ："+aLong +"\n");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
