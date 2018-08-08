package com.github.liangyunfeng.coordinator.three;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.liangyunfeng.coordinator.R;

/**
 * Created by yunfeng.l on 2018/7/4.
 */

/**
 * CollapsingToolbarLayout 出现的目的只是为了增强 Toolbar。
 *
 * 它为 Toolbar 带来了下面几个特性。
 *
 * Collapsing Title 可折叠的标题
 * Content Scrim 内容纱布
 * Status bar scrim 状态栏纱布
 * Parallax scrolling children 子 View 的视差滚动行为
 * Pinned position children 子类的位置固定行为
 *
 */
public class ThreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
    }
}