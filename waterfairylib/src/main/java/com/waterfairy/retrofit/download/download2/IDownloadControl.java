package com.waterfairy.retrofit.download.download2;

/**
 * Created by shui on 2017/4/26.
 * 文件下载控制 返回boolean  值为isDownloading
 */

public interface IDownloadControl {
    /**
     * 开始 继续下载
     * @return isDownloading
     */
    void start();

    /**
     * 暂停
     * @return isDownloading
     */
    void pause();

    /**
     * 停止 同暂停(待定)
     * @return isDownloading
     */
    void stop();

}
