package com.waterfairy.tool.pageTurn;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.waterfairy.tool.R;
import com.waterfairy.tool.widget.MyPageTurningAdapter;
import com.waterfairy.tool.widget.PageTurningView;
import com.waterfairy.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by water_fairy on 2017/5/31.
 * 995637517@qq.com
 */

public class PageTurningActivity extends AppCompatActivity implements PageTurningView.OnPageChangeListener {
    private PageTurningView pageTurningView;
    private List<PageBean> mList;
    private MyPageTurningAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_turning);
        pageTurningView = (PageTurningView) findViewById(R.id.page_turn);
        mAdapter = new MyPageTurningAdapter(getList());
        pageTurningView.setErrorBitmap(R.mipmap.jj);
        pageTurningView.setAdapter(mAdapter);
        handler.sendEmptyMessageDelayed(0, 3000);
        pageTurningView.setOnPageChangeListener(this);

    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                pageTurningView.setCurrentItem(2);
            }
            return false;
        }
    });

    private List<PageBean> getList() {
        mList = new ArrayList<>();
        PageBean pageBean1 = new PageBean("/sdcard/test/p1.jpeg", true);
        PageBean pageBean2 = new PageBean("/sdcard/test/p2.jpeg", true);
        PageBean pageBean3 = new PageBean("/sdcard/test/p3.jpeg", true);
        PageBean pageBean4 = new PageBean("/sdcard/test/p4.jpeg", true);
        PageBean pageBean5 = new PageBean("/sdcard/test/p1.jpeg", true);
        PageBean pageBean6 = new PageBean("/sdcard/test/p2.jpeg", true);
        PageBean pageBean7 = new PageBean("/sdcard/test/p3.jpeg", true);
        PageBean pageBean8 = new PageBean("/sdcard/test/p4.jpeg", true);
        mList.add(pageBean1);
        mList.add(pageBean2);
        mList.add(pageBean3);
        mList.add(pageBean4);
        mList.add(pageBean5);
        mList.add(pageBean6);
        mList.add(pageBean7);
        mList.add(pageBean8);
        return mList;
    }

    @Override
    public void onPageSelected(int position) {
        ToastUtils.show("选中" + position);
    }

    @Override
    public void onToEdge(int position) {
        ToastUtils.show("边缘" + position);
    }

    @Override
    public void onClickOnly() {
        ToastUtils.show("点击");
    }

    @Override
    public void onDoubleClickOnly() {
        ToastUtils.show("点击两次");
    }

    @Override
    public void onTurning(int dir) {

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
