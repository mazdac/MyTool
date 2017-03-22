package com.waterfairy.tool.application;

import android.app.Application;

import com.waterfairy.utils.ToastUtils;

/**
 * Created by water_fairy on 2017/2/17.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.initToast(this);
    }
}
