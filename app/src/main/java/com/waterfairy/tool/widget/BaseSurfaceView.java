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
    private ValueAnimator valueAnimator;
    private int currentTimes = 0;
    private int times = 100;//绘画频率
    private int sleepTime = 1;


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

    private OnFloatChangeListener onFloatChangeListener;
    protected boolean isDrawing;

    protected boolean isDrawing() {
        return isDrawing;
    }

    protected void setClock(OnFloatChangeListener onFloatChangeListener) {
        this.onFloatChangeListener = onFloatChangeListener;

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (isDrawing) {
                    float ratio = currentTimes / (float) times;//绘画过的比例
                    BaseSurfaceView.this.onFloatChangeListener.onChange(ratio);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (currentTimes >= times) isDrawing = false;
                    currentTimes++;
                }
            }
        }).start();

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

    public interface OnFloatChangeListener {
        void onChange(float value);
    }
}
