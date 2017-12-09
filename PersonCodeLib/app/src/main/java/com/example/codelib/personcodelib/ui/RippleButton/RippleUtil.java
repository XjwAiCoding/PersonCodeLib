package com.example.codelib.personcodelib.ui.RippleButton;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;

public class RippleUtil {
    private static final String TAG = "RippleUtil";
    public static final int FRAME_INTERVAL = 1000 / 60;

    public static final int MAX_RIPPLE_RADIUS = dip2px(200);
    public static final int MIN_RIPPLE_RADIUS = dip2px(30);
    public static final int RIPPLE_DURATION = 400;
    public static final int RIPPLE_BACKGROUND_OFFSET = 56;


    public static final int BTN_INSET_HORIZONTAL = 4;
    public static final int BTN_INSET_VERTICAL = 5;
    public static final int BTN_INSET_VERTICAL_APPCOMPAT = 6;


    public static int px2dip(int pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void setBackground(View v, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            v.setBackground(drawable);
        } else {
            v.setBackgroundDrawable(drawable);
        }
    }

    public static void setBackground(Button btn, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            btn.setBackground(drawable);
        } else {
            btn.setBackgroundDrawable(drawable);
        }
    }

    public static int alphaColor(int color, int alpha) {
        return (alpha << 24) | 0xffffff & color;
    }

    public static int produceBackgroundColor(int rippleColor) {
        int a = (rippleColor & 0xff000000) >> 24;
        int r = (rippleColor & 0xff0000) >> 16;
        int g = (rippleColor & 0xff00) >> 8;
        int b = rippleColor & 0xff;

        r = makeOffset(r);
        g = makeOffset(g);
        b = makeOffset(b);

        return Color.argb(a, r, g, b);
    }

    public static int makeOffset(int value) {
        int dest = value < 128 ? value + RIPPLE_BACKGROUND_OFFSET : value - RIPPLE_BACKGROUND_OFFSET;
        if (dest < 0) {
            return 0;
        } else if (dest > 255) {
            return 255;
        } else {
            return dest;
        }
    }

}
