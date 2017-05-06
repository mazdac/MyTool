package com.waterfairy.retrofit.download.download2;

import java.util.HashMap;

/**
 * Created by shui on 2017/5/6.
 */

public class DownloadManager implements IDownloadManager {
    private static final int REMOVE_ONE = 1;
    private static final int REMOVE_ALL = 2;
    private static final int START_ALL = 3;
    private static final int PAUSE_ALL = 4;
    private static final int STOP_ALL = 5;
    private static DownloadManager DOWNLOAD_MANGER;
    private HashMap<String, DownloadControl> controlHashMap;

    private DownloadManager() {

    }

    public static DownloadManager getInstance() {
        if (DOWNLOAD_MANGER == null) DOWNLOAD_MANGER = new DownloadManager();
        return DOWNLOAD_MANGER;
    }


    /**
     * 添加下载
     *
     * @param downloadInfo
     * @return
     */
    @Override
    public DownloadControl add(DownloadInfo downloadInfo) {
        if (downloadInfo == null) return null;
        DownloadControl downloadControl = get(downloadInfo.getUrl());
        if (downloadControl == null) {
            downloadControl = new DownloadControl(downloadInfo);
        }
        controlHashMap.put(downloadInfo.getUrl(), downloadControl);
        return downloadControl;
    }

    /**
     * 添加下载
     *
     * @param url
     * @return
     */
    @Override
    public DownloadControl get(String url) {
        if (controlHashMap == null) controlHashMap = new HashMap<>();
        return controlHashMap.get(url);
    }


    /**
     * 移除下载
     *
     * @param url
     */
    @Override
    public void remove(String url) {
        if (controlHashMap != null) {
            IDownloadControl iDownloadControl = controlHashMap.get(url);
            if (iDownloadControl != null) {
                iDownloadControl.stop();
                controlHashMap.remove(url);
            }
        }
    }

    /**
     * 移除下载
     */
    @Override
    public void removeAll() {
        if (controlHashMap != null) {
            for (String url : controlHashMap.keySet()) {
                remove(url);
            }
        }
    }

    /**
     * 暂停下载
     */
    @Override
    public void pauseAll() {
        if (controlHashMap != null) {
            for (String url : controlHashMap.keySet()) {
                IDownloadControl iDownloadControl = controlHashMap.get(url);
                if (iDownloadControl != null) {
                    iDownloadControl.pause();
                }
            }
        }

    }

    /**
     * 停止下载
     */
    @Override
    public void stopAll() {
        if (controlHashMap != null) {
            for (String url : controlHashMap.keySet()) {
                IDownloadControl iDownloadControl = controlHashMap.get(url);
                if (iDownloadControl != null) {
                    iDownloadControl.stop();
                }
            }
        }
    }

    /**
     *
     */
    @Override
    public void startAll() {
        if (controlHashMap != null) {
            for (String url : controlHashMap.keySet()) {
                IDownloadControl iDownloadControl = controlHashMap.get(url);
                if (iDownloadControl != null) {
                    iDownloadControl.start();
                }
            }
        }
    }


}
