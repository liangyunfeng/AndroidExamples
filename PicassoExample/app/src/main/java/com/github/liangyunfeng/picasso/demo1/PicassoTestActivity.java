package com.github.liangyunfeng.picasso.demo1;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.liangyunfeng.picasso.MainActivity;
import com.github.liangyunfeng.picasso.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunfeng.l on 2018/5/31.
 */

public class PicassoTestActivity extends AppCompatActivity {

    Button mBtn1, mBtn2, mBtn3, mBtn4;
    ImageView mImageView1, mImageView2, mImageView3, mImageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picasso_test_activity);

        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn3 = (Button) findViewById(R.id.btn3);
        mBtn4 = (Button) findViewById(R.id.btn4);
        mImageView1 = (ImageView) findViewById(R.id.iv1);
        mImageView2 = (ImageView) findViewById(R.id.iv2);
        mImageView3 = (ImageView) findViewById(R.id.iv3);
        mImageView4 = (ImageView) findViewById(R.id.iv4);

        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get()
                        .load("http://i.imgur.com/DvpvklR.png")
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(mImageView1, new Callback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(PicassoTestActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(PicassoTestActivity.this, "onError", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get()
                        .load(R.mipmap.ic_launcher)
                        .into(mImageView2);
            }
        });

        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true)
                    return;
                Picasso.get()
                        .load(Uri.fromFile(new File("")))
                        .tag(this)
                        .resize(500, 300)
                        .centerCrop()
                        .centerInside()
                        .fit()
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE, NetworkPolicy.OFFLINE)
                        .noFade()
                        .noPlaceholder()
                        .onlyScaleDown()
                        .into(mImageView3);
            }
        });

        mBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                // 设置缓存目录
                File directory = new File(FileUtil.getCachePath(this));
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                // 设置缓存大小为运行时缓存的八分之一
                long maxSize = Runtime.getRuntime().maxMemory() / 8;

                OkHttpClient client = new OkHttpClient.Builder()
                        .cache(new Cache(directory, maxSize))
                        .build();

                Picasso picasso = new Picasso.Builder(this)
                        .downloader(new OkHttp3Downloader(client))
                        .build();

                Picasso.setSingletonInstance(picasso);
                 */
                Picasso singeltonPicasso = new Picasso.Builder(PicassoTestActivity.this)
                        //重新定义下载器，指定缓存路径
                        .downloader(new OkHttp3Downloader(new File(getExternalCacheDir(),"my-cache"),50*1024*1024))
                        //log日志开启
                        .loggingEnabled(true)
                        .build();
                //设置到全局的Picasso中
                Picasso.setSingletonInstance(singeltonPicasso);

                List<Transformation> transformations = new ArrayList<Transformation>();
                transformations.add(new CircleTransformation());
                transformations.add(new BlurTransformation(PicassoTestActivity.this));

                Picasso.get()
                        //.load(new File(""))
                        .load("http://i.imgur.com/DvpvklR.png")
                        .transform(transformations)
                        .into(mImageView4);

                /*
                //实例化一个OkHttpClient为其设置自定义的缓存
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setCache(new Cache(new File(getExternalCacheDir(),"my-cache"),50*1024*1024));
                //实例化OkHttpDownloader
                OkHttpDownloader downloader = new OkHttpDownloader(okHttpClient);
                //将downloader构建进Picasso中
                //-----------------------------------------------------------------------------------------------
                //获取缓存操作的对象
                Cache cache = okHttpClient.getCache();
                try {
                //删除所以缓存目录下的文件
                    cache.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 */
            }
        });
    }
}
