package com.waterfairy.tool.rxjava.retrofit.down;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by water_fairy on 2017/4/26.
 * 995637517@qq.com
 */

public class RetrofitDownloadManger {
    private static final String BASE_URL = "http://p.gdown.baidu.com/";

    private static final RetrofitDownloadManger retrofitDownloadManger = new RetrofitDownloadManger();
    private RetrofitDownloadInterceptor retrofitDownloadInterceptor;

    public static RetrofitDownloadManger getInstance() {
        return retrofitDownloadManger;
    }

    private RetrofitDownloadManger() {
    }

    OkHttpClient okHttpClient;
    RetrofitDownloadService apiService;
    Retrofit retrofit;

    public RetrofitDownloadService getService() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        //特别注意下载大文件时千万不要打开下面注释代码 否则会导致OOM
        //.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        okHttpClient = new OkHttpClient().newBuilder()//
                .readTimeout(5, TimeUnit.SECONDS)//
                .addInterceptor(new RetrofitDownloadInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)//
                .build();

        Gson gson = new GsonBuilder().setLenient().create();
         retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callbackExecutor(executorService) //默认CallBack回调在主线程进行,当设置下载大文件时需设置注解@Stream 不加这句话会报android.os.NetworkOnMainThreadException
                .build();

        apiService = retrofit.create(RetrofitDownloadService.class);

        return apiService;
    }

    private Interceptor getInterceptor(OnDownLoadingListener listener) {
        retrofitDownloadInterceptor = new RetrofitDownloadInterceptor(listener);
        return retrofitDownloadInterceptor;
    }

    public RetrofitDownloadManger savePath(String savePath, String fileName, String url) {
        retrofitDownloadInterceptor.addFile(savePath, fileName, url);
        return retrofitDownloadManger;
    }

    public void downloadFile(String savePath, String name, OnDownLoadingListener onDownLoadingListener) throws IOException {
        File file = new File(savePath);
        boolean canDown = false;
        if (!file.exists()) {
            canDown = file.mkdirs();
        } else {
            canDown = true;
        }
        if (canDown) {
            file = new File(file, name);
            if (!file.exists()) {
                try {
                    canDown = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    canDown = false;
                }
            }
        } else {
            throw new IOException("文件夹创建失败");
        }
        if (!canDown) throw new IOException("文件创建失败");
        downloadFile(file, onDownLoadingListener);

    }

    private void downloadFile(final File file, final OnDownLoadingListener onDownLoadingListener) {
        if (apiService==null)getService();
        List<Interceptor> interceptors = okHttpClient.networkInterceptors();
        interceptors.add(new RetrofitDownloadInterceptor(onDownLoadingListener));

        getService().downloadFile().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                onDownLoadingListener.downloadStart();
                if (response.isSuccessful()) {
                    BufferedSink sink = null;
                    //下载文件到本地
                    try {
                        sink = Okio.buffer(Okio.sink(file));
                        sink.writeAll(response.body().source());
                    } catch (Exception e) {
                        if (e != null) {
                            e.printStackTrace();
                        }
                    } finally {
                        try {
                            if (sink != null) sink.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.d("DownloadActivity", "==responseCode==" + response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                onDownLoadingListener.onError();
            }
        });
    }


    public static interface OnDownLoadingListener {
        void downloadStart();

        void onDownloading(boolean done, long totalLen, long currentLen);

        void onError();

    }
}
