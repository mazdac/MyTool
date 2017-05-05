package com.waterfairy.retrofit.download;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shui on 2017/4/26.
 */

public class DownloadManger {

    private static final DownloadManger DOWNLOAD_MANGER = new DownloadManger();
    private HashMap<String, DownloadInfo> downloadInfoHashMap;

    public static DownloadManger getInstance() {
        return DOWNLOAD_MANGER;
    }

    /**
     * 下载文件
     *
     * @param info 下载信息
     * @return 返回控制器
     */
    public DownloadControl downFile(final DownloadInfo info) {
        if (downloadInfoHashMap == null) {
            downloadInfoHashMap = new HashMap<>();
        }
        final OnDownloadingListener onDownloadingListener = info.getOnDownloadingListener();
        DownloadControl control = null;
        DownloadService downloadService = info.getDownloadService();
        if (downloadService == null) {
            DownloadProgress downloadProgress = new DownloadProgress(onDownloadingListener, info);
            info.setDownloadProgress(downloadProgress);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(info.getTimeOut(), TimeUnit.SECONDS)
                    .addInterceptor(new DownloadInterceptor(downloadProgress))
                    .build();
            downloadService = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(info.getBasePath())
                    .addConverterFactory(GsonConverterFactory.create())
                    .callbackExecutor(Executors.newCachedThreadPool())
                    .build()
                    .create(DownloadService.class);
        }
        Call<ResponseBody> responseCall = downloadService.download("bytes=" + info.getCurrentLen() + "-", info.getUrl());
        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                writeCache(response.body(), info);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                onDownloadingListener.onError("下载失败");
            }
        });
        info.setCall(responseCall);
        control = info.getControl();
        if (control == null) {
            control = new Control(info);
            info.setControl(control);
        }
        onDownloadingListener.onStartDownload();
        downloadInfoHashMap.put(info.getUrl(), info);
        return control;
    }

    /**
     * 文件存储
     *
     * @param responseBody
     * @param info
     */
    private void writeCache(ResponseBody responseBody, DownloadInfo info) {
        OnDownloadingListener onDownloadingListener = info.getOnDownloadingListener();
        File file = new File(info.getSavePath());
        long totalLen = 0;
        long currentLen = info.getCurrentLen();
        if (info.getTotalLen() == 0) {
            totalLen = responseBody.contentLength();
            info.setTotalLen(totalLen);
        } else {
            totalLen = info.getTotalLen();
            info.setLastLen(currentLen);
        }
        if (canSave(file)) {
            FileChannel channelOut = null;
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                channelOut = randomAccessFile.getChannel();
                MappedByteBuffer mappedByteBuffer =
                        channelOut.map(FileChannel.MapMode.READ_WRITE, currentLen, totalLen - currentLen);
                byte[] buffer = new byte[1024 * 8];
                int len;
                int record = 0;
                while ((len = responseBody.byteStream().read(buffer)) != -1) {
                    mappedByteBuffer.put(buffer, 0, len);
                    record += len;
                }
                responseBody.byteStream().close();
                channelOut.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
                onDownloadingListener.onError("文件读取错误");
            }

        } else {
            onDownloadingListener.onError("文件创建失败");
        }
    }

    /**
     * 创建文件
     *
     * @param file
     * @return
     */
    private boolean canSave(File file) {
        boolean canSave = false;
        if (file.exists()) {
            canSave = true;
        } else {
            if (!file.getParentFile().exists()) {
                canSave = file.getParentFile().mkdirs();
            } else {
                canSave = true;
            }
            if (canSave) {
                try {
                    canSave = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    canSave = false;
                }
            }
        }
        return canSave;
    }

    public class DownloadProgress implements OnDownloadingListener {
        private OnDownloadingListener onDownloadingListener;
        private DownloadInfo downloadInfo;

        public void setOnDownloadingListener(OnDownloadingListener onDownloadingListener) {
            this.onDownloadingListener = onDownloadingListener;

        }

        DownloadProgress(OnDownloadingListener onDownloadingListener, DownloadInfo downloadInfo) {
            this.onDownloadingListener = onDownloadingListener;
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void onStartDownload() {

        }

        @Override
        public void onDownloading(boolean done, long total, long current) {
            current = downloadInfo.getLastLen() + current;
            if (onDownloadingListener != null)
                onDownloadingListener.onDownloading(done, downloadInfo.getTotalLen(), current);
            downloadInfo.setCurrentLen(current);
            if (done) {
                downloadInfoHashMap.remove(downloadInfo.getUrl());
            }
        }

        @Override
        public void onError(String msg) {

        }
    }

    private class Control implements DownloadControl {
        private DownloadInfo downloadInfo;

        private Control(DownloadInfo info) {
            this.downloadInfo = info;
        }

        @Override
        public boolean start() {
            if (downloadInfo == null) return false;
            downFile(downloadInfo);
            return true;
        }

        @Override
        public boolean pause() {
            downloadInfo.getCall().cancel();
            return false;
        }

        @Override
        public boolean stop() {
            downloadInfo.getCall().cancel();
            return false;
        }
    }

    public DownloadInfo getDownloadInfo(String url) {
        if (downloadInfoHashMap != null) {
            return downloadInfoHashMap.get(url);
        }
        return null;
    }
}
