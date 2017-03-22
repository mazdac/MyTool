package com.waterfairy.tool.rxjava.img.model;

import android.graphics.Bitmap;

import com.waterfairy.tool.rxjava.img.base.BaseListener;

/**
 * Created by water_fairy on 2017/3/13.
 */

public interface IRXJavaViewImageListener extends BaseListener {
    void onGetImg(Bitmap bitmap);
}
