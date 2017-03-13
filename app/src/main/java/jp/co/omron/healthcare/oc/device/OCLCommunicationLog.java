//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package jp.co.omron.healthcare.oc.device;

import android.util.Log;

import com.waterfairy.tool.OutUtils;
import com.waterfairy.tool.utils.DateUtils;

import java.util.Calendar;

public class OCLCommunicationLog {
    private final String a = OCLCommunicationLog.class.getSimpleName();
    private int b = -1;
    private byte[] c = null;
    private Calendar d = null;
    private Calendar e = null;
    private final String TAG = "omron_xu_OCL_log";

    public OCLCommunicationLog(int var1, byte[] var2, Calendar var3, Calendar var4) {
        this.b = var1;
        this.c = var2;
        this.d = var3;
        this.e = var4;
    }

    public Calendar getCommunicationStartTime() {
        Log.i(TAG, "getCommunicationStartTime: " + DateUtils.trans(this.d.getTime().getTime()));

        return this.d;
    }

    public byte[] getLog() {
        OutUtils.printWrite1(this.c);
        return this.c;
    }

    public Calendar getLogAcquisitionTime() {
        Log.i(TAG, "getLogAcquisitionTime: " + DateUtils.trans(this.e.getTime().getTime()));
        return this.e;
    }

    public int getUserId() {
        Log.i(TAG, "getUserId: " + this.b);

        return this.b;
    }
}
