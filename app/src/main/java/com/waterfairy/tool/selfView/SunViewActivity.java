package com.waterfairy.tool.selfView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.waterfairy.tool.R;
import com.waterfairy.tool.widget.sun.SunView;

public class SunViewActivity extends AppCompatActivity {
    private SunView sunView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun_view);
        sunView = (SunView) findViewById(R.id.sun_view);
        intiData();
    }

    private void intiData() {
        sunView.initData(6, 10, 18);
    }
}
