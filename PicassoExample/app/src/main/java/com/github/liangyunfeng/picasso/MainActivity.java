package com.github.liangyunfeng.picasso;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.liangyunfeng.picasso.demo1.PicassoTestActivity;
import com.github.liangyunfeng.picasso.demo2.PicassoRecyclerViewActivity;
import com.github.liangyunfeng.picasso.demo3.PicassoStaggeredActivity;


/**
 *  使用Picasso加载图片的内存优化实践:

 1 图片裁剪
 在列表页尽量使用裁剪后的图片，在查看大图模式下才加载完整的图片。
 图片裁剪示例
 Picasso.with( imageView.getContext() )
 .load(url)
 .resize(dp2px(250),dp2px(250))
 .centerCrop()
 .into(imageView);

 picasso默认情况下会使用全局的ApplicationContext，即开发者传进去Activity，picasso也会通过activity获取ApplicationContext。

 2 查看大图放弃memory cache
 Picasso默认会使用设备的15%的内存作为内存图片缓存，且现有的api无法清空内存缓存。我们可以在查看大图时放弃使用内存缓存，图片从网络下载完成后会缓存到磁盘中，加载会从磁盘中加载，这样可以加速内存的回收。
 Picasso.with(getApplication())
 .load(mURL)
 .memoryPolicy(NO_CACHE, NO_STORE)
 .into(imageView);

 3 RecyclableImageView
 重写ImageView的onDetachedFromWindow方法，在它从屏幕中消失时回调，去掉drawable引用，能加快内存的回收。
 public class RecyclerImageView extends ImageView
 {
     ...
     @Override
     protected void onDetachedFromWindow() {
     super.onDetachedFromWindow();
     setImageDrawable(null);
     }
 }

4 新进程中查看大图

列表页的内存已经非常稳定，但是查看大图时，大图往往占用了20+m内存，加上现有进程中的内存，非常容易oom，在新进程中打开Activity成为比较取巧的避免oom的方式。
<activity android:name=".DetailActivity" android:process=":picture"/>

只要在AndroidManifest.xml中定义Activity时加入process属性，即可在新进程中打开此Activity。由此，picasso也将在新进程中创建基于新ApplicationContext的单例。

5 列表页滑动优化
picasso可以对多个加载请求设置相同的tag，即
Object tag = new Object();

Picasso.with( imageView.getContext() )
     .load(url)
     .resize(dp2px(250),dp2px(250))
     .centerCrop()
     .tag(tag)
     .into(imageView);

例如在RecyclerView滑动时监听，处理不同的表现：
mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
     @Override
     public void onScrollStateChanged(RecyclerView recyclerView, int newState)
     {
        if (newState == RecyclerView.SCROLL_STATE_IDLE)
        {
            Picasso.with(context).resumeTag(tag);
        }
        else
        {
            Picasso.with(context).pauseTag(tag);
        }
     }
});

6 RGB_565
对于不透明的图片可以使用RGB_565来优化内存。
Picasso.with( imageView.getContext() )
     .load(url)
     .config(Bitmap.Config.RGB_565)
     .into(imageView);

默认情况下，Android使用ARGB_8888
Android中有四种，分别是：
ALPHA_8：每个像素占用1byte内存
ARGB_4444:每个像素占用2byte内存
ARGB_8888:每个像素占用4byte内存
RGB_565:每个像素占用2byte内存

 */
public class MainActivity extends AppCompatActivity {

    Button mBtn1, mBtn2, mBtn3, mBtn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn1 = (Button) findViewById(R.id.demo1);
        mBtn2 = (Button) findViewById(R.id.demo2);
        mBtn3 = (Button) findViewById(R.id.demo3);
        mBtn4 = (Button) findViewById(R.id.demo4);

        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PicassoTestActivity.class);
                startActivity(intent);
            }
        });

        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PicassoRecyclerViewActivity.class);
                startActivity(intent);
            }
        });

        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PicassoStaggeredActivity.class);
                startActivity(intent);
            }
        });

        mBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
