package com.github.liangyunfeng.eventbus.demo1;

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

public class BaseDemo2Activity extends AppCompatActivity {
    private final String TAG = "BaseDemo1Activity";

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_demo2);
        mTextView = (TextView)findViewById(R.id.tv);

        // 注册订阅者
        EventBus.getDefault().register(this);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("Hello EventBus!"));
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent event) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
