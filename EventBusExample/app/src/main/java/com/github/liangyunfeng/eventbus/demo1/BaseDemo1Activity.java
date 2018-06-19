package com.github.liangyunfeng.eventbus.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.liangyunfeng.eventbus.R;
import com.github.liangyunfeng.eventbus.demo1.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseDemo1Activity extends AppCompatActivity {
    private final String TAG = "BaseDemo1Activity";

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_demo1);
        mTextView = (TextView)findViewById(R.id.tv);

        // 注册订阅者
        EventBus.getDefault().register(this);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseDemo1Activity.this, BaseDemo2Activity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new MessageEvent("Hello EventBus for Sticky!"));
            }
        });

    }

    //@Subscribe(threadMode = ThreadMode.BACKGROUND)
    //@Subscribe(threadMode = ThreadMode.POSTING)
    //@Subscribe(threadMode = ThreadMode.ASYNC)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {    // 和方法名无关，可以任意方法名； 绑定和参数类型有关
        Log.v(TAG, "onMessageEvent(): event = " + event.getMessage());
        mTextView.setText("BaseDemo1Activity.onMessageEvent: event = " + event.getMessage());
    }



    // 2.线程模式:
    // ThreadMode.POSTING 订阅者方法将在发布事件所在的线程中被调用。这是 默认的线程模式。事件的传递是同步的，一旦发布事件，所有该模式的订阅者方法都将被调用。这种线程模式意味着最少的性能开销，因为它避免了线程的切换。因此，对于不要求是主线程并且耗时很短的简单任务推荐使用该模式。使用该模式的订阅者方法应该快速返回，以避免阻塞发布事件的线程，这可能是主线程。
    // ThreadMode.MAIN 订阅者方法将在主线程（UI线程）中被调用。因此，可以在该模式的订阅者方法中直接更新UI界面。如果发布事件的线程是主线程，那么该模式的订阅者方法将被直接调用。使用该模式的订阅者方法必须快速返回，以避免阻塞主线程。
    // ThreadMode.MAIN_ORDERED 订阅者方法将在主线程（UI线程）中被调用。因此，可以在该模式的订阅者方法中直接更新UI界面。事件将先进入队列然后才发送给订阅者，所以发布事件的调用将立即返回。这使得事件的处理保持严格的串行顺序。使用该模式的订阅者方法必须快速返回，以避免阻塞主线程。
    // ThreadMode.BACKGROUND 订阅者方法将在后台线程中被调用。如果发布事件的线程不是主线程，那么订阅者方法将直接在该线程中被调用。如果发布事件的线程是主线程，那么将使用一个单独的后台线程，该线程将按顺序发送所有的事件。使用该模式的订阅者方法应该快速返回，以避免阻塞后台线程。
    // ThreadMode.ASYNC 订阅者方法将在一个单独的线程中被调用。因此，发布事件的调用将立即返回。如果订阅者方法的执行需要一些时间，例如网络访问，那么就应该使用该模式。避免触发大量的长时间运行的订阅者方法，以限制并发线程的数量。EventBus使用了一个线程池来有效地重用已经完成调用订阅者方法的线程。

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEventPosting(MessageEvent event) {
        Log.i(TAG, "onMessageEventPosting(), current thread is " + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMain(MessageEvent event) {
        Log.i(TAG, "onMessageEventMain(), current thread is " + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEventBackground(MessageEvent event) {
        Log.i(TAG, "onMessageEventBackground(), current thread is " + Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEventAsync(MessageEvent event) {
        Log.i(TAG, "onMessageEventAsync(), current thread is " + Thread.currentThread().getName());
    }



    // 3.粘性事件:
    // 如果先发布了事件，然后有订阅者订阅了该事件，那么除非再次发布该事件，否则订阅者将永远接收不到该事件。
    // 此时，可以使用粘性事件。发布一个粘性事件之后，EventBus将在内存中缓存该粘性事件。
    // 当有订阅者订阅了该粘性事件，订阅者将接收到该事件。

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)     // sticky = true
    public void onMessageStickyEvent(MessageEvent event) {
        Log.v(TAG, "onMessageEvent(): sticky event = " + event.getMessage());
        mTextView.setText("BaseDemo1Activity.onMessageEvent: sticky event = " + event.getMessage());

        // 移除指定的粘性事件
        //EventBus.getDefault().removeStickyEvent(Object event);

        // 移除指定类型的粘性事件
        //EventBus.getDefault().removeStickyEvent(Class<T> eventType);

        // 移除所有的粘性事件
        //EventBus.getDefault().removeAllStickyEvents();

        EventBus.getDefault().removeStickyEvent(event);
    }



    // 4.事件优先级:
    // EventBus支持在定义订阅者方法时指定事件传递的优先级。默认情况下，订阅者方法的事件传递优先级为0。
    // 数值越大，优先级越高。在相同的线程模式下，更高优先级的订阅者方法将优先接收到事件。
    // 注意：优先级只有在相同的线程模式下才有效。

    // Activity订阅了MessageEvent事件，定义了5个不同优先级的订阅者方法。
    // 当接收到MessageEvent事件时，订阅者方法将打印日志消息。
    // 优先级为2的订阅者方法在接收到事件之后取消了事件的传递, 所以优先级小于2的订阅方法将收不到事件的传递。

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 1)
    public void onMessageEvent1(MessageEvent event) {
        Log.i(TAG, "onMessageEvent1(), message is " + event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 2)
    public void onMessageEvent2(MessageEvent event) {
        Log.i(TAG, "onMessageEvent2(), message is " + event.getMessage());
        // 取消事件
        EventBus.getDefault().cancelEventDelivery(event);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 3)
    public void onMessageEvent3(MessageEvent event) {
        Log.i(TAG, "onMessageEvent3(), message is " + event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 4)
    public void onMessageEvent4(MessageEvent event) {
        Log.i(TAG, "onMessageEvent4(), message is " + event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.POSTING, priority = 5)
    public void onMessageEvent5(MessageEvent event) {
        Log.i(TAG, "onMessageEvent5(), message is " + event.getMessage());
    }



    // 5.订阅者索引:
    // 默认情况下，EventBus在查找订阅者方法时采用的是反射。订阅者索引是EventBus 3的一个新特性。它可以加速订阅者的注册，是一个可选的优化。
    // 订阅者索引的原理是：使用EventBus的注解处理器在应用构建期间创建订阅者索引类，该类包含了订阅者和订阅者方法的相关信息。
    // EventBus官方推荐在Android中使用订阅者索引以获得最佳的性能。
    //
    // 要开启订阅者索引的生成，你需要在构建脚本中使用annotationProcessor属性将EventBus的注解处理器添加到应用的构建中，
    // 还要设置一个eventBusIndex参数来指定要生成的订阅者索引的完全限定类名。

    // 第一步：我们在前面的基本使用的那个例子上进行修改。首先，修改模块下的build.gradle构建脚本：
    /*android {
        defaultConfig {
            ...
            javaCompileOptions {
                annotationProcessorOptions {
                    arguments = [eventBusIndex: 'com.github.liangyunfeng.eventbus.MyEventBusIndex']
                }
            }
        }
        ...
    }

    dependencies {
        ...
        compile 'org.greenrobot:eventbus:3.1.1'
        annotationProcessor 'org.greenrobot:eventbus-annotation-processor:3.1.1'
    }*/

    // 第二步：build一下工程。EventBus注解处理器将为你生成一个订阅者索引类。如下所示：
    /** This class is generated by EventBus, do not edit. */
    /*public class MyEventBusIndex implements SubscriberInfoIndex {
        private static final Map<Class<?>, SubscriberInfo> SUBSCRIBER_INDEX;

        static {
            SUBSCRIBER_INDEX = new HashMap<Class<?>, SubscriberInfo>();

            putIndex(new SimpleSubscriberInfo(MainActivity.class, true, new SubscriberMethodInfo[] {
                    new SubscriberMethodInfo("onMessageEvent", MessageEvent.class, ThreadMode.MAIN),
            }));

        }

        private static void putIndex(SubscriberInfo info) {
            SUBSCRIBER_INDEX.put(info.getSubscriberClass(), info);
        }

        @Override
        public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {
            SubscriberInfo info = SUBSCRIBER_INDEX.get(subscriberClass);
            if (info != null) {
                return info;
            } else {
                return null;
            }
        }
    }*/

    // 第三步：在应用自定义的Application类的onCreate()方法中将订阅者索引类添加到EventBus中，并将该EventBus设置成默认的EventBus。示例代码如下所示：
    /*public class MyApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            // 配置EventBus
            EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        }
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
