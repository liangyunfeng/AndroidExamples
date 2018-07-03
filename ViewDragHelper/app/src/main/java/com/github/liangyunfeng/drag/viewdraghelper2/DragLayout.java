package com.github.liangyunfeng.drag.viewdraghelper2;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by yunfeng.l on 2018/7/2.
 */

/**
 * ViewDragHelper是support.v4下提供的用于处理拖拽滑动的辅助类,查看Android的DrawerLayout源码,可以发现,它内部就是使用了该辅助类来处理滑动事件的.
 *
 * 有了ViewDragHelper,我们在写与拖拽滑动相关的自定义控件的时候就变得非常简单了,例如我们可以用来实现自定义侧滑菜单,再也不需要在onTouchEvent方法里计算滑动距离来改变布局边框的位置了.
 *
 * 使用ViewDragHelper类的大体步骤分为3步:
 * 1.在自定义的ViewGroup子类下通过ViewDragHelper的静态方法获取到ViewDragHelper的实例引用,注意它是一个单例的.
 * 2.有了ViewDragHelper的引用后,我们就需要传递相关的触摸事件给ViewDragHelper来帮我们处理,那么怎么传递呢?
 *   可以通过重写onInterceptTouchEvent和onTouchEvent这2个函数来传递
 * 3:重写ViewDragHelper.Callback()的相关回调方法,处理事件
 */
public class DragLayout extends FrameLayout {
    private String TAG = "DragLayout";

    private ViewDragHelper dragHelper;

    private LinearLayout mLeftContent;  // 左侧面板
    private LinearLayout mMainContent;  // 主体面板

