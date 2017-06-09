package com.waterfairy.tool.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.waterfairy.tool.R;

import java.io.File;

public class GlideActivity extends AppCompatActivity {
    private ImageView imageView;
    private String img = "/sdcard/book1712/160504838494012.jpg";
    private String img1 = "/sdcard/book1712/160459233760366.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        imageView = (ImageView) findViewById(R.id.img);
        Picasso.with(this)
                .load(new File(img1))
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap bitmap) {
                        return bitmap;
                    }

                    @Override
                    public String key() {
                        return "fdsafdsa";
                    }
                })
                .into(imageView);
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDensity = 160;
        o.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(img1, o);

        Glide.with(this).load(new File(img1)).transform(new BitmapTransformation(this) {
            @Override
            protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int width, int height) {
                Log.i("test", "transform: " + width + "--" + height);
                return bitmap;
            }

            @Override
            public String getId() {
                return "fdsafd";
            }
        }).diskCacheStrategy(DiskCacheStrategy.NONE).override(o.outWidth, o.outHeight).into(new ImageViewTarget<GlideDrawable>(imageView) {
            @Override
            protected void setResource(GlideDrawable glideDrawable) {
            }
        });
    }
}
