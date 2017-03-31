package com.waterfairy.tool.h5;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by water_fairy on 2017/3/30.
 */

public interface WebInterface {
    public static final String TAG = "webInterface";

    @JavascriptInterface
    public void callFromJSBaseDataType(int result);

    @JavascriptInterface
    public void callAndroidMethod();

}
