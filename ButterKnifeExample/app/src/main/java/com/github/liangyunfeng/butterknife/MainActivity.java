package com.github.liangyunfeng.butterknife;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindBitmap;
import butterknife.BindBool;
import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
二、绑定注解
@BindViews      绑定多个view id为一个view的list变量
@BindView       绑定一个view id为一个view 变量
@BindArray      绑定string里面array数组,@BindArray(R.array.city ) String[] citys ;
@BindBitmap     绑定图片资源为Bitmap，@BindBitmap( R.mipmap.wifi ) Bitmap bitmap;
@BindBool       绑定真假boolean
@BindColor      绑定color,@BindColor(R.color.colorAccent) int black;
@BindDimen      绑定Dimen,@BindDimen(R.dimen.borth_width) int mBorderWidth;
@BindDrawable   绑定Drawable,@BindDrawable(R.drawable.test_pic) Drawable mTestPic;
@BindFloat      绑定float
@BindInt        绑定int
@BindString     绑定一个String id为一个String变量,@BindString( R.string.app_name ) String meg;

三、 事件注解
@OnClick            点击事件
@OnCheckedChanged   选中，取消选中
@OnEditorAction     软键盘的功能键
@OnFocusChange      焦点改变
@OnItemClick        item被点击(注意这里有坑，如果item里面有Button等这些有点击的控件事件的，需要设置这些控件属性focusable为false)
@OnItemLongClick    item长按(返回真可以拦截onItemClick)
@OnItemSelected     item被选择事件
@OnLongClick        长按事件
@OnPageChange       页面改变事件
@OnTextChanged      EditText里面的文本变化事件
@OnTouch            触摸事件
@Optional           选择性注入，如果当前对象不存在，就会抛出一个异常，为了压制这个异常，可以在变量或者方法上加入一下注解,让注入变成选择性的,如果目标View存在,则注入, 不存在,则什么事情都不做=如下代码

四、绑定
在Activity中使用
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //绑定初始化ButterKnife
    ButterKnife.bind(this);
}

在Fragment中使用
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.my_fragment, container, false);
    //这里需要加多一个参数view
    ButterKnife.bind(this, view);
    return view;
}

Fragment生命周期和activity有点不同，销毁的时候可以进行解绑
public class MyFragment extends Fragment {

    @BindView(R.id.bt_test)
    Button btTest;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        //绑定的时候返回了一个Unbinder对象
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

在Adapter ViewHolder中使用
static class ViewHolder {
    @BindView(R.id.tv_item_test)
    TextView tvItemTest;
    @BindView(R.id.bt_item_test)
    Button btItemTest;
    public ViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}

item里面Button控件的点击事件也可以使用注解@OnClick来实现，在适配器里面添加就行，如：
    //item的button的点击事件
    @OnClick(R.id.bt_item_test)
    void onClick(){
        Toast.makeText(context,"点击了按钮",Toast.LENGTH_SHORT).show();

    }

    static class ViewHolder {
        @BindView(R.id.tv_item_test)
        TextView tvItemTest;
        @BindView(R.id.bt_item_test)
        Button btItemTest;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    @BindViews({R.id.tv_user, R.id.tv_psw})
    List<TextView> mTvList;

    @BindView(R.id.et_user) EditText mEt_User;
    @BindView(R.id.et_psw) EditText mEt_Psw;

    @BindString(R.string.login_error) String error_msg;

    @BindArray(R.array.cities) String[] cities;

    @BindBitmap(R.mipmap.ic_launcher)
    Bitmap mBitmap;

    @BindBool(R.bool.isTablet) boolean isTablet;

    @BindColor(R.color.colorPrimary) int mColor;

    @BindDimen(R.dimen.btn_width) int mWidth;

    /*@OnClick(R.id.btn_submit) public void submit(View view) {
        Toast.makeText(this, error_msg + "with name: " + mEt_User.getText().toString() + " and password: " + mEt_Psw.getText().toString(), Toast.LENGTH_SHORT).show();
    }*/

    @OnClick({R.id.btn_submit,R.id.btn_cancel})
    public void submit(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                Toast.makeText(this, error_msg + "with name: " + mEt_User.getText().toString() + " and password: " + mEt_Psw.getText().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                mEt_User.setText("");
                mEt_Psw.setText("");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //设置多个view的属性
        //方式1 传递值
        ButterKnife.apply(mTvList,ENABLED,false);
        //方式2 指定值
        ButterKnife.apply(mTvList,DISABLE);
        //方式3 设置View的Property
        ButterKnife.apply(mTvList, View.ALPHA, 0.0f);
        ButterKnife.apply(mTvList, View.ALPHA, 1.0f);
    }

    //Action接口设置属性
    static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            view.setEnabled(false);
        }
    };

    //Setter接口设置属性
    static final ButterKnife.Setter<View, Boolean> ENABLED = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };
}

// 自定义View使用注解事件
// 不用指定id，直接注解OnClick
class MyButton extends AppCompatButton {

    public MyButton(Context context) {
        super(context);
    }

    @OnClick
    public void onClick() {
    }
}