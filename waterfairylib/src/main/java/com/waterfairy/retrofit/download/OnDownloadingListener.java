package com.waterfairy.retrofit.download;

/**
 * Created by shui on 2017/4/26.
 */

public interface OnDownloadingListener {
    /**
     * 开始下载
     */
    void onStartDownload();

    /**
     * 下载中
     *
     * @param done
     * @param total
     * @param current
     */
    void onDownloading(boolean done, long total, long current);

    /**
     * 下载失败
     *
     * @param msg
     */
    void onError(String msg);

}
