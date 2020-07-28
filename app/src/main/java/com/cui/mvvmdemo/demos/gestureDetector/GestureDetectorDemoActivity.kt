package com.cui.mvvmdemo.demos.gestureDetector

import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.base.BaseActivity
import com.cui.mvvmdemo.bean.Girl
import com.cui.mvvmdemo.ui.adapter.GirlsAdapter
import kotlinx.android.synthetic.main.activity_gesturedetector_demo_layout.*


/**
 * description : TODO:类的作用
 * author : cuiqingchao
 * date : 2019/11/30 17:56
 */
class GestureDetectorDemoActivity : BaseActivity() {
    private val mGirlList: ArrayList<Girl> = ArrayList()
    private var girlsAdapter //新闻列表的适配器
            : GestureGirlsAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_gesturedetector_demo_layout
    }

    override fun initView() {

    }

    override fun initData() {
        initGirlsData()
        girlsAdapter = GestureGirlsAdapter(this,mGirlList)
        gesture_recycler_view.adapter = girlsAdapter
        gesture_recycler_view.setHasFixedSize(true)
    }

    override fun initListener() {

    }

    override fun requestData() {
    }

    override fun refreshView() {

    }

    private fun initGirlsData() {
        val girl1 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/1.jpg")
        val girl2 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/2.jpg")
        val girl3 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/3.jpg")
        val girl4 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/4.jpg")
        val girl5 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/5.jpg")
        val girl6 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/6.jpg")
        val girl7 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/7.jpg")
        val girl8 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/8.jpg")
        val girl9 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/9.jpg")
        val girl10 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/10.jpg")
        val girl11 = Girl("崔莹莹", "模特，180cm,爱好健身跑步", "file:///android_asset/11.jpg")
        mGirlList.add(girl1)
        mGirlList.add(girl2)
        mGirlList.add(girl3)
        mGirlList.add(girl4)
        mGirlList.add(girl5)
        mGirlList.add(girl6)
        mGirlList.add(girl7)
        mGirlList.add(girl8)
        mGirlList.add(girl9)
        mGirlList.add(girl10)
        mGirlList.add(girl11)
    }


}
