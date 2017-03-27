package com.waterfairy.zero.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.waterfairy.zero.R;

/**
 * Created by water_fairy on 2017/3/27.
 */

public class PlayProgressBar extends RelativeLayout {
    private static final String TAG = "playProgress";
    private Context mContext;
    private final int MAX_PROGRESS = 100;
    private int mProgress;
    private int mMaxProgress = MAX_PROGRESS;
    private View mBGView, mProgressView, mPressedView;
    private int mViewWidth;
    private int dp2;
    private OnProgressChangedListener listener;


    public PlayProgressBar(Context context) {
        super(context);
        this.mContext = context;
    }

    public PlayProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mMaxProgress = w;
        initView();
    }

    private void initView() {
        //背景进度条
        mBGView = new View(mContext);
        addView(mBGView);
        mBGView.setBackgroundColor(mContext.getResources().getColor(R.color.bg_bg_progress));
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) mBGView.getLayoutParams();
        layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.dp1);
        layoutParams.width = mViewWidth;
        layoutParams.addRule(CENTER_VERTICAL);

        //进度条
        mProgressView = new View(mContext);
        addView(mProgressView);
        mProgressView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        RelativeLayout.LayoutParams progressLayout = (LayoutParams) mProgressView.getLayoutParams();
        dp2 = mContext.getResources().getDimensionPixelSize(R.dimen.dp2);
        progressLayout.height = dp2;
        progressLayout.width = 100;
        progressLayout.addRule(CENTER_VERTICAL);

        //触摸进度
        mPressedView = new View(mContext);
        addView(mPressedView);
        mPressedView.setBackgroundResource(R.mipmap.style_progress_pressed);
        RelativeLayout.LayoutParams pressedViewLayout = (LayoutParams) mPressedView.getLayoutParams();
        pressedViewLayout.width = 100;
        pressedViewLayout.addRule(CENTER_VERTICAL);
        mPressedView.setVisibility(GONE);
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        initProgress();
    }


    private void initProgress() {
        RelativeLayout.LayoutParams pressedLayout = (LayoutParams) mPressedView.getLayoutParams();
        pressedLayout.width = mProgress;
        mPressedView.setLayoutParams(pressedLayout);

        RelativeLayout.LayoutParams progressLayout = (LayoutParams) mProgressView.getLayoutParams();
        progressLayout.width = mProgress;
        progressLayout.height = dp2;
        mProgressView.setLayoutParams(progressLayout);
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float rate = event.getX() / mViewWidth;
        mProgress = (int) (mMaxProgress * rate);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mPressedView.setVisibility(VISIBLE);
                initProgress();
                break;
            case MotionEvent.ACTION_UP:
                mPressedView.setVisibility(GONE);
                if (listener != null) {
                    listener.onProgressChange(mProgress, rate);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                initProgress();
                break;
        }
        return true;
    }

    public void setOnProgressChanged(OnProgressChangedListener listener) {
        this.listener = listener;
    }

    public interface OnProgressChangedListener {
        void onProgressChange(int progress, float rate);
    }
}
