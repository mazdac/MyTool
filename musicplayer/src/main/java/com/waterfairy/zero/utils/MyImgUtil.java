package com.waterfairy.zero.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.waterfairy.zero.R;
import com.waterfairy.zero.activity.MainActivity;

/**
 * Created by water_fairy on 2017/3/23.
 */

public class MyImgUtil {

    public static void loadAlbum(Context context, ImageView img, int resId) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        int resWidth = bitmap.getWidth();
        int resHeight = bitmap.getHeight();
        int targetHeight = context.getResources().getDimensionPixelSize(R.dimen.album_width);
        boolean isWidthBig = true;
        if (resHeight >= resWidth) {
            isWidthBig = false;
        }

        int targetWidth = targetHeight;

        if (isWidthBig) {
            targetHeight    = (int) (targetWidth * (resHeight / (float) resWidth));
        } else {
            targetWidth  = (int) (targetWidth * (resWidth / (float) resHeight));
        }
        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        layoutParams.width = targetWidth;
        layoutParams.height = targetHeight;
        img.setImageBitmap(bitmap);

    }
}