    int mHeight;
    int mWidth;
    int mRange;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 重写此方法,可以获取该容器下的所有的直接子View
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftContent = (LinearLayout) getChildAt(0);
        mMainContent = (LinearLayout) getChildAt(1);
    }

    private void init() {
        // step1 通过ViewDragHelper的单例方法获取ViewDragHelper的实例
        dragHelper = ViewDragHelper.create(this, mCallback);
    }

    // step2 传递触摸事件,需要重写onInterceptTouchEvent和onTouchEvent
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 由ViewDragHelper类来决定是否要拦截事件
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            // 由ViewDragHelper类来决定是否要处理触摸事件
            dragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回true,可以持续接收到后续事件
        return true;
    }

    // step3 重写ViewDragHelper.Callback()的相关回调方法,处理事件
    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        /**
         * 1.改方法是abstract的方法,必须要实现,其返回结果决定当前child是否可以拖拽
         * @param child 当前被拖拽的view
         * @param pointerId pointerId 区分多点触摸的id
         * @return true表示允许拖拽, false则不允许拖拽 ,默认返回false
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.d(TAG, "tryCaptureView:当前被拖拽的view:" + child);
            // 如果需要拖动left view来联动main view，需要在这里同时添加child == mLeftContent也返回true
            if (child == mMainContent || child == mLeftContent) {
                return true; //只有主题布局可以被拖动
            }
            return false;
        }

        /**
         * 根据建议值修正将要移动到的横向位置,此时没有发生真正的移动
         * @param child 当前被拖拽的View
         * @param left 新的建议值
         * @param dx 水平位置的变化量
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.d(TAG, "clampViewPositionHorizontal:" + "旧的left坐标oldLeft:" + child.getLeft()
                    + " 水平位置的变化量dx:" + dx + " 新的建议值left:" + left);

            Log.d(TAG, "clampViewPositionHorizontal 建议值left:" + left + " mRange:" + mRange);

            // 控制滑动范围
            if (left > mRange) {
                left = mRange;
            } else if (left < 0) {
                left = 0;
            }

            /**
             * 该方法返回的是水平方向的移动建议值,该建议值等于当前的X坐标+水平方向的变化量,向右移动,偏移量为正值,向左移动则为负数.
             * 默认返回的是调用父类的重写的方法,查看源码可以发现默认返回的是0,如果建议值等于0的话,就表示水平方向不会移动.
             * 如果想要移动,我们需要返回它提供的建议值left,我们来看看运行的log:
             *
             * 由上面的log可以看出,分别是向右拖拽和向左拖拽的结果,如果我们返回了它的建议值,就可以实现水平方向的拖动了.
             * 将clampViewPositionHorizontal的返回值修改成return left;
             *
             * 跟我们预想的结果一样,只有主体布局可以滑动,左侧的布局不能滑动,如果想要左侧布局也可以滑动,那么只需要在tryCaptureView直接返回true即可.
             */
            //return super.clampViewPositionHorizontal(child, left, dx); //父类默认返回0
            return left;
        }

        // 同样的,如果要实现垂直方向的拖拽滚动,就需要重新下面这个方法了.

        /**
         * 根据建议值修正将要移动到的纵向位置,此时没有发生真正的移动
         * @param child 当前被拖拽的View
         * @param top 新的建议值
         * @param dy 垂直位置的变化量
         * @return
         */
        /*@Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.d(TAG, "clampViewPositionHorizontal:" + "旧的top坐标oldTop:" + child.getTop()
                    + " 垂直位置的变化量dy:" + dy + " 新的建议值top:" + top);

            // 控制滑动范围
            if (top > mRange) {
                top = mRange;
            } else if (top < 0) {
                top = 0;
            }

            return top;
        }*/

        /**
         * 该回调方法和tryCaptureView一样都可以获取到当前被拖拽的View,不同点在于tryCaptureView是可以决定哪个View是可以被拖拽滑动的,
         * 而onViewCaptured只是用来获取当前到底哪个View被正在拖拽而已.
         *
         * 当capturedChild被捕获时调用
         * @param capturedChild 当前被拖拽的view
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            Log.d(TAG, "onViewCaptured:当前被拖拽的view:" + capturedChild);
            super.onViewCaptured(capturedChild, activePointerId);
        }

        // 那么话又说回来,我们有什么办法可以限制子View的滑动范围呢,如果范围不能很好的控制的话,那滑动也没有什么意义了.
        // 还记得上面介绍的clampViewPositionHorizontal和clampViewPositionVertical吗,分别用于设置水平方向和垂直方向的移动建议值,
        // 假设我们要限制该自定义控件的子View在水平方向移动的范围为0-屏幕宽度的0.6,那么如何控制呢.要实现这个限制,
        // 我们现在获取屏幕的宽度,由于当前的自定义控件是全屏显示的,所以直接就可以那控件的宽度来作为屏幕的宽度,
        // 那么如何获取呢?有2种方式,分别是在onMeasure和onSizeChange方法里面调用getMeasuredWidth()方法获取.
        // 前者是测量完之后获取,后者是当控件的宽高发生变化后获取,例如:

        /**
         * 返回拖拽的范围,不对拖拽进行真正的限制,仅仅决定了动画执行的速度
         * @param child
         * @return 返回一个固定值
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            int range = super.getViewHorizontalDragRange(child);
            Log.d(TAG, "被拖拽的范围getViewHorizontalDragRange:" + range);
            return range;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return super.getViewVerticalDragRange(child);
        }

        // 如此一来,我们就可以随意的控制子View的拖拽滑动的范围了.那么新的问题又来的,如果现在的需求是只能mMainContent被拖拽,mLeftContent不能被拖拽,
        // 也许你会说,这还不简单吗,直接在tryCaptureView判断当前拖拽的子View是mLeftContent的话就返回false不就得了,没错,如果需求只是这样的话确实可以满足了,
        // 但是如果在加上一个条件,那就是拖拽mLeftContent的时候的效果相当于把mMainContent拖拽了,什么意思呢,也就说现在mMainContent已经是打开的状态了,
        // 我想通过滑动mLeftContent就能把mMainContent滑动了.而mLeftContent还是原来的位置不动.这个要怎么实现呢?
        //
        // 首先可以肯定的是,tryCaptureView方法必须返回true,表示mMainContent和mLeftContent都可以被滑动,
        // 接下来要处理的就是如何在mLeftContent滑动的时候是滑动mMainContent的.那么现在就要介绍另一个回调方法了,如下所示:
        //
        // 该方法是随着View的位置发生变化而不断回调的.四个参数如上面的注释所述,通过该方法可以拿到当前正在拖拽滑动的View是哪个View,有了这依据之后,
        // 我们就将在mLeftContent上的滑动的水平方向和垂直方向的变化量传递给mMainContent,这样一来,滑动mLeftContent的效果不就等于滑动mMainContent了吗.
        // 需要注意的是,该回调方法在低版本上为了兼容还需要加上invalidate();这句代码,invalidate是用来刷新界面的,他会导致界面的重绘.
        //
        // 由log可以看出,最新的left和top值是等于上一次的位置+水平/垂直方向的变化量.这个特点有点类似建议值了.不
        // 同的是建议值发生了改变不代表View就一定已经处于滑动,除非返回的值也是建议值,但是onViewPositionChanged方法就不同了,
        // 这个方法只要一执行,就说明目标View是处于滑动状态的.
        //
        // 以水平方向滑动为例,垂直方向不移动,接下来就可以在onViewPositionChanged方法内做判断了,如下所示:
        //
        //

        /**
         * 当View的位置发生变化的时候,处理要做的事情(更新状态,伴随动画,重绘界面)
         * 此时,View已经发生了位置的改变
         *
         * @param changedView 改变位置的View
         * @param left        新的左边值
         * @param top         新的上边值
         * @param dx          水平方向的变化量
         * @param dy          垂直方向的变化量
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            // 获取最新的left坐标
            int newLeft = left;
            if (changedView == mLeftContent) {
                // 如果滑动的是mLeftContent,则将其水平变化量dx传递给mMainContent,记录在newLeft中
                newLeft = mMainContent.getLeft() + dx;
            }

            // 矫正范围
            if (newLeft > mRange) {
                newLeft = mRange;
            } else if (newLeft < 0) {
                newLeft = 0;
            }

            // 再次判断,限制mLeftContent的滑动
            if (changedView == mLeftContent) {
                // 强制将mLeftContent的位置摆会原来的位置,这里通过layout方法传入左,上,右,下坐标来实现
                // 当然方法不限于这一种,例如还可以通过scrollTo(x,y)方法
                mLeftContent.layout(0, 0, mWidth, mHeight);
                // 改变mMainContent的位置
                mMainContent.layout(newLeft, 0, newLeft + mWidth, mHeight);
            }

            Log.v("lyf", "onViewPositionChanged: left = " + left + ", newLeft = " + newLeft);

            // 为了兼容低版本, 每次修改值之后, 进行重绘
            invalidate();
        }

        // 由上面的效果图可以发现已经可以实现当手指向右滑动mLeftContent时,滑动的效果等于向右滑动mMainContent,当同时也会发现一个问题,
        // 那就是手指在mLeftContent向左滑动的时候并没有效果,这是因为我们限制了子View的滑动范围就是0-mRange,所以,如果滑动时小于0是没有效果的.
        // 那如果我们想要实现在mLeftContent当手指有向左滑动的趋势,或者手指在mMainContent有向左滑动的趋势时,
        // 就关闭mLeftContent,让mMainContent自动向左滑动到x=0的位置,反之就是打开mLeftContent,让mMainContent滑动到x=mRange的位置,
        // 这个要怎么实现呢?首先我们要能够想到的时,这个向左滑动的趋势肯定是与手指松手后相关的,那有没有一个回调方法是与手指触摸松开相关的呢?
        // 下面将介绍另一个回调方法,如下所示:
        //
        // 有了这个方法,我们就可以实现我们刚刚说到的效果了,首先我们来考虑下那些情况是和打开mLeftContent相关的,有2种情况:
        // 1.当前水平方向的速度xvel=0,同时mMainContent的x位置是大于mRange/2.0f的.
        // 2.当前水平方向的速度xvel>0
        //
        // 考虑了所有打开的情况,那么剩下的情况就是关闭mLeftContent.
        //
        // 具体逻辑如下:
        //
        // 细心的话,可以发现上面的打开和关闭动画都是瞬间完成的,看起来效果不怎么好,如何实现平滑的打开和关闭呢?
        //
        // 通过ViewDragHelper的smoothSlideViewTo(View child, int finalLeft, int finalTop)方法可以实现平滑的滚动目标View到指定的left或者top坐标位置,
        // 接收3个参数,参数child表示要滑动的目标View,finalLeft和finalTop表示目标view最终平滑滑动到的位置.翻看源码,发现其实现原理是通过Scroller对象来实现的,
        // 也就说我们还需要重写computeScroll方法,不断的刷新当前界面,具体设置如下:

        /**
         * 当被拖拽的View释放的时候回调,通常用于执行收尾的操作(例如执行动画)
         * @param releasedChild 被释放的View
         * @param xvel 水平方向的速度,向右释放为正值,向左为负值
         * @param yvel 垂直方向的速度,向下释放为正值,向上为负值
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.d(TAG, "View被释放onViewReleased:" + "释放时水平速度xvel:" + xvel + " 释放时垂直速度yvel:" + yvel);
            super.onViewReleased(releasedChild, xvel, yvel);
            if (xvel > 0 || (xvel == 0 && mMainContent.getLeft() > mRange / 2.0f)) {
                //打开mLeftContent,即mMainContent的x=mRange
                // mMainContent.layout(mRange, 0, mRange + mWidth, mHeight);    // 不平滑
                if (dragHelper.smoothSlideViewTo(mMainContent, mRange, 0)) {
                    // 返回true代表还没有移动到指定位置, 需要刷新界面.
                    // 参数传this(child所在的ViewGroup)
                    ViewCompat.postInvalidateOnAnimation(DragLayout.this);
                }
            } else {
                //关闭mLeftContent,即mMainContent的x=0
                // mMainContent.layout(0, 0, mWidth, mHeight);
                if (dragHelper.smoothSlideViewTo(mMainContent, 0, 0)) {
                    ViewCompat.postInvalidateOnAnimation(DragLayout.this);
                }
            }
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 当尺寸有变化的时候调用
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        // 移动的范围
        mRange = (int) (mWidth * 0.6f);
    }

    // 调用smoothSlideViewTo，翻看源码,发现其实现原理是通过Scroller对象来实现的,也就说我们还需要重写computeScroll方法,不断的刷新当前界面
    @Override
    public void computeScroll() {
        super.computeScroll();
        // 2. 持续平滑动画 (高频率调用)
        if (dragHelper.continueSettling(true)) {
            //  如果返回true, 动画还需要继续执行
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
