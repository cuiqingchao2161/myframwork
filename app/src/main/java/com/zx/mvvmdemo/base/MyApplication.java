package com.zx.mvvmdemo.base;

import android.app.Application;

import com.zx.mvvmdemo.http.HttpUtils;
import com.zx.mvvmdemo.http.retrofitinterface.RetrofitInterface;
import com.zx.mvvmdemo.utils.DensityHelper;

/**
 * Created by Administrator on 2018/11/25.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        HttpUtils.init(this);
        DensityHelper.init(this, 1080); //DESIGN_WIDTH为设计图宽度，同样不要忘记清单文件配置Application，另 布局中使用pt
    }
}
