package com.waterfairy.tool.rxjava.retrofit.down;

import com.waterfairy.retrofit.download.DownloadInfo;

/**
 * Created by water_fairy on 2017/5/25.
 * 995637517@qq.com
 */

public class MyDownloadInfo extends DownloadInfo {

    public MyDownloadInfo(String basePath, String savePath, String url) {
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

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getBasePath() {
        return basePath;
    }

    @Override
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String getSavePath() {
        return savePath;
    }

    @Override
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    @Override
    public long getCurrentLen() {
        return currentLen;
    }

    @Override
    public void setCurrentLen(long currentLen) {
        this.currentLen = currentLen;
    }

    @Override
    public long getLastLen() {
        return lastLen;
    }

    @Override
    public void setLastLen(long lastLen) {
        this.lastLen = lastLen;
    }

    @Override
    public long getTotalLen() {
        return totalLen;
    }

    @Override
    public void setTotalLen(long totalLen) {
        this.totalLen = totalLen;
    }

    @Override
    public int getTimeOut() {
        return timeOut;
    }

    @Override
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }
}
