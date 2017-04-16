package com.waterfairy.tool.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shui on 2017/4/12.
 */

public class MoveView extends View implements View.OnTouchListener {

    private int mWidth, mHeight;// 块 大小
    private List<Rect> mRectList;// 块 集合
    private List<Integer> mPointList;//每块区域的左边 x 坐标
    private int currentTag;//当前选中的块  0,1,2...
    private int startX;//触摸开始的点(x坐标)
    private int maxNum;//最多块的数量
    private List<Paint> paintList;//每块用到的画笔(块颜色的处理)

    public MoveView(Context context) {
        super(context);
    }

    public MoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        mRectList = new ArrayList<>();
        mPointList = new ArrayList<>();
        paintList = new ArrayList<>();

        initData(3, 200, 200);//初始化 ,默认3个
    }

    /**
     * 初始化
     *
     * @param num    块数量
     * @param width  宽
     * @param height 高
     */
    public void initData(int num, int width, int height) {
        this.maxNum = num;
        this.mWidth = width;
        this.mHeight = height;
        mRectList.removeAll(mRectList);
        mPointList.removeAll(mPointList);
        paintList.removeAll(paintList);


        for (int i = 0; i < num; i++) {
            int leftPoint = i * width;
            int rightPoint = (i + 1) * width;
            Rect rect = new Rect(leftPoint, 0, rightPoint, height);//块区域
            mPointList.add(leftPoint);
            mRectList.add(rect);
            Paint paint = new Paint();
            Random random = new Random();
            paint.setColor(Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255)));//颜色随机
            this.paintList.add(paint);
        }
        invalidate();//刷新绘图
    }

    /**
     * 继承 onDraw
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mRectList.size(); i++) {
            Rect rect = mRectList.get(i);
            canvas.drawRect(rect, paintList.get(i));
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(4);
            paint.setTextSize(50);
            canvas.drawText(i + 1 + "", rect.left + mWidth / 2 - 32, mHeight / 2 - 32, paint);
        }
    }

    /**
     * 继承触摸事件
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        if (action == MotionEvent.ACTION_DOWN) {
            setCurrentTag(x);
            startX = (int) x;//定位按下位置
        } else if (action == MotionEvent.ACTION_MOVE) {
            int endX = (int) x;//定位移动的位置
            int temp = endX - startX;
            move(currentTag, temp);
            invalidate();
            startX = (int) x;//定位上次移动位置
        }
        return true;
    }

    /**
     * 每一块的移动
     *
     * @param currentTag
     * @param temp
     */
    private synchronized void move(int currentTag, int temp) {
        if (mRectList.size() <= 0 || mRectList.size() <= currentTag || currentTag == -1) return;
        Rect rect = mRectList.get(currentTag);
        if (temp > 0 && currentTag < maxNum - 1) {
            //右移
            Rect rightRect = mRectList.get(currentTag + 1);
            if (rect.right >= rightRect.left) {
                //修改有边第一个块坐标,移动过快会有坐标跳跃(从12->20)
                rightRect.left = rect.right;
                rightRect.right = rightRect.left + mWidth;
                //移动下一个块
                move(currentTag + 1, temp);
            }
        } else if (temp < 0 && currentTag > 0) {
            //左移  (类似右移)
            Rect leftRect = mRectList.get(currentTag - 1);
            if (rect.left <= leftRect.right) {
                leftRect.right = rect.left;
                leftRect.left = leftRect.right - mWidth;
                move(currentTag - 1, temp);
            }
        }
        rect.left += temp;
        rect.right += temp;
    }

    /**
     * 获取当前触摸块 0.1.2....
     * @param x
     */
    public void setCurrentTag(float x) {
        for (int i = 0; i < mRectList.size(); i++) {
            Rect rect = mRectList.get(i);
            if (rect.left < x && rect.right > x) {
                currentTag = i;
                return;
            }
        }
        currentTag = -1;
    }
}
