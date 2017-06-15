package com.waterfairy.tool.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;


import com.waterfairy.widget.baseView.BaseSurfaceView;
import com.waterfairy.widget.baseView.Coordinate;
import com.waterfairy.widget.baseView.OnCanvasChangeListener;
import com.waterfairy.widget.baseView.OnFloatChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状图
 * Created by water_fairy on 2017/4/28.
 * 995637517@qq.com
 */

public class HistogramView extends BaseSurfaceView {
    private static final String TAG = "HistogramView";
    private int mLeft = 2 * 14;//(2个字节长度)左边距,下边距
    private float mXPerWidth, mXHisWidth;//myhiswidth  占有3分 Xper
    private int mTriangleWidth = 10;//箭头宽度
    private int maxHeight;//最高
    private List<HistogramEntity> mDataList;
    private int mXMinNum = 7;//最小柱
    private int mYMinNum = 3;//x
    private float mYPerHeight;//y轴每份高度( /最大值所得);
    private float mYHisHeight;//( y轴 0 50 100) 之间的高度
    private List<HistogramBean> histogramBeanList;
    private Paint mMainPaint, xPaint, linePaint;
    private Bitmap mBMBg;
    private int mMaxValue;
    protected String mXTitle = "", mYTitle = "";
    private int mTextSize;

//    private List<XLineCoordinate> xNames;//x轴坐标显示


    public HistogramView(Context context) {
        super(context, null);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBgColor = Color.WHITE;
        float density = context.getResources().getDisplayMetrics().density;
        mLeft = (int) (mLeft * density);
        mTriangleWidth = (int) (mTriangleWidth * density);
        initColors();
    }

