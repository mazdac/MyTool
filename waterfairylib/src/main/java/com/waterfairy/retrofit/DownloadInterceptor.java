package com.waterfairy.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by shui on 2017/4/26.
 */

public class DownloadInterceptor implements Interceptor {
    private OnDownloadingListener downloadingListener;

    public DownloadInterceptor(OnDownloadingListener downloadingListener) {
        this.downloadingListener = downloadingListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response oriResponse = chain.proceed(chain.request());
        return oriResponse.newBuilder()
                .body(new DownloadResponseBody(oriResponse.body(), downloadingListener))
                .build();
    }
}
