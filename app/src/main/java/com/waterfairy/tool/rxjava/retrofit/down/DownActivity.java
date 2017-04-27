package com.waterfairy.tool.rxjava.retrofit.down;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.waterfairy.tool.R;

public class DownActivity extends AppCompatActivity {

    private static final String TAG = "down";
    private boolean isLoading1 = false;
    //    private boolean isLoading2 = false;
    DownloadInfo downloadInfo;
    //    DownloadInfo downloadInfo1;
    private TextView textView;
    private DownloadManger downloadManger;
    private DownloadControl downloadControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        textView = (TextView) findViewById(R.id.text1);

        downloadManger = DownloadManger.getInstance();
        downloadInfo = new DownloadInfo(
                "http://img04.tooopen.com/"
                , "/sdcard/jj.apk"
                , "http://p.gdown.baidu.com/24fe9faf892c646dcdf17b72f104be04a8bf81047e161ed6f4471efc17b1489079efa02aef7570b30dc0f07de0464abd62c35bfbf8485335c1250aa4f4faf9718b3dac74ce6c6839bffc9ecb1b366c8be2c1c84ec7db4c68fd44473564fed8273214a19b89fa57fa4d9503a6a9a6cb686754c69cf6f519a2486da7e319402feeabc129d1188ccfe3"
                , new OnDownloadingListener() {
            @Override
            public void onStartDownload() {
                Log.i(TAG, "onStartDownload: ");
            }

            @Override
            public void onDownloading(boolean done, final long total, final long current) {
                Log.i(TAG, "onDownloading: " + done + "-" + current + "-" + total);
                DownActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText((int) (current / (float) total * 100) + "");

                    }
                });
            }

            @Override
            public void onError(String msg) {
                Log.i(TAG, "onError: " + msg);

            }
        });
        downloadControl = downloadManger.downFile(downloadInfo);
        isLoading1 = true;

    }

    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            if (isLoading1) {
                isLoading1 = downloadControl.pause();
            } else {
                isLoading1 = downloadControl.start();
            }
        } else {

        }
    }
}
