package com.waterfairy.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.waterfairy.tool.aidl.IMyAidlInterface;

/**
 * Created by shui on 2017/4/10.
 */

public class AidlService extends Service {
    private static final String TAG="aidlServerService";
    private IBinder mBinder=new IMyAidlInterface.Stub() {
        @Override
        public void join() throws RemoteException {
            Log.i(TAG, "join: 添加 ");
        }

        @Override
        public void leave() throws RemoteException {

        }

        @Override
        public void registerCallback() throws RemoteException {

        }

        @Override
        public void unregisterCallback() throws RemoteException {

        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onServiceConnected: 绑定中 ,返回 binder");
        return mBinder;
    }
}
