package com.waterfairy.wifi.listener;

/**
 * Created by water_fairy on 2017/3/13.
 */

public interface OnWifiDeviceSearchListener {
    void onSearch(String ipAddress);

    void onRightSearch(String ipAddress);

    void onSearchFinish();
}
