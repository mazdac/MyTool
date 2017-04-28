package com.waterfairy.tool.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 圆饼图
 * Created by water_fairy on 2017/4/28.
 * 995637517@qq.com
 * 说明:
 * 开始: 1493357805844
 * 结束: 1493357837231
 * 差值: 31,387 31秒  25秒   多出6秒
 */

public class PieView extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "pieView";
    //    private List<Float> mRatios;//比例
    private List<Integer> mColors;//颜色
    private List<Float> mAngles;//角度
    private List<Float> mStartAngles;//对应开始角度
    private List<Paint> mPaints;//对应画笔
    private int times = 100;//绘画频率
    private long sleepTime = 5;//间隔时间
    private int waitTime = 500;//等待时间
    private SurfaceHolder surfaceHolder;
    private boolean isDrawing;//绘画中
    private int mCount;//板块数量
    private int mWidth;//布局宽
    private int mHeight;//布局高
    private int mRadius;//圆半径
    private int padding;//布局padding
    private RectF mCircleRectF;//圆区域
    private Thread drawThread;//绘画线程
    private boolean drawCircle;
    private int circleColor;
    private int circleStrokeWidth;
    private int startDegree = 0;
    private List<StrokePoint> mStrokePoints;


    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
    }

    public PieView(Context context) {
        super(context, null);
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setSleepTIme(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "onMeasure: width:" + mWidth + " -height" + mHeight);
        int tempRadius = (Math.min(mHeight, mWidth)) / 2;
        mRadius = (int) (tempRadius * 4F / 7);
        padding = (int) (tempRadius * 1F / 7);
        if (mCircleRectF == null) {
            int halfWidth = mWidth / 2;
            int halfHeight = mHeight / 2;
            mCircleRectF = new RectF(
                    halfWidth - mRadius,
                    halfHeight - mRadius,
                    halfWidth + mRadius,
                    halfHeight + mRadius);
        }
        initStrokePoint();

    }

    private void initStrokePoint() {
        mStrokePoints = new ArrayList<>();
        for (int i = 0; i < mCount; i++) {
            float angle = mStartAngles.get(i) + mAngles.get(i) / 2;
            mStrokePoints.add(getStrokePoint(angle));
        }
    }

    public void initData(List<Float> ratios, List<Integer> colors) {
        mAngles = new ArrayList<>();
        mStartAngles = new ArrayList<>();
        mPaints = new ArrayList<>();
        mStrokePoints = new ArrayList<>();
        mColors = colors;
        mCount = ratios.size();
        calcCircleData(ratios, colors);
        //计算文本所需数据
    }

    /**
     * 计算圆图所需数据
     *
     * @param ratios
     * @param colors
     */
    private void calcCircleData(List<Float> ratios, List<Integer> colors) {
        float total = 0;
        //总数
        for (int i = 0; i < mCount; i++) {
            total += ratios.get(i);
        }
        //对应开始角度
        float startAngle = 0;
        float lastAngle = 0;
        for (int i = 0; i < mCount; i++) {
            //计算对应角度
            float angle = 360 * (ratios.get(i) / total);
            mAngles.add(angle);//获取角度
            //计算对应开始角度
            startAngle += lastAngle;
            lastAngle = angle;
            float startTemp = startAngle + startDegree;
            mStartAngles.add(startTemp);

            //设置对应颜色
            Paint paint = new Paint();
            paint.setAntiAlias(true);//去锯齿
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(colors.get(i));//颜色
            mPaints.add(paint);
        }
    }

    private StrokePoint getStrokePoint(float angle) {
        Log.i(TAG, "getStrokePoint: "+angle);
        StrokePoint strokePoint = new StrokePoint();
        int y = (int) ( mHeight / 2-(Math.sin(Math.toRadians(angle)) * mRadius ));
        int x = (int) ( (Math.cos(Math.toRadians(angle)) * mRadius )+mWidth/2);
        strokePoint.setX(x).setY(y);
        Log.i(TAG, "getStrokePoint: " + x + "--" + y);
        return strokePoint;
    }

    private void startDraw() {
        if (isDrawing) return;
        isDrawing = true;
        drawThread = new Thread(new Runnable() {
            int currentTimes = 0;

            @Override
            public void run() {
                try {
                    drawBg();
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "开始: " + new Date().getTime());
                while (isDrawing) {
                    float ratio = currentTimes / (float) times;//绘画过的比例
                    startDraw(ratio);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (currentTimes >= times) isDrawing = false;
                    currentTimes++;

                }
                drawPoint();
                Log.i(TAG, "结束: " + new Date().getTime());
            }
        });
        drawThread.start();
    }

    private void drawPoint() {

        Canvas canvas = surfaceHolder.lockCanvas();


        for (int i = 0; i < mCount; i++) {
            StrokePoint strokePoint = mStrokePoints.get(i);
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.DKGRAY);
            canvas.drawCircle(strokePoint.getX(), strokePoint.getY(), 10,paint);
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void drawBg() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) return;
        canvas.drawColor(Color.WHITE);
        if (drawCircle) {
            drawCircle(canvas);
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * 设置圆边属性
     *
     * @param strokeWidth
     * @param color
     */
    public void setBgCircle(int strokeWidth, int color) {
        if (strokeWidth != 0 && color != 0) drawCircle = true;
        else drawCircle = false;
        this.circleColor = color;
        this.circleStrokeWidth = strokeWidth;
    }

    /**
     * 画圆边
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(circleColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleStrokeWidth);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, paint);
    }

    public void reStart() {
        startDraw();
    }

    private void startDraw(float ratio) {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) return;
        canvas.drawColor(Color.WHITE);
        if (drawCircle) drawCircle(canvas);
        float sin = (float) Math.sin(Math.toRadians(ratio * 90));
        for (int i = 0; i < mCount; i++) {
            Paint paint = mPaints.get(i);
            float startAngle = mStartAngles.get(i);
            canvas.drawArc(mCircleRectF, startAngle, mAngles.get(i) * sin, true, paint);
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
        startDraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed: ");
    }

    class StrokePoint {
        public int getX() {
            return x;
        }

        public StrokePoint setX(int x) {
            this.x = x;
            return this;
        }

        public int getY() {
            return y;
        }

        public StrokePoint setY(int y) {
            this.y = y;
            return this;
        }

        private int x;
        private int y;
    }

}
