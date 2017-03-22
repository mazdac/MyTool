package com.waterfairy.tool.rxjava.img.presenter;

import android.app.Activity;
import android.graphics.Bitmap;

import com.waterfairy.tool.R;
import com.waterfairy.tool.rxjava.img.activity.RXJavaViewImageActivity;
import com.waterfairy.tool.rxjava.img.base.BaseListener;
import com.waterfairy.tool.rxjava.img.model.IRXJavaViewImageListener;
import com.waterfairy.tool.rxjava.img.model.IRXJavaViewImageModel;
import com.waterfairy.tool.rxjava.img.model.RXJavaViewImageModel;
import com.waterfairy.tool.rxjava.img.view.RXJavaViewImageView;

/**
 * Created by water_fairy on 2017/3/13.
 */

public class RXJavaViewImagePresenter implements IRXJavaViewImageListener {
    private Activity mActivity;
    private RXJavaViewImageActivity mRXJavaViewImageActivity;
    private RXJavaViewImageView mView;
    private IRXJavaViewImageModel mModel;

    public RXJavaViewImagePresenter(RXJavaViewImageView view) {
        mView = view;
        this.mActivity = (Activity) view;
        mModel = new RXJavaViewImageModel(this, mActivity);
        if (mActivity instanceof RXJavaViewImageActivity) {
            mRXJavaViewImageActivity = (RXJavaViewImageActivity) mActivity;
        }

    }

    public void loadImg() {
        mModel.getImgBitmap(R.mipmap.jj);
    }


    @Override
    public void onGetImg(Bitmap bitmap) {
        mView.loadImg(bitmap);
    }
}
