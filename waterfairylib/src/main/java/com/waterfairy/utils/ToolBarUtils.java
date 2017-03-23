package com.waterfairy.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by water_fairy on 2017/3/23.
 */

public class ToolBarUtils {
    public static Toolbar initToolBarBack(Context context, int toolbarId, int titleId, final OnToolBarBackClickListener listener) {
        AppCompatActivity activity = (AppCompatActivity) context;
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarId);
        toolbar.setTitle(context.getResources().getString(titleId));
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onToolBarBackClick();
                }
            }
        });
        return toolbar;
    }

    public static Toolbar initToolBarMenu(Activity context, int drawerLayoutId, int toolbarId, int titleId, boolean transparent) {
        AppCompatActivity activity = (AppCompatActivity) context;
        DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(drawerLayoutId);
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarId);
        if (transparent) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setElevation(0);
            }
        }
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(context, drawerLayout, toolbar, titleId, titleId);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        if (titleId == 0) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        return toolbar;
    }

    public static interface OnToolBarBackClickListener {
        void onToolBarBackClick();
    }
}
