package com.github.liangyunfeng.picasso.demo3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.github.liangyunfeng.picasso.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yunfeng.l on 2018/6/1.
 */

public class PicassoStaggeredActivity extends AppCompatActivity {

    //获取的json数据中的数据集合
    private List<TestBean.DataBean.WallpaperListInfoBean> list = new ArrayList<>();

    //创建一个list集合存储recyclerview中的图片的高度
    private List<Integer> heights = new ArrayList<>();

    //声明recyclerview引用
    private RecyclerView mRecyclerView;

    //声明自定义请求类
    private MyAdapter adapter;

    //用插件自动生成初始化view代码的方法
    private void assignViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.staggered_recycler_view);

        //设置recyclerview要实现的类型为StaggeredGrid瀑布流类型
        //并再构造方法中指定列数3，纵向排列
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, RecyclerView.VERTICAL));
    }

    //网络请求数据的网址
    private String url = "http://bz.budejie.com/?typeid=2&ver=3.4.3&no_cry=1&client=android&c=wallPaper&a=random&bigid=0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picasso_staggered_activity);

        //初始化控件
        assignViews();

        //开启网络下载数据的方法
        startTask();
    }

    private void startTask() {
        //通过类名直接调用静态方法获取单例对象再调用getBeanOfOK()方法传入参数通过接口回调获取数据
        OkHttpUtils.getInstance().getBeanOfOk(this, url, TestBean.class,
                new OkHttpUtils.CallBack<TestBean>() {
                    @Override
                    public void getData(TestBean testBean) {
                        Log.i("===", "getData: " + testBean.toString());
                        if (testBean != null) {
                            //如果不为空用本地list接收
                            list.addAll(testBean.getData().getWallpaperListInfo());
                            //数据一旦回调成功初始化数据源和适配器
                            initHights();
                            initAdapter();
                        }
                    }
                });
    }

    private void initAdapter() {
        //创建自定义适配器对象
        adapter = new MyAdapter(this, list, heights);

        //设置recyclerview适配器
        mRecyclerView.setAdapter(adapter);

        //刷新适配器
        adapter.notifyDataSetChanged();
    }

    private void initHights() {
        //设置随机数
        Random random = new Random();

        for (int i = 0; i < list.size(); i++) {
            //集合中存储每个回调图片对应的随机高度
            heights.add(random.nextInt(200) + 200);
        }
    }
}