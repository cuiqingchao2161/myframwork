package com.wolfwang.demo_vr;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.Stack;

/**
 * Created by qingchao.cui on 2019/3/14.
 */

public class QfAdTvApp extends Application {
    private static QfAdTvApp mApp;
    private Stack<Activity> mActivityStack;
    private boolean isUpdateRegester;
    private final String TAG = QfAdTvApp.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static QfAdTvApp getApp(){
        return mApp;
    }
}
