package com.github.liangyunfeng.coordinator.one;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * Created by yunfeng.l on 2018/7/4.
 */

/**
 * 自定义 Behavior 的总结
 * 1.确定 CoordinatorLayout 中 View 与 View 之间的依赖关系，通过 layoutDependsOn() 方法，返回值为 true 则依赖，否则不依赖。
 * 2.当一个被依赖项 dependency 尺寸或者位置发生变化时，依赖方会通过 Byhavior 获取到，然后在 onDependentViewChanged 中处理。如果在这个方法中 child 尺寸或者位置发生了变化，则需要 return true。
 * 3.当 Behavior 中的 View 准备响应嵌套滑动时，它不需要通过 layoutDependsOn() 来进行依赖绑定。只需要在 onStartNestedScroll() 方法中通过返回值告知 ViewParent，它是否对嵌套滑动感兴趣。返回值为 true 时，后续的滑动事件才能被响应。
 * 4.嵌套滑动包括滑动(scroll) 和 快速滑动(fling) 两种情况。开发者根据实际情况运用就好了。
 * 5.Behavior 通过 3 种方式绑定：1. xml 布局文件。2. 代码设置 layoutparam。3. 自定义 View 的注解。
 *
 */
public class DependencyView extends TextView {

    private final int mSlop;
    private float mLastX;
    private float mLastY;

    public DependencyView(Context context) {
        this(context, null);
    }

    public DependencyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DependencyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setClickable(true);

        mSlop = ViewConfiguration.getTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return super.onTouchEvent(event);
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int deltax = (int) (event.getX() - mLastX);
                int deltay = (int) (event.getY() - mLastY);
                if (Math.abs(deltax) > mSlop || Math.abs(deltay) > mSlop) {
                    // 法一
                    ViewCompat.offsetTopAndBottom(this, deltay);
                    ViewCompat.offsetLeftAndRight(this, deltax);
                    // 法二
                    //scrollBy(-deltax, -deltay);
                    // 法三
                    //setTranslationX(event.getX() - originalLeft);
                    //setTranslationY(event.getY() - originalTop);
                    mLastX = event.getX();
                    mLastY = event.getY();
                }

                break;

            case MotionEvent.ACTION_UP:
                mLastX = event.getX();
                mLastY = event.getY();
                break;

            default:
                break;

        }

        return true;
    }
}
