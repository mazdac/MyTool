package com.waterfairy.tool.rxjava.retrofit.down;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.waterfairy.retrofit.download.download2.DownloadControl;
import com.waterfairy.retrofit.download.download2.DownloadManager;
import com.waterfairy.retrofit.download.download2.DownloadInfo;
import com.waterfairy.retrofit.download.download2.OnDownloadListener;
import com.waterfairy.tool.R;
import com.waterfairy.utils.ToastUtils;

public class DownActivity extends AppCompatActivity {

    private static final String TAG = "down";
    //    private boolean isLoading2 = false;
    DownloadInfo downloadInfo;
    //    DownloadInfo downloadInfo1;
    private TextView textView, textView2;
    private DownloadManager downloadManager;
    private DownloadControl downloadControl, downloadControl2;
    private String downloadUrl = "http://p.gdown.baidu.com/24fe9faf892c646dcdf17b72f104be04a8bf81047e161ed6f4471efc17b1489079efa02aef7570b30dc0f07de0464abd62c35bfbf8485335c1250aa4f4faf9718b3dac74ce6c6839bffc9ecb1b366c8be2c1c84ec7db4c68fd44473564fed8273214a19b89fa57fa4d9503a6a9a6cb686754c69cf6f519a2486da7e319402feeabc129d1188ccfe3";
    private OnDownloadListener downloadListener, downloadListener2;
    private String downloadUrl2 = "http://sw.bos.baidu.com/sw-search-sp/software/1e41f08ea1bea/QQ_8.9.2.20760_setup.exe";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        textView = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
//        initDownload1(downloadUrl);
        initDownload2(downloadUrl);
        initDownload3(downloadUrl2);

    }

    private void initDownload2(String downloadUrl) {

        downloadManager = DownloadManager.getInstance();
        downloadControl = downloadManager.get(downloadUrl);
        if (downloadControl == null)
            downloadControl = downloadManager.add(getDownloadInfo("/sdcard/jj2.apk", downloadUrl));
        downloadControl.setDownloadListener(getDownloadListener1());
    }

    private void initDownload3(String downloadUrl) {
        downloadManager = DownloadManager.getInstance();
        downloadControl2 = downloadManager.get(downloadUrl);
        if (downloadControl2 == null)
            downloadControl2 = downloadManager.add(getDownloadInfo("/sdcard/jj3.apk", downloadUrl));
        downloadControl2.setDownloadListener(getDownloadListener2());
    }

    private OnDownloadListener getDownloadListener1() {
        downloadListener = new OnDownloadListener() {
            @Override
            public void onStartDownload() {
                Log.i(TAG, "onStartDownload: ");
            }

            @Override
            public void onDownloading(final boolean done, final long total, final long current) {
                Log.i(TAG, "onDownloading: " + done + "-" + current + "-" + total);
                DownActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText((int) (current / (float) total * 100) + "");
                        if (done)
                            ToastUtils.show("下载完成");
                    }
                });
            }

            @Override
            public void onError(String msg) {
                Log.i(TAG, "onError: " + msg);
                ToastUtils.showOnUiThread(DownActivity.this,msg);
            }

            @Override
            public void onContinue() {

            }
        };
        ;

        return downloadListener;
    }

    private OnDownloadListener getDownloadListener2() {
        downloadListener2 = new OnDownloadListener() {
            @Override
            public void onStartDownload() {
                Log.i(TAG, "onStartDownload: ");
            }

            @Override
            public void onDownloading(boolean done, final long total, final long current) {
                if (done) {
                    ToastUtils.show("下载完成");
                }
                Log.i(TAG, "onDownloading: " + done + "-" + current + "-" + total);
                DownActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView2.setText((int) (current / (float) total * 100) + "");

                    }
                });
            }

            @Override
            public void onError(String msg) {
                Log.i(TAG, "onError: " + msg);
                ToastUtils.showOnUiThread(DownActivity.this,msg);
            }

            @Override
            public void onContinue() {

            }
        };
        ;

        return downloadListener2;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            if (downloadControl.getState() == DownloadControl.DOWNLOADING) {
                downloadControl.pause();
            } else {
                downloadControl.start();
            }
        } else {

            if (downloadControl2.getState() == DownloadControl.DOWNLOADING) {
                downloadControl2.pause();
            } else {
                downloadControl2.start();
            }
        }
    }

    public DownloadInfo getDownloadInfo(String downloadPath, String downloadUrl) {
        return new DownloadInfo(
                "http://img04.tooopen.com/"
                , downloadPath
                , downloadUrl);
    }
}
