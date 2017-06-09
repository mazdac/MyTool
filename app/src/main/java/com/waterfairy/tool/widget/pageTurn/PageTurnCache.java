package com.waterfairy.tool.widget.pageTurn;

import android.graphics.Bitmap;
import android.util.SparseArray;

/**
 * Created by water_fairy on 2017/6/6.
 * 995637517@qq.com
 */

public class PageTurnCache implements GetBitmapThread.OnGetBitmapListener {
    private SparseArray<Bitmap> bitmapHashMap;

    public static final int PAGE_PRE = 1;
    public static final int PAGE_CUR = 2;
    public static final int PAGE_NEXT = 3;

    public static final int PAGE_PRE_1 = 11;
    public static final int PAGE_PRE_2 = 12;
    public static final int PAGE_CUR_1 = 21;
    public static final int PAGE_CUR_2 = 22;
    public static final int PAGE_NEXT_1 = 31;
    public static final int PAGE_NEXT_2 = 32;
    private int deviceDensity;
    private int width;
    private int height;

    public PageTurnCache(int deviceDensity, int width, int height) {
        this.deviceDensity = deviceDensity;
        this.width = width;
        this.height = height;
        bitmapHashMap = new SparseArray<>();
    }

    public Bitmap getBitmap(int tag) {
        return bitmapHashMap.get(tag);
    }

    /**
     * 左边缓存
     *
     * @param path
     */
    public void cachePre(String path, OnCacheSuccessListener onCacheSuccessListener) {
        changePre();
        cacheBitmap(PAGE_PRE, path, onCacheSuccessListener);
    }

    public void cachePre(String path) {
        cachePre(path, null);
    }

    /**
     * 右边缓存
     *
     * @param path
     */
    public void cacheNext(String path, OnCacheSuccessListener onCacheSuccessListener) {
        changeNext();
        cacheBitmap(PAGE_NEXT, path, onCacheSuccessListener);
    }

    public void cacheNext(String path) {
        cacheNext(path, null);
    }

    /**
     * 缓存的方向
     *
     * @param bigTag
     * @param path
     */
    public void cacheBitmap(int bigTag, String path, OnCacheSuccessListener onCacheSuccessListener) {
        GetBitmapThread bitmapThread = new GetBitmapThread(path, deviceDensity, width, height, bigTag, onCacheSuccessListener);
        bitmapThread.setOnGetBitmapListener(this);
        bitmapThread.start();
    }

    public void cacheBitmap(int bigTag, String path) {
        cacheBitmap(bigTag, path, null);
    }

    @Override
    public synchronized void onGetBitmap(int tag, Bitmap bitmap, Bitmap bitmap1, Bitmap bitmap2, OnCacheSuccessListener onCacheSuccessListener) {
        int tag1 = 0, tag2 = 0;
        if (tag == PAGE_PRE) {
            tag1 = PAGE_PRE_1;
            tag2 = PAGE_PRE_2;
        } else if (tag == PAGE_CUR) {
            tag1 = PAGE_CUR_1;
            tag2 = PAGE_CUR_2;
        } else if (tag == PAGE_NEXT) {
            tag1 = PAGE_NEXT_1;
            tag2 = PAGE_NEXT_2;
        }
        bitmapHashMap.put(tag1, bitmap1);
        bitmapHashMap.put(tag2, bitmap2);
        bitmapHashMap.put(tag, bitmap);
        if (onCacheSuccessListener != null) {
            onCacheSuccessListener.onCacheOk(bitmap, bitmap1, bitmap2);
        }

    }

    public void changeNext() {
        bitmapHashMap.put(PAGE_PRE_1, getBitmap(PAGE_CUR_1));
        bitmapHashMap.put(PAGE_PRE_2, getBitmap(PAGE_CUR_2));
        bitmapHashMap.put(PAGE_CUR_1, getBitmap(PAGE_NEXT_1));
        bitmapHashMap.put(PAGE_CUR_2, getBitmap(PAGE_NEXT_2));
        bitmapHashMap.put(PAGE_PRE, getBitmap(PAGE_CUR));
        bitmapHashMap.put(PAGE_CUR, getBitmap(PAGE_NEXT));
    }

    public void changePre() {
        bitmapHashMap.put(PAGE_NEXT_1, getBitmap(PAGE_CUR_1));
        bitmapHashMap.put(PAGE_NEXT_2, getBitmap(PAGE_CUR_2));
        bitmapHashMap.put(PAGE_CUR_1, getBitmap(PAGE_PRE_1));
        bitmapHashMap.put(PAGE_CUR_2, getBitmap(PAGE_PRE_2));
        bitmapHashMap.put(PAGE_NEXT, getBitmap(PAGE_CUR));
        bitmapHashMap.put(PAGE_CUR, getBitmap(PAGE_PRE));
    }

    public interface OnCacheSuccessListener {
        void onCacheOk(Bitmap bitmap, Bitmap bitmap1, Bitmap bitmap2);
    }

}
