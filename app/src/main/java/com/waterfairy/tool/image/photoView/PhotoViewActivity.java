package com.waterfairy.tool.image.photoView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.waterfairy.tool.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends AppCompatActivity {
    private PhotoView photoView;
    private PhotoViewAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        photoView = (PhotoView) findViewById(R.id.photo_view);
        attacher = new PhotoViewAttacher(photoView);
        attacher.update();
    }
}
