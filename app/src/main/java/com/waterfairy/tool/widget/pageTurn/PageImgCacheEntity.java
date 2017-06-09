package com.waterfairy.tool.widget.pageTurn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.waterfairy.utils.MD5Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by water_fairy on 2017/6/8.
 * 995637517@qq.com
 */

public class PageImgCacheEntity {
    private static final String TAG = "PageImgCacheEntity";

    private static PageImgCacheEntity pageSaveEntity;
    private Context context;
    private String rootPath;
    private boolean canSave;

    private PageImgCacheEntity() {

    }

    public static PageImgCacheEntity getInstance() {
        if (pageSaveEntity == null) pageSaveEntity = new PageImgCacheEntity();
        return pageSaveEntity;
    }

    /**
     * 建议放到Application 中
     */
    public void initContext(Context context) {
        this.context = context;
        rootPath = context.getExternalCacheDir() + File.separator + "pageTurnCache" + File.separator;
        File file = new File(rootPath);
        if (!file.exists()) {
            canSave = file.mkdirs();
        } else {
            canSave = true;
        }
    }

    public Bitmap getBitmapFromLocal(String path, int tag) {

        if (!canSave) return null;
        String filePath = rootPath + MD5Utils.getMD5Code(path) + "-" + tag;
        File file = new File(filePath);
        if (file.exists()) {
            return BitmapFactory.decodeFile(filePath);
        } else {
            return null;
        }
    }

    public void saveBitmapToLocal(Bitmap bitmap, String path, int tag) {
        new Thread() {
            @Override
            public void run() {
                if (bitmap == null) return;
                super.run();
                String filePath = rootPath + MD5Utils.getMD5Code(path) + "-" + tag;
                File file = new File(filePath);
                try {
                    if (!file.exists()) {
                        boolean newFile = file.createNewFile();
                        if (!newFile) return;
                    }
                    FileOutputStream os = new FileOutputStream(filePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
                    os.flush();
                    os.close();
                    if (file.length() == 0) file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }.start();
    }

}
