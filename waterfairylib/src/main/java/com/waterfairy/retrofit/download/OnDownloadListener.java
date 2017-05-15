package com.waterfairy.retrofit.download;

/**
 * Created by shui on 2017/4/26.
 */

public interface OnDownloadListener {

    void onDownloading(boolean done, long total, long current);

    void onError(int code);

    void onChange(int code);
}
