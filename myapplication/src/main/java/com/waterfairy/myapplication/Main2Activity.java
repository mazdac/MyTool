package com.waterfairy.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main2Activity extends AppCompatActivity {
    private SunView sunView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        sunView = (SunView) findViewById(R.id.sun_view);
        intiData();
    }

    private void intiData() {
        sunView.initData(6, 10, 18);
    }
}
