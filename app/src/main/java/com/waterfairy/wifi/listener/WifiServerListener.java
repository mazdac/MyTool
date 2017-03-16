package com.waterfairy.wifi.listener;

/**
 * Created by water_fairy on 2017/3/13.
 */

public interface WifiServerListener {
    void onStarting();

    void onStartServerSuccess();

    void onStartServerError();

    void onConnectSuccess(String ipAddress);

    void onRead(byte[] readBytes);

    void onWrite(String ipAddress, byte[] writeBytes);

    void onWriteError(String ipAddress);

    void onDisconnect(boolean isServerOpen, String ipAddress);

    void onServerClose();
}
