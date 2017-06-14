package com.waterfairy.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by shui on 2017/4/11.
 */

public class SelfView extends View {
    private Canvas canvas;

    private static final String TAG = "selfView";

    public SelfView(@NonNull Context context) {
        super(context);
    }

    public SelfView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    float num;

    @Override
    protected void onDraw(Canvas canvas) {

        Log.i(TAG, "onDraw: ");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(14 * 3);
        paint.setColor(Color.parseColor("#0011FF"));
        canvas.drawText("hah", 100 * num, 100, paint);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "onLayout: " + changed + "-" + left + "-" + top + "-" + right + "-" + bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.AT_MOST) {
            //wrap_content
            Log.i(TAG, "onMeasure: wrap_content " + "_" + size);
        } else if (mode == MeasureSpec.EXACTLY) {
            //match_parent
            Log.i(TAG, "onMeasure: match_parent " + "_" + size);
        }
//        setMeasuredDimension(500, 400);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: " + w + "-" + h + "-" + oldw + "-" + oldh);
    }


    public void drawOnSelfView(float x, float y) {

        num = x / y;
        postInvalidate();
    }
}