    private void initColors() {
        mMainPaint = new Paint();
        mMainPaint.setAntiAlias(true);
        mMainPaint.setColor(Color.parseColor("#c6f17e"));
        mMainPaint.setStrokeWidth(5);

        xPaint = new Paint();
        xPaint.setAntiAlias(true);
        xPaint.setColor(Color.parseColor("#3095cd"));
        xPaint.setStrokeWidth(3);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#8cbe41"));
        linePaint.setStrokeWidth(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureSpec = heightMeasureSpec;
        if (MeasureSpec.getSize(widthMeasureSpec) < MeasureSpec.getSize(heightMeasureSpec)) {
            measureSpec = widthMeasureSpec;
        }
        super.onMeasure(measureSpec, measureSpec);

    }


    @Override
    protected void startDraw() {
        draw();
    }

    @Override
    protected void drawFinishView(Canvas canvas) {

    }

    @Override
    protected void beforeDraw() {
        initDataAfterMeasure();
    }


    private void draw() {
        drawSteady();
//        drawHistogram(1);
        setClock(new OnCanvasChangeListener() {
            @Override
            public void onChange(Canvas canvas, float value) throws Exception {
                drawHistogram(value);
            }

        });


    }

    private void drawHistogram(float value) {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if (canvas == null) return;
        canvas.drawBitmap(mBMBg, 0, 0, null);
        HistogramBean lastHistogramBean = null;
        for (int i = 0; histogramBeanList != null && i < histogramBeanList.size(); i++) {
            HistogramBean histogramBean = histogramBeanList.get(i);
            Coordinate br = histogramBean.getBr();
            Coordinate ul = histogramBean.getUl();
            float extra = ul.getExtra();
            RectF rectF = new RectF(ul.x, (ul.y - 2 + extra) - extra * value, br.x, (br.y - 2));
            canvas.drawRect(rectF, mMainPaint);
            if (i > 0) {
                Coordinate center = histogramBean.getCenter();
                Coordinate lastCenter = lastHistogramBean.getCenter();
                canvas.drawLine(lastCenter.x, (lastCenter.y - 2 + lastCenter.getExtra()) - lastCenter.getExtra() * value, center.x, (center.y - 2 + extra) - extra * value, linePaint);
            }
            lastHistogramBean = histogramBean;
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * 画静态的 (x,y轴)
     */
    private void drawSteady() {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if (canvas == null) return;
        canvas.setBitmap(mBMBg);//设置背景图片
        canvas.drawColor(mBgColor);//透明处理
        //画y轴
        canvas.drawLine(mLeft, mTriangleWidth, mLeft, mHeight - mLeft, xPaint);
        //画x轴
        canvas.drawLine(mLeft, mHeight - mLeft, mWidth - mTriangleWidth, mHeight - mLeft, xPaint);
        //x箭头
        Path xPath = new Path();
        xPath.lineTo(mLeft, 0 + 4);
        xPath.lineTo(mLeft + mTriangleWidth / 2, (float) (mTriangleWidth * Math.sqrt(3) / 2) + 4);
        xPath.lineTo(mLeft - mTriangleWidth / 2, (float) (mTriangleWidth * Math.sqrt(3) / 2) + 4);
        xPath.lineTo(mLeft, 0 + 4);
        canvas.drawPath(xPath, xPaint);
        //y箭头
        Path yPath = new Path();
        yPath.lineTo(mWidth - (float) (mTriangleWidth * Math.sqrt(3) / 2) - 4, mHeight - mLeft - (float) (mTriangleWidth / 2));
        yPath.lineTo(mWidth - 4, (mHeight - mLeft));
        yPath.lineTo(mWidth - (float) (mTriangleWidth * Math.sqrt(3) / 2) - 4, mHeight - mLeft + (float) (mTriangleWidth / 2));
        yPath.lineTo(mWidth - (float) (mTriangleWidth * Math.sqrt(3) / 2) - 4, mHeight - mLeft - (float) (mTriangleWidth / 2));
        canvas.drawPath(yPath, xPaint);
        //x坐标
//        for (int i = 0; i < xNames.size(); i++) {
//            XLineCoordinate xLineCoordinate = xNames.get(i);
//            Coordinate coordinate = xLineCoordinate.getCoordinate();
////            int value = xLineCoordinate.getValue();
//
////            canvas.drawText(xLineCoordinate.getValue()+"",);
//        }
        //y坐标
        canvas.drawText(mMaxValue + "", mLeft / 4, mHeight - mLeft - mMaxValue * mYPerHeight, xPaint);
        canvas.drawText(mMaxValue / 2f + "", mLeft / 4, mHeight - mLeft - mMaxValue * mYPerHeight / 2, xPaint);
        canvas.drawText(0 + "", mLeft / 4, mHeight - mLeft, xPaint);
        canvas.drawText(mYTitle, 0, mLeft * 5 / 6, xPaint);
        //x坐标
        xPaint.setTextSize(mTextSize * 7 / 10);
        for (int i = 0; mDataList != null && i < mDataList.size(); i++) {
            HistogramEntity histogramEntity = mDataList.get(i);
            canvas.drawText(histogramEntity.getxName(), histogramBeanList.get(i).ul.x, mHeight - mLeft * 4 / 5 + mTriangleWidth, xPaint);
        }
        xPaint.setTextSize(mTextSize);
//        canvas.drawText(mXTitle, mWidth - mXTitle.getBytes().length * mLeft / (float) 6 * -mLeft / (float) 3, mHeight - mLeft + mTriangleWidth, xPaint);
        canvas.drawText(mXTitle, mWidth - mXHisWidth - mTriangleWidth, mHeight - (mLeft / 3), xPaint);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
        super.surfaceCreated(holder);
        mBMBg = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
    }

    private void initDataAfterMeasure() {
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
        super.onInitDataOk();
    }

    public void setTextSize(int textSize) {
        xPaint.setTextSize(textSize);
        mLeft = 2 * textSize;
        mTextSize = textSize;
        mTriangleWidth = (int) (textSize * 5 / (float) 7);
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
                        new Coordinate(x1, y1).setExtra(yHeight),
                        new Coordinate(x2, y2).setExtra(yHeight),
                        new Coordinate(x3, y3).setExtra(yHeight));
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
        mMaxValue = 0;
        if (mDataList != null) {
            for (int i = 0; i < mDataList.size(); i++) {
                int value = mDataList.get(i).getValue();
                if (value > mMaxValue) mMaxValue = value;
            }
        } else {
            mMaxValue = 1;
        }
        //每份value值得高度
        mYPerHeight = (mHeight - mLeft * 2 - mTriangleWidth) / (float) mMaxValue;
        //y轴间隔高度
        mYHisHeight = mYPerHeight / (float) (minYNum - 1);
        //x坐标
//        xNames = new ArrayList<>();
//        for (int i = 0; i < minYNum; i++) {
//            int perValue = mMaxValue / minYNum;
//            XLineCoordinate xLine = new XLineCoordinate(new Coordinate(mLeft, mHeight - mLeft - i * mYHisHeight), perValue * i);
//            xNames.add(xLine);
//        }

    }


    public void setBgColor(int bgColor) {
        this.mBgColor = bgColor;
    }

    public void setBgColor(String color) {
        mBgColor = Color.parseColor(color);
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


    public void setXYTitle(String xTitle, String yTitle) {
        mXTitle = xTitle;
        mYTitle = yTitle;
    }
}
