package com.waterfairy.tool.widget;

import android.widget.BaseAdapter;

/**
 * Created by water_fairy on 2017/5/31.
 * 995637517@qq.com
 */

public interface PageTurningAdapter {
    /**
     * 获取数量
     *
     * @return Count
     */
    int getCount();

    String getImg(int position);

    boolean isFile(int position);

}
