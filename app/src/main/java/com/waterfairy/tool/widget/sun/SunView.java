package com.waterfairy.tool.widget.sun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.waterfairy.widget.baseView.BaseSurfaceView;
import com.waterfairy.widget.baseView.OnCanvasChangeListener;

/**
 * Created by water_fairy on 2017/6/14.
 * 995637517@qq.com
 */

public class SunView extends BaseSurfaceView implements OnCanvasChangeListener {
    private static final String TAG = "SunView";
    private float mCenterX, mCenterY; //弧形圆心
    private float mArcStartX, mArcStartY, mArcEndX, mArcEndY;//圆弧起止点
    private float mRadius;//半径
    private Paint mArcPaint, mSunPaint, mTextPaint, mShadowPaint, mTranglePaint, mTranglePaint2;
    private int mTotalAngle;//当前时间的角度
    private RectF mArcRectF;

    private int upTime, currentTime, downTime;

    public SunView(Context context) {
        super(context);
    }

    public SunView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void initData(int upTime, int currentTime, int downTime) {
        this.upTime = upTime;
        this.currentTime = currentTime;
        this.downTime = downTime;
        //计算角度
        mTotalAngle = (int) ((currentTime - upTime) / (float) (downTime - upTime) * 180);
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
        mTranglePaint = new Paint();
        mTranglePaint2 = new Paint();

        mArcPaint.setColor(Color.GRAY);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mSunPaint.setColor(Color.RED);

        mTextPaint.setTextSize(getResources().getDisplayMetrics().density * 16);
        mTextPaint.setColor(Color.CYAN);
//扇形阴影
        mShadowPaint.setColor(Color.GRAY);
        mShadowPaint.setStrokeJoin(Paint.Join.ROUND);

        mTranglePaint.setColor(Color.BLUE);
        mTranglePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mTranglePaint2.setColor(Color.GREEN);
        mTranglePaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mArcPaint.setAntiAlias(true);
        mSunPaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        mShadowPaint.setAntiAlias(true);
        mTranglePaint.setAntiAlias(true);
        mTranglePaint2.setAntiAlias(true);
    }

    /**
     * 初始化参数
     */
    @Override
    protected void beforeDraw() {

        initPoint();

    }

    @Override
    protected void startDraw() {
        setClock(this, 1000);
    }

    @Override
    protected void drawFinishView(Canvas canvas) {
        drawBg(canvas);
        drawArcLine(canvas);
        drawShadow(canvas, 1);
    }

    private void drawBg(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
    }

    @Override
    public void onChange(Canvas canvas, float value) throws Exception {
//        drawBg(canvas);
        drawArcLine(canvas);
        drawShadow(canvas, value);
    }


    private void initPoint() {
        int min = Math.min(mWidth, mHeight);
        mRadius = (int) (min * 0.35f);
        mCenterX = mWidth / 2f;
        mCenterY = (int) (mHeight - min * 0.05f);

        mArcStartX = mCenterX - mRadius;
        mArcEndX = mCenterX + mRadius;
        mArcStartY = mCenterY - mRadius;
        mArcEndY = mCenterY + mRadius;
        mArcRectF = new RectF(mArcStartX, mArcStartY, mArcEndX, mArcEndY);

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
    private void drawShadow(Canvas canvas, float value) {
        float currentAngle = mTotalAngle * value;
        Log.i(TAG, "drawShadow: " + value);
        canvas.drawArc(mArcRectF, 180, currentAngle, true, mShadowPaint);
        float tempHeight = (float) Math.sin(currentAngle / 180f * Math.PI) * mRadius;
        float tempWidth = (float) Math.cos(currentAngle / 180f * Math.PI) * mRadius;
        Path path = new Path();
        path.moveTo(mCenterX, mCenterY);//圆心
        path.lineTo(mCenterX - tempWidth, mCenterY);
        path.lineTo(mCenterX - tempWidth, mCenterY - tempHeight);
        path.lineTo(mCenterX, mCenterY);
        if (currentAngle >= 90) {
            canvas.drawPath(path, mShadowPaint);
        } else {
            canvas.drawPath(path, mTranglePaint);
        }

        canvas.drawArc(mArcRectF, 180, 180, true, mTranglePaint2);


    }

    /**
     * 画文本
     */
    private void drawText(Canvas canvas) {

    }


}
