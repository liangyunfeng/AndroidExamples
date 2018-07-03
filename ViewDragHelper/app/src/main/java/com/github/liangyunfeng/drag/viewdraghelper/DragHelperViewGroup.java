package com.github.liangyunfeng.drag.viewdraghelper;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by yunfeng.l on 2018/7/2.
 */

/**
 * // 决定了是否需要捕获这个 child，只有捕获了才能进行下面的拖拽行为
 * abstract boolean tryCaptureView(View child, int pointerId)
 * <p>
 * // 修整 child 水平方向上的坐标，left 指 child 要移动到的坐标，dx 相对上次的偏移量
 * int clampViewPositionHorizontal(View child, int left, int dx)
 * <p>
 * // 修整 child 垂直方向上的坐标，top 指 child 要移动到的坐标，dy 相对上次的偏移量
 * int clampViewPositionVertical(View child, int top, int dy)
 * <p>
 * // 手指释放时的回调
 * void onViewReleased(View releasedChild, float xvel, float yvel)
 * <p>
 * <p>
 * // 是否应该拦截 children 的触摸事件，只有拦截了 ViewDragHelper 才能进行后续的动作, 将它放在 ViewGroup 中的 onInterceptTouchEvent() 方法中就好了
 * boolean shouldInterceptTouchEvent(MotionEvent ev)
 * <p>
 * // 处理 ViewGroup 中传递过来的触摸事件序列在 ViewGroup 中的 onTouchEvent() 方法中处理
 * void processTouchEvent(MotionEvent ev)
 * <p>
 * <p>
 * // tryCaptureView() 方法返回 true 时才会导致下面的回调方法被调用clampViewPositionHorizontal() 和 clampViewPositionVertical() 中处理 child 拖拽时的位置坐标。
 * <p>
 * <p>
 * // 将 child 安置到坐标 (finalLeft,finalTop) 的位置。
 * settleCapturedViewAt(int finalLeft, int finalTop)
 * <p>
 * <p>
 * // 记录 child 刚开始被拖拽时的位置，这个可以在回调方法中设置
 * void onViewCaptured(View capturedChild, int activePointerId)
 * <p>
 * 回弹效果的思路：
 * 1. 在 onViewCaptured() 方法中记录拖拽前的坐标。
 * 2. 在 onViewReleased() 方法中调用 settleCapturedViewAt() 方法来重定位 child。
 * <p>
 * <p>
 * // 边缘拖拽开始
 * public void onEdgeDragStarted(int edgeFlags, int pointerId)
 * <p>
 * // 边缘被点击
 * public void onEdgeTouched(int edgeFlags, int pointerId)
 * <p>
 * // 设置相应哪个方向的侧滑，EDGE_LEFT， EDGE_RIGHT， EDGE_TOP， EDGE_BOTTOM， EDGE_ALL
 * void setEdgeTrackingEnabled (int edgeFlags)
 * <p>
 * <p>
 * // 快速滚动的意思，一般手指离开后 view 还会由于惯性继续滑动。
 * void flingCapturedView(int minLeft, int minTop, int maxLeft, int maxTop);
 * <p>
 * // 让 child 平滑地滑动到某个位置
 * boolean smoothSlideViewTo(View child, int finalLeft, int finalTop)
 * <p>
 * <p>
 * <p>
 * 现在看来，它也挺好实现的不是吗？
 * <p>
 * 当然，我演示的时候是用的继承的 FrameLayout，而实际上要做的工作还很多，大家可以尝试用 RecyclerView 来实现它。
 * 这个动图的效果只是一个引子，怎么去完美实现它属于对 ViewDragHelper 的实战了，是另外一个课题了。有兴趣的同学可以自行去研究。
 * <p>
 * 现在，对这篇博文进行一点知识点的回顾。
 * 1.不借助于 ViewDragHelper ，自己也能实现拖拽功能，比如博文的例子，比如 Launcher2 工程中相关的代码。
 * 2.ViewDragHelper 是一个工具类，为拖拽而生，它提供了一系列的方法和回调方法用来操纵拖拽及跟踪 child 被拖拽时的位置、状态。
 * 3.回调方法 tryCaptureView() 返回值为 true 时，ViewDragHelper 才能拖动对应的 child。但是可以直接调用 captureChildView() 方法来指定被拖动的 child。
 * 4.ViewDragHelper 要在 ViewGroup 中的 onInterceptTouchEvent() 方法中调用 shouldInterceptTouchEvent() 方法，然后在 ViewGroup 中的 onTouchEvent() 方法调用 processTouchEvent()。
 * 5.ViewDragHelper 内部有一个 Scroller 变量，所以涉及到位移动画如 settleCapturedViewAt()、flingCapturedView()、smoothSlideViewTo() 方法时要复写 ViewGroup 的 computeScroll() 方法，在这个方法中调用 ViewDragHelper 的 continueSettling()。
 * 6.如果要移动像 Button 这样 clickable == true 的控件，要复写 ViewDragHelper.Callback 中的两个回调方法 getViewHorizontalDragRange() 和 getViewVerticalDragRange()，使它们对应方法的返回值大于 0 就好了。
 */
