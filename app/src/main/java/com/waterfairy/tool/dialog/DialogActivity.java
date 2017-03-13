package com.waterfairy.tool.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.waterfairy.tool.R;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSystemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initView();
    }

    private void initView() {
        mSystemButton = (Button) findViewById(R.id.system_dialog);
        mSystemButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_dialog:
                handler.sendEmptyMessageDelayed(0, 3000);
                break;
            case R.id.system_dialog_activity:
                handler.sendEmptyMessageDelayed(1, 6000);
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showDialog();
                    break;
                case 1:
                    showDialogActivity();
                    break;
            }
        }
    };

    private void showDialogActivity() {
        Intent intent = new Intent(this, TransDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        startActivity(intent);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("what is your name?");
        builder.setTitle("hha");
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }
}
