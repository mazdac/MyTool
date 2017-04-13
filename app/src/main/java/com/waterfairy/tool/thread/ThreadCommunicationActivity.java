package com.waterfairy.tool.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.waterfairy.tool.R;

import org.w3c.dom.Text;

public class ThreadCommunicationActivity extends AppCompatActivity {

    private static final String TAG = "threadTest";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_communication);

        textView = (TextView) findViewById(R.id.text);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                textView.setText("1");
                Log.i(TAG, "run: 1");
                new MyThread().start();
            }
        });
        thread.start();
        handler.sendEmptyMessageDelayed(0,3000);
    }

    Handler handlerThread;

    public class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            textView.setText("2");
            Log.i(TAG, "run: 2");

            Looper.prepare();
            handlerThread = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.i(TAG, "handleMessage: 123");
                }
            };

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView.setText("3");
            Log.i(TAG, "handleMessage: 3");
            handlerThread.sendEmptyMessage(0);
        }
    };
}
