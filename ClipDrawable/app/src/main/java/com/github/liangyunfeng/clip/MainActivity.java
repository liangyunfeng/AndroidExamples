package com.github.liangyunfeng.clip;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTvShow;
    ImageView mImageShow;
    SeekBar mSeekBar;
    Button mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6;

    Drawable mDrawable;
    ClipDrawable mClipDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvShow = (TextView) findViewById(R.id.tv_info);
        mImageShow = (ImageView) findViewById(R.id.iv_show);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn3 = (Button) findViewById(R.id.btn3);
        mBtn4 = (Button) findViewById(R.id.btn4);
        mBtn5 = (Button) findViewById(R.id.btn5);
        mBtn6 = (Button) findViewById(R.id.btn6);

        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
        mBtn6.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = seekBar.getMax();
                double scale = (double) progress / (double) max;
                ClipDrawable drawable = (ClipDrawable) mImageShow.getBackground();
                drawable.setLevel((int) (10000 * scale));
                mTvShow.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /**
         * Android中通过资源文件获取drawable的几种方法
         * 1、
         * Resources resources = getBaseContext().getResources();
         * Drawable drawable = resources.getDrawable(R.drawable.backgroud);
         * mImageShow.setBackground(drawable);
         *
         * 2、
         * Resources r = getBaseContext().getResources();
         * InputStream is = r.openRawResource(R.drawable.backgroud);
         * BitmapDrawable bmpDraw = new BitmapDrawable(is);
         * Bitmap bmp = bmpDraw.getBitmap();
         *
         * 3、
         * Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.backgroud);
         * Bitmap newb = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
         *
         * 4、
         * InputStream is = getResources().openRawResource(R.drawable.backgroud);
         * Bitmap mBitmap = BitmapFactory.decodeStream(is);
         */

        Resources resources = getBaseContext().getResources();
        //mDrawable = resources.getDrawable(R.drawable.backgroud);
        mDrawable = resources.getDrawable(R.drawable.backgroud, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                mClipDrawable = new ClipDrawable(mDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
                mImageShow.setBackground(mClipDrawable);
                break;
            case R.id.btn2:
                mClipDrawable = new ClipDrawable(mDrawable, Gravity.RIGHT, ClipDrawable.HORIZONTAL);
                mImageShow.setBackground(mClipDrawable);
                break;
            case R.id.btn3:
                mClipDrawable = new ClipDrawable(mDrawable, Gravity.CENTER, ClipDrawable.HORIZONTAL);
                mImageShow.setBackground(mClipDrawable);
                break;
            case R.id.btn4:
                mClipDrawable = new ClipDrawable(mDrawable, Gravity.TOP, ClipDrawable.VERTICAL);
                mImageShow.setBackground(mClipDrawable);
                break;
            case R.id.btn5:
                mClipDrawable = new ClipDrawable(mDrawable, Gravity.BOTTOM, ClipDrawable.VERTICAL);
                mImageShow.setBackground(mClipDrawable);
                break;
            case R.id.btn6:
                mClipDrawable = new ClipDrawable(mDrawable, Gravity.CENTER, ClipDrawable.VERTICAL);
                mImageShow.setBackground(mClipDrawable);
                break;
            default:
                break;
        }
        mSeekBar.setProgress(0);
    }
}
