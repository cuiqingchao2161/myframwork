package com.hiscene.publiclib.core

import android.app.Application
import kotlin.properties.Delegates

/**
 * description : TODO:类的作用
 * author : cuiqingchao
 * date : 2019/9/19 14:40
 */
open class BaseApplication : Application() {
    companion object {

        var instance: BaseApplication by Delegates.notNull()

        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}