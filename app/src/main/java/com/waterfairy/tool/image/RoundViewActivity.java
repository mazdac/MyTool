package com.waterfairy.tool.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.waterfairy.tool.R;
import com.waterfairy.utils.ImageUtils;

public class RoundViewActivity extends AppCompatActivity {
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_view);
        initView();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.img);
    }

    public void onClick(View view) {
        Bitmap bitmap = null;
        Bitmap resourceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.jj);
        switch (view.getId()) {
            case R.id.round:
                bitmap = ImageUtils.round(resourceBitmap, 30);
                break;
            case R.id.thumb:
                bitmap = ImageUtils.thumbnail(resourceBitmap, 100, 100);
                break;
            case R.id.saturation:
                bitmap = ImageUtils.saturation(resourceBitmap, 0.01f);
                break;
            case R.id.lum:
                bitmap = ImageUtils.lum(resourceBitmap, 0.5f);
                break;
            case R.id.hue:
                bitmap = ImageUtils.hue(resourceBitmap, 1f);
                break;
            case R.id.all:
                bitmap = ImageUtils.colorMatrix(resourceBitmap, 0.6f, 0.5f, 0.5f);
                break;
            case R.id.emboss:
                bitmap = ImageUtils.emboss(resourceBitmap);
                break;
            case R.id.negative:
                bitmap = ImageUtils.negative(resourceBitmap);
                break;
            case R.id.overlay:
                bitmap = ImageUtils.overlay(resourceBitmap, ImageUtils.emboss(resourceBitmap));
                break;
            case R.id.reflection:
                bitmap = ImageUtils.reflection(resourceBitmap);
                break;
        }
        imageView.setImageBitmap(bitmap);

    }
}
