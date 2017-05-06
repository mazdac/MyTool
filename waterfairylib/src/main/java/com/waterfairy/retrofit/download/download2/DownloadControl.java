package com.waterfairy.retrofit.download.download2;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shui on 2017/5/6.
 */

public class DownloadControl implements IDownloadControl, OnDownloadSuccessListener {

    public static final int START = 1;
    public static final int DOWNLOADING = 2;
    public static final int PAUSE = 3;
    public static final int STOP = 4;
    public static final int FINISH = 5;
    public static final int ERROR = 6;

    private DownloadInfo downloadInfo;
    private IDownloadService downloadService;
    private DownloadProgress downloadProgress;
    private Call<ResponseBody> call;
    private String url;
    private int downloadState;

    public DownloadControl(DownloadInfo info) {
        this.downloadInfo = info;
        url = downloadInfo.getUrl();
        initDownload();
    }


    private void initDownload() {
        if (downloadService == null) {
            downloadProgress = new DownloadProgress(downloadInfo, this);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(downloadInfo.getTimeOut(), TimeUnit.SECONDS)
                    .addInterceptor(new DownloadInterceptor(downloadProgress))
                    .build();
            downloadService = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(downloadInfo.getBasePath())
                    .addConverterFactory(GsonConverterFactory.create())
                    .callbackExecutor(Executors.newCachedThreadPool())
                    .build()
                    .create(IDownloadService.class);
        }
    }

    public DownloadControl setDownloadListener(OnDownloadListener onDownloadListener) {
        if (downloadProgress != null)
            downloadProgress.setOnDownloadListener(onDownloadListener);
        return this;
    }

    @Override
    public void start() {
        if (downloadState == DOWNLOADING) {
            returnMsg("正在下载中");
        } else if (downloadState == FINISH) {
            returnMsg("下载完成");
        } else {
            call = downloadService.download("bytes=" + downloadInfo.getCurrentLen() + "-", url);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    new FileWriter().writeFile(
                            getDownloadListener(),
                            response.body(),
                            downloadInfo);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    returnMsg("下载失败");
                }
            });
            OnDownloadListener downloadListener = getDownloadListener();

            if (downloadListener != null) {
                if (downloadState == PAUSE || downloadState == STOP) {
                    downloadListener.onContinue();
                } else {
                    downloadListener.onStartDownload();
                }
            }
            downloadState = DOWNLOADING;
        }


    }

    private void returnMsg(String msg) {
        OnDownloadListener downloadListener = getDownloadListener();
        if (downloadListener != null) downloadListener.onError(msg);
    }

    private OnDownloadListener getDownloadListener() {
        OnDownloadListener onDownloadListener = null;
        if (downloadProgress != null)
            onDownloadListener = downloadProgress.getOnDownloadListener();
        return onDownloadListener;
    }

    @Override
    public void pause() {
        handle(PAUSE);
    }

    private void handle(int state) {
        if (call != null)
            call.cancel();
        downloadState = state;
    }

    @Override
    public void stop() {
        handle(STOP);
    }

    public int getState() {
        return downloadState;
    }

    @Override
    public void onDownloadSuccess(String url) {
        downloadState = FINISH;
        DownloadManager.getInstance().remove(url);
    }
}
