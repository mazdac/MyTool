package com.waterfairy.retrofit.download2;

/**
 * Created by shui on 2017/5/6.
 */

public class DownloadProgress implements OnDownloadingListener {
    private OnDownloadListener onDownloadListener;//开放给用户的接口
    private OnDownloadSuccessListener onDownloadSuccessListener;//内部接口 下载完成时关闭control
    private DownloadInfo downloadInfo;//下载信息

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;

    }

    public OnDownloadListener getOnDownloadListener() {
        return onDownloadListener;

    }

    public DownloadProgress(DownloadInfo downloadInfo, OnDownloadSuccessListener onDownloadSuccessListener) {
        this.downloadInfo = downloadInfo;
        this.onDownloadSuccessListener = onDownloadSuccessListener;
    }


    @Override
    public void onDownloading(boolean done, long total, long current) {
        current = downloadInfo.getLastLen() + current;
        downloadInfo.setCurrentLen(current);
        if (done) {
            downloadInfo.setState(DownloadInfo.FINISH);
            onDownloadSuccessListener.onDownloadSuccess(downloadInfo.getUrl());
        }
        if (onDownloadListener != null)
            onDownloadListener.onDownloading(done, downloadInfo.getTotalLen(), current);
    }

}