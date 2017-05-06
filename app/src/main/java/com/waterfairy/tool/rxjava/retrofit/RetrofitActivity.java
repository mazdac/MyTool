package com.waterfairy.tool.rxjava.retrofit;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.waterfairy.tool.R;

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
//

    }
}
