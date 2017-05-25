package com.waterfairy.tool.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状图
 * Created by water_fairy on 2017/4/28.
 * 995637517@qq.com
 */

public class HistogramView extends BaseSurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "HistogramView";
    private int mLeft = 3 * 14;//(3个字节长度)左边距,下边距
    private float mXPerWidth, mXHisWidth;//myhiswidth  占有3分 Xper
    private int mTriangleWidth = 14;//每份宽度,柱宽度,箭头宽度
    private int maxHeight;//最高
    private List<HistogramEntity> mDataList;
    private int mXMinNum = 7;//最小柱
    private int mYMinNum = 3;//x
    private float mYPerHeight;//y轴每份高度( /最大值所得);
    private float mYHisHeight;//( y轴 0 50 100) 之间的高度
    private List<HistogramBean> histogramBeanList;
    private Paint mMainPaint;
    private Bitmap mBMBg;


    public HistogramView(Context context) {
        super(context, null);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder.addCallback(this);
        float density = context.getResources().getDisplayMetrics().density;
        mLeft = (int) (mLeft * density);
        mTriangleWidth = (int) (mTriangleWidth * density);
        initPaints();
    }

    private void initPaints() {
        mMainPaint = new Paint();
        mMainPaint.setAntiAlias(true);
        mMainPaint.setColor(Color.parseColor("#ff0000"));
        mMainPaint.setStrokeWidth(6);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure: " + mWidth + "--" + mHeight);
    }

    @Override
    protected void start() {


    }

    private void draw() {
        drawSteady();
        drawHistogram();

    }

    private void drawHistogram() {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawBitmap(mBMBg, 0, 0, null);
        for (int i = 0; i < histogramBeanList.size(); i++) {
            HistogramBean histogramBean = histogramBeanList.get(i);
            Coordinate br = histogramBean.getBr();
            Coordinate center = histogramBean.getCenter();
            Coordinate ul = histogramBean.getUl();
            RectF rectF = new RectF(ul.x, ul.y, br.x, br.y);
            canvas.drawRect(rectF, mMainPaint);
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * 画静态的 (x,y轴)
     */
    private void drawSteady() {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.setBitmap(mBMBg);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawLine(mLeft, mTriangleWidth, mLeft, mHeight - mLeft, mMainPaint);
        canvas.drawLine(mLeft, mHeight - mLeft, mWidth - mTriangleWidth, mHeight - mLeft, mMainPaint);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
        mBMBg = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        initDataAfterMesaure();
        draw();
    }

    private void initDataAfterMesaure() {
        //计算x,y坐标
        initXData(mXMinNum);
        initYData(mYMinNum);
        //计算坐标
        initCoordinate();
    }

    public void initData(List<HistogramEntity> list, int minYNum, int minXNum) {
        mDataList = list;
        mXMinNum = minXNum;
        mYMinNum = minYNum;
    }

    private void initCoordinate() {
        if (mDataList != null) {
            histogramBeanList = new ArrayList<>();
            for (int i = 0; i < mDataList.size(); i++) {
                HistogramEntity histogramEntity = mDataList.get(i);
                float yHeight = histogramEntity.getValue() * mYPerHeight;
                float x1 = 0, x2 = 0, x3 = 0;//()
                x1 = mLeft + (3 + 4 * i) * mXPerWidth;
                x3 = mLeft + (6 + 4 * i) * mXPerWidth;
                x2 = (x1 + x3) / 2;
                float y1 = 0, y2 = 0, y3 = 0;
                y1 = mHeight - mLeft - yHeight;
                y2 = y1;
                y3 = mHeight - mLeft;
                HistogramBean histogramBean = new HistogramBean(
                        new Coordinate(x1, y1),
                        new Coordinate(x2, y2),
                        new Coordinate(x3, y3));
                histogramBeanList.add(histogramBean);
            }
        }
    }

    /**
     * 计算x坐标
     */
    private void initXData(int minNum) {
        //确定柱子数量
        int listCount = 0;
        if (mDataList != null && ((listCount = mDataList.size()) > minNum)) {
            mXMinNum = listCount;
        } else {
            this.mXMinNum = minNum;
        }
        //获取平均宽度
        mXPerWidth = (mWidth - mLeft - mTriangleWidth) / (float) (4 * mXMinNum + 6);
        mXHisWidth = mXPerWidth * 3;
    }

    /**
     * 计算y坐标
     */
    private void initYData(int minYNum) {
        if (minYNum < 2) minYNum = 2;
        int maxValue = 0;
        if (mDataList != null) {
            for (int i = 0; i < mDataList.size(); i++) {
                int value = mDataList.get(i).getValue();
                if (value > maxValue) maxValue = value;
            }
        } else {
            maxValue = 1;
        }
        //每份value值得高度
        mYPerHeight = (mHeight - mLeft - mTriangleWidth) / (float) maxValue;
        //y轴间隔高度
        mYHisHeight = mYPerHeight / (float) (minYNum - 1);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed: ");
    }

    public class HistogramBean {
        public HistogramBean(Coordinate ul, Coordinate center, Coordinate br) {
            this.setUl(ul);
            this.setBr(br);
            this.setCenter(center);
        }

        private Coordinate ul;//左上
        private Coordinate br;//右下
        private Coordinate center;//上中心

        public Coordinate getBr() {
            return br;
        }

        public void setBr(Coordinate br) {
            this.br = br;
        }

        public Coordinate getUl() {
            return ul;
        }

        public void setUl(Coordinate ul) {
            this.ul = ul;
        }

        public Coordinate getCenter() {
            return center;
        }

        public void setCenter(Coordinate center) {
            this.center = center;
        }
    }
}
