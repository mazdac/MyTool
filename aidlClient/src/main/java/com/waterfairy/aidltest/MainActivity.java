package com.waterfairy.aidltest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.waterfairy.tool.aidl.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "aidlServerService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent("android.intent.action.AIDLService");
        intent.setPackage("com.waterfairy.aidlserver");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {

        private IMyAidlInterface iMyAidlInterface;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: 绑定成功");
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
         /*   try {
                iMyAidlInterface.join();
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected:断开服务 ");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
