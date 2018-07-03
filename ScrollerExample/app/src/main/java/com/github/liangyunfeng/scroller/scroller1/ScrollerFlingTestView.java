package com.github.liangyunfeng.scroller.scroller1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Created by yunfeng.l on 2018/7/2.
 */

/**
 * Scroller 的完整实战
 * <p>
 * 我们现在的目标是自定义一个 View，检验我们所学习的 Scroller 知识及 View 自身滑动机制如 scrollBy。包括：
 * 1. Scroller 的 scroll 滚动，也就是普通滚动。
 * 2. 自定义 View 对触摸机制的反馈，也就是手指能够滑动 View 的内容而不需要外部控件的点击事件触发，在 View 的 onTouchEvent() 自行处理，这考察 scrollBy 的知识。
 * 3. Scroller 的 fling 滚动，也就是快速滚动。
 * <p>
 * <p>
 * 好了，现在我们来聚集目标 3 ，它要实现的是一个 fling 动作，也就是快速滚动动力。
 * 在手指离开屏幕时，我们要判断它的初始速度，如果速度大于我们特定的一个阀值，那么我们就得借助 Scroller 中 fling() 方法的力量了。
 * 我们仍然在 onTouchEvent 处理手指离开屏幕时的情景，最重要的是如何来捕捉手指的速度。
 * Android 中给我们提供了这么一个类 VelocityTracker，看名字就知道是速度追踪器的意思。
 * <p>
 * VelocityTracker 怎么使用呢？
 * <p>
 * //1. 第一步创建
 * VelocityTracker mVelocityTracker = VelocityTracker.obtain();
 * <p>
 * //2. 将 MotionEvent 事件添加到 VelocityTracker 变量中去
 * mVelocityTracker.addMovement(event);
 * <p>
 * //3. 计算 x、y 轴的速度,
 * //第一个参数表明时间单位，1000 代表 1000 ms 也就是 1 s，计算 1 s 内滚动多少个像素,后面表示最大速度
 * mVelocityTracker.computeCurrentVelocity(1000,600.0f);
 * <p>
 * //4. 获取速度
 * float xVelocity = mVelocityTracker.getXVelocity();
 * float yVelocity = mVelocityTracker.getYVelocity();
 * <p>
 * //之后的事情就是你自己根据获取的速度值做一些处理了。
 * <p>
 * <p>
 * <p>
 * 总结
 * <p>
 * 文章最后，我建议大家可以闭上眼睛回顾下这篇博文的内容，这样有助于自己的记忆与理解，并且这种方法对于学习其它新的知识也非常有效。
 * <p>
 * 这篇文章的主要内容可以总结如下：
 * 1.View 滑动的基础是 mScrollX 和 mScrollY 两个属性。
 * 2.Android 系统处理滑动时会在 onDraw(Canvas canvas) 方法之前，对 canvas 对象进行平移，canvas.translate(mLeft-mScrollX,mRight-mScrollY)。平移的目的是将坐标系从 ViewGroup 转换到 child 中。
 * 3.调用一个 View 的滑动有 scrollBy() 和 scrollTo() 两种，前一种是增量式，后一种直接到位。
 * 4.如果要实现平滑滚动的效果，不借助于 Scroller 而自己实现属性动画也是可以完成的，原因还是针对 mScrollX 或者 mScrollY 的变化引起的重绘。
 * 5.View 滚动的区域是内容，也就是它绘制的内容，而对于一个 ViewGroup 而言，它的内容还包括它的 children。所以，如果想移动一个 View，本身那么就应该调用它的 parent 的 scrollBy() 或者 scrollTo() 方法。
 * 6.Scroller 本身并不神秘与复杂，它只是模拟提供了滚动时相应数值的变化，复写自定义 View 中的 computeScroll() 方法，在这里获取 Scroller 中的 mCurrentX 和 mCurrentY，根据自己的规则调用 scrollTo() 方法，就可以达到平稳滚动的效果。
 * 7.Scroller 提供快速滚动的功能，需要在自定义 View 的 onTouchEvent() 方法中获取相应方向的初始速度，然后调用 Scroller 的 startFling() 方法。
 * 8.最重要的一点就是要深刻理解 mScrollX、mScrollY 在 Canvas 坐标中的意义，要区分手指滑动方向、内容滑动方向和 mScrollX、mScrollY 数值的关系。
 */
public class ScrollerFlingTestView extends View {

    private static final String TAG = "TestView";

    Paint mPaint;

    // Scroller
    Scroller mScroller;

    float mLastPointX;
    float mLastPointY;
    int mSlop;
    int MIN_FING_VELOCITY = 5;

    VelocityTracker mVelocityTracker;

    public ScrollerFlingTestView(Context context) {
        this(context, null);
    }

    public ScrollerFlingTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollerFlingTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        // Scroller
        mScroller = new Scroller(context);

        // 获取最小能够识别的滑动距离
        mSlop = ViewConfiguration.getTouchSlop();
        setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);

        canvas.save();
        canvas.translate(100, 100);

        canvas.drawCircle(0, 0, 40.0f, mPaint);
        canvas.drawCircle(50, 50, 40.0f, mPaint);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //1. 第一步创建
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        //2. 将 MotionEvent 事件添加到 VelocityTracker 变量中去
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                restoreTouchPoint(event);
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (event.getX() - mLastPointX);
                int deltaY = (int) (event.getY() - mLastPointY);
                if (Math.abs(deltaX) > mSlop || Math.abs(deltaY) > mSlop) {
                    // 取值的正负与手势的方向相反，这在前面的文章已经解释过了
                    scrollBy(-deltaX, -deltaY);
                    restoreTouchPoint(event);
                }
                break;

            case MotionEvent.ACTION_UP:
                // 3. 计算 x、y 轴的速度,
                mVelocityTracker.computeCurrentVelocity(1000, 2000.0f);
                //4. 获取速度
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                int yVelocity = (int) mVelocityTracker.getYVelocity();
                Log.d(TAG, "onTouchEvent: xVelocity:" + xVelocity + " yVelocity:" + yVelocity);

                // 5. 之后的事情就是你自己根据获取的速度值做一些处理了
                if (Math.abs(xVelocity) > MIN_FING_VELOCITY
                        || Math.abs(yVelocity) > MIN_FING_VELOCITY) {

                    // 固定X，Y轴fling
                    //xVelocity = Math.abs(xVelocity) > Math.abs(yVelocity) ? -xVelocity : 0;
                    //yVelocity = xVelocity == 0 ? -yVelocity : 0;

                    mScroller.fling(getScrollX(), getScrollY(),
                            -xVelocity, -yVelocity, -1000, 1000, -1000, 2000);
                    invalidate();
                }


                // 如果不需要加速度等效果，可以直接使用startScroll来fling
                //mScroller.startScroll(getScrollX(), getScrollY(), dx, dy);
                //invalidate();
                break;

            default:
                break;
        }

        return true;
    }

    private void restoreTouchPoint(MotionEvent event) {
        mLastPointX = event.getX();
        mLastPointY = event.getY();
    }

    public void startScrollBy(int dx, int dy) {
        mScroller.forceFinished(true);
        int startX = getScrollX();
        int startY = getScrollY();
        mScroller.startScroll(startX, startY, startX + dx, startY + dy, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            if (mScroller.getCurrX() == getScrollX()
                    && mScroller.getCurrY() == getScrollY()) {
                postInvalidate();
            }
        }
    }
}
