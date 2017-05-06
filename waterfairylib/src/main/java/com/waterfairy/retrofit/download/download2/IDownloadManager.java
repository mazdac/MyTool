package com.waterfairy.retrofit.download.download2;

/**
 * Created by shui on 2017/5/6.
 */

public interface IDownloadManager {
    /**
     * 添加下载
     *
     * @param downloadInfo
     * @return
     */
    DownloadControl add(DownloadInfo downloadInfo);
    /**
     * 添加下载
     *
     * @param url
     * @return
     */
    DownloadControl get(String url);
    /**
     * 移除下载
     *
     * @param url
     */
    void remove(String url);

    /**
     * 移除下载
     */

    void removeAll();

    /**
     * 暂停下载
     */

    void pauseAll();

    /**
     * 停止下载
     */

    void stopAll();

    /**
     *
     */
    void startAll();


}
