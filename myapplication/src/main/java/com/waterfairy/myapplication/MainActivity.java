package com.waterfairy.myapplication;

import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moveView = (MoveView) findViewById(R.id.move_view);
    }

    private MoveView moveView;

    public void onClick(View view) {
        int num = 0;
        switch (view.getId()) {
            case R.id.button1:
                num = 1;
                break;
            case R.id.button2:
                num = 2;
                break;
            case R.id.button3:
                num = 3;
                break;
            case R.id.button4:
                num = 4;
                break;
            case R.id.ensure:
                String temp=((EditText)findViewById(R.id.edit)).getText().toString();
                num=Integer.parseInt(TextUtils.isEmpty(temp)?"3":temp);
                break;

        }

        if (num==0){
            Toast.makeText(this,"not 0",Toast.LENGTH_SHORT).show();
            return;
        }
        moveView.initData(num,200,200);
    }
}
