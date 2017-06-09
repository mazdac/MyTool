package com.waterfairy.tool.widget.pageTurn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.waterfairy.tool.R;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by water_fairy on 2017/5/31.
 * 995637517@qq.com
 */


public class PageTurningView extends RelativeLayout implements View.OnTouchListener, GestureDetector.OnDoubleTapListener {
    private static final String TAG = "pageTurningView";
    private PageTurningAdapter mAdapter;//adapter
    private Context mContext;//context
    //ImageView 左上,左下   右上,右下
    private ImageView mLeftAbove, mLeftBelow, mRightAbove, mRightBelow;
    private LinearLayout mLLLeftAbove, mLLRightAbove;
    private int mCurrentPosition, mMaxCount;//当前position ,总数
    private OnPageChangeListener onPageChangeListener;//监听
    private static final int TURN_LEFT = -1;//左滑,
    private static final int TURN_RIGHT = 1;//右滑
    private LinearLayout mAboveLin;//左上,右上两个ImageView
    private RelativeLayout mTouchRel;//触摸层
    private boolean canClick = true;//翻页中不可点击
    private int turnPageTime = 500;
    private boolean isPhotoViewType = false;//图片放大模式
    private PhotoView mPhotoView;//图片查看view
    private int mWidth, mHeight;
    private PageTurnCache pageTurnCache;//图片集合
    private int mLastPosition;//记录上次位置

    private ScaleAnimation mAniLeftFromLeft, mAniLeftFromRight, mAniRightFromLeft, mAniRightFromRight;
    private int mErrorImg;

    public PageTurningView(Context context) {
        this(context, null);
    }

