package com.waterfairy.tool.widget.pageTurn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by water_fairy on 2017/6/6.
 * 995637517@qq.com
 */

public class GetBitmapThread extends Thread {
    private String path;
    private int deviceDensity;
    private int width, height;
    private static final String TAG = "GetBitmapThread";
    private int bigTag;
    private PageTurnCache.OnCacheSuccessListener onCacheSuccessListener;

    public GetBitmapThread(String path, int deviceDensity, int width, int height, int bigTag, PageTurnCache.OnCacheSuccessListener onCacheSuccessListener) {
        this.path = path;
        this.deviceDensity = deviceDensity;
        this.width = width;
        this.height = height;
        this.bigTag = bigTag;
        this.onCacheSuccessListener = onCacheSuccessListener;
    }

    @Override
    public void run() {
        super.run();
        PageImgCacheEntity instance = PageImgCacheEntity.getInstance();
        Bitmap bitmapFromLocal = null;
        Log.i(TAG, "getBitmapFromLocal 图片获取1: " + System.currentTimeMillis());
        bitmapFromLocal = instance.getBitmapFromLocal(path, PageTurnCache.PAGE_CUR);
        Log.i(TAG, "getBitmapFromLocal 图片获取2: " + System.currentTimeMillis());
        if (bitmapFromLocal == null) {
            Log.i(TAG, "getBitmapFromLocal 原图转换1: " + System.currentTimeMillis());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            float scale = 1f;
            float scaleY = options.outHeight / (float) height;
            float scaleX = options.outWidth / (float) width;
            scale = scaleY > scaleX ? scaleY : scaleX;
            options.inScaled = true;
//        options.inSampleSize = calculateInSampleSize(options);
            options.inDensity = (int) (deviceDensity * scale);
            options.inTargetDensity = deviceDensity;
            options.inJustDecodeBounds = false;
//            Log.i(TAG, "bitmapSize_before: " + options.outWidth + "--" + options.outHeight);
            bitmapFromLocal = BitmapFactory.decodeFile(path, options);
//            Log.i(TAG, "bitmapSize_after: " + width + "--" + height);
            Log.i(TAG, "getBitmapFromLocal 原图转换2: " + System.currentTimeMillis());
            instance.saveBitmapToLocal(bitmapFromLocal, path, PageTurnCache.PAGE_CUR);
        }
        divideBitmap(bitmapFromLocal);

    }

    private void divideBitmap(Bitmap bitmap) {

        Bitmap bitmap1 = null, bitmap2 = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int halfWidth = width / 2;
            Log.i(TAG, "divideBitmap 图片分割1: " + System.currentTimeMillis());
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, halfWidth, height);
            Log.i(TAG, "divideBitmap 图片分割2: " + System.currentTimeMillis());
            bitmap2 = Bitmap.createBitmap(bitmap, width - halfWidth, 0, halfWidth, height);
        }
        if (onGetBitmapListener != null)
            onGetBitmapListener.onGetBitmap(bigTag, bitmap, bitmap1, bitmap2, onCacheSuccessListener);
    }

    public int calculateInSampleSize(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > this.height || width > this.width) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > this.height
                    && (halfWidth / inSampleSize) > this.width) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private OnGetBitmapListener onGetBitmapListener;

    public void setOnGetBitmapListener(OnGetBitmapListener onGetBitmapListener) {
        this.onGetBitmapListener = onGetBitmapListener;
    }

    interface OnGetBitmapListener {
        void onGetBitmap(int tag, Bitmap bitmap, Bitmap bitmap1, Bitmap bitmap2, PageTurnCache.OnCacheSuccessListener onCacheSuccessListener);
    }
}
