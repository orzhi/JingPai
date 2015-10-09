package com.xcode.lockcapture.common;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by kimmy on 2015/4/7.
 */
public class Utils {


    public static ObjectAnimator GenerateColorAnimator(Context context, int animatorID, Object target) {
        ObjectAnimator colorAnimation = (ObjectAnimator) AnimatorInflater.loadAnimator(context, animatorID);
        colorAnimation.setTarget(target);
        colorAnimation.setEvaluator(new ArgbEvaluator());
        return colorAnimation;
    }

    public static void MkDir(String url) {
        File file = new File(url);
        if (!file.exists())
            file.mkdirs();
    }


    public static String GenerateDateFileName() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

    public static String wrapUrlWithPicassoPrefix(String url) {
        return "file://" + GlobalConfig.RawImageStoreUrl + url;
    }

    public static void CopyFile(String sourceUrl, String destUrl) throws IOException {
        File sourcefile = new File(sourceUrl);

        if (sourcefile.exists()) {
            File destFile = new File(destUrl);
            CopyFile(sourcefile, destFile);
        }
    }

    public static void CopyFile(File sourceFile, File destFile) throws IOException {
        if (destFile.exists())
            return;

        destFile.createNewFile();
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();

            // previous code: destination.transferFrom(source, 0, source.size());
            // to avoid infinite loops, should be:
            long count = 0;
            long size = source.size();
            while ((count += destination.transferFrom(source, count, size - count)) < size) ;
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static String getWritePath(Context context) {

        //外部有挂载
        if (Environment.isExternalStorageEmulated()) {
            File file = context.getExternalFilesDir(null);

            if (file != null)
                return file.getAbsolutePath();
        }

        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 获取程序版本，参考build.gradle里面的versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
}
