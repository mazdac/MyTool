package com.waterfairy.tool.pageTurn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.waterfairy.tool.R;
import com.waterfairy.tool.widget.MyPageTurningAdapter;
import com.waterfairy.tool.widget.PageTurningView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by water_fairy on 2017/5/31.
 * 995637517@qq.com
 */

public class PageTurningActivity extends AppCompatActivity {
    private PageTurningView pageTurningView;
    private List<PageBean> mList;
    private MyPageTurningAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_turning);
        pageTurningView = (PageTurningView) findViewById(R.id.page_turn);
        mAdapter = new MyPageTurningAdapter(getList());
        pageTurningView.setAdapter(mAdapter);
    }

    private List<PageBean> getList() {
        mList = new ArrayList<>();
        PageBean pageBean1 = new PageBean("/sdcard/test/p1.jpeg", true);
        PageBean pageBean2 = new PageBean("/sdcard/test/p2.jpeg", true);
        PageBean pageBean3 = new PageBean("/sdcard/test/p3.jpeg", true);
        PageBean pageBean4 = new PageBean("/sdcard/test/p4.jpeg", true);
        mList.add(pageBean1);
        mList.add(pageBean2);
        mList.add(pageBean3);
        mList.add(pageBean4);
        return mList;
    }

    public static class PageBean {
        public PageBean(String path, boolean isFile) {
            this.path = path;
            this.isFile = isFile;
        }

        private String path;
        private boolean isFile;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isFile() {
            return isFile;
        }

        public void setFile(boolean file) {
            isFile = file;
        }
    }

}
