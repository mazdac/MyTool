package com.waterfairy.tool.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.waterfairy.tool.rxjava.retrofit.Response;

/**
 * Created by shui on 2017/4/11.
 */

public class SelfSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "surfaceView";
    private Paint paint;
    private Path path;
    private SurfaceHolder surfaceHolder;
    private Bitmap bitmap;
    private int color;

    public SelfSurfaceView(Context context) {
        super(context);
    }

    public SelfSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setFocusable(true);

        paint = new Paint();
        paint.setColor(Color.parseColor("#ff0000"));
//        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(8);
        path = new Path();

        bitmap = Bitmap.createBitmap(1080, 500, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed: ");
    }


    private float startX, startY;

    public void setStart(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
        path.moveTo(startX, startY);
    }


    public void draw(float x, float y) {
        path.lineTo(x, y);
        Canvas canvas = surfaceHolder.lockCanvas();//获取画布
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.setBitmap(bitmap);
        canvas.drawCircle(x, y, 3, paint);
//        canvas.drawPath(path, paint);
        canvas.drawLine(startX, startY, x, y, paint);
        startX = x;
        startY = y;
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void onPause() {

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    private boolean isThreadRun;

    public void setColor(final int color) {
        this.color = color;
        paint.setColor(color);
        isThreadRun = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                int num = 0;
                while (isThreadRun) {
                    num += 5;
                    try {
                        Thread.sleep(1);
                        if (color == Color.RED) {
                            drawSin(num, 1024);
                        } else if (color == Color.GREEN) {
                            drawCos(num, 1024);
                        } else if (color == Color.BLUE) {
                            drawCircle(num);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (num >= 1024) {
                        isThreadRun = false;
                        startX = 0;
                        startY = 0;
                    }
                }
            }
        }.start();
    }

    private void drawCircle(int num) {

        int a = 400, b = 190, r = 180;
        float x = (float) (r * Sin(num))-a;
        float y = (float) (Math.sqrt(r * r - x * x) + b);
        draw((float) x+400, y);
    }

    private void drawCos(int num, int i) {
        draw(num, (float) (cos(num)) * 180 + 190);
    }

    private void drawSin(int num, int total) {

        Log.i(TAG, "drawSin: " + num);
        //x2+y2=1;


        draw(num, (float) (Sin(num)) * 180 + 190);

    }

    public double Sin(int i) {
        double result = 0;
        //在这里我是写sin函数，其实可以用cos，tan等函数的，不信大家尽管试试
//result = Math.cos(i * Math.PI / 180);
        result = Math.sin(i * Math.PI / 180);
//result = Math.tan(i * Math.PI / 180);
        return result;
    }

    public double cos(int i) {
        double result = 0;
        //在这里我是写sin函数，其实可以用cos，tan等函数的，不信大家尽管试试
        result = Math.cos(i * Math.PI / 180);
//        result = Math.sin(i * Math.PI / 180);
//result = Math.tan(i * Math.PI / 180);
        return result;
    }


}
