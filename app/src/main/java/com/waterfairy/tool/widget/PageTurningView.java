package com.waterfairy.tool.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.waterfairy.tool.R;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by water_fairy on 2017/5/31.
 * 995637517@qq.com
 */


public class PageTurningView extends RelativeLayout implements View.OnTouchListener {
    private static final String TAG = "pageTurningView";
    private PageTurningAdapter mAdapter;
    private Context mContext;
    private PhotoView mPhotoView;
    private ImageView mLeftAbove, mLeftBelow, mRightAbove, mRightBelow;
    //    private ImageView mLeftCenter, mRightCenter;
    private RelativeLayout mAnimRel;
    private RelativeLayout mTouchRel;
    private int mCurrentPage, mMaxCount;
    private int mWidth, mHeight;
    private AdapterDataSetObserver mAdapterDataSetObserver;
    private OnPageChangeListener onPageChangeListener;
    private static final int TURN_LEFT = -1;
    private static final int TURN_RIGHT = 1;
    private final int mIDLeftBelow = View.generateViewId();
    //    private final int mIDLeftCenter = View.generateViewId();
    private final int mIDLeftAbove = View.generateViewId();

    private final int mIDRightBelow = View.generateViewId();
    //    private final int mIDRightCenter = View.generateViewId();
    private final int mIDRightAbove = View.generateViewId();

    private ScaleAnimation mAniLeftFromLeft, mAniLeftFromRight, mAniRightFromLeft, mAniRightFromRight;

    public PageTurningView(Context context) {
        this(context, null);
    }

