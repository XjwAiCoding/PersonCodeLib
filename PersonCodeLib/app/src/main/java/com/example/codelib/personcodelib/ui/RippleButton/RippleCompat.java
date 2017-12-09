package com.example.codelib.personcodelib.ui.RippleButton;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class RippleCompat {
    private static final String TAG = "RippleCompat";
    private static Context sContext = null;

    public static void init(Context context) {
        sContext = context;
    }

    public static void apply(View v) {
        apply(v, RippleConfig.getDefaultConfig(), null);
    }

    /**
     * set ripple with ripple color.
     *
     * @param v           view to set
     * @param rippleColor ripple color
     */
    public static void apply(View v, int rippleColor) {
        RippleConfig config = new RippleConfig();
        config.setIsFull(true);
        config.setRippleColor(rippleColor);
        apply(v, config, null);
    }


    public static void apply(View v, RippleConfig config) {
        apply(v, config, null);
    }

    //此方法是真正的入口
    public static void apply(View v, RippleConfig config, RippleCompatDrawable.OnFinishListener onFinishListener) {
        v.setFocusableInTouchMode(true);//设置view在触摸情况下获取焦点
        final RippleCompatDrawable drawable = new RippleCompatDrawable(config);
        if (onFinishListener != null) {
            drawable.addOnFinishListener(onFinishListener);
        }
        handleAttach(v, drawable);
        measure(drawable, v);//测量波纹半径，设置波纹的填充padding
        adaptBackground(drawable, v, config);
    }

    private static void handleAttach(final View v, final RippleCompatDrawable drawable) {
        if (Build.VERSION.SDK_INT >= 12) {
            //当view被添加到一个window里，此监听器会被回调
            v.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    //view附着在window上时调用
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    //view从window中销毁时调用
                    drawable.finishRipple();
                }
            });
        }
    }

    private static void adaptBackground(RippleCompatDrawable rippleDrawable, View v, RippleConfig config) {
        Drawable background;
        if (config.getBackgroundDrawable() != null) {
            rippleDrawable.setBackgroundDrawable(config.getBackgroundDrawable());
        }

        background = v.getBackground();
        if (background != null) {
            RippleUtil.setBackground(v, new LayerDrawable(new Drawable[]{background, rippleDrawable}));
        } else {
            RippleUtil.setBackground(v, rippleDrawable);
        }

    }


    private static void measure(final RippleCompatDrawable drawable, final View v) {
        if (v instanceof Button) {
            fitButton(drawable, v instanceof AppCompatButton);
        }
        v.setFocusableInTouchMode(true);//设置触摸时获取焦点
        v.setOnTouchListener(new ForwardingTouchListener(drawable, v));
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int radius = Math.max(v.getMeasuredWidth(), v.getMeasuredHeight());//获取波纹半径
                drawable.setMeasure(v.getMeasuredWidth(), v.getMeasuredHeight());
                if (drawable.isFull()) {
                    drawable.setMaxRippleRadius(radius);
                }
            }
        });
    }

    private static void fitButton(final RippleCompatDrawable drawable, boolean isAppCompatStyle) {
        drawable.setPadding(RippleUtil.BTN_INSET_HORIZONTAL,
                isAppCompatStyle ? RippleUtil.BTN_INSET_VERTICAL_APPCOMPAT : RippleUtil.BTN_INSET_VERTICAL,
                RippleUtil.BTN_INSET_HORIZONTAL,
                isAppCompatStyle ? RippleUtil.BTN_INSET_VERTICAL_APPCOMPAT : RippleUtil.BTN_INSET_VERTICAL);
    }

    private static class ForwardingTouchListener implements View.OnTouchListener {
        RippleCompatDrawable drawable;

        private ForwardingTouchListener(RippleCompatDrawable drawable, final View v) {
            this.drawable = drawable;
            drawable.addOnFinishListener(new RippleCompatDrawable.OnFinishListener() {
                @Override
                public void onFinish() {
                    v.performClick();
                }
            });
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return isInBound(event.getX(), event.getY()) && drawable.onTouch(v, event);

                default:
                    return drawable.onTouch(v, event);
            }
        }

        private boolean isInBound(float x, float y) {
            Rect bound;
            if (drawable.getBackgroundDrawable() == null || drawable.getBackgroundDrawable() instanceof ColorDrawable) {
                bound = drawable.getClipBound();
            } else {
                bound = drawable.getDrawableBound();
            }
            return x >= bound.left && x <= bound.right && y >= bound.top && y <= bound.bottom;
        }
    }
}
