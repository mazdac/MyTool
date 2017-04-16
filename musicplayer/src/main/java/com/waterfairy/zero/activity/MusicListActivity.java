package com.waterfairy.zero.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.waterfairy.zero.R;
import com.waterfairy.zero.utils.ConstantUtils;

public class MusicListActivity extends AppCompatActivity {
    private ListView mLVMusicList;
    private int mCurrentPosition;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        findView();
        initView();
        initData();
    }

    private void findView() {
        mLVMusicList = (ListView) findViewById(R.id.music_list);
    }

    private void initView() {

    }

    private void initData() {
        Intent intent = getIntent();
        mCurrentPosition = intent.getIntExtra(ConstantUtils.CURRENT_PAGE, 0);
        mCurrentPage = intent.getIntExtra(ConstantUtils.CURRENT_PAGE, 0);
    }

}
