package com.cui.mvvmdemo.base

import android.util.Log

import com.cui.lib.toast.ToastUtils
import com.cui.lib.utils.DensityHelper
import com.cui.lib.utils.WebViewHelper
import com.cui.lib.toast.style.ToastAliPayStyle
import com.cui.mvvmdemo.retrofitinterface.HttpUtils
import com.hiscene.publiclib.core.BaseApplication
import com.tencent.smtt.sdk.QbSdk

/**
 * Created by Administrator on 2018/11/25.
 */

class MyApplication : BaseApplication() {

    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        HttpUtils.init(this)
        DensityHelper.init(this, 1080f) //DESIGN_WIDTH为设计图宽度，同样不要忘记清单文件配置Application，另 布局中使用pt

        Thread.setDefaultUncaughtExceptionHandler { t, e -> Log.e("app", " uncaughtException is " + e.message) }


        initWeb()
        ToastUtils.init(this, ToastAliPayStyle())
    }

    private fun initWeb() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb = object : QbSdk.PreInitCallback {

            override fun onViewInitFinished(arg0: Boolean) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, cb)

        WebViewHelper.createWebView()
    }

}
