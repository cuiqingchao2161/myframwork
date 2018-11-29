package com.zx.mvvmdemo.base;

import android.app.Application;

import com.zx.mvvmdemo.http.HttpUtils;
import com.zx.mvvmdemo.http.retrofitinterface.RetrofitInterface;

/**
 * Created by Administrator on 2018/11/25.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpUtils.init(this);
    }
}
