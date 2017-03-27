package com.waterfairy.zero.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.waterfairy.zero.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activiy);

        handler.sendEmptyMessageDelayed(0, 500);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(SplashActivity.this,
                    R.anim.slide_in_right, R.anim.slide_out_left);
            ActivityCompat.startActivity(SplashActivity.this,
                    new Intent(SplashActivity.this, MainActivity.class), compat.toBundle());
            finish();
        }
    };
}
