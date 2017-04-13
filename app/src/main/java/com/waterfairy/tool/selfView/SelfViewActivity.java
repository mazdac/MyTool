package com.waterfairy.tool.selfView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.waterfairy.tool.R;
import com.waterfairy.tool.widget.SelfSurfaceView;
import com.waterfairy.tool.widget.SelfView;

public class SelfViewActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "selfActivity";
    private SelfView selfView;
    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private ImageView imageView;
    private SelfSurfaceView selfSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_view);
        selfView = (SelfView) findViewById(R.id.self_view);
        selfView.setOnTouchListener(this);
        imageView = (ImageView) findViewById(R.id.img);
        imageView = (ImageView) findViewById(R.id.img);
        selfSurfaceView = (SelfSurfaceView) findViewById(R.id.surface_view);

        {
            bitmap = Bitmap.createBitmap(1080, 500, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#ff0000"));
            paint.setStrokeWidth(6);
        }

    }


    float startX, startY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.i(TAG, "onTouch: " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
            {
                selfSurfaceView.setStart(startX, startY);
            }
            break;
            case MotionEvent.ACTION_UP: {
                imageView.setImageBitmap(selfSurfaceView.getBitmap());
            }
            break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
            {
                selfSurfaceView.draw(x, y);
            }
//            canvas.drawLine(startX, startY, x, y, paint);
//            canvas.drawCircle(x, y, 3, paint);
//            imageView.setImageBitmap(bitmap);
//            startX = x;
//            startY = y;


            break;

        }
        return true;
    }


    private void draw(float x, float y) {
        Log.i("draw", "draw: " + x + "-" + y);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.sin) {
            selfSurfaceView.setColor(Color.RED);
        } else if (view.getId()==R.id.cos){
            selfSurfaceView.setColor(Color.GREEN);
        }else if (view.getId()==R.id.cir){
            selfSurfaceView.setColor(Color.BLUE);
        }

    }

}
