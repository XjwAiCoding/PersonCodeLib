package com.example.codelib.personcodelib.ui.ToggleButton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.codelib.personcodelib.R;

/**
 * Created by xujiawei on 2017/12/17.
 */

public class SlideSwitch extends View {

    private static final int DEFAULT_COLOR_THEME = Color.parseColor("#ff00ee00");
    private static final int DEF_WIDTH = 100;
    private static final int DEF_HEIGHT = 50;

    private Paint mPaint;
    private int mColorTheme;
    private boolean mIsOpen;

    private RectF mBackCircleRectF;//后退圆
    private RectF mFrontCircleRectF;//前进圆
    private Rect mBackCircleRect;
    private Rect mFrontCircleRect;

    private int mMinLeftMargin = 0;//滑动最小左间距
    private int mMaxLeftMargin;//滑动最大左间距
    private int mFrontRectLeftMargin;
    private int mAlpha;
    private int mFrontRectLeftMarginBegin;
    private int mEventStartX;

    private SlideListener mListener;

    public SlideSwitch(Context context) {
        this(context, null);
    }

    public SlideSwitch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlideSwitch);
        mColorTheme = array.getColor(R.styleable.SlideSwitch_theme_color, DEFAULT_COLOR_THEME);
        mIsOpen = array.getBoolean(R.styleable.SlideSwitch_isOpen, false);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimenSize(DEF_WIDTH, widthMeasureSpec);
        int height = measureDimenSize(DEF_HEIGHT, heightMeasureSpec);
        if (width < height) {
            width = height * 2;
        }
        setMeasuredDimension(width, height);
        initDrawArguments();
    }

    private int measureDimenSize(int defSize, int measureSpec) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.UNSPECIFIED) {
            result = defSize;
        } else {
            result = Math.min(specSize, defSize);
        }
        return result;
    }

    private void initDrawArguments() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mBackCircleRectF = new RectF();
        mFrontCircleRectF = new RectF();
        mBackCircleRect = new Rect(0, 0, width, height);
        mFrontCircleRect = new Rect();

        mMaxLeftMargin = width - height;
        if (mIsOpen) {
            mFrontRectLeftMargin = mMaxLeftMargin;
            mAlpha = 255;
        } else {
            mFrontRectLeftMargin = mMinLeftMargin;
            mAlpha = 0;
        }

        mFrontRectLeftMarginBegin = mFrontRectLeftMargin;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawCircle(canvas);
    }

    private void onDrawCircle(Canvas canvas) {
        int radius = mBackCircleRect.height() / 2;
        mPaint.setColor(Color.GRAY);//灰色
        mBackCircleRectF.set(mBackCircleRect);
        canvas.drawRoundRect(mBackCircleRectF, radius, radius, mPaint);//圆角矩形

        mPaint.setColor(mColorTheme);
        mPaint.setAlpha(mAlpha);
        canvas.drawRoundRect(mBackCircleRectF, radius, radius, mPaint);

        mFrontCircleRect.set(mFrontRectLeftMargin, 0, mFrontRectLeftMargin + mBackCircleRect.height(),
                mBackCircleRect.height());
        mFrontCircleRectF.set(mFrontCircleRect);
        mPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(mFrontCircleRectF, radius, radius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mEventStartX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int eventLastX = (int) event.getRawX();
                int diffX = eventLastX - mEventStartX;
                int tempX = diffX + mFrontRectLeftMarginBegin;
                tempX = (tempX > mMaxLeftMargin ? mMaxLeftMargin : tempX);
                tempX = (tempX < mMinLeftMargin ? mMinLeftMargin : tempX);
                if (tempX >= mMinLeftMargin && tempX <= mMaxLeftMargin) {
                    mFrontRectLeftMargin = tempX;
                    mAlpha = (int) (255 * (float) tempX / (float) mMaxLeftMargin);
                    invalidateView();
                }
                break;
            case MotionEvent.ACTION_UP:
                int wholeX = (int) (event.getRawX() - mEventStartX);
                mFrontRectLeftMarginBegin = mFrontRectLeftMargin;
                boolean toRight;
                toRight = (mFrontRectLeftMarginBegin > mMaxLeftMargin / 2 ? true : false);
                if (Math.abs(wholeX) < 3) {
                    toRight = !toRight;
                }
                moveToDest(toRight);
                break;
            default:
                break;
        }
        return true;
    }

    public void moveToDest(final boolean toRight) {
        ValueAnimator toDestAnim = ValueAnimator.ofInt(mFrontRectLeftMargin,
                toRight ? mMaxLeftMargin : mMinLeftMargin);
        toDestAnim.setDuration(500);
        toDestAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        toDestAnim.start();
        toDestAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFrontRectLeftMargin = (Integer) animation.getAnimatedValue();
                mAlpha = (int) (255 * (float) mFrontRectLeftMargin / (float) mMaxLeftMargin);
                invalidateView();
            }
        });
        toDestAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (toRight) {
                    mIsOpen = true;
                    if (mListener != null) {
                        mListener.open();
                    }
                    mFrontRectLeftMarginBegin = mMaxLeftMargin;
                } else {
                    mIsOpen = false;
                    if (mListener != null) {
                        mListener.close();
                    }
                    mFrontRectLeftMarginBegin = mMinLeftMargin;
                }
            }
        });
    }

    public void setOpenState(boolean isOpen) {
        this.mIsOpen = isOpen;
        initDrawArguments();
        invalidateView();
        if (mListener != null) {
            if (isOpen == true) {
                mListener.open();
            } else {
                mListener.close();
            }
        }
    }


    /**
     * draw again
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setSlideListener(SlideListener listener) {
        this.mListener = listener;
    }

    public interface SlideListener {
        void open();

        void close();
    }
}
