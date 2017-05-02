package com.waterfairy.retrofit.download;

/**
 * Created by shui on 2017/4/26.
 * 文件下载控制 返回boolean  值为isDownloading
 */

public interface DownloadControl {
    /**
     * 开始 继续下载
     * @return isDownloading
     */
    boolean start();

    /**
     * 暂停
     * @return isDownloading
     */
    boolean pause();

    /**
     * 停止 同暂停(待定)
     * @return isDownloading
     */
    boolean stop();

}
