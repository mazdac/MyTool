package com.waterfairy.zero.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.waterfairy.zero.R;
import com.waterfairy.zero.activity.MainActivity;

/**
 * Created by water_fairy on 2017/3/23.
 */

public class MyImgUtil {

    public static void loadAlbum(Context context, CardView mCV, ImageView img, int resId, boolean idDefault) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        int resWidth = bitmap.getWidth();
        int resHeight = bitmap.getHeight();
        int srcWidth = context.getResources().getDimensionPixelSize(R.dimen.album_width);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_card_view);
        boolean isWidthBig = true;
        if (resHeight >= resWidth) {
            isWidthBig = false;
        }
        int targetHeight = srcWidth;
        int targetWidth = srcWidth;

        if (isWidthBig) {
            if (resWidth > targetHeight) {
                targetHeight = (int) (targetWidth * (resHeight / (float) resWidth));
            } else {
                targetHeight = resHeight;
                targetWidth = resWidth;
            }
        } else {
            if (resHeight > targetWidth) {
                targetWidth = (int) (targetWidth * (resWidth / (float) resHeight));
            } else {
                targetHeight = resHeight;
                targetWidth = resWidth;
            }
        }
        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        layoutParams.width = targetWidth;
        layoutParams.height = targetHeight;
        Bitmap matrixBitmap = ImageUtils.matrix(bitmap, targetWidth, targetHeight, false);
        img.setImageBitmap(matrixBitmap);
        if (idDefault) {
            ViewGroup.LayoutParams cvLayoutParams = mCV.getLayoutParams();
            cvLayoutParams.width = srcWidth - padding;
            cvLayoutParams.height = srcWidth - padding;
        }

    }
}
