package com.cui.mvvmdemo.demos.kotlin

import android.content.Intent
import android.util.Log
import com.cui.mvvmdemo.R
import com.cui.lib.base.BaseActivity

class KotlinTestActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun initView() {

    }

    override fun initData() {
        var intent : Intent? = null
        Log.i(TAG,"intent?.action:"+intent?.action)
        when(intent?.action){
            "sss" -> {
                Log.i(TAG,"null")
            }

            "sss1" -> {
                Log.i(TAG,"null")
            }

            null -> {
                Log.i(TAG,"null")
            }
        }
    }

    override fun initListener() {

    }

    override fun requestData() {

    }

    override fun refreshView() {

    }
}
