package com.example.codelib.personcodelib.ui.RippleButton;

import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

/**
 * 设置波纹相关配置属性
 */
public class RippleConfig {
    /* ripple animation duration*/
    private int rippleDuration = RippleUtil.RIPPLE_DURATION;

    /* ripple fade duration, default equal to rippleDuration */
    private int fadeDuration = RippleUtil.RIPPLE_DURATION;

    /* ripple color*/
    private int rippleColor = 0x7000ff00;

    /* background image*/
    private Drawable backgroundDrawable = null;


    /* max ripple radius. */
    private int maxRippleRadius = RippleUtil.MAX_RIPPLE_RADIUS;

    /* ripple animation interpolator*/
    private Interpolator interpolator = new AccelerateInterpolator();

    /* ripple shape type */
    private RippleCompatDrawable.Type type = RippleCompatDrawable.Type.CIRCLE;

    /* true if ripple full of the view, and maxRippleRadius would be ignore. */
    private boolean isFull = false;


    private static RippleConfig dConfig = new RippleConfig();

    public static RippleConfig getDefaultConfig() {
        return dConfig;
    }

    public int getRippleDuration() {
        return rippleDuration;
    }

    public void setRippleDuration(int rippleDuration) {
        this.rippleDuration = rippleDuration;
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setIsFull(boolean isFull) {
        this.isFull = isFull;
    }

    public int getMaxRippleRadius() {
        return maxRippleRadius;
    }

    public void setMaxRippleRadius(int maxRippleRadius) {
        if (isFull) return;
        this.maxRippleRadius = maxRippleRadius;
    }

    public int getFadeDuration() {
        return fadeDuration;
    }

    public void setFadeDuration(int fadeDuration) {
        this.fadeDuration = fadeDuration;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setType(RippleCompatDrawable.Type type) {
        this.type = type;
    }

    public Path getPath() {
        switch (type) {

            case CIRCLE:
                return RipplePathFactory.produceCirclePath();

            case HEART:
                return RipplePathFactory.produceHeartPath();

            case TRIANGLE:
                return RipplePathFactory.produceTrianglePath();

            default:
                return RipplePathFactory.produceCirclePath();
        }
    }

    public Drawable getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public void setBackgroundDrawable(Drawable backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
    }

}
