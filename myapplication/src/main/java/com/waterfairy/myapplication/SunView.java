package com.waterfairy.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import java.util.List;

/**
 * Created by water_fiay on 2017/6/14.
 * 995637517@qq.com
 */

public class SunView extends BaseSurfaceView {
    private int mCenterX, mCenterY; //弧形圆心
    private int mArcStartX, mArcStartY, mArcEndX, mArcEndY;//圆弧起止点
    private int mRadius;//半径
    private Paint mArcPaint, mSunPaint, mTextPaint, mShadowPaint;
    private int mTotalAngle;//当前时间的角度
    private RectF mArcRectF;

    private int upTime, currentTime, downTime;

    public SunView(Context context) {
        super(context);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void startDraw() {

    }

    public void initData(int upTime, int currentTime, int downTime) {
        this.upTime = upTime;
        this.currentTime = currentTime;
        this.downTime = downTime;
        //计算角度
        mTotalAngle = (int) (currentTime - upTime / (float) (downTime - upTime) * 180);
        //初始话画笔
        initPaint();
        //提示数据初始话完成
        onInitDataOk();
    }

    private void initPaint() {
        mArcPaint = new Paint();
        mSunPaint = new Paint();
        mTextPaint = new Paint();
        mShadowPaint = new Paint();

        mArcPaint.setColor(Color.GRAY);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mShadowPaint.setColor(Color.DKGRAY);
        mShadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mSunPaint.setColor(Color.RED);
        mTextPaint.setTextSize(getResources().getDisplayMetrics().density * 16);
        mTextPaint.setColor(Color.BLACK);

        mArcPaint.setAntiAlias(true);
        mSunPaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        mShadowPaint.setAntiAlias(true);
    }

    /**
     * 初始化参数
     */
    @Override
    protected void beforeDraw() {

        initPoint();
        setClock(new OnFloatChangeListener() {
            @Override
            public void onChange(float value) {
                startDraw(value);
            }
        });
    }

    private void initPoint() {
        int min = Math.min(mWidth, mHeight);
        mRadius = (int) (min * 0.35f);
        mCenterX = mWidth / 2;
        mCenterY = (int) (mHeight - min * 0.05f);

        mArcStartX = mCenterX - mRadius;
        mArcEndX = mCenterX + mRadius;
        mArcStartY = mCenterY - mRadius;
        mArcEndY = mCenterY + mRadius;
        mArcRectF = new RectF(mArcStartX, mArcStartY, mArcEndX, mArcEndY);

    }

    private void startDraw(float radio) {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        Log.i("terst", "startDraw: ");
        canvas.drawColor(Color.WHITE);
        drewShadow(canvas, radio);
        drawArcLine(canvas);
        mSurfaceHolder.unlockCanvasAndPost(canvas);

    }

    /**
     * 画太阳
     *
     * @param canvas
     */
    private void drawSun(Canvas canvas) {

    }

    /**
     * 画弧线
     *
     * @param canvas
     */
    private void drawArcLine(Canvas canvas) {
        canvas.drawArc(mArcRectF, 180, 180, true, mArcPaint);
    }

    /**
     * 画阴影
     */
    private void drewShadow(Canvas canvas, float radio) {
        canvas.drawArc(mArcRectF, 180, mTotalAngle * radio, true, mShadowPaint);
        Path path = new Path();


    }

    /**
     * 画文本
     */
    private void drawText(Canvas canvas) {

    }


}
