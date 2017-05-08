package com.waterfairy.tool.rxjava.retrofit.down;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.waterfairy.retrofit.download2.DownloadControl;
import com.waterfairy.retrofit.download2.DownloadManager;
import com.waterfairy.retrofit.download2.DownloadInfo;
import com.waterfairy.retrofit.download2.OnDownloadListener;
import com.waterfairy.tool.R;
import com.waterfairy.utils.ToastUtils;

public class DownActivity extends AppCompatActivity implements DownloadManager.OnAllHandleListener {

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

    Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        textView = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        downloadManager = DownloadManager.getInstance();
//        initDownload1(downloadUrl);
        initDownload2(downloadUrl);
        initDownload3(downloadUrl2);
        downloadManager.setOnAllHandleListener(this);

    }

    private void initDownload2(String downloadUrl) {

        if (downloadControl == null)
            downloadControl = downloadManager.add(getDownloadInfo("/sdcard/jj2.apk", downloadUrl));
        downloadControl.setDownloadListener(getDownloadListener1());
        DownloadInfo downloadInfo = downloadControl.getDownloadInfo();
        setPercent(textView, downloadInfo.getCurrentLen(), downloadInfo.getTotalLen());

    }

    private void setPercent(TextView textView, long currentLen, long totalLen) {
        int percent = 0;
        if (totalLen != 0) {
            percent = (int) (currentLen / (float) totalLen * 100);
        }
        textView.setText(percent + "");
    }

    private void initDownload3(String downloadUrl) {
        downloadControl2 = downloadManager.get(downloadUrl);
        if (downloadControl2 == null)
            downloadControl2 = downloadManager.add(getDownloadInfo("/sdcard/jj3.apk", downloadUrl));
        downloadControl2.setDownloadListener(getDownloadListener2());
        DownloadInfo downloadInfo = downloadControl2.getDownloadInfo();
        setPercent(textView2, downloadInfo.getCurrentLen(), downloadInfo.getTotalLen());
    }

    private OnDownloadListener getDownloadListener1() {
        downloadListener = new OnDownloadListener() {

            @Override
            public void onDownloading(final boolean done, final long total, final long current) {
                Log.i(TAG, "onDownloading: " + done + "-" + current + "-" + total);
                DownActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DownActivity.this.setPercent(textView, current, total);
                        if (done)
                            ToastUtils.show("下载完成");
                    }
                });
            }

            @Override
            public void onError(int code) {
                ToastUtils.showOnUiThread(DownActivity.this, code + "");
            }


            @Override
            public void onChange(int code) {
                ToastUtils.showOnUiThread(DownActivity.this, code + "");
            }

        };
        ;

        return downloadListener;
    }

    private OnDownloadListener getDownloadListener2() {
        downloadListener2 = new OnDownloadListener() {

            @Override
            public void onDownloading(boolean done, final long total, final long current) {
                if (done) {
                    ToastUtils.show("下载完成");
                }
                Log.i(TAG, "onDownloading: " + done + "-" + current + "-" + total);
                DownActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DownActivity.this.setPercent(textView2, current, total);

                    }
                });
            }

            @Override
            public void onError(int code) {
                ToastUtils.showOnUiThread(DownActivity.this, code + "");
            }


            @Override
            public void onChange(int code) {
                ToastUtils.showOnUiThread(DownActivity.this, code + "");
            }
        };
        ;

        return downloadListener2;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            int state = downloadControl == null ? DownloadControl.STOP : downloadControl.getState();
            if (state == DownloadControl.DOWNLOADING) {
                downloadControl.pause();
                button1.setText("开始");
            } else if (state == DownloadControl.PAUSE || state == DownloadControl.INIT) {
                downloadControl.start();
                button1.setText("暂停");
            } else if (state == DownloadControl.STOP) {
                initDownload2(downloadUrl);
                downloadControl.start();
                button1.setText("暂停");
            }
        } else {
            int state = downloadControl2 == null ? DownloadControl.STOP : downloadControl2.getState();
            if (state == DownloadControl.DOWNLOADING) {
                downloadControl2.pause();
                button2.setText("开始");
            } else if (state == DownloadControl.PAUSE || state == DownloadControl.INIT) {
                downloadControl2.start();
                button2.setText("暂停");
            } else if (state == DownloadControl.STOP) {
                initDownload3(downloadUrl2);
                downloadControl2.start();
                button2.setText("暂停");
            }
        }
    }

    public DownloadInfo getDownloadInfo(String downloadPath, String downloadUrl) {
        return new DownloadInfo(
                "http://img04.tooopen.com/"
                , downloadPath
                , downloadUrl);
    }

    public void handle(View view) {
        switch (view.getId()) {
            case R.id.remove1:
                if (downloadManager.remove(downloadUrl)) {
                    button1.setText("重启");
                    downloadControl = null;
                }
                break;
            case R.id.remove2:
                if (downloadManager.remove(downloadUrl2)) {
                    button2.setText("重启");
                    downloadControl2 = null;
                }
                break;
            case R.id.removeall:
                if (downloadManager.removeAll()) {
                    button1.setText("重启");
                    downloadControl = null;
                    button2.setText("重启");
                    downloadControl2 = null;
                }
                break;
            case R.id.pauseall:
                downloadManager.pauseAll();
                button1.setText("开始");
                button2.setText("开始");
                break;
            case R.id.stopall:
                downloadManager.stopAll();
                button1.setText("重启");
                textView.setText("0");
                button2.setText("重启");
                textView2.setText("0");
                break;
            case R.id.startall:
                downloadManager.startAll();
                button1.setText("暂停");
                button2.setText("暂停");
                break;
        }
    }

    @Override
    public void onAllHandle(int code) {
        ToastUtils.showOnUiThread(this, code + "");
    }
}
