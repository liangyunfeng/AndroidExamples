package com.github.liangyunfeng.coordinator.four;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.github.liangyunfeng.coordinator.R;

/**
 * Created by yunfeng.l on 2018/7/4.
 */

/**
 *OnOffsetChangedListener
 *void onOffsetChanged (AppBarLayout appBarLayout, int verticalOffset)
 *
 * verticalOffset 是 AppBarLayout 相对于完全展开时没有滑动的距离。它在初始位置为 0，其它时候都为负数。
 * 它绝对值的最大值为 AppBarLayout 的 TotalScollRange。
 *
 * 我们可以根据实际的业务需求，通过监听 AppBarLayout 的位移做一些相应的处理。
 */
public class FourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        final ImageView circleIcon = (ImageView) findViewById(R.id.circle_icon);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int newalpha = 255 + verticalOffset;
                newalpha = newalpha < 0 ? 0 : newalpha;
                circleIcon.setAlpha(newalpha);
            }
        });
    }
}