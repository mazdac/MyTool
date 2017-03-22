package com.waterfairy.tool.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.waterfairy.tool.R;

public class KeyCodeActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_code);
        textView = (TextView) findViewById(R.id.text);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String content = textView.getText().toString();
        textView.setText(content + "-" + keyCode);
        return super.onKeyDown(keyCode, event);
    }
}
