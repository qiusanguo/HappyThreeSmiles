package com.teach.zhly.testaarandjar;

import android.app.Application;

import com.hts.security.sdk.SecurityUtil;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SecurityUtil.init();
    }
}
