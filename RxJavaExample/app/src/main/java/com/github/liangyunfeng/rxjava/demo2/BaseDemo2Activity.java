package com.github.liangyunfeng.rxjava.demo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.liangyunfeng.rxjava.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class BaseDemo2Activity extends AppCompatActivity {

    private final String TAG = "BaseDemo2Activity";
    StringBuilder mRxOperatorsText = new StringBuilder();
    ImageView mImageView;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_demo2);

        mImageView = (ImageView)findViewById(R.id.iv);
        mTextView = (TextView)findViewById(R.id.tv);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.base_demo_btn1:
                testDisposable();
                break;
            case R.id.base_demo_btn2:
                testMap();
                break;
            case R.id.base_demo_btn3:
                testZip();
                break;
            case R.id.base_demo_btn4:
                testConcat();
                break;
            case R.id.base_demo_btn5:
                testFlatmap();
                break;
            case R.id.base_demo_btn6:
                testConcatMap();
                break;
            case R.id.base_demo_btn7:
                testDistinct();
                break;
            case R.id.base_demo_btn8:
                testFilter();
                break;
            case R.id.base_demo_btn9:
                testBuffer();
                break;
            case R.id.base_demo_btn10:
                testTimer();
                break;
            case R.id.base_demo_btn11:
                testInterval();
                break;
            case R.id.base_demo_btn12:
                testDoOnNext();
                break;
            case R.id.base_demo_btn13:
                testSkip();
                break;
            case R.id.base_demo_btn14:
                testTake();
                break;
            case R.id.base_demo_btn15:
                testSingle();
                break;
            case R.id.base_demo_btn16:
                testDebounce();
                break;
            case R.id.base_demo_btn17:
                testDefer();
                break;
            case R.id.base_demo_btn18:
                testLast();
                break;
            case R.id.base_demo_btn19:
                testMerge();
                break;
            case R.id.base_demo_btn20:
                testReduce();
                break;
            case R.id.base_demo_btn21:
                testScan();
                break;
            case R.id.base_demo_btn22:
                testWindow();
                break;
            case R.id.base_demo_btn23:

                break;
            default:
                break;
        }
    }

    private void testDisposable() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observableEmitter) throws Exception {
                mRxOperatorsText.append("Observable emit 1" + "\n");
                Log.e(TAG, "Observable emit 1");
                observableEmitter.onNext(1);
                mRxOperatorsText.append("Observable emit 2" + "\n");
                Log.e(TAG, "Observable emit 2");
                observableEmitter.onNext(2);
                mRxOperatorsText.append("Observable emit 3" + "\n");
                Log.e(TAG, "Observable emit 3");
                observableEmitter.onNext(3);
                observableEmitter.onComplete();
                mRxOperatorsText.append("Observable emit 4" + "\n");
                Log.e(TAG, "Observable emit 4");
                observableEmitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {
            private int i;
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                mRxOperatorsText.append("onSubscribe : " + disposable.isDisposed() + "\n");
                Log.e(TAG, "onSubscribe : " + disposable.isDisposed());
                mDisposable = disposable;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                mRxOperatorsText.append("onNext : value : " + integer + "\n");
                Log.e(TAG, "onNext : value : " + integer);
                i++;
                if(i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                    mRxOperatorsText.append("onNext : isDisposable : " + mDisposable.isDisposed() + "\n");
                    Log.e(TAG, "onNext : isDisposable : " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                mRxOperatorsText.append("onError : value : " + throwable.getMessage() + "\n");
                Log.e(TAG, "onError : value : " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                mRxOperatorsText.append("onComplete" + "\n");
                Log.e(TAG, "onComplete");
            }
        });
    }

    private void testMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                observableEmitter.onNext(2);
                observableEmitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mRxOperatorsText.append("accept : " + s +"\n");
                Log.e(TAG, "accept : " + s);
            }
        });
    }

    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> observableEmitter) throws Exception {
                if(!observableEmitter.isDisposed()) {
                    observableEmitter.onNext("A");
                    mRxOperatorsText.append("String emit : A \n");
                    Log.e(TAG, "String emit : A");
                    observableEmitter.onNext("B");
                    mRxOperatorsText.append("String emit : B \n");
                    Log.e(TAG, "String emit : B");
                    observableEmitter.onNext("C");
                    mRxOperatorsText.append("String emit : C \n");
                    Log.e(TAG, "String emit : C");
                }
            }
        });
    }

    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observableEmitter) throws Exception {
                if (!observableEmitter.isDisposed()) {
                    observableEmitter.onNext(1);
                    mRxOperatorsText.append("Integer emit : 1 \n");
                    Log.e(TAG, "Integer emit : 1");
                    observableEmitter.onNext(2);
                    mRxOperatorsText.append("Integer emit : 2 \n");
                    Log.e(TAG, "Integer emit : 2");
                    observableEmitter.onNext(3);
                    mRxOperatorsText.append("Integer emit : 3 \n");
                    Log.e(TAG, "Integer emit : 3");
                    observableEmitter.onNext(4);
                    mRxOperatorsText.append("Integer emit : 4 \n");
                    Log.e(TAG, "Integer emit : 4");
                    observableEmitter.onNext(5);
                    mRxOperatorsText.append("Integer emit : 5 \n");
                    Log.e(TAG, "Integer emit : 5");
                }
            }
        });
    }

    /**
     *
     * zip 组合事件的过程就是分别从发射器 A 和发射器 B 各取出一个事件来组合，并且一个事件只能被使用一次，组合的顺序是严格按照事件发送的顺序来进行的，所以上面截图中，可以看到，1 永远是和 A 结合的，2 永远是和 B 结合的。
     * 最终接收器收到的事件数量是和发送器发送事件最少的那个发送器的发送事件数目相同，所以如截图中，5 很孤单，没有人愿意和它交往，孤独终老的单身狗。
     *
     */
    private void testZip() {
        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                return s + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mRxOperatorsText.append("zip : accept : " + s + "\n");
                Log.e(TAG, "zip : accept : " + s);
            }
        });
    }

    private void testConcat() {
        Observable.concat(Observable.just(1,2,3), Observable.just(4,5,6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mRxOperatorsText.append("concat : "+ integer + "\n");
                        Log.e(TAG, "concat : "+ integer);
                    }
                });
        /*Observable.just(1,2,3,4,5,6)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mRxOperatorsText.append("concat : "+ integer + "\n");
                        Log.e(TAG, "concat : "+ integer);
                    }
                });*/
    }

    private void testFlatmap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                observableEmitter.onNext(2);
                observableEmitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "flatMap : accept : " + s);
                        mRxOperatorsText.append("flatMap : accept : " + s + "\n");
                    }
                });
    }

    private void testConcatMap() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                observableEmitter.onNext(2);
                observableEmitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "concatMap : accept : " + s);
                        mRxOperatorsText.append("concatMap : accept : " + s + "\n");
                    }
                });
    }

    private void testDistinct() {
        Observable.just(1,1,1,2,2,3,4,5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mRxOperatorsText.append("distinct : " + integer + "\n");
                        Log.e(TAG, "distinct : " + integer + "\n");
                    }
                });
    }

    private void testFilter() {
        Observable.just(1,20,65,-5,7,19)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer >= 10;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                mRxOperatorsText.append("filter : " + integer + "\n");
                Log.e(TAG, "filter : " + integer + "\n");
            }
        });
    }

    private void testBuffer() {
        Observable.just(1,2,3,4,5)
                .buffer(3,2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        mRxOperatorsText.append("buffer size : " + integers.size() + "\n");
                        Log.e(TAG, "buffer size : " + integers.size() + "\n");
                        mRxOperatorsText.append("buffer value : ");
                        Log.e(TAG, "buffer value : " );
                        for (Integer i : integers) {
                            mRxOperatorsText.append(i + "");
                            Log.e(TAG, i + "");
                        }
                        mRxOperatorsText.append("\n");
                        Log.e(TAG, "\n");
                    }
                });
    }

    private void testTimer() {
        mRxOperatorsText.append("timer start : " + System.nanoTime() + "\n");
        Log.e(TAG, "timer start : " + System.nanoTime() + "\n");
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mRxOperatorsText.append("timer :" + aLong + " at " + System.nanoTime() + "\n");
                        Log.e(TAG, "timer :" + aLong + " at " + System.nanoTime() + "\n");
                    }
                });
    }

    Disposable mDisposable;

    private void testInterval () {
        mRxOperatorsText.append("interval start : " + System.nanoTime() + "\n");
        Log.e(TAG, "interval start : " + System.nanoTime() + "\n");
        mDisposable = Observable.interval(3,2,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mRxOperatorsText.append("interval :" + aLong + " at " + System.nanoTime() + "\n");
                        Log.e(TAG, "interval :" + aLong + " at " + System.nanoTime() + "\n");
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

    /**
     * 它的作用是让订阅者在接收到数据之前干点有意思的事情。假如我们在获取到数据之前想先保存一下它，无疑我们可以这样实现。
     */
    private void testDoOnNext() {
        Observable.just(1,2,3,4)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mRxOperatorsText.append("doOnNext 保存 " + integer + "成功" + "\n");
                        Log.e(TAG, "doOnNext 保存 " + integer + "成功" + "\n");
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                mRxOperatorsText.append("doOnNext :" + integer + "\n");
                Log.e(TAG, "doOnNext :" + integer + "\n");
            }
        });
    }

    private void testSkip() {
        Observable.just(1,2,3,4,5)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mRxOperatorsText.append("skip : "+integer + "\n");
                        Log.e(TAG, "skip : "+integer + "\n");
                    }
                });
    }

    private void testTake() {
        Observable.fromArray(1,2,3,4,5)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mRxOperatorsText.append("take : "+integer + "\n");
                        Log.e(TAG, "accept: take : "+integer + "\n" );
                    }
                });
    }

    private void testSingle() {
        Single.just(new Random().nextInt())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {

                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        mRxOperatorsText.append("single : onSuccess : "+integer+"\n");
                        Log.e(TAG, "single : onSuccess : "+integer+"\n" );
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        mRxOperatorsText.append("single : onError : "+throwable.getMessage()+"\n");
                        Log.e(TAG, "single : onError : "+throwable.getMessage()+"\n");
                    }
                });
    }

    /**
     * 去除发送间隔时间小于 500 毫秒的发射事件，所以 1 和 3 被去掉了。
     */
    private void testDebounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> observableEmitter) throws Exception {
                // send events with simulated time wait
                observableEmitter.onNext(1); // skip
                Thread.sleep(400);
                observableEmitter.onNext(2); // deliver
                Thread.sleep(505);
                observableEmitter.onNext(3); // skip
                Thread.sleep(100);
                observableEmitter.onNext(4); // deliver
                Thread.sleep(605);
                observableEmitter.onNext(5); // deliver
                Thread.sleep(510);
                observableEmitter.onComplete();
            }
        }).debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mRxOperatorsText.append("debounce :" + integer + "\n");
                        Log.e(TAG,"debounce :" + integer + "\n");
                    }
                });
    }

    /**
     * 每次订阅都会创建一个新的 Observable，并且如果没有被订阅，就不会产生新的 Observable
     */
    private void testDefer() {
        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(1,2,3);
            }
        });

        observable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                mRxOperatorsText.append("defer : " + integer + "\n");
                Log.e(TAG, "defer : " + integer + "\n");
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                mRxOperatorsText.append("defer : onError : " + throwable.getMessage() + "\n");
                Log.e(TAG, "defer : onError : " + throwable.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                mRxOperatorsText.append("defer : onComplete\n");
                Log.e(TAG, "defer : onComplete\n");
            }
        });
    }

    private void testLast() {
        Observable.just(1,2,3)
                .last(4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mRxOperatorsText.append("last : " + integer + "\n");
                        Log.e(TAG, "last : " + integer + "\n");
                    }
                });
    }

    /**
     * merge 的作用是把多个 Observable 结合起来，接受可变参数，也支持迭代器集合。注意它和 concat 的区别在于，不用等到 发射器 A 发送完所有的事件再进行发射器 B 的发送。
     */
    private void testMerge() {
        Observable.merge(Observable.just(1,2), Observable.just(3,4,5))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mRxOperatorsText.append("merge :" + integer + "\n");
                        Log.e(TAG, "accept: merge :" + integer + "\n" );
                    }
                });
    }

    /**
     * reduce 操作符每次用一个方法处理一个值，可以有一个 seed 作为初始值。
     * 可以看到，代码中，我们中间采用 reduce ，支持一个 function 为两数值相加，所以应该最后的值是：1 + 2 = 3 + 3 = 6 ， 而Log 日志完美解决了我们的问题。
     * BaseDemo2Activity: accept: reduce : 6
     */
    private void testReduce() {
        Observable.just(1,2,3)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                mRxOperatorsText.append("reduce : " + integer + "\n");
                Log.e(TAG, "accept: reduce : " + integer + "\n");
            }
        });
    }

    /**
     * scan 操作符作用和上面的 reduce 一致，唯一区别是 reduce 是个只追求结果的坏人，而 scan 会始终如一地把每一个步骤都输出。
     */
    private void testScan() {
        Observable.just(1,2,3)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                mRxOperatorsText.append("scan " + integer + "\n");
                Log.e(TAG, "accept: scan " + integer + "\n");
            }
        });
    }

    /**
     * 按照实际划分窗口，将数据发送给不同的 Observable
     */
    private void testWindow() {
        mRxOperatorsText.append("window\n");
        Log.e(TAG, "window\n");
        Observable.interval(1, TimeUnit.SECONDS) // 间隔一秒发一次
                .take(15) // 最多接收15个
                .window(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Observable<Long>>() {
                    @Override
                    public void accept(@NonNull Observable<Long> longObservable) throws Exception {
                        mRxOperatorsText.append("Sub Divide begin...\n");
                        Log.e(TAG, "Sub Divide begin...\n");
                        longObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long aLong) throws Exception {
                                        mRxOperatorsText.append("Next:" + aLong + "\n");
                                        Log.e(TAG, "Next:" + aLong + "\n");
                                    }
                                });
                    }
                });
    }

















}
