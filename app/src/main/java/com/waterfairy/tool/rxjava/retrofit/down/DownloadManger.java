package com.waterfairy.tool.rxjava.retrofit.down;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
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

    public static DownloadManger getInstance() {
        return DOWNLOAD_MANGER;
    }

    public void downFile(final DownloadInfo info) {
        final OnDownloadingListener onDownloadingListener = info.getOnDownloadingListener();
        DownloadService downloadService = info.getDownloadService();
        if (downloadService == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(info.getTimeOut(), TimeUnit.SECONDS)
                    .addInterceptor(new DownloadInterceptor(new DownloadProgress(onDownloadingListener,info)))
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
        onDownloadingListener.onStartDownload();
    }

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

        public DownloadProgress(OnDownloadingListener onDownloadingListener, DownloadInfo downloadInfo) {
            this.onDownloadingListener = onDownloadingListener;
            this.downloadInfo = downloadInfo;
        }

        @Override
        public void onStartDownload() {

        }

        @Override
        public void onDownloading(boolean done, long total, long current) {
            onDownloadingListener.onDownloading(done, downloadInfo.getTotalLen(), current);
            downloadInfo.setCurrentLen(current);
        }

        @Override
        public void onError(String msg) {

        }
    }
}
