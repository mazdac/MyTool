package com.waterfairy.tool.widget;

import com.waterfairy.tool.pageTurn.PageTurningActivity;

import java.util.List;

/**
 * Created by water_fairy on 2017/5/31.
 * 995637517@qq.com
 */

public class MyPageTurningAdapter implements PageTurningAdapter {
    private List<PageTurningActivity.PageBean> list;

    public MyPageTurningAdapter(List<PageTurningActivity.PageBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        else return list.size();
    }

    @Override
    public String getImg(int position) {
        return list.get(position).getPath();
    }

    @Override
    public boolean isFile(int position) {
        return list.get(position).isFile();
    }
}
