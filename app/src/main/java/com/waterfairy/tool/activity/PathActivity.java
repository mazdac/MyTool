package com.waterfairy.tool.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.waterfairy.tool.R;

import java.io.File;
import java.util.EventListener;

public class PathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        initData();
    }

    private void initData() {
        TextView textView = (TextView) findViewById(R.id.text);

        String absolutePath = getExternalCacheDir().getAbsolutePath();
        String cacheDir = getCacheDir().getAbsolutePath();
        String downloadCacheDirectory = Environment.getDownloadCacheDirectory().getAbsolutePath();
        String absolutePath1 = Environment.getDataDirectory().getAbsolutePath();
        String absolutePath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
        String rootDirectory = Environment.getRootDirectory().getAbsolutePath();

        textView.setText(
                "getExternalCacheDir().getAbsolutePath() \t" + absolutePath + "\n" +
                        "getCacheDir().getAbsolutePath()\t" + cacheDir + "\n" +
                        "Environment.getDownloadCacheDirectory().getAbsolutePath()\t" + downloadCacheDirectory + "\n" +
                        "Environment.getDataDirectory().getAbsolutePath()\t" + absolutePath1 + "\n" +
                        "Environment.getExternalStorageDirectory().getAbsolutePath()\t" + absolutePath2 + "\n" +
                        "Environment.getRootDirectory().getAbsolutePath()\t" + rootDirectory + "\n"
        );

    }
}
