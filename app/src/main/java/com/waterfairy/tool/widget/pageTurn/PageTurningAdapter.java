package com.waterfairy.tool.widget.pageTurn;

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

    /**
     * 获取img地址
     *
     * @param position
     * @return
     */
    String getImg(int position);

    boolean isFile(int position);

    /**
     * 选中当前pos
     *
     * @param position
     */
    void onPageSelected(int position);

}
