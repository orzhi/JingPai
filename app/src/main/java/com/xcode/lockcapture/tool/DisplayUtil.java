package com.xcode.lockcapture.tool;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class DisplayUtil {
    private static float scale = -1.0f;
    private static float fontScale = -1.0f;
    private static int width;
    private static int height;

    /**
     * 初始化工具类
     * <p/>
     * activity
     */
    public static void init(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        scale = dm.density;
        fontScale = dm.scaledDensity;
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    public static int getSreenWidth() {
        return width;
    }

    public static int getSreenHeight() {
        return height;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dip2px(double dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getMeSureHeight(int width, int height) {
        int scaleHeight;
        float scale = (float) DisplayUtil.width / (float) width;
        scaleHeight = (int) (scale * height);
        return scaleHeight;
    }

    public static int getMeSureHeight(int baseWidth, int width, int height) {
        int scaleHeight;
        float scale = (float) baseWidth / (float) width;
        scaleHeight = (int) (scale * height);
        return scaleHeight;
    }

    public static float getDensity() {
        return scale;
    }

    /**
     * 沉浸式窗体，隐藏status bar和 navigation bar
     *
     * @param context
     */
    public static void immersiveWindow(Activity context) {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        } else {
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        context.getWindow().setAttributes(params);
    }
}