package com.waterfairy.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by shui on 2017/4/26.
 */

public class DownloadInfo {
    public static final int START = 1;
    public static final int DOWNLOADING = 2;
    public static final int PAUSE = 3;
    public static final int STOP = 4;
    public static final int FINISH = 5;
    public static final int ERROR = 6;

    public DownloadInfo(String basePath,String savePath,  String url, OnDownloadingListener onDownloadingListener) {
        this.basePath=basePath;
        this.savePath = savePath;
        this.url = url;
        this.onDownloadingListener = onDownloadingListener;
    }

    private String url;//下载路径
    private String basePath;//基础路径
    private String savePath;//保存路径
    private long currentLen;//当前下载的位置
    private long lastLen;//上次下载的位置
    private long totalLen;//总长度
    private Call<ResponseBody> call;
    private OnDownloadingListener onDownloadingListener;
    private int state;//下载状态
    private DownloadService downloadService;
    private DownloadControl control;
    private int timeOut = 5;//

    public DownloadControl getControl() {
        return control;
    }

    public long getLastLen() {
        return lastLen;
    }

    public void setLastLen(long lastLen) {
        this.lastLen = lastLen;
    }

    public DownloadService getDownloadService() {
        return downloadService;
    }

    public void setDownloadService(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    public long getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(long totalLen) {
        this.totalLen = totalLen;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Call<ResponseBody> getCall() {
        return call;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getCurrentLen() {
        return currentLen;
    }

    public void setCurrentLen(long currentLen) {
        this.currentLen = currentLen;
    }


    public OnDownloadingListener getOnDownloadingListener() {
        return onDownloadingListener;
    }

    public void setOnDownloadingListener(OnDownloadingListener onDownloadingListener) {
        this.onDownloadingListener = onDownloadingListener;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setCall(retrofit2.Call<ResponseBody> call) {
        this.call = call;
    }

    public void setControl(DownloadControl control) {
        this.control = control;
    }
}
