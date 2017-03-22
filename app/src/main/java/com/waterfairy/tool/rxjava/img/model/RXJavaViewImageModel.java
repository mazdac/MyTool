package com.waterfairy.tool.rxjava.img.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.waterfairy.tool.rxjava.img.activity.RXJavaViewImageActivity;
import com.waterfairy.tool.rxjava.img.base.BaseListener;
import com.waterfairy.utils.ImageUtils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by water_fairy on 2017/3/13.
 */

public class RXJavaViewImageModel implements IRXJavaViewImageModel {
    private static final String TAG = "rxJavaViewImageModel";
    private IRXJavaViewImageListener mListener;
    private Activity mActivity;
    private RXJavaViewImageActivity mRXJavaViewImageActivity;

    public RXJavaViewImageModel(BaseListener baseListener, Activity activity) {
        mListener = (IRXJavaViewImageListener) baseListener;
        mActivity = activity;
        if (activity instanceof RXJavaViewImageActivity) {
            mRXJavaViewImageActivity = (RXJavaViewImageActivity) activity;
        }
    }

    @Override
    public void getImgBitmap(final int resImg) {
        final int blurRadius = 25;
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), resImg);
//                Bitmap blurImg = ImageUtils.blur(mActivity, bitmap, blurRadius, true);
                bitmap = ImageUtils.selfBlur1(bitmap, 300, false);
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: 获取图片完成");
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof OutOfMemoryError) {
                    Log.i(TAG, "onCompleted: 内存不足,请降低高斯半径,获取图片像素");
                } else {
                    Log.i(TAG, "onCompleted: 获取图片失败");
                }
            }

            @Override
            public void onNext(Bitmap bitmap) {
                mListener.onGetImg(bitmap);
            }
        });
    }
}