    public PageTurningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
        initAnim();

    }

    private void initAnim() {
//        left开头 左侧 ,right开头 右侧
        mAniLeftFromLeft = new ScaleAnimation(1, -1, 1, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        mAniLeftFromLeft.setDuration(400);
        mAniLeftFromLeft.setFillBefore(true);
        mAniLeftFromLeft.setFillAfter(true);

        mAniLeftFromRight = new ScaleAnimation(-1, 1, 1, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        mAniLeftFromRight.setDuration(400);
        mAniLeftFromRight.setFillBefore(true);
        mAniLeftFromRight.setFillAfter(true);

        mAniRightFromLeft = new ScaleAnimation(-1, 1, 1, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        mAniRightFromLeft.setDuration(400);
        mAniRightFromLeft.setFillBefore(true);
        mAniRightFromLeft.setFillAfter(true);

        mAniRightFromRight = new ScaleAnimation(1, -1, 1, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        mAniRightFromRight.setDuration(400);
        mAniRightFromRight.setFillBefore(true);
        mAniRightFromRight.setFillAfter(true);
    }

    private void initView() {

        LayoutInflater.from(mContext).inflate(R.layout.layout_turn_page, this);
        mPhotoView = (PhotoView) findViewById(R.id.photo_view);
        mLeftAbove = (ImageView) findViewById(R.id.left_above);
        mLeftBelow = (ImageView) findViewById(R.id.left_below);
        mRightAbove = (ImageView) findViewById(R.id.right_above);
        mRightBelow = (ImageView) findViewById(R.id.right_below);
        mAnimRel = (RelativeLayout) findViewById(R.id.anim_view);
        mTouchRel = (RelativeLayout) findViewById(R.id.touch_rel);
        mTouchRel.setOnTouchListener(this);

    }

    private void setAniRelVisibility(boolean visibility) {
        if (visibility) {
            mAnimRel.setVisibility(VISIBLE);
        } else {
            mAnimRel.setVisibility(GONE);
        }
    }

    public void setAdapter(PageTurningAdapter adapter) {
        mAdapter = adapter;
        if (mAdapterDataSetObserver == null)
            mAdapterDataSetObserver = new AdapterDataSetObserver();
        mMaxCount = mAdapter.getCount();
        mCurrentPage = 0;
//        initLayout();
    }


    public void setCurrentItem(int position) {

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mAdapterDataSetObserver.onChanged();
    }

    private float mLastX;


    private boolean turnPage(int dir) {
        if (mCurrentPage == mMaxCount - 1 && dir == TURN_LEFT) {
            //最后一页
            if (onPageChangeListener != null) {
                onPageChangeListener.onToEdge(mCurrentPage);
            }
            return false;
        } else if (mCurrentPage == 0 && dir == TURN_RIGHT) {
            //第一页
            if (onPageChangeListener != null) {
                onPageChangeListener.onToEdge(mCurrentPage);
            }
            return false;
        }
        //动效
        initBeforeAnim(dir);
        //翻页
        if (dir == TURN_LEFT) {
            mCurrentPage++;
        } else {
            mCurrentPage--;
        }

//        initLayout();
        return true;
    }

    private void initBeforeAnim(int dir) {
        Bitmap previousBitmap = null, currentBitmap = null, afterBitmap = null;
        Bitmap leftBelowBitmap = null, leftAboveBitmap = null, rightBelowBitmap = null, rightAboveBitmap = null;
        //当前张
        String currentPath = mAdapter.getImg(mCurrentPage);
        currentBitmap = BitmapFactory.decodeFile(currentPath);
        int currentWidth = currentBitmap.getWidth();
        int currentHeight = currentBitmap.getHeight();

        if (mCurrentPage != 0 && dir == TURN_RIGHT) {
            //前一张
            String previousPath = mAdapter.getImg(mCurrentPage - 1);
            previousBitmap = BitmapFactory.decodeFile(previousPath);

            int preWidth = previousBitmap.getWidth();
            int preHeight = previousBitmap.getHeight();
            leftBelowBitmap = Bitmap.createBitmap(previousBitmap, 0, 0, preWidth / 2, preHeight);
            leftAboveBitmap = Bitmap.createBitmap(previousBitmap, preWidth - preWidth / 2, 0, preWidth / 2, preHeight);
            rightBelowBitmap = Bitmap.createBitmap(currentBitmap, currentWidth - currentWidth / 2, 0, currentWidth / 2, currentHeight);
            rightAboveBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, currentWidth / 2, currentHeight);
            mLeftBelow.setImageBitmap(leftBelowBitmap);
            mLeftAbove.setImageBitmap(rightAboveBitmap);

            mRightBelow.setImageBitmap(rightBelowBitmap);
            mRightAbove.setImageBitmap(leftAboveBitmap);

        }
        if (mCurrentPage != mMaxCount - 1 && dir == TURN_LEFT) {
            //后一张
            String afterPath = mAdapter.getImg(mCurrentPage + 1);
            afterBitmap = BitmapFactory.decodeFile(afterPath);

            int afterWidth = afterBitmap.getWidth();
            int afterHeight = afterBitmap.getHeight();
            rightBelowBitmap = Bitmap.createBitmap(afterBitmap, afterWidth - afterWidth / 2, 0, afterWidth / 2, afterHeight);
            rightAboveBitmap = Bitmap.createBitmap(afterBitmap, 0, 0, afterWidth / 2, afterHeight);
            leftBelowBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, currentWidth / 2, currentHeight);
            leftAboveBitmap = Bitmap.createBitmap(currentBitmap, currentWidth - currentWidth / 2, 0, currentWidth - currentWidth / 2, currentHeight);

            mRightBelow.setImageBitmap(rightBelowBitmap);
            mRightAbove.setImageBitmap(leftAboveBitmap);
            mLeftBelow.setImageBitmap(leftBelowBitmap);
            mLeftAbove.setImageBitmap(rightAboveBitmap);
        }
        setAniRelVisibility(true);
        setAnim(dir);

    }

    private void setBitmap() {

    }

    /**
     * 动画之后  或最初的设置
     */
    public void initLayout() {
        setAniRelVisibility(false);
        Bitmap currentBitmap = null;
        String currentPath = mAdapter.getImg(mCurrentPage);
        currentBitmap = BitmapFactory.decodeFile(currentPath);
        mPhotoView.setImageBitmap(currentBitmap);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                float difference = endX - mLastX;
                if (difference > 30) {
                    //右滑
                    return turnPage(TURN_RIGHT);
                } else if (difference < -30) {
                    //左滑
                    return turnPage(TURN_LEFT);
                }
                if (onPageChangeListener != null) {
                    onPageChangeListener.onClickOnly();
                }
                break;
        }

        return true;
    }


    public void setAnim(int dir) {
        if (dir == TURN_LEFT) {
            mLeftAbove.startAnimation(mAniLeftFromRight);
            mRightAbove.startAnimation(mAniRightFromRight);
        } else {
            mLeftAbove.startAnimation(mAniLeftFromLeft);
            mRightAbove.startAnimation(mAniRightFromLeft);
        }

    }

    class AdapterDataSetObserver {
        void onChanged() {

        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public interface OnPageChangeListener {
        void onPageSelected();

        void onToEdge(int position);

        void onClickOnly();
    }
}
