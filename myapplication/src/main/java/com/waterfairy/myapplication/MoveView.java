package com.waterfairy.myapplication;

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

    private int mWidth, mHeight;
    private List<Rect> mRectList;
    private List<Integer> mPointList;
    private int mCurrentTag;
    private int currentTag;
    private int startX;
    private int maxNum;
    private List<Paint> paint;

    public MoveView(Context context) {
        super(context);
    }

    public MoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        mRectList = new ArrayList<>();
        mPointList = new ArrayList<>();
        paint = new ArrayList<>();

        initData(3, 200, 200);
    }

    public void initData(int num, int width, int height) {
        this.maxNum = num;
        this.mWidth = width;
        this.mHeight = height;
        mRectList.removeAll(mRectList);
        mPointList.removeAll(mPointList);
        paint.removeAll(paint);
        for (int i = 0; i < num; i++) {
            int leftPoint = i * width;
            int rightPoint = (i + 1) * width;
            Rect rect = new Rect(leftPoint, 0, rightPoint, height);
            mPointList.add(leftPoint);
            mRectList.add(rect);
            Paint paint = new Paint();
            Random random = new Random();
            paint.setColor(Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            this.paint.add(paint);
        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mRectList.size(); i++) {
            Rect rect = mRectList.get(i);
            canvas.drawRect(rect, paint.get(i));
            Paint paint=new Paint();
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(4);
            paint.setTextSize(50);
            canvas.drawText(i+1+"",rect.left+mWidth/2-32,mHeight/2-32,paint);
        }
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        if (action == MotionEvent.ACTION_DOWN) {
            setCurrentTag(x);
            startX = (int) x;
        } else if (action == MotionEvent.ACTION_MOVE) {
            int endX = (int) x;
            int temp = endX - startX;
            move(currentTag, temp);
            startX = (int) x;
        }
        return true;
    }

    private synchronized void move(int currentTag, int temp) {
        if (mRectList.size() <= 0 || mRectList.size() <= currentTag || currentTag == -1) return;
        Rect rect = mRectList.get(currentTag);
        if (temp > 0 && currentTag < maxNum - 1) {
            //右移
            if (rect.right >= mRectList.get(currentTag + 1).left) {
                move(currentTag + 1, temp);
            }
        } else if (temp < 0 && currentTag > 0) {
            //左移
            if (rect.left <= mRectList.get(currentTag - 1).right) {
                move(currentTag - 1, temp);
            }
        }
        rect.left += temp;
        rect.right += temp;
        invalidate();
    }

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
