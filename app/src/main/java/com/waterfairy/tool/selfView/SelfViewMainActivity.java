package com.waterfairy.tool.selfView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.waterfairy.tool.R;

/**
 * Created by water_fairy on 2017/5/25.
 * 995637517@qq.com
 */

public class SelfViewMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_view_mian);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pie:
                startActivity(new Intent(this, PieSelfViewActivity.class));
                break;
            case R.id.histogram:
                startActivity(new Intent(this, HistogramActivity.class));
                break;
        }
    }
}
