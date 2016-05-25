package com.xcode.lockcapture.tool;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.View;

/**
 * Created by Administrator on 2016/5/25.
 */
public class UIUtil {

    /**
     * 5.0以上，设置波纹点击背景
     * 如果小于5.0则使用默认的drawable的selector
     */
    public static void trySetRippleBg(View view, @DrawableRes int defaultBG) {
        boolean isShowRipple = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

        if (isShowRipple) {
            TypedArray typedArray = generateRippleTypedArray(view.getContext());
            view.setBackgroundDrawable(typedArray.getDrawable(0));
            typedArray.recycle();
        } else {
            view.setBackgroundResource(defaultBG);
        }
    }


    /**
     * 创建一个波纹点击背景的typeArray
     * 使用方式 typedArray.getDrawable(0)
     *
     * @param context
     * @return
     */
    public static TypedArray generateRippleTypedArray(Context context) {
        int[] attrs = new int[]{android.R.attr.selectableItemBackground};
        return context.getTheme().obtainStyledAttributes(attrs);
    }


    public static void setRippleBg(View view, TypedArray rippleTypedArray) {
        view.setBackgroundDrawable(rippleTypedArray.getDrawable(0));
    }

}
