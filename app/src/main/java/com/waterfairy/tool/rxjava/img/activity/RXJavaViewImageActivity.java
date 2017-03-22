package com.waterfairy.tool.rxjava.img.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.waterfairy.tool.R;
import com.waterfairy.tool.rxjava.img.presenter.RXJavaViewImagePresenter;
import com.waterfairy.tool.rxjava.img.view.RXJavaViewImageView;

public class RXJavaViewImageActivity extends AppCompatActivity implements RXJavaViewImageView, View.OnClickListener {
    private RXJavaViewImagePresenter mPresenter;
    private ImageView mIVImg;
    private Button mBTGetImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_view_image);
        findView();
        initView();
        initData();
    }

    private void initData() {
        mPresenter = new RXJavaViewImagePresenter(this);

    }

    private void initView() {
        mBTGetImg.setOnClickListener(this);
    }

    private void findView() {
        mIVImg = (ImageView) findViewById(R.id.img);
        mBTGetImg = (Button) findViewById(R.id.get_img);
    }

    @Override
    public void loadImg(Bitmap bitmap) {
        mIVImg.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.get_img:
                mPresenter.loadImg();
                break;
        }
    }
}
