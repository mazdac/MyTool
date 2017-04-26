package com.waterfairy.tool.rxjava.retrofit.down;

import com.squareup.okhttp.Interceptor;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by water_fairy on 2017/4/26.
 * 995637517@qq.com
 */

public class RetrofitDownloadInterceptor implements okhttp3.Interceptor {
    private RetrofitDownloadManger.OnDownLoadingListener listener;
    private RetrofitDownloadProgressResponseBody progressBody;

    public RetrofitDownloadInterceptor(RetrofitDownloadManger.OnDownLoadingListener listener) {
        this.listener = listener;
    }

    public RetrofitDownloadInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new RetrofitDownloadProgressResponseBody(originalResponse.body(), listener))
                .build();
    }



    public void addFile(String savePath, String fileName, String url) {
        progressBody.addFile(savePath,fileName,url);
    }


}
