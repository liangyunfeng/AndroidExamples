package com.github.liangyunfeng.measure.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.github.liangyunfeng.measure.R;

/**
 * Created by yunfeng.l on 2018/7/3.
 */

public class TagLayout extends ViewGroup {

    int mGap;

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        mGap = (int) a.getDimension(R.styleable.TagLayout_gap, 10);
        a.recycle();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //只关心子元素的 margin 信息，所以这里用 MarginLayoutParams
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        /**对子元素进行尺寸的测量，这一步必不可少*/
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //一定要设置这个函数，不然会报错
        setMeasuredDimension(widthSize, heightSize);     // 直接设置最大的宽度和高度
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topStart = getPaddingTop();
        int leftStart = getPaddingLeft();
        int childW = 0;
        int childH = 0;
        int row = 0;
        MarginLayoutParams layoutParams = null;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            layoutParams = (MarginLayoutParams) child.getLayoutParams();

            //子元素不可见时，不参与布局，因此不需要将其尺寸计算在内
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            childW = child.getMeasuredWidth();
            childH = child.getMeasuredHeight();

            if ((leftStart + mGap + layoutParams.leftMargin + childW + layoutParams.rightMargin) > r) {     // 当前行如果不能装下这个childView，那么就换下一行
                row++;
                leftStart = getPaddingStart();                          // 换行后，初始化左边开始位置
                topStart = getPaddingTop() + (childH + mGap) * row;     // 换行后，初始化上边开始位置
            }

            if (leftStart == getPaddingLeft()) {        // 第一行左边没有gap
                leftStart += layoutParams.leftMargin;
            } else {
                leftStart += mGap + layoutParams.leftMargin;
            }

            child.layout(leftStart, topStart, leftStart + childW, topStart + childH);

            leftStart += childW + layoutParams.rightMargin;     // 把左边位置移到这个childView后面
        }
    }
}
