package com.waterfairy.wifi.manger;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.util.Log;

import com.waterfairy.wifi.listener.OnWifiDeviceSearchListener;
import com.waterfairy.wifi.listener.WifiServerListener;
import com.waterfairy.wifi.listener.WifiUserListener;
import com.waterfairy.wifi.thread.SearchWifiThread;
import com.waterfairy.wifi.thread.ServerThread;
import com.waterfairy.wifi.thread.UserThread;

import java.util.HashMap;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by water_fairy on 2017/3/13.
 */

public class WifiManager {
    private static final String TAG = "WifiManger";
    private static final WifiManager WIFI_MANAGER = new WifiManager();
    private int port = 30000;//服务器端口

    private HashMap<String, UserThread> mDeviceHashMap;//客户端进程
    private ServerThread serverThread;//服务器进程
    private SearchWifiThread searchWifiThread;//搜索服务器进程

    public static WifiManager getInstance() {
        return WIFI_MANAGER;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * 客户端
     *
     * @param ip
     */

    public void connect(String ip, WifiUserListener callback) {
        connect(ip, null, null, callback);
    }

    /**
     * @param ip
     * @param checkMsg  连接校验
     * @param returnMsg 校验返回
     * @param callback
     * @return 是否已将连接
     */
    public boolean connect(String ip, String checkMsg, String returnMsg, WifiUserListener callback) {
        if (mDeviceHashMap == null) {
            mDeviceHashMap = new HashMap<>();
        }
        if (mDeviceHashMap.containsKey(ip)) {
            Log.i(TAG, "connect: 已连接");
            return true;
        }
        new UserThread(ip, port, checkMsg, returnMsg, mDeviceHashMap, callback).start();
        return false;
    }

    /**
     * 设备服务器
     *
     * @param isOneUser 是否为一个用户
     * @param listener
     */
    public void setAsServer(boolean isOneUser, WifiServerListener listener) {
        setAsServer(isOneUser, null, null, listener);
    }

    /**
     * 作为服务器
     *
     * @param isOneUser  是否为一个用户
     * @param receiveMsg 接收校验
     * @param returnMsg  返回校验
     * @param listener
     */
    public void setAsServer(boolean isOneUser, String receiveMsg, String returnMsg, WifiServerListener listener) {
        if (serverThread == null) {
            serverThread = new ServerThread(port, isOneUser, receiveMsg, returnMsg, listener);
        }
        if (!serverThread.isServerOpen()) {
            if (!serverThread.isAlive()) {
                serverThread.start();
            }
        }
    }


    /**
     * 客户端写入
     *
     * @param ip
     * @param bytes
     */
    public void writeMsgFromUser(String ip, final byte[] bytes) {
        final UserThread userThread = mDeviceHashMap.get(ip);
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (userThread != null) {
                    userThread.write(bytes);
                }
            }
        }.start();

    }

    /**
     * 客户端写入(只有一个用户或者上一个用户)
     *
     * @param bytes
     */
    public void writeMsgFromUser(byte[] bytes) {
        writeMsgFromUser(UserThread.lastUserIpAddress, bytes);
    }

    /**
     * 由服务端写入
     * 用于只有一个用户的时候  isOneUser = true
     *
     * @param bytes
     */
    public void writeMsgFromServer(byte[] bytes) {
        if (serverThread != null) {
            writeMsgFromServer(serverThread.getLastUser(), bytes);
        }
    }

    /**
     * 由服务端写入
     *
     * @param ipAddress
     * @param bytes
     */
    public void writeMsgFromServer(final String ipAddress, final byte[] bytes) {
        if (serverThread != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    serverThread.write(ipAddress, bytes);
                }
            }.start();
        }
    }

    /**
     * 搜索局域网中的设备
     *
     * @param activity
     * @param onWifiDeviceSearchListener
     */
    public void searchWifi(Activity activity, OnWifiDeviceSearchListener onWifiDeviceSearchListener) {
        searchWifi(activity, null, null, onWifiDeviceSearchListener);
    }


    /**
     * 搜索指定设备
     *
     * @param activity
     * @param checkMsg                   验证信息
     * @param returnMsg                  返回信息
     * @param onWifiDeviceSearchListener
     */
    public void searchWifi(Activity activity, String checkMsg, String returnMsg, OnWifiDeviceSearchListener onWifiDeviceSearchListener) {
        String localIpIndex = getLocalIpIndex(activity);
        searchWifiThread = new SearchWifiThread(activity, localIpIndex, port, checkMsg, returnMsg, onWifiDeviceSearchListener);
        searchWifiThread.start();

    }

    /**
     * 停止搜索服务器,可能不起作用
     */
    public void stopSearchWifi() {
        if (searchWifiThread != null)
            searchWifiThread.stopSearch();
    }

    /**
     * 获取本地ip
     *
     * @param context
     * @return
     */
    public String getLocalIP(Context context) {
        android.net.wifi.WifiManager wifiService = (android.net.wifi.WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiinfo = wifiService.getConnectionInfo();
        return intToIp(wifiinfo.getIpAddress());
    }

    /**
     * 获取本地ip前缀
     *
     * @param context
     * @return
     */
    private String getLocalIpIndex(Context context) {
        String localIP = getLocalIP(context);
        if (!localIP.equals("")) {
            return localIP.substring(0, localIP.lastIndexOf(".") + 1);
        }
        return "";
    }

    private String intToIp(int ipAddress) {
        return (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF)
                + "." + (ipAddress >> 24 & 0xFF);
    }

    /**
     * 客户端断开指定ip的用户连接
     *
     * @param ip
     */
    public void disconnect(String ip) {
        UserThread userThread = mDeviceHashMap.get(ip);
        if (userThread != null) {
            userThread.disconnect();
        }
    }

    /**
     * 用户断开连接 (连接服务器的上一个用户,或者只有一个用户)
     */
    public void disconnect() {
        disconnect(UserThread.lastUserIpAddress);
    }

    /**
     * 由服务器断开用户(一个用户的时候或者上一个用户)连接
     */
    public void disconnectFromServer() {
        serverThread.disconnect();
    }

    /**
     * 由服务器断开指定用户连接
     *
     * @param ip
     */
    public void disconnectFromServer(String ip) {
        serverThread.disconnect(ip);
    }

    /**
     * 关闭服务器,断开所有用户的连接
     */
    public void closeServer() {
        if (serverThread != null) {
            serverThread.closeServer();
        }
        serverThread = null;
    }

}
