package com.github.liangyunfeng.rxjava.demo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.liangyunfeng.rxjava.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseDemo1Activity extends AppCompatActivity {

    private final String TAG = "BaseDemo1Activity";

    int resId;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_demo1);

        resId = R.mipmap.ic_launcher;
        mImageView = (ImageView)findViewById(R.id.iv);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_demo_btn1:
                testBase();
                break;
            case R.id.base_demo_btn2:
                testAction();
                break;
            case R.id.base_demo_btn3:
                testPrintArray();
                break;
            case R.id.base_demo_btn4:
                testSetImage();
                break;
            case R.id.base_demo_btn5:
                testSchedulerBase();
                break;
            case R.id.base_demo_btn6:
                testMap();
                break;
            case R.id.base_demo_btn7:
                testFlatmap();
                break;
            case R.id.base_demo_btn8:
                testLift();
                break;
            case R.id.base_demo_btn9:
                testRetrofit();
                break;
            case R.id.base_demo_btn10:
                testRetrofitWithRxJava();
                break;
            case R.id.base_demo_btn11:
                testRetrofit2();
                break;
            case R.id.base_demo_btn12:
                testRetrofitWithRxJava2();
                break;
            case R.id.base_demo_btn13:
                testRxBinding();
                break;
            case R.id.base_demo_btn14:
                testRxBus();
                break;
            case R.id.base_demo_btn15:

                break;
            default:
                break;
        }
    }

    private void testBase() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                Log.v(TAG, "onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.v(TAG, "onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.v(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.v(TAG, "onComplete");
            }
        };

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>(){

            @Override
            public void subscribe(ObservableEmitter<String> subscriber) throws Exception {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onComplete();
            }

        });

        Observable.just("Hello", "Hi", "Aloha");

        Observable.fromArray("Hello", "Hi", "Aloha");

        String[] words = {"Hello", "Hi", "Aloha"};
        Observable.fromArray(words);

        //List<String> list = new ArrayList<String>();
        //list.addAll(Arrays.asList(words));
        //Observable.fromIterable((Iterable<? extends T>) list.iterator());

        observable.subscribe(observer);
    }

    private void testAction() {
        Observable observable = Observable.just("Hello", "Hi", "Aloha");

        Consumer<String> onNextAction = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.v(TAG, "onNextAction.accept : " + s);
            }
        };

        Consumer<? extends Throwable> onErrorAction = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.v(TAG, "onErrorAction.accept : " + throwable.getMessage());
            }
        };

        Action onCompleteAction = new Action() {
            @Override
            public void run() throws Exception {
                Log.v(TAG, "onCompleteAction.run :");
            }
        };

        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);

        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);

        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompleteAction);
    }

    private void testPrintArray() {
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable.fromArray(words)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.v(TAG, "test3.accept : " + s);
                    }
                });
    }

    private void testSetImage() {
        Observable.create(new ObservableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Drawable> observableEmitter) throws Exception {
                Drawable drawable = getDrawable(resId);
                observableEmitter.onNext(drawable);
                observableEmitter.onComplete();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                Log.v(TAG, "testSetImage.onSubscribe: " + disposable.toString());
            }

            @Override
            public void onNext(@NonNull Drawable drawable) {
                mImageView.setImageDrawable(drawable);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.v(TAG, "testSetImage.onError: " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                Log.v(TAG, "testSetImage.onComplete: ");
            }
        });
    }

    /**
     * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
     * Schedulers.io(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
     * Schedulers.computation(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
     * Schedulers.single()
     * AndroidSchedulers.mainThread(): 它指定的操作将在 Android 主线程运行
     */
    private void testSchedulerBase() {
        Observable.just(1,2,3,4,5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.v(TAG, "testSchedulerBase.accept: " + integer);
                    }
                });
    }

    private Bitmap getBitmapFromPath(String path) {
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path;
        Bitmap bitmap = BitmapFactory.decodeFile(filename);
        Log.v(TAG, "getBitmapFromPath: path = " + filename);
        return bitmap;
    }

    private void showBitmap(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    private void testMap() {
        Observable.just("images/logo.png")
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull String s) throws Exception {
                        return getBitmapFromPath(s);
                    }
                }).subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) throws Exception {
                showBitmap(bitmap);
            }
        });
    }

    private Student[] getStudents() {
        Student[] students = new Student[3];

        students[0] = new Student();
        students[0].setName("张三");
        Course ac1 = new Course();
        ac1.setName("语文");
        Course ac2 = new Course();
        ac2.setName("数学");
        Course ac3 = new Course();
        ac3.setName("英语");
        List<Course> alist = new ArrayList<>();
        alist.add(ac1);
        alist.add(ac2);
        alist.add(ac3);
        students[0].setCourses(alist);

        students[1] = new Student();
        students[1].setName("李四");
        Course bc1 = new Course();
        bc1.setName("物理");
        Course bc2 = new Course();
        bc2.setName("化学");
        Course bc3 = new Course();
        bc3.setName("数学");
        Course bc4 = new Course();
        bc4.setName("英语");
        List<Course> blist = new ArrayList<>();
        blist.add(bc1);
        blist.add(bc2);
        blist.add(bc3);
        blist.add(bc4);
        students[1].setCourses(blist);

        students[2] = new Student();
        students[2].setName("刘五");
        Course cc1 = new Course();
        cc1.setName("政治");
        Course cc2 = new Course();
        cc2.setName("地理");
        Course cc3 = new Course();
        cc3.setName("语文");
        Course cc4 = new Course();
        cc4.setName("数学");
        Course cc5 = new Course();
        cc5.setName("英语");
        List<Course> clist = new ArrayList<>();
        clist.add(cc1);
        clist.add(cc2);
        clist.add(cc3);
        clist.add(cc4);
        clist.add(cc5);
        students[2].setCourses(clist);

        return students;
    }

    private void testFlatmap() {
        Student[] students = getStudents();
        /*Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {}

            @Override
            public void onNext(@NonNull Object o) {

            }

            @Override
            public void onError(@NonNull Throwable throwable) {}

            @Override
            public void onComplete() {}
        };*/

        /*Observable..just(students)
                .map(new Function<Student[], String>() {
                    @Override
                    public String apply(@NonNull Student[] students) throws Exception {
                        return null;
                    }
                })*/

        // Each Student Name
        Consumer<String> consumer1 = new Consumer<String>() {
            @Override
            public void accept(String name) throws Exception {
                Log.v(TAG, "testFlatma.consumer1.acceptp : " + name);
            }
        };

        Observable.fromArray(students)
                .map(new Function<Student, String>() {
                    @Override
                    public String apply(@NonNull Student student) throws Exception {
                        return student.getName();
                    }
                }).subscribe(consumer1);

        // Course Name of Each Student
        Consumer<Student> consumer2 = new Consumer<Student>() {
            @Override
            public void accept(Student student) throws Exception {
                List<Course> list = student.getCourses();
                for (int i = 0; i < list.size(); i++) {
                    Log.v(TAG, "testFlatma.consumer2.acceptp : course = " + list.get(i).getName());
                }
            }
        };

        Observable.fromArray(students)
                .subscribe(consumer2);


        // Course Name of Each Student
       /* Consumer<Course> consumer3 = new Consumer<Course>() {
            @Override
            public void accept(Course course) throws Exception {
                Log.v(TAG, "testFlatma.consumer3.acceptp : course = " + course.getName());
            }
        };
        
        Observable.fromArray(students)
                .flatMap(new Function<Student, ObservableSource<Course>>() {
                    @Override
                    public ObservableSource<Course> apply(@NonNull Student student) throws Exception {
                        List<Course> list = student.getCourses();
                        Course[] courses = new Course[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            courses[i] = list.get(i);
                        }
                        return Observable.fromArray(courses);
                        //return Observable.fromArray((Course[]) student.getCourses().toArray());
                    }
                }).subscribe(consumer3);*/


        /*//（Retrofit + RxJava）：
        networkClient.token() // 返回 Observable<String>，在订阅时请求 token，并在响应后发送 token
                .flatMap(new Func1<String, Observable<Messages>>() {
                    @Override
                    public Observable<Messages> call(String token) {
                        // 返回 Observable<Messages>，在订阅时请求消息列表，并在响应后发送请求到的消息列表
                        return networkClient.messages();
                    }
                })
                .subscribe(new Action1<Messages>() {
                    @Override
                    public void call(Messages messages) {
                        // 处理显示消息列表
                        showMessages(messages);
                    }
                });*/

    }

    private void testLift() {
        /*Observable observable = new Observable() {
            @Override
            protected void subscribeActual(Observer observer) {

            }
        };*/

        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter observableEmitter) throws Exception {

            }
        });

        observable.lift(new ObservableOperator() {
            @Override
            public Observer apply(@NonNull Observer observer) throws Exception {
                return null;
            }
        }).lift(new ObservableOperator() {
            @Override
            public Observer apply(@NonNull Observer observer) throws Exception {
                return null;
            }
        }).lift(new ObservableOperator() {
            @Override
            public Observer apply(@NonNull Observer observer) throws Exception {
                return null;
            }
        });

        class LiftAllTransformer implements ObservableTransformer<Integer, String> {
            @Override
            public ObservableSource<String> apply(@NonNull Observable<Integer> observable) {
                return observable.lift(new ObservableOperator<String, Integer>() {
                    @Override
                    public Observer<? super Integer> apply(@NonNull Observer<? super String> observer) throws Exception {
                        return null;
                    }
                }).lift(new ObservableOperator<String, String>() {
                    @Override
                    public Observer<? super String> apply(@NonNull Observer<? super String> observer) throws Exception {
                        return null;
                    }
                }).lift(new ObservableOperator<String, String>() {
                    @Override
                    public Observer<? super String> apply(@NonNull Observer<? super String> observer) throws Exception {
                        return null;
                    }
                });
            }
        }

        ObservableTransformer liftAll = new LiftAllTransformer();
        observable.compose(liftAll)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });

        observable.compose(liftAll)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });

        observable.compose(liftAll)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });

        observable.compose(liftAll)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });

    }

    private void processUser(User user) {

    }

    class UserView {
        public void setUser(User user) {

        }
    }

    // Retrofit传统方式
    private void testRetrofit() {
        String baseUrl = "";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ProtoConverterFactory.create())        // 支持Prototocobuff解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   // 支持RxJava
                .build();

        User_GetInterface request = retrofit.create(User_GetInterface.class);

        String userId = "";
        final UserView userView = new UserView();

        Call<ResponseBody> call = request.getUser(userId, new Callback<User>() {
            @Override
            public void success(final User user) {
                new Thread() {
                    @Override
                    public void run() {
                        processUser(user); // 尝试修正 User 数据
                        runOnUiThread(new Runnable() { // 切回 UI 线程
                            @Override
                            public void run() {
                                userView.setUser(user);
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void failure(Exception error) {
                // Error handling
            }
        });

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                final User user = gson.fromJson(response.body().toString(), User.class);
                new Thread() {
                    @Override
                    public void run() {
                        processUser(user); // 尝试修正 User 数据
                        runOnUiThread(new Runnable() { // 切回 UI 线程
                            @Override
                            public void run() {
                                userView.setUser(user);
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Error handling
            }
        });
    }

    // Retrofit通过结合RxJava方式
    private void testRetrofitWithRxJava() {
        String baseUrl = "";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ProtoConverterFactory.create())        // 支持Prototocobuff解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   // 支持RxJava
                .build();

        User_GetInterface request = retrofit.create(User_GetInterface.class);

        String userId = "";
        final UserView userView = new UserView();
        request.getUser(userId)
                .doOnNext(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        processUser(user);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) { }

                    @Override
                    public void onNext(@NonNull User user) {
                        userView.setUser(user);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        // Error handling
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    // Retrofit传统方式
    private void testRetrofit2() {
        String baseUrl = "";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ProtoConverterFactory.create())        // 支持Prototocobuff解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   // 支持RxJava
                .build();

        final User_GetInterface request = retrofit.create(User_GetInterface.class);

        final String userId = "";
        final UserView userView = new UserView();

        request.getToken(new Callback<String>() {
            @Override
            public void success(String token) {
                request.getUser(token, userId, new Callback<User>() {
                    @Override
                    public void success(User user) {
                        userView.setUser(user);
                    }

                    @Override
                    public void failure(Exception error) {
                        // Error handling
                    }
                });
            }

            @Override
            public void failure(Exception error) {
                // Error handling
            }
        });
    }

    // Retrofit通过结合RxJava方式
    private void testRetrofitWithRxJava2() {
        String baseUrl = "";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ProtoConverterFactory.create())        // 支持Prototocobuff解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   // 支持RxJava
                .build();

        final User_GetInterface request = retrofit.create(User_GetInterface.class);

        final String userId = "";
        final UserView userView = new UserView();
        request.getToken()
                .flatMap(new Function<String, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(@NonNull String token) throws Exception {
                        return request.getUser(token, userId);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) { }

                    @Override
                    public void onNext(@NonNull User user) {
                        userView.setUser(user);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        // Error handling
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    private void testRxBinding() {


    }

    private void testRxBus() {

    }
}
