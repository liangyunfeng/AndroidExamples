package com.github.liangyunfeng.drag.viewdraghelper3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by yunfeng.l on 2018/7/2.
 */

public class DragLayout extends FrameLayout {
    private Context context;

    private GestureDetectorCompat gestureDetector;
    private ViewDragHelper dragHelper;
    private DragListener dragListener;

    /**
     * 水平可以滚动的范围
     */
    private int horizontalRange;
    /**
     * 垂直可以滚动的范围
     */
    private int verticalRange;
    /**
     * 默认滚动式水平的
     */
    private Orientation orientation = Orientation.Horizontal;

    private int viewWidth;
    private int viewHeight;
    private int distanceLeft;
    private int distanceTop;

    private ViewGroup layoutMenu;
    private ViewGroup layoutContent;

    private Status status = Status.Close;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gestureDetector = new GestureDetectorCompat(context,
                new XYScrollDetector());
        dragHelper = ViewDragHelper.create(this, dragHelperCallback);
    }

    class XYScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx,
                                float dy) {
            if (orientation == Orientation.Vertical) {
                return Math.abs(dy) >= Math.abs(dx);
            }
            return Math.abs(dy) <= Math.abs(dx);
        }
    }

    private ViewDragHelper.Callback dragHelperCallback = new ViewDragHelper.Callback() {
        // 这个是返回被横向移动的子控件child的左坐标left，和移动距离dx，我们可以根据这些值来返回child的新的left。
        // 这个方法必须重写，要不然就不能移动了。
        // 返回横向坐标左右边界值
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (orientation == Orientation.Vertical)
                return 0;

            if (distanceLeft + dx < 0) {
                // 右边界
                return 0;
            } else if (distanceLeft + dx > horizontalRange) {
                // 左边界
                return horizontalRange;
            } else {
                // 左右边界范围内
                return left;
            }
        }

        // 这个方法用来返回可以被移动的View对象，我们可以通过判断child与我们想移动的View是的相等来控制谁能移动。
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        // 横向的边界范围
        @Override
        public int getViewHorizontalDragRange(View child) {
            return horizontalRange;
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            if (orientation == Orientation.Horizontal)
                return 0;

            if (distanceTop + dy < 0) {
                return 0;
            } else if (distanceTop + dy > verticalRange) {
                return verticalRange;
            } else {
                return top;
            }
        }

        public int getViewVerticalDragRange(View child) {
            return verticalRange;
        }

        // ACTION_UP事件后调用其方法
        // 当releasedChild被释放的时候，xvel和yvel是x和y方向的加速度
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            if (orientation == Orientation.Vertical) {
                if (releasedChild == layoutMenu)
                    return;

                if (yvel > 0) {
                    // 加速度向下
                    open();
                } else if (yvel < 0) {
                    // 加速度向上
                    close();
                } else if (releasedChild == layoutContent
                        && distanceTop > verticalRange * 0.3) {
                    // 如果释放时，手指在内容区且内容区离左边的距离是range * 0.3
                    open();
                } else {
                    close();
                }
            } else {
                if (xvel > 0) {
                    // 加速度向
                    open();
                } else if (xvel < 0) {
                    // 加速度向左
                    close();
                } else if (releasedChild == layoutContent
                        && distanceLeft > horizontalRange * 0.3) {
                    // 如果释放时，手指在内容区且内容区离左边的距离是range * 0.3
                    open();
                } else if (releasedChild == layoutMenu
                        && distanceLeft > horizontalRange * 0.7) {
                    // 如果释放时，手指在菜单区且内容区离左边的距离是range * 0.7
                    open();
                } else {
                    close();
                }
            }
        }

        // view在拖动过程坐标发生变化时会调用此方法，包括两个时间段：手动拖动和自动滚动
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            if (orientation == Orientation.Horizontal) {
                if (changedView == layoutContent) {
                    distanceLeft = left;
                } else {
                    distanceLeft = distanceLeft + left;
                }
                if (distanceLeft < 0) {
                    distanceLeft = 0;
                } else if (distanceLeft > horizontalRange) {
                    distanceLeft = horizontalRange;
                }
                layoutMenu.layout(0, 0, viewWidth, viewHeight);
                layoutContent.layout(distanceLeft, 0, distanceLeft + viewWidth,
                        viewHeight);
                dispatchDragEvent(distanceLeft);
            } else {
                distanceTop = top;
                if (distanceTop < 0) {
                    distanceTop = 0;
                } else if (distanceTop > verticalRange) {
                    distanceTop = verticalRange;
                }
                layoutMenu.layout(0, 0, viewWidth, viewHeight);
                layoutContent.layout(0, distanceTop, viewWidth, distanceTop
                        + viewHeight);
                dispatchDragEvent(distanceTop);
            }
        }
    };

    public interface DragListener {
        /**
         * 已经打开
         */
        public void onOpen();

        /**
         * 已经关闭
         */
        public void onClose();

        /**
         * 真在拖拽
         */
        public void onDrag(float percent);

    }

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        layoutMenu = (ViewGroup) getChildAt(0);
        layoutContent = (ViewGroup) getChildAt(1);
        layoutMenu.setClickable(true);
        layoutContent.setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = layoutMenu.getMeasuredWidth();
        viewHeight = layoutMenu.getMeasuredHeight();
        horizontalRange = (int) (viewWidth * 0.7);
        verticalRange = (int) (viewHeight * 0.9);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        layoutMenu.layout(0, 0, viewWidth, viewHeight);
        if (orientation == Orientation.Horizontal) {
            layoutContent.layout(distanceLeft, 0, distanceLeft + viewWidth,
                    viewHeight);
        } else {
            layoutContent.layout(0, distanceTop, viewWidth, distanceTop
                    + viewHeight);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (orientation == Orientation.Vertical) {
            if ((Status.Open == getStatus() && ev.getY() < verticalRange)) {
                return false;
            }
        }

        return dragHelper.shouldInterceptTouchEvent(ev)
                && gestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        try {
            // 在processTouchEvent中对ACTION_DOWN、ACTION_MOVE和ACTION_UP事件进行了处理：
            // 1.在ACTION_DOWN中调用回调接口中的tryCaptureView方法，看当前touch的view是否允许拖动
            // 在此项目中的是直接return true，两个view都是允许拖动的
            // 2.在ACTION_MOVE中，view的坐标发生改变，调用回调接口中的onViewPositionChanged方法，
            // 根据坐标信息对view进行layout，通过ViewHelper这个类中的setScaleX、setScaleY方法，实现在
            // 拖动的过程中view在XY坐标上进行相应比例的缩放；
            // 3.在ACTION_UP后调用回调接口中的onViewReleased方法，此方法中一个重要的任务是在ACTION_UP事件
            dragHelper.processTouchEvent(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private void dispatchDragEvent(int mainLeft) {
        float percent;
        if (orientation == Orientation.Horizontal) {
            percent = mainLeft / (float) horizontalRange;
            animateView(percent);
        } else {
            percent = mainLeft / (float) verticalRange;
        }

        Status lastStatus = status;

        if (dragListener == null)
            return;

        dragListener.onDrag(percent);

        if (lastStatus != getStatus() && status == Status.Close) {
            dragListener.onClose();
        } else if (lastStatus != getStatus() && status == Status.Open) {
            dragListener.onOpen();
        }
    }

    private void animateView(float percent) {
        float f1 = 1 - percent * 0.3f;
        /*ViewHelper.setScaleX(layoutContent, f1);
        ViewHelper.setScaleY(layoutContent, f1);
        ViewHelper.setTranslationX(
                layoutMenu,
                -layoutMenu.getWidth() / 2.3f
                        + layoutMenu.getWidth() / 2.3f * percent);
        ViewHelper.setScaleX(layoutMenu, 0.5f + 0.5f * percent);
        ViewHelper.setScaleY(layoutMenu, 0.5f + 0.5f * percent);
        ViewHelper.setAlpha(layoutMenu, percent);*/
        layoutContent.setScaleX(f1);
        layoutContent.setScaleY(f1);
        layoutMenu.setTranslationY(-layoutMenu.getWidth() / 2.3f
                + layoutMenu.getWidth() / 2.3f * percent);
        layoutMenu.setScaleX(0.5f + 0.5f * percent);
        layoutMenu.setScaleY(0.5f + 0.5f * percent);
        layoutMenu.setAlpha(percent);

        getBackground().setColorFilter(
                evaluate(percent, Color.BLACK, Color.TRANSPARENT),
                PorterDuff.Mode.SRC_OVER);
    }

    private Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public enum Status {
        Drag, Open, Close
    }

    public enum Orientation {
        Horizontal, Vertical;
    }

    public Status getStatus() {
        if (orientation == Orientation.Horizontal) {
            if (distanceLeft == 0) {
                status = Status.Close;
            } else if (distanceLeft == horizontalRange) {
                status = Status.Open;
            } else {
                status = Status.Drag;
            }
        } else {
            if (distanceTop == 0) {
                status = Status.Close;
            } else if (distanceTop == verticalRange) {
                status = Status.Open;
            } else {
                status = Status.Drag;
            }
        }

        return status;
    }

    public ViewGroup getlayoutContent() {
        return layoutContent;
    }

    public ViewGroup getlayoutMenu() {
        return layoutMenu;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void open() {
        open(true);
    }

    public void open(boolean animate) {
        if (animate) {
            if (orientation == Orientation.Horizontal) {
                if (dragHelper.smoothSlideViewTo(layoutContent,
                        horizontalRange, 0)) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
            } else {
                if (dragHelper.smoothSlideViewTo(layoutContent, 0,
                        verticalRange)) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
            }
        } else {
            layoutContent.layout(horizontalRange, 0, horizontalRange
                    + viewWidth, viewHeight);
            dispatchDragEvent(horizontalRange);
        }
    }

    public void close() {
        close(true);
    }

    public void close(boolean animate) {
        if (animate) {
            if (dragHelper.smoothSlideViewTo(layoutContent, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layoutContent.layout(0, 0, viewWidth, viewHeight);
            dispatchDragEvent(0);
        }
    }
}
