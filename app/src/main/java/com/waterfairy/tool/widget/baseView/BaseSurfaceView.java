package com.waterfairy.tool.widget.baseView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by water_fairy on 2017/5/25.
 * 995637517@qq.com
 */

public abstract class BaseSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    protected SurfaceHolder mSurfaceHolder;
    protected int mWidth, mHeight;
    private int currentTimes = 0;
    private int times = 100;//绘画频率
    private int sleepTime = 1;
    protected ViewDrawObserver viewDrawObserver;
    protected int mBgColor;


    public BaseSurfaceView(Context context) {
        this(context, null);
    }

    public BaseSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBgColor = Color.WHITE;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//透明
        setZOrderOnTop(true);//置顶
        viewDrawObserver = new ViewDrawObserver();
        mSurfaceHolder.addCallback(this);
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
        if (isDrawing) return;
        isDrawing = true;
        currentTimes = 0;

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (isDrawing ) {
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        onCreateOk();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing = false;
    }


    private class ViewDrawObserver implements ViewCreateObserver {
        private boolean viewState;
        private boolean dataState;

        @Override
        public void onUpdate(int type, boolean state) {
            if (type == TYPE_VIEW) {
                viewState = state;
            } else if (type == TYPE_DATA) {
                dataState = state;
            }
            if (viewState && dataState) {
                beforeDraw();
                startDraw();
            }
        }
    }

    protected void onInitDataOk() {
        viewDrawObserver.onUpdate(ViewCreateObserver.TYPE_DATA, true);
    }

    protected void onCreateOk() {
        viewDrawObserver.onUpdate(ViewCreateObserver.TYPE_VIEW, true);
    }

    protected abstract void startDraw();

    protected abstract void beforeDraw();

}
