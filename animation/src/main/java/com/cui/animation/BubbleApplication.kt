package com.cui.animation

import android.util.Log

import com.cui.lib.toast.ToastUtils
import com.cui.lib.utils.DensityHelper
import com.cui.lib.toast.style.ToastAliPayStyle
import com.cui.lib.core.BaseApplication

/**
 * Created by Administrator on 2018/11/25.
 */

class BubbleApplication : BaseApplication() {

    companion object {
        @get:Synchronized
        var instance: BubbleApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        DensityHelper.init(this, 1080f) //DESIGN_WIDTH为设计图宽度，同样不要忘记清单文件配置Application，另 布局中使用pt

        Thread.setDefaultUncaughtExceptionHandler { t, e -> Log.e("app", " uncaughtException is " + e.message) }

        ToastUtils.init(this, ToastAliPayStyle())
    }


}
