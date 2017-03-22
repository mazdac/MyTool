package com.waterfairy.tool.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.waterfairy.tool.R;
import com.waterfairy.update.UpdateManager;

public class UpdateActivity extends AppCompatActivity implements UpdateManager.OnUpdateCallback {
    private boolean isHand;
    private Button mBTHand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        mBTHand = (Button) findViewById(R.id.handle);
    }

    @Override
    public void onUpdate(final boolean update, final boolean ignore) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ToastUtils.show(update + "-" + ignore);
            }
        });
    }

    public void onClick(View view) {
        String saveName = "com.huizetime";
        String key = "";
        switch (view.getId()) {
            case R.id.evaluation:
                key = "evaluation";
                break;
            case R.id.shopping:
                key = "shopping";
                break;
            case R.id.store:
                key = "store";
                break;
            case R.id.health:
                key = "health";
                break;
            case R.id.sign:
                key = "sign";
                break;
            case R.id.basketball:
                key = "basketball";
                break;
            case R.id.basketballtv:
                key = "basketballtv";
                break;
            case R.id.get:
                EditText editText1 = (EditText) findViewById(R.id.edit1);
                EditText editText2 = (EditText) findViewById(R.id.edit2);
                saveName = editText1.getText().toString();
                key = editText2.getText().toString();
                break;
            case R.id.handle:
                isHand = !isHand;
                if (!isHand) {
                    mBTHand.setText("自动检查");
                } else {
                    mBTHand.setText("手动检查");
                }
                return;
        }
        saveName = saveName + "." + key;
        UpdateManager updateManager = UpdateManager.getInstance();
        updateManager.setSaveName(saveName).checkVersion(this, key, isHand).setOnUpdateCallback(this).setOnUpdateCallback(this);
    }
}
