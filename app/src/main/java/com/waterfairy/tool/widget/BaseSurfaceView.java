package com.waterfairy.tool.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by water_fairy on 2017/5/25.
 * 995637517@qq.com
 */

public abstract class BaseSurfaceView extends SurfaceView {
    protected SurfaceHolder mSurfaceHolder;
    protected int mWidth, mHeight;

    public BaseSurfaceView(Context context) {
        this(context, null);
    }

    public BaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//透明
        setZOrderOnTop(true);//置顶
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

    }

    public void setClock() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0f, 1f);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

            }
        });
    }

    protected abstract void start();

    /**
     * 坐标
     */
    public static class Coordinate {
        public Coordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float x;
        public float y;
    }
}
