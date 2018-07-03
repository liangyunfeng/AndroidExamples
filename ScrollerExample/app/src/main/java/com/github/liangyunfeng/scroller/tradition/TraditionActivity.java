package com.github.liangyunfeng.scroller.tradition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.liangyunfeng.scroller.R;

/*
上面代码可以得到下面的结论：
1. computeScrollOffset() 方法会返回当前动画的状态，true 代表动画进行中，false 代表动画结束了。
2. 如果动画没有结束，那么每次调用 computeScrollOffset() 方法，它就会更新 mCurrentX 和 mCurrentY 的数值。

但是，翻阅 Scroller 的代码，也没有找到一个定时器，或者是一个属性动画启动的地方，相关联的只有一个插值器。
所以，我的猜测就是，如果要让 Scroller 正常运行，就编写下面这样的代码。

Scroller scroller = new Scroller(context);

scroller.startScroll(0,0,100,100);

boolean condition = true;

while ( condition ) {

    if ( scroller.computeScrollOffset() ) {
        ...
    }

    .....
}

实际上，官方文档也是这样建议的。因为 computeScrollOffset() 被不断地调用，所以 Scroller 中的 mCurrentX 和 mCurrentY 被不断地被更新，所以 Scroller 动画就能够跑去起来。
但是，Scroller 跑去起来想 View 本身滚动与否没有一丁点关系，我们还需要一些东西，需要什么？

雀桥，你在哪里？

如果把 mScrollX 与 mScrollY 比作牛郎，把 Scroller 比作织女的话，要想相会，雀桥是少不了的。

Scroller 中 mCurrentX、mCurrentY 必须要与 View 中的 mScrollX、mScrollY 建立相应的映射才能够使 View 真正产生滚动的效果，那么就必须要找一个合适的场所，进行这个庄严的仪式，这个场所我称为雀桥。
那么，在一个 View 中由谁担任呢？

不知道大家还有没有印象？文章开始的地方，分析到 ViewGroup 的 drawChild() 时，ViewGroup 会调用 View 的 draw() 方法，在这个方法中有这么一段代码。
if (!drawingWithRenderNode) {
    computeScroll();
    sx = mScrollX;
    sy = mScrollY;
}

程序会先调用，computeScroll() 方法，然后再对 sx 和 sy 进行赋值，最终 canvas.translate(mLeft-sx,mTop-sy)，导致了 View 本身的滚动效果。

然后在 View.java 中 computeScroll() 方法只是一个空方法，但它的注释说，这个方法由继承者实现，主要用来更新 mScrollX 与 mScrollY。

看到这里，大家应该意识到了，computeScroll() 就是雀桥，我们可以在每次重绘时调用 Scroller.computeScrollOffset() 方法，
然后通过获取 mCurrentX 与 mCurrentY，依照我们特定的意图来更改 mScrollX 和 mScrollY 的值，这样 Scroller 就能驱动起来，并且 Scroller 中的属性也驱动了 View 本身的滚动。

用一个例子来加深理解，继续改造我们的 TestView，现在不借助于属性动画的方式，通过 Scroller 来进行滚动操作。
 */
public class TraditionActivity extends AppCompatActivity {

    //int dx[] = {0, 0, -10, -10, -30, 20};
    int dx[] = new int[]{0, 0, -10, -10, -30, 20};
    int dy[] = new int[]{10, 0, 0, -5, -50, 30};
    int count = 0;

    Button mBtnTest;
    TestView mTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradtion);
        mTestView = (TestView) findViewById(R.id.testview);
        mBtnTest = (Button) findViewById(R.id.test);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mTestView.scrollBy(-1 * 10, -1 * 10);

                //mTestView.scrollBy(dx[count % 6], dy[count % 6]);
                //count++;

                // 通过动画自己实现
                //mTestView.startGunDong(-1 * 100, -1 * 100);

                // 通过Scroller类实现
                mTestView.startScrollBy(-1 * 100, -1 * 100);
            }
        });
    }
}
