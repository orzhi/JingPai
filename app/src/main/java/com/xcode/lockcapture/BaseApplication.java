package com.xcode.lockcapture;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by Administrator on 2015/10/9.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "900009633", false);
    }
}
