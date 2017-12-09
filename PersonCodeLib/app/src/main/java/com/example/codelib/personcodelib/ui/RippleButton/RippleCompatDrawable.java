package com.example.codelib.personcodelib.ui.RippleButton;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import java.util.ArrayList;

public class RippleCompatDrawable extends Drawable implements View.OnTouchListener {
    private enum Speed {PRESSED, NORMAL}

    public enum Type {CIRCLE, HEART, TRIANGLE}

    public interface OnFinishListener {
        void onFinish();
    }

    /* ClipBound for widget inset padding.*/
    private Rect mClipBound;
    /* Drawable bound for background image. */
    private Rect mDrawableBound;

    private ArrayList<OnFinishListener> mOnFinishListeners;
    private Paint mRipplePaint;
    private Speed mSpeed;
    private Path mRipplePath;
    private Interpolator mInterpolator;
    private ValueAnimator mFadeAnimator;
    private Drawable mBackgroundDrawable;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mRippleColor;
    private int mBackgroundColor;
    private int mBackgroundColorAlpha = 0;
    private int mRippleDuration;
    private int mMaxRippleRadius;
    private int mFadeDuration;
    private int mAlpha;
    private int mAlphaDelta = 0;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;

    private long mStartTime;
    private int x;
    private int y;
    private float mScale = 0f;
    private int lastX;
    private int lastY;
    private float lastScale = 0f;

    private boolean isFull = false;
    private boolean isWaving = false;
    private boolean isPressed = false;
    private boolean isFading = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRippleRunnable = new Runnable() {
        @Override
        public void run() {
            updateRipple(mSpeed);
            if (isWaving || isPressed) {
                mHandler.postDelayed(this, RippleUtil.FRAME_INTERVAL);
            } else if (!isFading) {
                startFadeAnimation();
            }
        }
    };

    public RippleCompatDrawable(RippleConfig config) {
        this(config.getRippleColor(), config.getMaxRippleRadius(),
                config.getRippleDuration(), config.getInterpolator(), config.getFadeDuration(),
                config.isFull(), config.getPath());
    }

    private RippleCompatDrawable(int rippleColor, int maxRippleRadius,
                                 int rippleDuration, Interpolator interpolator, int fadeDuration,
                                 boolean isFull, Path path) {
        setRippleColor(rippleColor);
        mMaxRippleRadius = maxRippleRadius;
        mRippleDuration = rippleDuration;
        mInterpolator = interpolator;
        mFadeDuration = fadeDuration;
        this.isFull = isFull;
        mRipplePath = path;

        mRipplePaint = new Paint();
        mRipplePaint.setAntiAlias(true);
        mAlpha = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        //clipRect：裁剪画布，只会显示被裁减的区域，之外的区域将不会显示
        if (mBackgroundDrawable == null) {
            canvas.clipRect(mClipBound);
        } else if (mBackgroundDrawable instanceof ColorDrawable) {
            canvas.clipRect(mClipBound);
            mBackgroundDrawable.setBounds(mClipBound);
            mBackgroundDrawable.draw(canvas);
        }

        canvas.drawColor(RippleUtil.alphaColor(mBackgroundColor, mBackgroundColorAlpha));
        canvas.save();//用来保存画布的状态
        canvas.translate(x, y);
        canvas.scale(mScale, mScale);
        mRipplePaint.setAlpha(mAlpha);
        canvas.drawPath(mRipplePath, mRipplePaint);
        canvas.restore();//用来恢复一系列操作的状态之后的状态，当和canvas.save( )一起使用时，恢复到canvas.save( )保存时的状态。
    }

