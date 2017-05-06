package com.waterfairy.retrofit.download.download2;

/**
 * Created by shui on 2017/4/26.
 */

public interface OnDownloadListener {
    void onStartDownload();

    void onDownloading(boolean done, long total, long current);

    void onError(String msg);

    void onContinue();
}
