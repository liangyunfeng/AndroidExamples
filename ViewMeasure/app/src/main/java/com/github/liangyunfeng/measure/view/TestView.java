package com.github.liangyunfeng.measure.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.liangyunfeng.measure.R;

/**
 * Created by yunfeng.l on 2018/7/3.
 */

public class TestView extends View {

    private int mTextSize;
    TextPaint mPaint;
    private String mText;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TestView);
        mText = ta.getString(R.styleable.TestView_android_text);
        mTextSize = ta.getDimensionPixelSize(R.styleable.TestView_android_textSize, 24);

        ta.recycle();

        mPaint = new TextPaint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        /**resultW 代表最终设置的宽，resultH 代表最终设置的高*/
        int resultW = widthSize;
        int resultH = heightSize;

        int contentW = 0;
        int contentH = 0;

        /**重点处理 AT_MOST 模式，TestView 自主决定数值大小，但不能超过 ViewGroup 给出的
         * 建议数值
         * */
        if (widthMode == MeasureSpec.AT_MOST) {
            if (!TextUtils.isEmpty(mText)) {
                contentW = (int)mPaint.measureText(mText);
                contentW += getPaddingLeft() + getPaddingRight();
                resultW = contentW < widthSize ? contentW : widthSize;
            }
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            if (!TextUtils.isEmpty(mText)) {
                contentH = mTextSize;
                contentH += getPaddingTop() + getPaddingBottom();
                resultH = contentH < widthSize ? contentH : heightSize;
            }
        }

        Log.v("lyf", "onMeasure: resultW = " + resultW + ", resultH = " + resultH);

        //一定要设置这个函数，不然会报错
        setMeasuredDimension(resultW, resultH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*int cx = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        int cy = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;

        canvas.drawColor(Color.RED);
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        canvas.drawText(mText, cx, cy, mPaint);*/

        int cx = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        int cy = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        Log.v("lyf", "onDraw: cx = " + cx + ", cy = " + cy + ", getWidth() = " + getWidth() + ", getHeight() = " + getHeight() + ", descent = " + mPaint.getFontMetrics().descent + ", [" + getPaddingLeft() + "," + getPaddingTop() + "," + getPaddingRight() + "," + getPaddingBottom() + "]");

        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        cy += metrics.descent;

        canvas.drawColor(Color.RED);
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        canvas.drawText(mText,cx,cy,mPaint);

    }
}

