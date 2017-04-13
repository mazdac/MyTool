package com.waterfairy.tool.aidltest;

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

public class AIDLService extends Service {
    private static final String TAG = "aidlService";
    private IBinder mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void join() throws RemoteException {
            Log.i(TAG, "join: ");
        }

        @Override
        public void leave() throws RemoteException {
            Log.i(TAG, "leave: ");
        }

        @Override
        public void registerCallback() throws RemoteException {
            Log.i(TAG, "registerCallback: ");
        }

        @Override
        public void unregisterCallback() throws RemoteException {
            Log.i(TAG, "unregisterCallback: ");
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