    public PageTurningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;
        pageTurnCache = new PageTurnCache(displayMetrics.densityDpi, mWidth, mHeight);
        initView();
        initAnim();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int tempW = MeasureSpec.getSize(widthMeasureSpec);
        int tempH = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = tempW == 0 ? mWidth : tempW;
        mHeight = tempH == 0 ? mHeight : tempH;
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
//        left开头 左侧 ,right开头 右侧
        if (mAniLeftFromLeft == null) {
            mAniLeftFromLeft = getAnim(1, -1, 1);
            mAniLeftFromRight = getAnim(-1, 1, 1);
            mAniRightFromLeft = getAnim(-1, 1, 0);
            mAniRightFromRight = getAnim(1, -1, 0);
        }
    }

    /**
     * 生成anim
     *
     * @param fromX
     * @param toX
     * @param pivotXValue
     * @return
     */
    private ScaleAnimation getAnim(float fromX, float toX, float pivotXValue) {
        ScaleAnimation animation = new ScaleAnimation(fromX, toX, 1, 1, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(turnPageTime);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * 初始化view
     */
    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_turn_page, this);
        mLeftAbove = (ImageView) findViewById(R.id.left_above);
        mLeftBelow = (ImageView) findViewById(R.id.left_below);
        mLLLeftAbove = (LinearLayout) findViewById(R.id.left_lin_above);
        mRightAbove = (ImageView) findViewById(R.id.right_above);
        mRightBelow = (ImageView) findViewById(R.id.right_below);
        mLLRightAbove = (LinearLayout) findViewById(R.id.right_lin_above);
        mPhotoView = (PhotoView) findViewById(R.id.photo_view);
        mPhotoView.setOnDoubleTapListener(this);
        mTouchRel = (RelativeLayout) findViewById(R.id.touch_rel);
        mTouchRel.setOnTouchListener(this);
        mAboveLin = (LinearLayout) findViewById(R.id.above_lin);

    }

    /**
     * 用于第一次setAdapter时
     *
     * @param visibility
     */
    private void setAboveLinVisibility(boolean visibility) {
        if (visibility) {
            mAboveLin.setVisibility(VISIBLE);
        } else {
            mAboveLin.setVisibility(GONE);
        }
    }

    public void setAdapter(PageTurningAdapter adapter) {
        mAdapter = adapter;
        mMaxCount = mAdapter.getCount();
        mCurrentPosition = 0;
        initFirst();
    }

    /**
     * 1.第一次 初始化
     * 2.手动选择页码
     */
    private void initFirst() {

        // 指定缓存并显示第一页    指定缓存第二页
        pageTurnCache.cacheBitmap(PageTurnCache.PAGE_CUR, mAdapter.getImg(mCurrentPosition), (Bitmap bitmap, Bitmap bitmap1, Bitmap bitmap2) -> {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeftBelow.setImageBitmap(bitmap1);
                    mRightBelow.setImageBitmap(bitmap2);
                    setAboveLinVisibility(false);
                    mPhotoView.setImageBitmap(bitmap);
                }
            });

        });
        cache(PageTurnCache.PAGE_NEXT, mCurrentPosition + 1);
        cache(PageTurnCache.PAGE_PRE, mCurrentPosition - 1);

    }

    /**
     * 缓存图片
     *
     * @param tag  指定tag
     * @param page 页码
     */
    private void cache(int tag, int page) {
        if (page + 1 <= mMaxCount && page >= 0)
            pageTurnCache.cacheBitmap(tag, mAdapter.getImg(page));
    }

    /**
     * 设置当前position
     * 如果position 只是加1或减1 正常加载
     * 如果position 跳级  重新去缓存图片
     *
     * @param position
     */
    public void setCurrentItem(int position) {
        Log.i(TAG, "setCurrentItem: " + position);
        if (!canClick) return;
        if (isPhotoViewType) {
            setType(false);
        }
        if (mCurrentPosition == position) return;
        mLastPosition = mCurrentPosition;

        if (mCurrentPosition > position) {
            if (mLastPosition - position > 1) {
                //跳级
                mCurrentPosition = position;
                initFirst();
            } else {
                turnPage(TURN_RIGHT);
            }

        } else if (mCurrentPosition < position) {
            if (position - mLastPosition > 1) {
                //跳级
                mCurrentPosition = position;
                initFirst();
            } else {
                turnPage(TURN_LEFT);
            }
        }
    }


    private float mLastX;


    private boolean turnPage(int dir) {

        canClick = false;
        //翻页动效
        if (onPageChangeListener != null) {
            onPageChangeListener.onTurning(dir);
        }
        initBeforeAnim(dir);
        setAnim(dir);
        //预缓存
        if (dir == TURN_LEFT) {
            mCurrentPosition++;
            cacheNext();
        } else {
            mCurrentPosition--;
            cachePre();
        }
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(mCurrentPosition);
        }
        if (mAdapter != null) {
            mAdapter.onPageSelected(mCurrentPosition);
        }
        mPhotoView.setImageBitmap(pageTurnCache.getBitmap(PageTurnCache.PAGE_CUR));
        return true;
    }

    private void initBeforeAnim(int dir) {
        setAboveLinVisibility(true);
        if (mCurrentPosition != 0 && dir == TURN_RIGHT) {
            //前一张
            mLeftBelow.setImageBitmap(pageTurnCache.getBitmap(PageTurnCache.PAGE_PRE_1));
            mLeftAbove.setImageBitmap(pageTurnCache.getBitmap(PageTurnCache.PAGE_CUR_1));
            mRightAbove.setImageBitmap(pageTurnCache.getBitmap(PageTurnCache.PAGE_PRE_2));
            mRightBelow.setImageBitmap(pageTurnCache.getBitmap(PageTurnCache.PAGE_CUR_2));
        }
        if (mCurrentPosition != mMaxCount - 1 && dir == TURN_LEFT) {
            //后一张
            mRightBelow.setImageBitmap(pageTurnCache.getBitmap(PageTurnCache.PAGE_NEXT_2));
            mRightAbove.setImageBitmap(pageTurnCache.getBitmap(PageTurnCache.PAGE_CUR_2));
            mLeftBelow.setImageBitmap(pageTurnCache.getBitmap(PageTurnCache.PAGE_CUR_1));
            mLeftAbove.setImageBitmap(pageTurnCache.getBitmap(PageTurnCache.PAGE_NEXT_1));
        }
    }

    private boolean hasPageTurnChangeEdge;

    private void cacheNext() {
        if (mCurrentPosition + 1 < mMaxCount) {
            hasPageTurnChangeEdge = false;
            pageTurnCache.cacheNext(mAdapter.getImg(mCurrentPosition + 1));
        } else {
            if (!hasPageTurnChangeEdge) {
                hasPageTurnChangeEdge = true;
                pageTurnCache.changeNext();
            }
        }
    }

    private void cachePre() {
        if (mCurrentPosition - 1 >= 0) {
            hasPageTurnChangeEdge = false;
            pageTurnCache.cachePre(mAdapter.getImg(mCurrentPosition - 1));
        } else {
            if (!hasPageTurnChangeEdge) {
                hasPageTurnChangeEdge = true;
                pageTurnCache.changePre();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!canClick) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                float difference = endX - mLastX;
                if (difference > 30) {
                    //右滑
                    return handleMove(TURN_RIGHT);
                } else if (difference < -30) {
                    //左滑
                    return handleMove(TURN_LEFT);
                }
                onClick();
                break;
        }

        return true;
    }

    private boolean handleMove(int dir) {
        if (mCurrentPosition == mMaxCount - 1 && dir == TURN_LEFT) {
            //最后一页
            if (onPageChangeListener != null) {
                onPageChangeListener.onToEdge(mCurrentPosition);
            }
            canClick = true;
            return false;
        } else if (mCurrentPosition == 0 && dir == TURN_RIGHT) {
            //第一页
            if (onPageChangeListener != null) {
                onPageChangeListener.onToEdge(mCurrentPosition);
            }
            canClick = true;
            return false;
        }
        turnPage(dir);
        return true;
    }

    int clickNum;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                clickNum = 0;
                if (onPageChangeListener != null) {
                    onPageChangeListener.onClickOnly();
                }
            } else if (msg.what == 1) {
                canClick = true;
            }

        }
    };

    private void onClick() {
        clickNum++;
        if (clickNum >= 2) {
            clickNum = 0;
            if (onPageChangeListener != null) {
                setType(true);
                onPageChangeListener.onDoubleClickOnly();
            }
            handler.removeMessages(0);
        } else {
            handler.sendEmptyMessageDelayed(0, 300);
        }

    }

    private void setType(boolean isPhotoView) {
        if (isPhotoViewType == isPhotoView) return;
        isPhotoViewType = isPhotoView;
        if (isPhotoView) {
            mPhotoView.setVisibility(VISIBLE);
            mTouchRel.setVisibility(GONE);
            mPhotoView.setScale(1.2f, 0.5f, 0.5f, true);
        } else {
            mPhotoView.setVisibility(GONE);
            mTouchRel.setVisibility(VISIBLE);
        }
    }


    public void setAnim(int dir) {
        handler.sendEmptyMessageDelayed(1, turnPageTime * 2);//600ms之后点击
        if (dir == TURN_LEFT) {
            mLLLeftAbove.startAnimation(mAniLeftFromRight);
            mLLRightAbove.startAnimation(mAniRightFromRight);
        } else {
            mLLLeftAbove.startAnimation(mAniLeftFromLeft);
            mLLRightAbove.startAnimation(mAniRightFromLeft);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void setErrorBitmap(int errorBitmap) {
        this.mErrorImg = errorBitmap;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        setType(false);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    private Activity activity;

    public void iniActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isPhotoView() {
        return isPhotoViewType;
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);

        void onToEdge(int position);

        void onClickOnly();

        void onDoubleClickOnly();

        void onTurning(int dir);
    }

    public int getCurrentPage() {
        return mCurrentPosition;
    }

    public int getMaxCount() {
        return mMaxCount;
    }
}
