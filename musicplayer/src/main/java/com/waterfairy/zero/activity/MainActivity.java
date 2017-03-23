package com.waterfairy.zero.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.waterfairy.utils.ImageUtils;
import com.waterfairy.utils.ToastUtils;
import com.waterfairy.utils.ToolBarUtils;
import com.waterfairy.zero.R;
import com.waterfairy.zero.utils.MyImgUtil;

public class MainActivity extends AppCompatActivity implements ToolBarUtils.OnToolBarBackClickListener {
    private static final String TAG = "main";
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ImageView mIVBgBlur;
    private ImageView mIVAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        ToolBarUtils.initToolBarMenu(this, R.id.drawer_layout, R.id.actionbar, 0, true);
        Bitmap bitmap = ImageUtils.selfBlur1(BitmapFactory.decodeResource(getResources(), R.mipmap.bg_1), 60, false);
        mIVBgBlur.setImageBitmap(bitmap);
        MyImgUtil.loadAlbum(this, mIVAlbum, R.mipmap.bg_1);
    }

    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.actionbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mIVAlbum = (ImageView) findViewById(R.id.iv_album);
        mIVBgBlur = (ImageView) findViewById(R.id.bg_img_blur);
    }

    @Override
    public void onToolBarBackClick() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                ToastUtils.show(R.string.search);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
