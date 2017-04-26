package com.waterfairy.tool.rxjava.retrofit;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.waterfairy.tool.R;
import com.waterfairy.tool.rxjava.retrofit.down.RetrofitDownloadManger;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.*;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {
    private String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/retrofit/b1";
    private static final String TAG = "retrofit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        getFil(savePath,"retrofit_largeFile_test1.apk");
        getFil(savePath,"retrofit_largeFile_test2.apk");

    }

    private void getFil(String savePath,String name) {
        String fileUrl = "01a3bd5737f2e4fcc0c1939b4798b259b3c31247e/com.supercell.clashroyale.mi.apk";
        RetrofitDownloadManger retrofitDownloadManger = RetrofitDownloadManger.getInstance();
        try {
            retrofitDownloadManger.downloadFile(savePath,name,new RetrofitDownloadManger.OnDownLoadingListener(){

                @Override
                public void downloadStart() {

                }

                @Override
                public void onDownloading(boolean done, long totalLen, long currentLen) {

                }

                @Override
                public void onError() {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
