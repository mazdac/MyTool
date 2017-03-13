package com.waterfairy.tool.image;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.waterfairy.tool.R;
import com.waterfairy.tool.image.photoView.PhotoViewActivity;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image2);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gao_si:
                startActivity(new Intent(this, GaoSiActivity.class));
                break;
            case R.id.photo_view:
                startActivity(new Intent(this, PhotoViewActivity.class));
                break;
            case R.id.clor_view:
                startActivity(new Intent(this, PhotoViewActivity.class));
                break;
            case R.id.round:
                startActivity(new Intent(this, RoundViewActivity.class));
                break;
        }
    }
}
