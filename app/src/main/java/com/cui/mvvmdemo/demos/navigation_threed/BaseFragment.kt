package com.cui.mvvmdemo.demos.navigation_threed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


/**
 *
 * @author xujiangang
 * @date 2018/6/29
 */

abstract class BaseFragment : Fragment() {
    open var TAG = "BaseFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(getLayoutId(), container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialize()
    }


    private fun initialize() {

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

}