public class DragHelperViewGroup extends FrameLayout {
    private static final String TAG = "DragHelperViewGroup";

    private ViewDragHelper mDragHelper;

    // 添加回弹效果
    private int mDragOriLeft;
    private int mDragOriTop;

    public DragHelperViewGroup(Context context) {
        this(context, null);
    }

    public DragHelperViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragHelperViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper = mDragHelper.create(this, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                Log.v(TAG, "tryCaptureView: child = " + child.getId() + ", pointerId = " + pointerId);
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.v(TAG, "clampViewPositionHorizontal: child = " + child.getId() + ", left = " + left + ", dx = " + dx);
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.v(TAG, "clampViewPositionVertical: child = " + child.getId() + ", top = " + top + ", dy = " + dy);
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                Log.v(TAG, "onViewReleased: releasedChild = " + releasedChild.getId() + ", xvel = " + xvel + ", yvel = " + yvel + ", mDragOriLeft = " + mDragOriLeft + ", mDragOriTop = " + mDragOriTop);

                // 测试flingCapturedView
                View child = getChildAt(1);
                if (child != null && child == releasedChild) {
                    // 测试flingCapturedView
                    mDragHelper.flingCapturedView(getPaddingLeft(), getPaddingTop(),
                            getWidth() - getPaddingRight() - child.getWidth(),
                            getHeight() - getPaddingBottom() - child.getHeight());
                } else {
                    // 添加回弹效果
                    mDragHelper.settleCapturedViewAt((int) mDragOriLeft, (int) mDragOriTop);
                }


                // 添加回弹效果
                // mDragHelper.settleCapturedViewAt(mDragOriLeft, mDragOriTop);
                invalidate();
            }

            // 添加回弹效果
            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                mDragOriLeft = capturedChild.getLeft();
                mDragOriTop = capturedChild.getTop();
                Log.v(TAG, "onViewCaptured: capturedChild = " + capturedChild.getId() + ", activePointerId = " + activePointerId + ", mDragOriLeft = " + mDragOriLeft + ", mDragOriTop = " + mDragOriTop);
            }

            // 添加边沿侧滑事件
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);

                // 添加边沿事件 来联动 滑动指定的child
                Log.d(TAG, "onEdgeDragStarted: " + edgeFlags);
                mDragHelper.captureChildView(getChildAt(2), pointerId);
                //mDragHelper.captureChildView(getChildAt(getChildCount() - 1), pointerId);
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                Log.d(TAG, "onEdgeTouched: " + edgeFlags);
            }

            @Override
            public boolean onEdgeLock(int edgeFlags) {
                Log.d(TAG, "onEdgeLock: " + edgeFlags);
                return super.onEdgeLock(edgeFlags);
            }
        });

        // 添加边沿侧滑事件
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v(TAG, "onInterceptTouchEvent: MotionEvent = " + ev);
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG, "onTouchEvent: MotionEvent = " + event);
        mDragHelper.processTouchEvent(event);
        return true;
    }

    // 添加回弹效果
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper != null && mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    // 测试smoothSlideViewTo
    public void testSmoothSlide(boolean isReverse) {
        if (mDragHelper != null) {
            View child = getChildAt(0);
            if (child != null) {
                if (isReverse) {
                    mDragHelper.smoothSlideViewTo(child,
                            getLeft(), getTop());
                } else {
                    mDragHelper.smoothSlideViewTo(child,
                            getRight() - child.getWidth(),
                            getBottom() - child.getHeight());
                }
                invalidate();
            }
        }
    }
}
