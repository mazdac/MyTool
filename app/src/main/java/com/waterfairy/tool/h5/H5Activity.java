package com.waterfairy.tool.h5;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.waterfairy.tool.R;
import com.waterfairy.utils.AssetsUtils;

import java.io.IOException;


public class H5Activity extends AppCompatActivity {

    private static final String TAG = "h5Activity";
    private WebView mWebView;

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        String path = getFilesDir().getPath() + "/html";
        ;
        Log.i(TAG, "onCreate: " + path);

        try {
            AssetsUtils.copyPath(this, "html", path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //webView
        mWebView = (WebView) findViewById(R.id.web_view);
        //设置webview可以支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(mWebInterface, "myh5");
        mWebView.setWebViewClient(new CustomWebView());


        final String mimeType = "text/html";
        final String encoding = "utf-8";
        final String html = "html/index.html";// TODO 从本地读取HTML文件

        Log.i(TAG, "onCreate: path: --- " + "file:///" + path);

        mWebView.loadUrl("file:///" + path + "/index.html");
    }


    private WebInterface mWebInterface = new WebInterface() {
        @JavascriptInterface
        @Override
        public void callFromJSBaseDataType(int result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:callFromAndroid(12)");
                }
            });
        }

        @JavascriptInterface
        @Override
        public void callAndroidMethod() {

        }
    };

    private class CustomWebView extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Uri url = request.getUrl();
            String urlPath = url.getPath();
            Log.i(TAG, "shouldInterceptRequest: " + urlPath);
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

}
