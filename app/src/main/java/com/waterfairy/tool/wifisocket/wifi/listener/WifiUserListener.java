package com.waterfairy.tool.wifisocket.wifi.listener;

/**
 * Created by water_fairy on 2017/3/13.
 */

public interface WifiUserListener {
    void onConnecting();

    void onConnectSuccess();

    void onConnectError();

    void onRead(byte[] readBytes);

    void onWrite(byte[] writeBytes);

    void onWriteError();

    void onDisconnect();

    void isRightServer(boolean isRightServer);
}
