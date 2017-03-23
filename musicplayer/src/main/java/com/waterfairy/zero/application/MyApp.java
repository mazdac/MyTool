package com.waterfairy.zero.application;

import android.app.Application;

import com.waterfairy.utils.ToastUtils;

/**
 * Created by water_fairy on 2017/3/23.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    private void initData() {
        ToastUtils.initToast(this);
    }
}
