package com.github.liangyunfeng.scroller.tradition;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by yunfeng.l on 2018/7/2.
 */

public class TestView extends View {
    private static final String TAG = "TestView";

    Paint mPaint;

    // Scroller
    Scroller mScroller;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        // Scroller
        mScroller = new Scroller(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);

        //canvas.drawCircle(0, 0, 40.0f, mPaint);

        // 法一：
        //canvas.drawCircle(100, 100, 40.0f, mPaint);
        //canvas.drawCircle(150, 150, 40.0f, mPaint);

        // 法二：
        canvas.save();
        canvas.translate(100, 100);

        canvas.drawCircle(0, 0, 40.0f, mPaint);
        canvas.drawCircle(50, 50, 40.0f, mPaint);

        canvas.restore();

        // 代码有差别吗？有。什么差别？后面一种运用 translate 平移操作。它绘制圆的时候坐标不是针对 （100，100）而是（0，0），
        // 它认为它是在（0，0）绘制了一个圆，而实际上的效果是它在（100，100）的地方绘制了一个圆。大家有没有体会到这种感觉？
        // 前面一节的时候，TestView 的 onDraw() 方法根本就没有改变，但是 mScrollX 和 mScrollY 的改变，导致它的 Canvas 坐标体系已经发生了改变，
        // 经过刚才的示例，我们可以肯定地说，在一个 View 的 onDraw() 方法之前，一定某些地方对 canvas 进行了 translate 平移操作。
        //
        //
        // 谁平移了 Canvas ?
        // 既然一个 View 中 onDraw() 方法获取到的 Canvas 已经经过了坐标系的变换，那么如果要追踪下去，肯定就是要调查 View.onDraw() 方法被谁调用。
        // 这个时候就需要阅读 View.java 或者其它类的源码了
    }

    public void startGunDong(int dx, int dy) {
        int startX = getScrollX();
        int startY = getScrollY();
        PropertyValuesHolder xholder = PropertyValuesHolder.ofInt("scrollX", startX, startX + dx);
        PropertyValuesHolder yholder = PropertyValuesHolder.ofInt("scrollY", startY, startY + dy);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, xholder, yholder);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        animator.start();
    }

    // Scroller
    public void startScrollBy(int dx, int dy) {
        mScroller.forceFinished(true);
        int startX = getScrollX();
        int startY = getScrollY();
        mScroller.startScroll(startX, startY, startX + dx, startY + dy, 1000);
        invalidate();
    }

    // Scroller

    /**
     * 实际上，官方文档也是这样建议的。因为 computeScrollOffset() 被不断地调用，所以 Scroller 中的 mCurrentX 和 mCurrentY 被不断地被更新，所以 Scroller 动画就能够跑去起来。
     * 但是，Scroller 跑去起来想 View 本身滚动与否没有一丁点关系，我们还需要一些东西，需要什么？
     *
     * 雀桥，你在哪里？
     *
     * 如果把 mScrollX 与 mScrollY 比作牛郎，把 Scroller 比作织女的话，要想相会，雀桥是少不了的。
     *
     * Scroller 中 mCurrentX、mCurrentY 必须要与 View 中的 mScrollX、mScrollY 建立相应的映射才能够使 View 真正产生滚动的效果，那么就必须要找一个合适的场所，进行这个庄严的仪式，这个场所我称为雀桥。
     * 那么，在一个 View 中由谁担任呢？
     *
     * 不知道大家还有没有印象？文章开始的地方，分析到 ViewGroup 的 drawChild() 时，ViewGroup 会调用 View 的 draw() 方法，在这个方法中有这么一段代码。
     * if (!drawingWithRenderNode) {
     *     computeScroll();
     *     sx = mScrollX;
     *     sy = mScrollY;
     * }
     *
     * 程序会先调用，computeScroll() 方法，然后再对 sx 和 sy 进行赋值，最终 canvas.translate(mLeft-sx,mTop-sy)，导致了 View 本身的滚动效果。
     *
     * 然后在 View.java 中 computeScroll() 方法只是一个空方法，但它的注释说，这个方法由继承者实现，主要用来更新 mScrollX 与 mScrollY。
     *
     * 看到这里，大家应该意识到了，computeScroll() 就是雀桥，我们可以在每次重绘时调用 Scroller.computeScrollOffset() 方法，
     * 然后通过获取 mCurrentX 与 mCurrentY，依照我们特定的意图来更改 mScrollX 和 mScrollY 的值，这样 Scroller 就能驱动起来，并且 Scroller 中的属性也驱动了 View 本身的滚动。
     *
     * 用一个例子来加深理解，继续改造我们的 TestView，现在不借助于属性动画的方式，通过 Scroller 来进行滚动操作。
     *
     *
     *
     *
     * 之前说过，Scroller.computeScrollOffset() 需要在动画期间循环调用，当时我用了一个 while 循环示例，但是上面的代码并没有使用 while。这是怎么回事？
     *
     * 回想一下 Android 常用的编程技巧，如果让一个自定义的 View 不断重绘，我们可以怎么做？
     * 1.通过一个 Handler 不停地发送消息，在接收消息时调用 postInvalidate() 或者 invalidate()，然后延时再发送相同的消息。
     * 2.在 onDraw() 方法中调用 postInvalidate() 方法，可以导致 onDraw() 方法不断重绘。
     *
     * 显然，我们在这里采取的是第二种方法。当调用 mScroller.startScroll() 时，我们马上调用了 invalidate() 方法，这样会导致重绘，
     * 重绘的过程中 computeScroll() 方法会被执行，而我们在 computeScrollOffset() 中获取了 Scroller 中的 mCurrentX 和 mCurrentY，
     * 然后通过 scrollTo() 方法设置了 mScrollX 和 mScrollY，这一过程本来会导致重绘，但是如果 scrollTo() 里面的参数没有变化的话，
     * 那么就不会触发重绘，所以呢，我们又在后面代码补充了一个 postInvalidate() 方法的调用，
     * 当然，为了避免重复请求，可以在这个代码之前添加条件判断，判断的依据就是此次参数是不是和 mScrollX、mScrollY 相等。
     * 所以代码可以改成这样：
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {     // 1.当computeScroll()被调用时，先调用computeScrollOffset计算mCurrX和mCurrY
            Log.d(TAG, "computeScroll: ");

            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());   //  2.调用scrollTo来更新当前View中的mScrollX和mScrollY
            //postInvalidate();
            if (mScroller.getCurrX() == getScrollX()
                    && mScroller.getCurrY() == getScrollY() ) {
                postInvalidate();   // 3. 调用invalidate()方法来更新界面，并重复调用computeScroll()来循环更新
            }
        } else {
            Log.d(TAG, "computeScroll is over: ");
        }
    }
}

