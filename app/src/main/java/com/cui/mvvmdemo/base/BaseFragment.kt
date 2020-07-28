package com.cui.mvvmdemo.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cui.mvvmdemo.navigation.BackHandlerHelper
import com.cui.mvvmdemo.navigation.FragmentBackHandler


/**
 *
 * @author xujiangang
 * @date 2018/6/29
 */

abstract class BaseFragment : Fragment(), FragmentBackHandler {
    open var TAG = "BaseFragment"
    var isLazyLoad = true
    //是否加载数据（暂时用于第一次加载判断，以后也许会有其他情况）
    private var isNeedLoad = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(getLayoutId(), container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isLazyLoad) {
            initialize()
        }
    }

    override fun onBackPressed(): Boolean {
        return BackHandlerHelper.handleBackPress(this)
    }

    private fun initialize() {
        Log.d(TAG, "initialize: ")

        initView()

        initData()

        initListener()

        requestData()
    }

    /**
     * 获得页面布局Id
     *
     * @return 布局Id
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化view相关参数
     */
    protected abstract fun initView()

    /**
     * 初始化固定值数据
     */
    protected abstract fun initData()

    /**
     * 设置监听（有可能需要用到数据 所以须放在initData后面）
     */
    protected abstract fun initListener()

    /**
     * 须放在initListener后面
     */
    protected abstract fun requestData()

    /**
     * view绑定易变数据
     */
    protected abstract fun refreshView()


    override fun onResume() {
        super.onResume()
        //如果是第一次且是懒加载
        //执行初始化方法
        if (isNeedLoad && isLazyLoad) {
            initialize()
            //数据已加载，置false，避免每次切换都重新加载数据
            isNeedLoad = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy ")
    }


}