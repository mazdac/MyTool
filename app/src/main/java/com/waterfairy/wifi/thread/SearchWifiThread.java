package com.waterfairy.wifi.thread;

import android.app.Activity;

import com.waterfairy.wifi.listener.OnSendCallback;
import com.waterfairy.wifi.listener.OnWifiDeviceSearchListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by water_fairy on 2017/3/15.
 */

public class SearchWifiThread extends Thread {
    private Activity activity;
    private List<Thread> threads;
    private int searchNum;//搜索过的数量
    private String localIpIndex;//192.168.1.
    private OnWifiDeviceSearchListener searchListener;
    private String checkMsg;//校验信息
    private String returnMsg;//返回信息
    private final int MAX_NUM = 256;
    private Runtime run = Runtime.getRuntime();//获取当前运行环境，来执行ping，相当于windows的cmd
    private String ping = "ping -c 1 -w 0.5 ";//其中 -c 1为发送的次数，-w 表示发送后等待响应的时间
    private int port;

    public SearchWifiThread(Activity activity, String localIpIndex, int port, String checkMsg, String returnMsg, OnWifiDeviceSearchListener searchListener) {
        this.activity = activity;
        this.localIpIndex = localIpIndex;
        this.port = port;
        this.checkMsg = checkMsg;
        this.returnMsg = returnMsg;
        this.searchListener = searchListener;
    }

    @Override
    public void run() {
        super.run();
        searchNum = 0;
        threads = new ArrayList<>();
        for (int i = 0; i < MAX_NUM; i++) {
            //开新的线程去ping
            //ping的通的 去连接 发送 checkMsg
            //如果 服务器返回 returnMsg 相同  则为目标服务器
//            try {
//                this.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            newThreadToConnect(i);
        }
    }

    private void newThreadToConnect(final int index) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                final String ip = localIpIndex + index;
                try {
                    //ping  result
                    Process exec = run.exec(ping + ip);
                    int result = exec.waitFor();
                    exec.destroy();
                    if (result == 0) {
                        //ping 成功
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchListener.onSearch(ip);
                            }
                        });

                        sendMsg(ip, checkMsg, new OnSendCallback() {
                            @Override
                            public void onBack(String msg) {
                                if (returnMsg.equals(msg)) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            searchListener.onRightSearch(ip);
                                        }
                                    });

                                }
                            }
                        });
                    }
                    searchNum++;
                    if (searchNum >= MAX_NUM) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchListener.onSearchFinish();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        threads.add(thread);
    }


    public void stopSearch() {
        for (int i = 0; i < threads.size(); i++) {
            Thread thread = threads.get(i);
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }
    }


    //向serversocket发送消息
    private void sendMsg(String ip, String msg, OnSendCallback onSendCallback) {

        String res = null;
        Socket socket = null;

        try {
            socket = new Socket(ip, port);
            //向服务器发送消息
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            os.print(msg);
            os.flush();// 刷新输出流，使Server马上收到该字符串

            //从服务器获取返回消息
            InputStream inputStream = socket.getInputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                res = new String(Arrays.copyOf(bytes, len));
                onSendCallback.onBack(res);
                break;
            }
        } catch (Exception unknownHost) {
            System.out.println("You are trying to connect to an unknown host!");
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

}
