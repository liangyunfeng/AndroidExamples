package com.github.liangyunfeng.picasso.demo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.liangyunfeng.picasso.R;
import com.github.liangyunfeng.picasso.demo2.AndroidVersion;
import com.github.liangyunfeng.picasso.demo2.DataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunfeng.l on 2018/5/31.
 */

public class PicassoRecyclerViewActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<AndroidVersion> mVersionsList;
    DataAdapter adapter;

    private final String android_version_names[] = {
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat",
            "Lollipop",
            "Marshmallow"
    };

    private final String android_image_urls[] = {
            "https://img-blog.csdn.net/20160527205448521",
            "https://img-blog.csdn.net/20160527205516475",
            "https://img-blog.csdn.net/20160527205804527",
            "https://img-blog.csdn.net/20160527205831152",
            "https://img-blog.csdn.net/20160527205912200",
            "https://img-blog.csdn.net/20160527205946793",
            "https://img-blog.csdn.net/20160527210011184",
            "https://img-blog.csdn.net/20160527210026200",
            "https://img-blog.csdn.net/20160527210039778",
            "https://img-blog.csdn.net/20160527210053919"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picasso_recyclerview_activity);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mVersionsList = initData();
        adapter = new DataAdapter(getApplicationContext(), mVersionsList);
        mRecyclerView.setAdapter(adapter);
    }

    private List<AndroidVersion> initData() {
        List<AndroidVersion> androidVersions = new ArrayList<>();
        for (int i = 0; i < android_version_names.length; ++i) {
            AndroidVersion androidVersion = new AndroidVersion();
            androidVersion.setUrl(android_image_urls[i]);
            androidVersion.setVersionName(android_version_names[i]);
            androidVersions.add(androidVersion);
        }
        return androidVersions;
    }
}
