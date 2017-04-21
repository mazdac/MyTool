package com.waterfairy.tool.activity;

import android.content.Intent;
import android.databinding.adapters.SearchViewBindingAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.waterfairy.tool.R;
import com.waterfairy.tool.aidltest.AidlActivity;
import com.waterfairy.tool.bluetooth.BTToolActivity;
import com.waterfairy.tool.databinding.DataBindingActivity;
import com.waterfairy.tool.date.DateActivity;
import com.waterfairy.tool.dialog.DialogActivity;
import com.waterfairy.tool.exception.ExceptionTestActivity;
import com.waterfairy.tool.h5.H5Activity;
import com.waterfairy.tool.image.ImageActivity;
import com.waterfairy.tool.qr.QRListActivity;
import com.waterfairy.tool.regular.RegularActivity;
import com.waterfairy.tool.rxjava.RXJavaActivity;
import com.waterfairy.tool.selfView.SelfViewActivity;
import com.waterfairy.tool.thread.ThreadCommunicationActivity;
import com.waterfairy.utils.PermissionUtils;
import com.waterfairy.tool.video.bilibili.VideoActivity;
import com.waterfairy.tool.wifisocket.WifiMangerActivity;
import com.waterfairy.tool.xml.XMLActivity;

import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_STORAGE);
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blueTooth:
                startActivity(new Intent(this, BTToolActivity.class));
                break;
            case R.id.density:
                startActivity(new Intent(this, DensityActivity.class));
                break;
            case R.id.calc:
                startActivity(new Intent(this, CalcActivity.class));
                break;
            case R.id.date:
                startActivity(new Intent(this, DateActivity.class));
                break;
//            case R.id.qr:
//                startActivity(new Intent(this, QRActivity.class));
//                break;
            case R.id.qr:
                startActivity(new Intent(this, SmaliActivity.class));
                break;
            case R.id.update:
                startActivity(new Intent(this, UpdateActivity.class));
                break;
            case R.id.exception_text:
                startActivity(new Intent(this, ExceptionTestActivity.class));
                break;
            case R.id.text:
                startActivity(new Intent(this, TextActivity.class));
                break;
            case R.id.device_list:
                startActivity(new Intent(this, QRListActivity.class));
                break;
            case R.id.print:
                startActivity(new Intent(this, PrintActivity.class));
                break;
            case R.id.tool:
                startActivity(new Intent(this, ToolActivity.class));
                break;
            case R.id.xml:
                startActivity(new Intent(this, XMLActivity.class));
                break;
            case R.id.reg:
                startActivity(new Intent(this, RegularActivity.class));
                break;
            case R.id.before:
                int i = 1 / 0;
                break;
            case R.id.data_bind:
                startActivity(new Intent(this, DataBindingActivity.class));
                break;
            case R.id.video:
                startActivity(new Intent(this, VideoActivity.class));
                break;
            case R.id.rx_java:
                startActivity(new Intent(this, RXJavaActivity.class));
                break;
            case R.id.image:
                startActivity(new Intent(this, ImageActivity.class));
                break;
            case R.id.dialog:
                startActivity(new Intent(this, DialogActivity.class));
                break;
            case R.id.wifi:
                startActivity(new Intent(this, WifiMangerActivity.class));
                break;
            case R.id.key:
                startActivity(new Intent(this, KeyCodeActivity.class));
                break;
            case R.id.h5:
                startActivity(new Intent(this, H5Activity.class));
                break;
            case R.id.thread:
                startActivity(new Intent(this, ThreadCommunicationActivity.class));
                break;
            case R.id.aidl:
                startActivity(new Intent(this, AidlActivity.class));
                break;
            case R.id.self_view:
                startActivity(new Intent(this, SelfViewActivity.class));

            case R.id.viewpager:
                startActivity(new Intent(this, ViewPagerActivity.class));
                break;
        }
    }
}