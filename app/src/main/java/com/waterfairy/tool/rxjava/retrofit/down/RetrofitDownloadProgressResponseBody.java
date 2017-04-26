package com.waterfairy.tool.rxjava.retrofit.down;


import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * Created by water_fairy on 2017/4/26.
 * 995637517@qq.com
 */

public class RetrofitDownloadProgressResponseBody extends ResponseBody {
    private RetrofitDownloadManger.OnDownLoadingListener listener;
    private final ResponseBody responseBody;
    private BufferedSource bufferedSource;

    public RetrofitDownloadProgressResponseBody(ResponseBody responseBody, RetrofitDownloadManger.OnDownLoadingListener listener) {
        this.responseBody = responseBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (listener != null)
                    listener.onDownloading(bytesRead == -1, responseBody.contentLength(), totalBytesRead);
                return bytesRead;
            }
        };
    }

    public void addFile(String savePath, String fileName, String url) {

    }

    public interface OnRetrofitDownloadProgressListener {
        void onProgress(boolean done, long totalLen, long currentLen);
    }

    public void setDownloadProgressListener(RetrofitDownloadManger.OnDownLoadingListener listener) {
        this.listener = listener;
    }

}
