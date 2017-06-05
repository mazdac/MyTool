package com.waterfairy.tool.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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
import com.waterfairy.utils.ImageUtils;

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
    private int mCurrentPage, mMaxCount;//当前position ,总数
    private OnPageChangeListener onPageChangeListener;//监听
    private static final int TURN_LEFT = -1;//左滑,
    private static final int TURN_RIGHT = 1;//右滑
    private LinearLayout mAboveLin;//左上,右上两个ImageView
    private PhotoView mPhotoView;
    private RelativeLayout mTouchRel;
    private boolean canClick = true;//翻页中不可点击
    private int turnPageTime = 400;
    private boolean isPhotoViewType = false;
    private int mWidth, mHeight;


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
        mCurrentPage = 0;
        initFirst();
    }

    private Bitmap getBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        bitmap = ImageUtils.matrix(bitmap, mWidth, mHeight, false);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), mErrorImg);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565);
            }
        } else {

        }
        return bitmap;
    }

    /**
     * 第一次 初始化
     */
    private void initFirst() {
        setAboveLinVisibility(false);
        Bitmap bitmap = getBitmap(mAdapter.getImg(mCurrentPage));
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Bitmap left = Bitmap.createBitmap(bitmap, 0, 0, width / 2, height);
            Bitmap right = Bitmap.createBitmap(bitmap, width - width / 2, 0, width / 2, height);
            mLeftBelow.setImageBitmap(left);
            mRightBelow.setImageBitmap(right);
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    /**
     * 设置当前position
     *
     * @param position
     */
    public void setCurrentItem(int position) {
        if (!canClick) return;
        if (isPhotoViewType) {
            setType(false);
        }
        if (mCurrentPage == position) return;
        if (mCurrentPage > position) {
            mCurrentPage = position;
            turnPage(TURN_RIGHT);
        } else if (mCurrentPage < position) {
            mCurrentPage = position;
            turnPage(TURN_LEFT);
        }
    }


    private float mLastX;


    private boolean turnPage(int dir) {
        canClick = false;
        if (mCurrentPage == mMaxCount - 1 && dir == TURN_LEFT) {
            //最后一页
            if (onPageChangeListener != null) {
                onPageChangeListener.onToEdge(mCurrentPage);
            }
            canClick = true;
            return false;
        } else if (mCurrentPage == 0 && dir == TURN_RIGHT) {
            //第一页
            if (onPageChangeListener != null) {
                onPageChangeListener.onToEdge(mCurrentPage);
            }
            canClick = true;
            return false;
        }
        //翻页动效
        if (onPageChangeListener != null) {
            onPageChangeListener.onTurning(dir);
        }
        initBeforeAnim(dir);
        setAnim(dir);

        if (dir == TURN_LEFT) {
            mCurrentPage++;
        } else {
            mCurrentPage--;
        }
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(mCurrentPage);

        }
        if (mAdapter != null) {
            mAdapter.onPageSelected(mCurrentPage);
        }
        mPhotoView.setImageBitmap(getBitmap(mAdapter.getImg(mCurrentPage)));
        return true;
    }

    private void initBeforeAnim(int dir) {
        setAboveLinVisibility(true);
        Bitmap previousBitmap = null, currentBitmap = null, afterBitmap = null;
        Bitmap leftBelowBitmap = null, leftAboveBitmap = null, rightBelowBitmap = null, rightAboveBitmap = null;
        //当前张
        currentBitmap = getBitmap(mAdapter.getImg(mCurrentPage));
        int currentWidth = currentBitmap.getWidth();
        int currentHeight = currentBitmap.getHeight();

        if (mCurrentPage != 0 && dir == TURN_RIGHT) {
            //前一张
            previousBitmap = getBitmap(mAdapter.getImg(mCurrentPage - 1));
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
            afterBitmap = getBitmap(mAdapter.getImg(mCurrentPage + 1));
            int afterWidth = afterBitmap.getWidth();
            int afterHeight = afterBitmap.getHeight();
            rightBelowBitmap = Bitmap.createBitmap(afterBitmap, afterWidth - afterWidth / 2, 0, afterWidth / 2, afterHeight);
            rightAboveBitmap = Bitmap.createBitmap(afterBitmap, 0, 0, afterWidth / 2, afterHeight);
            leftBelowBitmap = Bitmap.createBitmap(currentBitmap, 0, 0, currentWidth / 2, currentHeight);
            leftAboveBitmap = Bitmap.createBitmap(currentBitmap, currentWidth - currentWidth / 2, 0, currentWidth / 2, currentHeight);
            mRightBelow.setImageBitmap(rightBelowBitmap);
            mRightAbove.setImageBitmap(leftAboveBitmap);
            mLeftBelow.setImageBitmap(leftBelowBitmap);
            mLeftAbove.setImageBitmap(rightAboveBitmap);
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
                    return turnPage(TURN_RIGHT);
                } else if (difference < -30) {
                    //左滑
                    return turnPage(TURN_LEFT);
                }
                onClick();
                break;
        }

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
        handler.sendEmptyMessageDelayed(1, turnPageTime);
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

    public interface OnPageChangeListener {
        void onPageSelected(int position);

        void onToEdge(int position);

        void onClickOnly();

        void onDoubleClickOnly();

        void onTurning(int dir);
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public int getMaxCount() {
        return mMaxCount;
    }
}
