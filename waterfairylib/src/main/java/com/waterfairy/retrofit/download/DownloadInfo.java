package com.waterfairy.retrofit.download;

/**
 * Created by shui on 2017/4/26.
 */

public abstract class DownloadInfo {
    public DownloadInfo(){

    }
    public static final int START = 1;
    public static final int DOWNLOADING = 2;
    public static final int PAUSE = 3;
    public static final int STOP = 4;
    public static final int FINISH = 5;
    public static final int ERROR = 6;

    public DownloadInfo(String basePath, String savePath, String url) {
        this.basePath = basePath;
        this.savePath = savePath;
        this.url = url;
    }

    protected String url;//下载路径
    protected String basePath;//基础路径
    protected String savePath;//保存路径
    protected long currentLen;//当前下载的位置
    protected long lastLen;//上次下载的位置
    protected long totalLen;//总长度
    protected int timeOut = 5;//超时 s
    protected int state;//下载状态

    public abstract String getUrl();

    public abstract void setUrl(String url);

    public abstract String getBasePath();

    public abstract void setBasePath(String basePath);

    public abstract String getSavePath();

    public abstract void setSavePath(String savePath);

    public abstract long getCurrentLen();

    public abstract void setCurrentLen(long currentLen);

    public abstract long getLastLen();

    public abstract void setLastLen(long lastLen);

    public abstract long getTotalLen();

    public abstract void setTotalLen(long totalLen);

    public abstract int getTimeOut();

    public abstract void setTimeOut(int timeOut);

    public abstract int getState();

    public abstract void setState(int state);
}
