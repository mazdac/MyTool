package com.waterfairy.tool.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.waterfairy.tool.R;
import com.waterfairy.tool.image.gaosi.FastBlurUtils;
import com.waterfairy.utils.ImageUtils;
import com.waterfairy.utils.ToastUtils;

import java.util.Date;

public class GaoSiActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "stre";
    private Bitmap matrix, resource;
    private ImageView imageView, imageViewCopy;
    private EditText systemRadius, selfRadius;
    private TextView textView;
    private SeekBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gao_si);
        initView();


        resource = BitmapFactory.decodeResource(getResources(), R.mipmap.jj);
        resource.getWidth();
        resource.getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

//        Bitmap matrix = ImageUtils.matrix(resource, displayMetrics.widthPixels, displayMetrics.heightPixels);


//


    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.img);
        imageViewCopy = (ImageView) findViewById(R.id.img_copy);
        systemRadius = (EditText) findViewById(R.id.blur_system_edit);
        selfRadius = (EditText) findViewById(R.id.blur_self_edit);
        textView = (TextView) findViewById(R.id.text);
        progressBar = (SeekBar) findViewById(R.id.seek_bar);
        progressBar.setMax(100);
        progressBar.setOnSeekBarChangeListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blur_self:
                blur(1);
                break;
            case R.id.blur_system:
                blur(0);
                break;
            case R.id.blur_self_2:
                blur(2);
                break;
        }
    }

    private void blur(int tag) {
        Bitmap blurBitmap = null;
        matrix = ImageUtils.matrix(resource, 1000, 800, false);
        float systemRadius = Float.parseFloat(this.systemRadius.getText().toString());
        int selfRadius = Integer.parseInt(this.selfRadius.getText().toString());
        long time1 = (new Date()).getTime();
        String content = "";
        switch (tag) {
            case 0:
                if (systemRadius > 0 && systemRadius <= 25) {

                    blurBitmap = ImageUtils.blur(this, matrix, systemRadius, false);//66ms
                    content = "系统" + systemRadius + "--";
                } else {
                    ToastUtils.show("0<radius<=25 之间");
                }
                break;
            case 1:
                blurBitmap = FastBlurUtils.doBlur(matrix, selfRadius, false);//120ms
                content = "自处理1" + selfRadius + "--";
                break;
            case 2:
                blurBitmap = ImageUtils.selfBlur2(matrix);
                content = "自处理2" + selfRadius + "--";
                break;
        }
        long time2 = (new Date()).getTime();
        content += (time2 - time1);
        Log.i(TAG, "blur: ->" + content);
        textView.setText(content + "\n" + textView.getText().toString());
        imageView.setImageBitmap(matrix);
        imageViewCopy.setImageBitmap(blurBitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        imageViewCopy.setAlpha((100 - progress) / 100f);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
