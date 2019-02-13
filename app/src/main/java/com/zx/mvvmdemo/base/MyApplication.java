package com.zx.mvvmdemo.base;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;
import com.zx.mvvmdemo.http.HttpUtils;
import com.zx.mvvmdemo.http.retrofitinterface.RetrofitInterface;
import com.zx.mvvmdemo.utils.DensityHelper;
import com.zx.mvvmdemo.utils.WebViewHelper;

/**
 * Created by Administrator on 2018/11/25.
 */

public class MyApplication extends Application {
    private static MyApplication me;

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;
        HttpUtils.init(this);
        DensityHelper.init(this, 1080); //DESIGN_WIDTH为设计图宽度，同样不要忘记清单文件配置Application，另 布局中使用pt

        initWeb();
    }

    private void initWeb(){
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

        WebViewHelper.createWebView();
    }

    public static MyApplication getInstance(){
        return me;
    }
}