    @Override
    public void setAlpha(int alpha) {
        mRipplePaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    private long elapsedOffset = 0;
    private static final String TAG = "RippleCompatDrawable";

    private void updateRipple(Speed speed) {
        float progress = 0f;
        if (isWaving) {
            long elapsed = SystemClock.uptimeMillis() - mStartTime;//间隔时间
            if (speed == Speed.PRESSED) {
                elapsed = elapsed / 5;
                elapsedOffset = elapsed * 4;
            } else {
                elapsed = elapsed - elapsedOffset;
            }
            progress = Math.min(1f, (float) elapsed / mRippleDuration);
            isWaving = (progress <= 0.99f);
            mScale = (mMaxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS * mInterpolator.getInterpolation(progress) + 1f;
            mBackgroundColorAlpha = (int) (Color.alpha(mBackgroundColor) * (progress <= 0.125f ? progress * 8 : 1f));
        } else {
            mScale = (mMaxRippleRadius - RippleUtil.MIN_RIPPLE_RADIUS) / RippleUtil.MIN_RIPPLE_RADIUS + 1f;
            mBackgroundColorAlpha = Color.alpha(mBackgroundColor);
        }
        if (lastX == x && lastY == y && lastScale == mScale) return;

        lastX = x;
        lastY = y;
        lastScale = mScale;

        mRipplePaint.setColor(mRippleColor);
        mRipplePaint.setStyle(Paint.Style.FILL);
        invalidateSelf();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();

                mSpeed = Speed.PRESSED;

                stopFading();
                mHandler.removeCallbacks(mRippleRunnable);
                mStartTime = SystemClock.uptimeMillis();
                mHandler.postDelayed(mRippleRunnable, RippleUtil.FRAME_INTERVAL);
                isWaving = true;
                isPressed = true;
                elapsedOffset = 0;
                lastX = x;
                lastY = y;
                mAlpha = Color.alpha(mRippleColor);

                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                mSpeed = Speed.PRESSED;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mSpeed = Speed.NORMAL;
                isPressed = false;
                startFadeAnimation();
                break;
        }
        return true;
    }

    public void triggerListener() {
        if (mOnFinishListeners != null && mOnFinishListeners.size() != 0) {
            for (OnFinishListener listener : mOnFinishListeners) {
                listener.onFinish();
            }
        }
    }

    public void finishRipple() {
        mHandler.removeCallbacks(mRippleRunnable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mFadeAnimator != null) {
            mFadeAnimator.removeAllUpdateListeners();
            mFadeAnimator.removeAllListeners();
            mFadeAnimator = null;
        } else {
            mHandler.removeCallbacks(mFadeRunnable4Froyo);
        }
    }

    private void startFadeAnimation() {
        isFading = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            startFadeAnimation4HoneyComb();
        } else {
            startFadeAnimation4Froyo();//API = 8 Froyo
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startFadeAnimation4HoneyComb() {
        if (mFadeAnimator == null) {
            mFadeAnimator = ValueAnimator.ofInt(Color.alpha(mRippleColor), 0);
            mFadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mAlpha = (int) animation.getAnimatedValue();
                    if (mAlpha <= mBackgroundColorAlpha) {
                        mBackgroundColorAlpha = mAlpha;
                    }
                    invalidateSelf();
                }
            });

            mFadeAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isFading = false;
                    triggerListener();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isFading = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mFadeAnimator.setDuration(mFadeDuration);
        } else {
            mFadeAnimator.cancel();
        }
        mFadeAnimator.start();
    }

    private void startFadeAnimation4Froyo() {
        mAlphaDelta = getAlphaDelta();
        mHandler.removeCallbacks(mFadeRunnable4Froyo);
        mHandler.post(mFadeRunnable4Froyo);
    }

    private void stopFading() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            if (mFadeAnimator != null) mFadeAnimator.cancel();
        } else {
            mHandler.removeCallbacks(mFadeRunnable4Froyo);
        }
        isFading = false;
    }

    private Runnable mFadeRunnable4Froyo = new Runnable() {
        @Override
        public void run() {
            if (mAlpha != 0) {
                int alpha = mAlpha - mAlphaDelta;
                if (alpha <= 0) {
                    alpha = 0;
                    isFading = false;
                    triggerListener();
                }
                mAlpha = alpha;
                if (mAlpha <= mBackgroundColorAlpha) mBackgroundColorAlpha = mAlpha;
                invalidateSelf();
                mHandler.postDelayed(this, RippleUtil.FRAME_INTERVAL);
            }
        }
    };

    protected void setPadding(float l, float t, float r, float b) {
        mPaddingLeft = RippleUtil.dip2px(l);
        mPaddingRight = RippleUtil.dip2px(r);
        mPaddingTop = RippleUtil.dip2px(t);
        mPaddingBottom = RippleUtil.dip2px(b);
    }

    protected void setMeasure(int width, int height) {
        mWidth = width;
        mHeight = height;
        setClipBound();
    }

    protected void setMaxRippleRadius(int maxRippleRadius) {
        mMaxRippleRadius = maxRippleRadius;
    }

    public boolean isFull() {
        return isFull;
    }

    protected void setBackgroundDrawable(Drawable backgroundDrawable) {
        mBackgroundDrawable = backgroundDrawable;
        mDrawableBound = null;
    }


    public void addOnFinishListener(OnFinishListener onFinishListener) {
        if (mOnFinishListeners == null) {
            mOnFinishListeners = new ArrayList<>();
        }
        mOnFinishListeners.add(onFinishListener);
    }

    public void setRippleColor(int rippleColor) {
        mRippleColor = rippleColor;
        mBackgroundColor = RippleUtil.produceBackgroundColor(rippleColor);
    }

    private void setClipBound() {
        if (mClipBound == null) {
            mClipBound = new Rect(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mHeight - mPaddingBottom);
        } else {
            mClipBound.set(mPaddingLeft, mPaddingTop, mWidth - mPaddingRight, mHeight - mPaddingBottom);
        }
    }

    protected Rect getDrawableBound() {
        return mDrawableBound;
    }

    protected Rect getClipBound() {
        return mClipBound;
    }

    protected Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    private int getAlphaDelta() {
        int times = mFadeDuration / RippleUtil.FRAME_INTERVAL + 1;
        return Color.alpha(mRippleColor) / times;
    }
}
